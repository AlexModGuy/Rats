package com.github.alexthe666.rats.server.block;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class MarbledCheeseGrassBlock extends GrassBlock implements BonemealableBlock {
	public MarbledCheeseGrassBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!level.isClientSide()) {
			if (!level.isAreaLoaded(pos, 3)) return;
			if (level.getMaxLocalRawBrightness(pos.above()) < 4 && level.getBlockState(pos.above()).getLightBlock(level, pos.above()) > 2) {
				level.setBlockAndUpdate(pos, RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get().defaultBlockState());
			} else {
				if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
					for (int i = 0; i < 4; ++i) {
						BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

						if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !level.isLoaded(blockpos)) {
							return;
						}

						if (level.getBlockState(blockpos).is(Blocks.DIRT) && level.getMaxLocalRawBrightness(blockpos.above()) >= 4 && level.getBlockState(blockpos.above()).getLightBlock(level, pos.above()) <= 2) {
							level.setBlockAndUpdate(blockpos, Blocks.GRASS.defaultBlockState());
						}
					}
				}
			}
		}
	}
}
