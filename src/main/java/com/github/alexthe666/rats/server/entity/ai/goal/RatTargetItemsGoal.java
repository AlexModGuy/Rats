package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class RatTargetItemsGoal extends Goal {
	private final TamedRat rat;
	private final PathNavigation navigation;
	@Nullable
	private ItemEntity targetItem = null;

	public RatTargetItemsGoal(TamedRat rat) {
		this.rat = rat;
		this.navigation = rat.getNavigation();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		//we only want the rat to pick up items if it can actually move around
		if (!this.rat.canMove() || this.rat.isInCage() || this.rat.isCurrentlyWorking)
			return false;

		if (!this.rat.isTargetCommand() || this.rat.getDepositPos().isEmpty()) return false;

		if (this.checkIfRatCanHold()) {
			//sort through items we can grab, get the closest one
			List<ItemEntity> items = this.rat.getLevel().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(), item ->
					(item.isOnGround() || item.isInWater()) &&
							this.rat.canRatPickupItem(item.getItem()));
			if (items.isEmpty()) return false;

			items.sort(Comparator.comparingDouble(this.rat::distanceToSqr));

			for (ItemEntity item : items) {
				//please, only go after items you can actually reach
				Path toPath = this.navigation.createPath(item, 1);
				if (toPath != null && toPath.canReach()) {
					if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
						if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) && !ItemStack.isSameItemSameTags(item.getItem(), this.rat.getItemInHand(InteractionHand.MAIN_HAND)))
							continue;
					}
					this.targetItem = item;
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkIfRatCanHold() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.isEmpty()) {
			return true;
		}
		return RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) && stack.getCount() < 64;
	}

	protected AABB getTargetableArea() {
		if (this.rat.isTame() && this.rat.getRadiusCenter().isPresent()) {
			Vec3 center = new Vec3(this.rat.getSearchCenter().getX() + 0.5D, this.rat.getSearchCenter().getY() + 0.5D, this.rat.getSearchCenter().getZ() + 0.5D);
			double radius = this.rat.getRadius();
			AABB aabb = new AABB(-radius, -radius, -radius, radius, radius, radius);
			return aabb.move(center);
		}
		return this.rat.getBoundingBox().inflate(RatConfig.defaultRatRadius);
	}

	@Override
	public boolean canContinueToUse() {
		return this.rat.isAlive() && !this.rat.isCurrentlyWorking && !this.navigation.isDone() && !this.navigation.isStuck() && this.targetItem != null && !this.targetItem.isRemoved() && this.checkIfRatCanHold();
	}

	@Override
	public void start() {
		if (this.targetItem != null) {
			this.navigation.moveTo(this.targetItem, 1.25D);
		}
	}

	@Override
	public void stop() {
		this.targetItem = null;
	}

	@Override
	public void tick() {
		if (this.targetItem != null && !this.targetItem.isRemoved()) {
			if (this.rat.distanceToSqr(this.targetItem) < this.rat.getRatHarvestDistance(0.0D)) {
				ItemStack duplicate = this.targetItem.getItem().copy();
				if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get())) {
					ItemStack alreadyHolding = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
					int extractSize = alreadyHolding.getCount() + duplicate.getCount() < 64 ? duplicate.getCount() : Math.min(64 - alreadyHolding.getCount(), duplicate.getCount());
					duplicate.setCount(extractSize);
					this.targetItem.getItem().shrink(extractSize);
					this.rat.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(duplicate.getItem(), alreadyHolding.getCount() + extractSize));
				} else {
					duplicate.setCount(1);
					this.targetItem.getItem().shrink(1);
					if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
						this.rat.spawnAtLocation(this.rat.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
					}
					this.rat.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
				}
				this.stop();
			} else {
				this.rat.getNavigation().moveTo(this.targetItem, 1.225F);
			}
		}
	}
}