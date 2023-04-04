package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.data.tags.RatsBlockTags;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.entity.rat.DiggingRat;
import com.github.alexthe666.rats.server.misc.RatPathingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.event.ForgeEventFactory;

public class RatRaidChestsGoal extends RatMoveToBlockGoal {

	private final DiggingRat entity;

	public RatRaidChestsGoal(DiggingRat entity) {
		super(entity, 1.0F, 16);
		this.entity = entity;
	}

	@Override
	public boolean canUse() {
		if (!this.entity.canMove() || this.entity.getOwner() != null || !RatConfig.ratsStealItems) {
			return false;
		}
		if (!this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
			return false;
		}
		if (this.nextStartTick <= 0) {
			if (!ForgeEventFactory.getMobGriefingEvent(this.entity.getLevel(), this.entity)) {
				return false;
			}
		}
		return super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return super.canContinueToUse() && this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isReachedTarget()) {
			BlockEntity entity = this.entity.getLevel().getBlockEntity(this.blockPos);
			if (entity instanceof Container feeder) {
				double distance = this.entity.distanceToSqr(this.blockPos.getX(), this.blockPos.getY(), this.blockPos.getZ());
				if (distance < 6.25F && distance > 2.72F) {
					this.toggleChest(feeder, true);
					this.entity.getLevel().playSound(null, this.blockPos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS);
				}
				if (distance <= 2.89F) {
					this.toggleChest(feeder, false);
					this.entity.getLevel().playSound(null, this.blockPos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS);
					ItemStack stack = RatUtils.getFoodFromInventory(feeder, this.entity.getLevel().getRandom());
					if (stack != ItemStack.EMPTY) {
						ItemStack duplicate = stack.copy();
						duplicate.setCount(1);
						if (!this.entity.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
							this.entity.spawnAtLocation(this.entity.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
						}
						this.entity.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
						stack.shrink(1);
						if (RatConfig.ratsContaminateFood && this.entity.getRandom().nextInt(3) == 0) {
							int slotToReplace = RatUtils.getContaminatedSlot(feeder, this.entity.getLevel().getRandom());
							if (slotToReplace != -1) {
								if (feeder.getItem(slotToReplace).isEmpty()) {
									ItemStack stack1 = new ItemStack(RatsItemRegistry.CONTAMINATED_FOOD.get());
									feeder.setItem(slotToReplace, stack1);
								} else if (feeder.getItem(slotToReplace).is(RatsItemRegistry.CONTAMINATED_FOOD.get())) {
									feeder.getItem(slotToReplace).grow(1);
								}
							}
						}
						this.entity.setFleePos(this.blockPos);
					}
				}
			}
		}
	}

	@Override
	protected boolean isValidTarget(LevelReader reader, BlockPos pos) {
		if (!reader.getBlockState(pos).is(RatsBlockTags.UNRAIDABLE_CONTAINERS)) {
			if (reader.getBlockState(pos).getBlock() instanceof EntityBlock) {
				if (RatUtils.isBlockProtected(this.entity.getLevel(), pos, this.entity)) return false;
				if (!RatPathingHelper.canSeeOrDigToBlock(this.entity, pos)) return false;
				BlockEntity entity = reader.getBlockEntity(pos);
				if (entity instanceof Container inventory) {
					try {
						//first lets check if the container has an ungenerated loot table. If so we don't want rats digging through those
						if (entity instanceof RandomizableContainerBlockEntity container && container.saveWithFullMetadata().contains(RandomizableContainerBlockEntity.LOOT_TABLE_TAG))
							return false;
						if (!inventory.isEmpty() && RatUtils.doesContainFood(inventory)) {
							return true;
						}
					} catch (Exception e) {
						RatsMod.LOGGER.warn("Rats stopped a " + entity.getClass().getSimpleName() + " from causing a crash during access");
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}

	public void toggleChest(Container te, boolean open) {
		if (te instanceof ChestBlockEntity chest) {
			if (open) {
				this.entity.getLevel().blockEvent(this.blockPos, chest.getBlockState().getBlock(), 1, 1);
			} else {
				this.entity.getLevel().blockEvent(this.blockPos, chest.getBlockState().getBlock(), 1, 0);

			}
		}
	}
}
