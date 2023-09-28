package com.github.alexthe666.rats.server.world;

import com.github.alexthe666.rats.registry.worldgen.RatlantisFeatureRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ThickBranchingTrunkPlacer extends TrunkPlacer {

	public static final Codec<ThickBranchingTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
			trunkPlacerParts(instance).and(instance.group(
					Codec.INT.fieldOf("trunk_width").forGetter(o -> o.trunkWidth),
					Codec.INT.fieldOf("base_radius").forGetter(o -> o.baseRadius),
					Codec.INT.fieldOf("branch_depth").forGetter(o -> o.branchDepth)
			)).apply(instance, ThickBranchingTrunkPlacer::new));

	private final int trunkWidth;
	private final int baseRadius;
	private final int branchDepth;

	public ThickBranchingTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int trunkWidth, int baseRadius, int branchDepth) {
		super(baseHeight, heightRandA, heightRandB);
		this.trunkWidth = trunkWidth;
		this.baseRadius = baseRadius;
		this.branchDepth = branchDepth;
	}

	@Override
	protected TrunkPlacerType<?> type() {
		return RatlantisFeatureRegistry.THICK_BASE_BRANCHING_TRUNK_PLACER.get();
	}

	@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> consumer, RandomSource random, int treeHeight, BlockPos pos, TreeConfiguration config) {
		List<FoliagePlacer.FoliageAttachment> attachments = new ArrayList<>();
		//normal tree:
		// trunk is 1x1, base is 3x3
		// place 5-6 random branches
		// allow branches to branch off each other up to 2 times
		//large tree:
		// trunk is 2x2, base is 4x4
		// trunk tapers off on 1 side becoming a 1x2 trunk
		// place 8-12 branches
		// allow branches to branch off each other up to 3 times

		//base
		for (int baseX = -this.baseRadius; baseX <= this.baseRadius; baseX++) {
			for (int baseZ = -this.baseRadius; baseZ <= this.baseRadius; baseZ++) {
				if (baseX * baseX + baseZ * baseZ <= this.baseRadius * this.baseRadius) {
					BlockPos basePos = pos.offset(baseX, 0, baseZ);
					consumer.accept(basePos, config.trunkProvider.getState(random, basePos));
					for (int baseHeight = 0; baseHeight <= random.nextInt(this.baseRadius + 1); baseHeight++) {
						consumer.accept(basePos.above(baseHeight), config.trunkProvider.getState(random, basePos.above(baseHeight)));
					}
					this.setDirtIfNeeded(reader, consumer, random, basePos.below(), config);
				}
			}
		}
		//trunk
		for (int currentHeight = 0; currentHeight < treeHeight; currentHeight++) {
			for (int xWidth = 0; xWidth < this.trunkWidth; xWidth++) {
				for (int zWidth = 0; zWidth < this.trunkWidth; zWidth++) {
					this.placeLog(reader, consumer, random, pos.offset(xWidth, currentHeight, zWidth), config);
				}
			}
		}

		//branches
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			int branchIterations = random.nextInt(this.branchDepth);
			int branchHeight = treeHeight - 1 + random.nextInt(3);
			attachments.addAll(this.generateRecursiveBranch(reader, consumer, random, pos, config, dir, branchHeight, new BlockPos.MutableBlockPos(), branchIterations, new ArrayList<>()));
		}

		return attachments;
	}

	private List<FoliagePlacer.FoliageAttachment> generateRecursiveBranch(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> consumer, RandomSource random, BlockPos pos, TreeConfiguration config, Direction genDir, int initialBranchHeight, BlockPos.MutableBlockPos mutablePos, int branchDepth, List<FoliagePlacer.FoliageAttachment> attachments) {
		Function<BlockState, BlockState> function = state -> state.trySetValue(RotatedPillarBlock.AXIS, genDir.getAxis());
		mutablePos.set(pos).move(Direction.UP, initialBranchHeight);
		int branchLength = random.nextInt(2) + 3;
		BlockPos blockpos = pos.relative(genDir, branchLength).above(initialBranchHeight + random.nextInt(2) + 1);
		int k = random.nextBoolean() ? 2 : 1;

		for (int l = 0; l < k; ++l) {
			this.placeLog(reader, consumer, random, mutablePos.move(genDir), config, function);
		}

		Direction direction = Direction.UP;

		while (true) {
			int i1 = mutablePos.distManhattan(blockpos);
			if (i1 <= 0) {
				attachments.add(new FoliagePlacer.FoliageAttachment(blockpos, 0, this.trunkWidth > 1));
				if (branchDepth <= 0) {
					return attachments;
				} else {
					return this.generateRecursiveBranch(reader, consumer, random, mutablePos.immutable(), config, random.nextBoolean() ? genDir.getClockWise() : genDir.getCounterClockWise(), 0, new BlockPos.MutableBlockPos(), branchDepth - 1, attachments);
				}
			}

			float f = (float) Math.abs(blockpos.getY() - mutablePos.getY()) / (float) i1;
			boolean flag1 = random.nextFloat() < f;
			mutablePos.move(flag1 ? direction : genDir);
			this.placeLog(reader, consumer, random, mutablePos, config, flag1 ? Function.identity() : function);
		}
	}

	private void setDirtIfNeeded(LevelSimulatedReader reader, BiConsumer<BlockPos, BlockState> consumer, RandomSource random, BlockPos pos, TreeConfiguration config) {
		if (!(((LevelReader) reader).getBlockState(pos).onTreeGrow((LevelReader) reader, consumer, random, pos, config)) && Feature.isDirt(((LevelReader) reader).getBlockState(pos))) {
			consumer.accept(pos, config.dirtProvider.getState(random, pos));
		}
	}
}
