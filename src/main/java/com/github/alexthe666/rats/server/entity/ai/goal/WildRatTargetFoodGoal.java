package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class WildRatTargetFoodGoal extends Goal {
	private final AbstractRat rat;
	private final PathNavigation navigation;
	@Nullable
	private ItemEntity targetItem = null;

	public WildRatTargetFoodGoal(AbstractRat rat) {
		this.rat = rat;
		this.navigation = rat.getNavigation();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		//we only want the rat to pick up items if it can actually move around and has an empty hand
		if (!this.rat.canMove() || this.rat.getOwner() != null || !this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || this.rat.isEating())
			return false;

		if (this.rat.getRandom().nextInt(reducedTickDelay(RatConfig.ratUpdateDelay)) != 0) {
			return false;
		}

		//sort through items we can grab, get the closest one
		List<ItemEntity> items = this.rat.getLevel().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(), item ->
				(item.isOnGround() || item.isInWater()) &&
						RatUtils.isRatFood(item.getItem()));
		if (items.isEmpty()) return false;

		items.sort(Comparator.comparingDouble(this.rat::distanceToSqr));

		for (ItemEntity item : items) {
			//please, only go after items you can actually reach
			Path toPath = this.navigation.createPath(item, 1);
			if (toPath != null && toPath.canReach()) {
				this.targetItem = item;
				return true;
			}
		}
		return false;
	}

	protected AABB getTargetableArea() {
		return this.rat.getBoundingBox().inflate(16.0F);
	}

	@Override
	public boolean canContinueToUse() {
		return this.rat.isAlive() && !this.navigation.isDone() && !this.navigation.isStuck() && this.targetItem != null && !this.targetItem.isRemoved();
	}

	@Override
	public void start() {
		if (this.targetItem != null) {
			this.navigation.moveTo(this.targetItem, 1.25D);
		}
	}

	@Override
	public void tick() {
		if (this.targetItem != null && !this.targetItem.isRemoved() && this.rat.distanceToSqr(this.targetItem) < 3.5F && this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
			ItemStack duplicate = this.targetItem.getItem().copy();
			duplicate.setCount(1);
			this.targetItem.getItem().shrink(1);
			if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && !this.rat.getLevel().isClientSide()) {
				this.rat.spawnAtLocation(this.rat.getItemInHand(InteractionHand.MAIN_HAND), 0.0F);
			}
			this.rat.setItemInHand(InteractionHand.MAIN_HAND, duplicate);
			if (this.rat instanceof Rat wildBoi && !wildBoi.hasPlague() && this.targetItem.getOwner() instanceof Player targetPlayer) {
				if (duplicate.is(RatsItemTags.CHEESE_ITEMS)) {
					wildBoi.setFleePos(this.targetItem.blockPosition());
					wildBoi.wildTrust += 10 + wildBoi.getRandom().nextInt(10);
					wildBoi.cheeseFeedings++;
					wildBoi.getLevel().broadcastEntityEvent(wildBoi, (byte) 82);
					if (wildBoi.wildTrust >= 100 && wildBoi.getRandom().nextInt(3) == 0 || wildBoi.cheeseFeedings >= 15) {
						TamedRat tamedRat = RatUtils.tameRat(wildBoi, wildBoi.getLevel());
						tamedRat.getLevel().broadcastEntityEvent(wildBoi, (byte) 83);
						tamedRat.tame(targetPlayer);
						tamedRat.setCommand(RatCommand.SIT);
					}
				}
			}
			this.stop();
		}
	}
}
