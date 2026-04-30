package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.block.entity.RatlantisPortalBlockEntity;
import com.github.alexthe666.rats.server.world.RatlantisTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class RatlantisPortalBlock extends BaseEntityBlock implements CustomItemRarity, Portal {
	public static final com.mojang.serialization.MapCodec<RatlantisPortalBlock> CODEC = simpleCodec(RatlantisPortalBlock::new);

	@Override
	protected com.mojang.serialization.MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}


	public RatlantisPortalBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public Rarity getRarity() {
		return Rarity.EPIC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		// Direct teleport on contact (server-side only). We bypass vanilla's PortalProcessor timer because
		// the Ratlantis portal is a single 1x2 frame; the cooldown set after changeDimension prevents the
		// destination block from bouncing the entity straight back.
		if (!(level instanceof ServerLevel sourceLevel)) return;
		if (entity.isOnPortalCooldown() || entity.isPassenger() || entity.isVehicle() || !entity.canUsePortal(false)) return;

		DimensionTransition transition = this.getPortalDestination(sourceLevel, entity, pos);
		if (transition == null) return;

		entity.setPortalCooldown();
		entity.changeDimension(transition);
	}

	@Override
	public int getPortalTransitionTime(ServerLevel level, Entity entity) {
		return 0;
	}

	@Override
	@Nullable
	public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
		ResourceKey<Level> targetKey = level.dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY)
				? Level.OVERWORLD
				: RatlantisDimensionRegistry.DIMENSION_KEY;
		MinecraftServer server = level.getServer();
		ServerLevel targetLevel = server == null ? null : server.getLevel(targetKey);
		if (targetLevel == null) return null;
		if (!entity.canChangeDimensions(level, targetLevel)) return null;

		RatlantisTeleporter teleporter = new RatlantisTeleporter(targetLevel);
		java.util.Optional<net.minecraft.BlockUtil.FoundRectangle> portalRect = teleporter.getOrMakePortal(entity.blockPosition());
		BlockPos destPos = portalRect.map(rect -> rect.minCorner).orElseGet(() -> targetLevel.getSharedSpawnPos());
		// Land one block above the bottom-portal position so the player doesn't suffocate inside the frame.
		Vec3 dest = new Vec3(destPos.getX() + 0.5D, destPos.getY() + 1.0D, destPos.getZ() + 0.5D);
		return new DimensionTransition(targetLevel, dest, Vec3.ZERO, entity.getYRot(), entity.getXRot(),
				DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET));
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!this.canSurviveAt(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	public boolean canSurviveAt(Level level, BlockPos pos) {
		return (level.getBlockState(pos.above()).is(RatlantisBlockRegistry.RATLANTIS_PORTAL.get()) || level.getBlockState(pos.above()).is(RatsBlockTags.MARBLED_CHEESE)) &&
				(level.getBlockState(pos.below()).is(RatlantisBlockRegistry.RATLANTIS_PORTAL.get()) || level.getBlockState(pos.below()).is(RatsBlockTags.MARBLED_CHEESE));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof RatlantisPortalBlockEntity) {
				for (int j = 0; j < 2; ++j) {
					double d0 = (float) pos.getX() + random.nextFloat();
					double d1 = (float) pos.getY() + random.nextFloat();
					double d2 = (float) pos.getZ() + random.nextFloat();
					double d3 = ((double) random.nextFloat() - 0.5D) * 0.5D;
					double d4 = ((double) random.nextFloat() - 0.5D) * 0.5D;
					double d5 = ((double) random.nextFloat() - 0.5D) * 0.5D;
					level.addParticle(ParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
				}
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RatlantisPortalBlockEntity(pos, state);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}
