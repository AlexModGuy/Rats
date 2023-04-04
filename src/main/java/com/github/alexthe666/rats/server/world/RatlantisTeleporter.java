package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsVillagerRegistry;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class RatlantisTeleporter implements ITeleporter {

	protected final ServerLevel level;

	public RatlantisTeleporter(ServerLevel level) {
		this.level = level;
	}

	public Optional<BlockUtil.FoundRectangle> getExistingPortal(BlockPos pos) {
		PoiManager manager = this.level.getPoiManager();
		manager.ensureLoadedAndValid(this.level, pos, 32);
		Optional<PoiRecord> optional = manager.getInSquare(type ->
				type.is(RatsVillagerRegistry.RATLANTIS_PORTAL.getKey()), pos, 32, PoiManager.Occupancy.ANY).sorted(Comparator.<PoiRecord>comparingDouble((poi) ->
				poi.getPos().distSqr(pos)).thenComparingInt(poi ->
				poi.getPos().getY())).filter(poi ->
				this.level.getBlockState(poi.getPos()).is(RatlantisBlockRegistry.RATLANTIS_PORTAL.get())).findFirst();
		return optional.map(poi -> {
			BlockPos blockpos = poi.getPos();
			this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
			BlockState blockstate = this.level.getBlockState(blockpos);
			return BlockUtil.getLargestRectangleAround(blockpos, Direction.Axis.Y, 21, Direction.Axis.Y, 21, (posIn) ->
					this.level.getBlockState(posIn) == blockstate);
		});
	}

	public Optional<BlockUtil.FoundRectangle> makePortal(BlockPos pos) {
		ChunkPos chunkPos = new ChunkPos(pos);
		//load chunk if it doesnt exist so the heightmap is correctly calculated
		if (!this.level.hasChunk(chunkPos.x, chunkPos.z)) {
			this.level.getChunk(chunkPos.x, chunkPos.z);
		}
		//set portal position to be on the ground (or rather, one block above it)
		pos = this.level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos).above();

		this.level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		this.level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
		BlockPos portalBottom = pos.offset(1, -1, 1);
		for (BlockPos currentPos : BlockPos.betweenClosedStream(portalBottom.offset(-2, 0, -2), portalBottom.offset(2, 0, 2)).map(BlockPos::immutable).toList()) {
			this.setBlockIfReplaceableSpot(currentPos, RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get().defaultBlockState());
			this.setBlockIfReplaceableSpot(currentPos.above(4), RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get().defaultBlockState());
		}
		for (int i = 1; i < 4; i++) {
			this.setBlockIfReplaceableSpot(portalBottom.offset(2, 0, 2).above(i), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
			this.setBlockIfReplaceableSpot(portalBottom.offset(2, 0, -2).above(i), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
			this.setBlockIfReplaceableSpot(portalBottom.offset(-2, 0, 2).above(i), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
			this.setBlockIfReplaceableSpot(portalBottom.offset(-2, 0, -2).above(i), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y));
		}
		this.level.setBlockAndUpdate(portalBottom, RatsBlockRegistry.MARBLED_CHEESE_RAW.get().defaultBlockState());
		this.level.setBlockAndUpdate(portalBottom.above(), RatlantisBlockRegistry.RATLANTIS_PORTAL.get().defaultBlockState());
		this.level.setBlockAndUpdate(portalBottom.above(2), RatlantisBlockRegistry.RATLANTIS_PORTAL.get().defaultBlockState());
		this.level.setBlockAndUpdate(portalBottom.above(3), RatsBlockRegistry.MARBLED_CHEESE_RAW.get().defaultBlockState());

		//the offset here defines where to place the player. If we dont have this it'll place us directly in the portal and I dont want that
		return Optional.of(new BlockUtil.FoundRectangle(portalBottom.offset(-1, 1, -1), 1, 2));
	}

	private void setBlockIfReplaceableSpot(BlockPos pos, BlockState state) {
		if (this.level.getBlockState(pos).isAir() ||
				(this.level.getBlockState(pos).canBeReplaced() && this.level.getFluidState(pos).isEmpty()) ||
				(this.level.getBlockState(pos).getDestroySpeed(this.level, pos) >= 0 && this.level.getBlockState(pos).getDestroySpeed(this.level, pos) < 10)) {
			this.level.setBlockAndUpdate(pos, state);
		}
	}

	@Override
	public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel level, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		boolean ratlantis = level.dimension() == RatlantisDimensionRegistry.DIMENSION_KEY;
		if (entity.getLevel().dimension() != RatlantisDimensionRegistry.DIMENSION_KEY && !ratlantis) {
			return null;
		} else {
			WorldBorder border = level.getWorldBorder();
			double minX = Math.max(-2.9999872E7D, border.getMinX() + 16.0D);
			double minZ = Math.max(-2.9999872E7D, border.getMinZ() + 16.0D);
			double maxX = Math.min(2.9999872E7D, border.getMaxX() - 16.0D);
			double maxZ = Math.min(2.9999872E7D, border.getMaxZ() - 16.0D);
			double coordinateDifference = DimensionType.getTeleportationScale(entity.level.dimensionType(), level.dimensionType());
			BlockPos blockpos = BlockPos.containing(Mth.clamp(entity.getX() * coordinateDifference, minX, maxX), entity.getY(), Mth.clamp(entity.getZ() * coordinateDifference, minZ, maxZ));
			return this.getOrMakePortal(blockpos).map((result) -> {
				BlockState state = entity.level.getBlockState(entity.portalEntrancePos);
				Direction.Axis axis;
				Vec3 vector3d;
				if (state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
					axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
					BlockUtil.FoundRectangle rectangle = BlockUtil.getLargestRectangleAround(entity.portalEntrancePos, axis, 21, Direction.Axis.Y, 21, (pos) -> entity.level.getBlockState(pos) == state);
					vector3d = PortalShape.getRelativePosition(rectangle, axis, entity.position(), entity.getDimensions(entity.getPose()));
				} else {
					axis = Direction.Axis.X;
					vector3d = new Vec3(0.5D, 0.0D, 0.0D);
				}

				return PortalShape.createPortalInfo(level, result, axis, vector3d, entity, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
			}).orElse(null);
		}
	}

	protected Optional<BlockUtil.FoundRectangle> getOrMakePortal(BlockPos pos) {
		Optional<BlockUtil.FoundRectangle> existingPortal = this.getExistingPortal(pos);
		if (existingPortal.isPresent()) {
			return existingPortal;
		} else {
			return this.makePortal(pos);
		}
	}

	@Override
	public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
		return false;
	}
}
