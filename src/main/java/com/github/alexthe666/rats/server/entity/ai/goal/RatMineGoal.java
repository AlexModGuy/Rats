package com.github.alexthe666.rats.server.entity.ai.goal;

public class RatMineGoal {
}/* extends BaseRatHarvestGoal {
	private final TamedRat rat;
	private int breakingTime;
	private int previousBreakProgress;
	private BlockState prevMiningState = null;

	public RatMineGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(false, this.rat.getDepositPos().isPresent())) {
			return false;
		}
		this.resetTarget();
		return this.getTargetBlock() != null;
	}

	private void resetTarget() {
		List<BlockPos> allBlocks = new ArrayList<>();
		NonNullList<ItemStack> mining = this.getMiningList();
		int RADIUS = this.rat.getRadius();
		for (BlockPos pos : BlockPos.betweenClosedStream(this.rat.getSearchCenter().offset(-RADIUS, -RADIUS, -RADIUS), this.rat.getSearchCenter().offset(RADIUS, RADIUS, RADIUS)).map(BlockPos::immutable).toList()) {
			if (this.doesListContainBlock(this.rat.level(), mining, pos)) {
				allBlocks.add(pos);
			}
		}
		if (!allBlocks.isEmpty()) {
			allBlocks.sort(this.getTargetSorter());
			this.setTargetBlock(allBlocks.get(0));
		}
	}

	@Nullable
	private NonNullList<ItemStack> getMiningList() {
		if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_MINER.get())) {
			CompoundTag tag = RatUpgradeUtils.getUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_MINER.get()).getTag();
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
			if (tag != null && tag.contains("Items", 9)) {
				ContainerHelper.loadAllItems(tag, nonnulllist);
			}
			return nonnulllist;
		} else {
			return null;
		}
	}

	private boolean doesListContainBlock(Level world, @Nullable NonNullList<ItemStack> list, BlockPos pos) {
		if (RatUtils.isBlockProtected(world, pos, this.rat)) return false;
		BlockState state = world.getBlockState(pos);
		ItemStack stack = state.getBlock().getCloneItemStack(world, pos, state);
		if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_MINER.get()) && list != null && !list.isEmpty()) {
			for (ItemStack currentStack : list) {
				if (currentStack.sameItem(stack)) {
					return true;
				}
			}
		}
		if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get())) {
			return state.is(Tags.Blocks.ORES);
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.checkTheBasics(false, true) && this.getTargetBlock() != null;
	}

	@Override
	public void tick() {
		if (this.getTargetBlock() != null) {
			BlockPos rayPos = RatPathingHelper.clipWithConditions(this.rat.level(), new ClipContext(this.rat.position(), Vec3.atCenterOf(this.getTargetBlock()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.rat), false).getBlockPos();
			this.rat.getNavigation().moveTo(rayPos.getX() + 0.5D, rayPos.getY(), rayPos.getZ() + 0.5D, 1.25D);
			if (!this.rat.getMoveControl().hasWanted() && (this.rat.isOnGround() || this.rat.isRidingSpecialMount())) {
				BlockState block = this.rat.level().getBlockState(rayPos);
				SoundType soundType = block.getBlock().getSoundType(block, this.rat.level(), rayPos, null);
				if (this.rat.blockPosition().getY() < rayPos.getY()) {
					if (this.rat.level().getBlockState(this.rat.blockPosition()).canBeReplaced() && this.rat.level().isEmptyBlock(this.rat.blockPosition().above())) {
						this.rat.getJumpControl().jump();
						this.rat.level().setBlockAndUpdate(this.rat.blockPosition(), Blocks.COBBLESTONE.defaultBlockState());
					}
				}
				if (block.getMaterial().blocksMotion() && block.getMaterial() != Material.AIR) {
					double distance = this.rat.getRatDistanceCenterSq(rayPos.getX() + 0.5D, rayPos.getY() + 0.5D, rayPos.getZ() + 0.5D);
					if (distance < this.rat.getRatHarvestDistance(2.0D)) {
						this.rat.level().broadcastEntityEvent(this.rat, (byte) 85);
						this.rat.crafting = true;
						if (block == this.prevMiningState) {
							this.rat.level().broadcastEntityEvent(this.rat, (byte) 85);
							this.rat.crafting = true;
						} else {
							this.rat.level().broadcastEntityEvent(this.rat, (byte) 86);
							this.rat.crafting = false;
						}
						if (distance < this.rat.getRatHarvestDistance(-1.0D)) {
							this.rat.setDeltaMovement(Vec3.ZERO);
							this.rat.getNavigation().stop();
						}
						this.breakingTime++;
						int hardness = (int) (block.getDestroySpeed(this.rat.level(), rayPos) * (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_MINER.get()) ? 10 : 20));
						int i = (int) ((float) this.breakingTime / hardness * 10.0F);
						if (this.breakingTime % 5 == 0) {
							this.rat.playSound(soundType.getHitSound(), soundType.volume + 1, soundType.pitch);
						}
						if (i != this.previousBreakProgress) {
							this.rat.level().destroyBlockProgress(this.rat.getId(), rayPos, i);
							this.previousBreakProgress = i;
						}
						if (this.breakingTime >= hardness) {
							this.rat.level().broadcastEntityEvent(this.rat, (byte) 86);
							this.rat.playSound(soundType.getBreakSound(), soundType.volume, soundType.pitch);
							this.breakingTime = 0;
							this.previousBreakProgress = -1;
							if (!RatUtils.isBlockProtected(this.rat.level(), rayPos, this.rat)) {
								this.rat.level().destroyBlock(rayPos, true);
							}
							if (this.rat.level().isEmptyBlock(this.getTargetBlock())) {
								this.stop();
							}
						}
						this.prevMiningState = block;
					}
				}
			}
		}
	}
}
*/