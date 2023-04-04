package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class GarbageBlock extends AbstractGarbageBlock {
	public GarbageBlock(Properties properties, double spawnRateModifier) {
		super(properties, spawnRateModifier);
	}

	@Override
	protected EntityType<? extends PathfinderMob> getEntityToSpawn() {
		return RatsEntityRegistry.RAT.get();
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		if (random.nextInt(500) == 0 && level.getBlockState(pos.above()).isAir()) {
			double d0 = pos.getX() + random.nextFloat();
			double d1 = pos.getY() + 1 + random.nextFloat();
			double d2 = pos.getZ() + random.nextFloat();
			level.addParticle(RatsParticleRegistry.FLY.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return entityType == RatsEntityRegistry.RAT.get();
	}

	@Override
	public int getDustColor(BlockState state, BlockGetter getter, BlockPos pos) {
		return 0X79695B;
	}
}
