package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class MarblePileFeature extends Feature<NoneFeatureConfiguration> {

	private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};

	public MarblePileFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		for (int i = 0; i < 4 + context.random().nextInt(6); ++i) {
			BlockPos blockpos = context.origin().offset(context.random().nextInt(15), 0, context.random().nextInt(15));
			blockpos = new BlockPos(blockpos.getX(), context.level().getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockpos.getX(), blockpos.getZ()), blockpos.getZ());
			if (context.level().getBlockState(blockpos.below()).canOcclude()) {
				if (context.random().nextInt(3) != 0) {
					if (context.random().nextInt(2) == 0) {
						Direction randFacing = HORIZONTALS[context.random().nextInt(HORIZONTALS.length - 1)];
						Direction.Axis axis = randFacing.getAxis();
						context.level().setBlock(blockpos, RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), 2);
						context.level().setBlock(blockpos.relative(randFacing), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), 2);
					} else {
						context.level().setBlock(blockpos, RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.values()[context.random().nextInt(Direction.Axis.values().length - 1)]), 2);
					}
				} else {
					BlockPos height = new BlockPos(blockpos);
					int h = 0;
					context.level().setBlock(height.below(), RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get().defaultBlockState(), 2);

					while (h < 3 + context.random().nextInt(3)) {
						context.level().setBlock(height.above(h), RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
						++h;
					}

					context.level().setBlock(height.above(h), RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get().defaultBlockState(), 2);
					context.level().setBlock(height.above(h + 1), RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get().defaultBlockState(), 2);

					for (Direction facing : HORIZONTALS) {
						BlockPos stairPos = height.above(h + 1).relative(facing);
						BlockState stairState = RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get().defaultBlockState().setValue(StairBlock.FACING, facing.getOpposite()).setValue(StairBlock.HALF, Half.TOP);
						FluidState fluidState = context.level().getFluidState(stairPos);
						stairState = stairState.setValue(StairBlock.WATERLOGGED, fluidState.is(Fluids.WATER));
						context.level().setBlock(stairPos, stairState, 2);
					}
				}
			}
		}
		return true;
	}
}
