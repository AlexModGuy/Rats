package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatTreeUtils;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RatHarvestTreesGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;
	private int breakingTime;
	private int previousBreakProgress;
	private int treeSize;
	private List<BlockPos> stumpBlocks;
	@Nullable
	private Block sapling;

	public RatHarvestTreesGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(this.rat.getDepositPos().isPresent(), this.rat.getDepositPos().isPresent())) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	private void resetTarget() {
		Level level = this.rat.level();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			if (RatTreeUtils.isTreeLog(level.getBlockState(pos)) && level.getBlockState(pos.below()).is(BlockTags.DIRT) && (this.treeSize = RatTreeUtils.calculateLogAmount(level, pos)) > 0) {
				Path path = this.rat.getNavigation().createPath(this.getOffsetToAirPos(pos), 1);
				if (path != null && path.canReach() && RatUtils.canRatBreakBlock(this.rat.level(), pos, this.rat)) {
					this.setTargetBlock(pos);
					break;
				}
			}
		}
	}

	@Override
	public void start() {
		super.start();
		this.stumpBlocks = RatTreeUtils.getAllStumpBlocks(this.rat.level(), this.getTargetBlock(), this.rat.level().getBlockState(this.getTargetBlock()));
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, true) && this.getTargetBlock() != null;
	}

	@Override
	public void stop() {
		super.stop();
		this.rat.crafting = false;
		this.sapling = null;
	}

	@Nullable
	public BlockPos getOffsetToAirPos(@Nullable BlockPos pos) {
		if (pos != null) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				BlockPos offsetPos = pos.relative(direction);
				if (this.rat.level().isEmptyBlock(offsetPos) || !this.rat.level().getBlockState(offsetPos).canOcclude()) {
					return offsetPos;
				}
			}
		}
		return pos;
	}

	@Override
	public void tick() {
		BlockPos offsetToAirPos = this.getOffsetToAirPos(this.getTargetBlock());
		this.rat.getLookControl().setLookAt(this.getTargetBlock().getCenter());
		this.rat.getNavigation().moveTo(offsetToAirPos.getX() + 0.5D, offsetToAirPos.getY(), offsetToAirPos.getZ() + 0.5D, 1.25D);
		if (RatTreeUtils.isTreeLog(this.rat.level().getBlockState(this.getTargetBlock()))) {
			double distance = this.rat.getRatDistanceCenterSq(this.getTargetBlock().getX(), this.getTargetBlock().getY(), this.getTargetBlock().getZ());
			if (distance < this.rat.getRatHarvestDistance(0.0D)) {
				this.rat.level().broadcastEntityEvent(this.rat, (byte) 85);
				this.rat.crafting = true;
				if (distance < this.rat.getRatHarvestDistance(-1.0D)) {
					this.rat.setDeltaMovement(Vec3.ZERO);
					this.rat.getNavigation().stop();
				}
				this.breakingTime++;
				int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
				if (breakingTime % 10 == 0) {
					this.rat.playSound(SoundEvents.WOOD_HIT, 1, 1);
					this.rat.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, 0.5F);
				}
				if (i != this.previousBreakProgress) {
					this.rat.level().destroyBlockProgress(this.rat.getId(), this.getTargetBlock(), i);
					this.previousBreakProgress = i;
				}
				if (this.breakingTime >= 160) {
					this.rat.level().broadcastEntityEvent(this.rat, (byte) 86);
					this.rat.playSound(SoundEvents.WOOD_BREAK, 1, 1);
					this.breakingTime = 0;
					this.previousBreakProgress = -1;
					this.fellTree();
					if (!this.stumpBlocks.isEmpty() && this.sapling != null && RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_REPLANTER.get())) {
						this.stumpBlocks.forEach(pos -> {
							if (RatUtils.canRatPlaceBlock(this.rat.level(), pos, this.rat)) {
								this.rat.level().setBlockAndUpdate(pos, this.sapling.defaultBlockState());
							}
						});
					}
					this.stop();
				}
			}
		} else {
			this.stop();
		}
	}

	private void fellTree() {
		Level level = this.rat.level();
		BlockPos base = this.getTargetBlock();
		BlockState baseBlock = level.getBlockState(base);
		List<BlockPos> logsToKill = RatTreeUtils.getLogsToBreak(level, base, new ArrayList<>(), level.getBlockState(base));
		for (BlockPos logpos : logsToKill) {
			if (RatUtils.canRatBreakBlock(this.rat.level(), logpos, this.rat)) {
				level.destroyBlock(logpos, true);
			}
		}
		this.handleLeafRemoval(level, base, baseBlock, logsToKill, this.treeSize);
	}

	private void handleLeafRemoval(Level level, BlockPos base, BlockState baseBlock, List<BlockPos> logstobreak, int logCount) {
		List<BlockPos> logs = new ArrayList<>();
		List<BlockPos> leaves = new ArrayList<>();

		BlockPos highestlog = base.immutable();
		for (BlockPos logpos : logstobreak) {
			if (logpos.getY() > highestlog.getY()) {
				highestlog = logpos.immutable();
			}

			for (BlockPos next : BlockPos.betweenClosed(base.getX() - 8, base.getY(), base.getZ() - 8, base.getX() + 8, RatTreeUtils.highestleaf.get(base), base.getZ() + 8)) {
				BlockState nextblock = level.getBlockState(next);
				if (RatTreeUtils.isTreeLog(nextblock)) {
					if (nextblock.is(baseBlock.getBlock()) || RatTreeUtils.areEqualLogTypes(baseBlock, nextblock)) {
						logs.add(next.immutable());
					}
				}
			}

			Pair<Integer, Integer> hv = RatTreeUtils.getHorizontalAndVerticalValue(logCount);
			int h = hv.getFirst();

			CopyOnWriteArrayList<BlockPos> leftoverleaves = new CopyOnWriteArrayList<>();

			BlockState leafblock = level.getBlockState(highestlog.above());
			for (BlockPos next : BlockPos.betweenClosed(base.getX() - h, base.getY(), base.getZ() - h, base.getX() + h, RatTreeUtils.highestleaf.get(base), base.getZ() + h)) {
				BlockState nextblock = level.getBlockState(next);

				if (!leafblock.is(nextblock.getBlock())) {
					continue;
				}

				if (RatTreeUtils.isTreeLeaf(nextblock)) {
					boolean logclose = false;
					for (BlockPos log : logs) {
						if (log.closerThan(next, 3)) {
							logclose = true;
							break;
						}
					}

					if (!logclose) {
						leaves.add(next.immutable());
					} else {
						leftoverleaves.add(next.immutable());
					}
				}
			}

			for (BlockPos leftoverleaf : leftoverleaves) {
				if (leftoverleaves.isEmpty()) {
					break;
				}

				Pair<Boolean, List<BlockPos>> connectedpair = RatTreeUtils.isConnectedToLogs(level, leftoverleaf);
				if (connectedpair.getFirst()) {
					for (BlockPos connectedpos : connectedpair.getSecond()) {
						leftoverleaves.remove(connectedpos);
					}
				} else {
					for (BlockPos connectedpos : connectedpair.getSecond()) {
						if (!leaves.contains(connectedpos)) {
							leaves.add(connectedpos.immutable());
						}

						leftoverleaves.remove(connectedpos);
					}
				}
			}
		}

		for (BlockPos leafPos : leaves) {
			if (this.sapling == null && this.rat.level().getBlockState(leafPos).is(BlockTags.LEAVES)) {
				this.sapling = RatTreeUtils.getSaplingFromLeaves((ServerLevel) this.rat.level(), this.rat.level().getBlockState(leafPos).getBlock());
			}
			if (RatUtils.canRatBreakBlock(this.rat.level(), leafPos, this.rat)) {
				level.destroyBlock(leafPos, true);
			}
		}

		RatTreeUtils.highestleaf.remove(base);
	}
}