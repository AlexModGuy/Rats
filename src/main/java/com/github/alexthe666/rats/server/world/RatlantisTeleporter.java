package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsVillagerRegistry;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Comparator;
import java.util.Optional;

// 1.21: Forge's ITeleporter is gone — the portal contract now lives on the block side via the
// Portal interface + Entity.changeDimension(DimensionTransition). This class is kept only as a
// helper that finds an existing portal frame (POI lookup) or builds a fresh portal frame around a
// destination position. RatlantisPortalBlock still calls getOrMakePortal(...) to anchor a transition
// target on arrival; the actual entity teleport happens in the block's entityInside / Portal hook.
public class RatlantisTeleporter {

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
		if (!this.level.hasChunk(chunkPos.x, chunkPos.z)) {
			this.level.getChunk(chunkPos.x, chunkPos.z);
		}
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

		return Optional.of(new BlockUtil.FoundRectangle(portalBottom.offset(-1, 1, -1), 1, 2));
	}

	private void setBlockIfReplaceableSpot(BlockPos pos, BlockState state) {
		if (this.level.getBlockState(pos).isAir() ||
				(this.level.getBlockState(pos).canBeReplaced() && this.level.getFluidState(pos).isEmpty()) ||
				(this.level.getBlockState(pos).getDestroySpeed(this.level, pos) >= 0 && this.level.getBlockState(pos).getDestroySpeed(this.level, pos) < 10)) {
			this.level.setBlockAndUpdate(pos, state);
		}
	}

	public Optional<BlockUtil.FoundRectangle> getOrMakePortal(BlockPos pos) {
		Optional<BlockUtil.FoundRectangle> existingPortal = this.getExistingPortal(pos);
		if (existingPortal.isPresent()) {
			return existingPortal;
		} else {
			return this.makePortal(pos);
		}
	}
}
