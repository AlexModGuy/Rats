package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.ai.goal.RatWorkGoal;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public abstract class BaseRatHarvestGoal extends Goal implements RatWorkGoal {

	private final BlockSorter targetSorter;
	@Nullable
	private BlockPos targetBlock = null;
	@Nullable
	private Entity targetEntity = null;
	protected final TamedRat rat;

	protected int nextStartTick;

	protected BaseRatHarvestGoal(TamedRat rat) {
		this.rat = rat;
		this.targetSorter = new BlockSorter(rat);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
	}

	public boolean checkTheBasics(boolean checkForItems, boolean checkForItemHolding) {
		if (!this.rat.canMove() || this.rat.getCommand() != RatCommand.HARVEST || this.rat.isInCage()) return false;
		if (checkForItemHolding && !this.checkIfItemCanBeHeld()) return false;
		return !checkForItems || !this.anyHoldableItemsAround();
	}

	@Override
	public boolean canUse() {
		if (this.nextStartTick > 0) {
			this.nextStartTick--;
			return false;
		}
		this.nextStartTick = this.adjustedTickDelay(RatConfig.ratHarvestDelay);
		return true;
	}

	protected boolean checkIfItemCanBeHeld() {
		ItemStack stack = this.rat.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.isEmpty()) {
			return true;
		}
		return RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) && stack.getCount() < 64;
	}

	private boolean anyHoldableItemsAround() {
		List<ItemEntity> items = this.rat.level().getEntitiesOfClass(ItemEntity.class, this.getTargetableArea(), item ->
				(item.onGround() || item.isInWater()) &&
						this.rat.canRatPickupItem(item.getItem()));
		if (items.isEmpty()) return false;

		items.sort(Comparator.comparingDouble(this.rat::distanceToSqr));

		List<ItemEntity> revisedList = new ArrayList<>();
		for (ItemEntity item : items) {
			//please, only go after items you can actually reach
			Path toPath = this.rat.getNavigation().createPath(item, 1);
			if (toPath != null && toPath.canReach()) {
				if (!this.rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
					if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) && !ItemStack.isSameItemSameTags(item.getItem(), this.rat.getItemInHand(InteractionHand.MAIN_HAND)))
						continue;
				}
				revisedList.add(item);
			}
		}

		return !revisedList.isEmpty();
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
	public void start() {
		this.rat.isCurrentlyWorking = true;
	}

	@Override
	public void stop() {
		this.rat.isCurrentlyWorking = false;
		this.rat.getNavigation().stop();
		this.setTargetBlock(null);
		this.setTargetEntity(null);
	}

	public BlockSorter getTargetSorter() {
		return this.targetSorter;
	}

	@Nullable
	public BlockPos getTargetBlock() {
		return this.targetBlock;
	}

	public void setTargetBlock(@Nullable BlockPos pos) {
		this.targetBlock = pos;
	}

	@Nullable
	public Entity getTargetEntity() {
		return this.targetEntity;
	}

	public void setTargetEntity(@Nullable Entity entity) {
		this.targetEntity = entity;
	}

	@Override
	public TaskType getRatTaskType() {
		return TaskType.HARVEST;
	}

	protected record BlockSorter(Entity entity) implements Comparator<BlockPos> {

		@Override
		public int compare(BlockPos pos1, BlockPos pos2) {
			double distance1 = this.distanceTo(pos1);
			double distance2 = this.distanceTo(pos2);
			return Double.compare(distance1, distance2);
		}

		private double distanceTo(BlockPos pos) {
			double deltaX = this.entity.getX() - (pos.getX() + 0.5);
			double deltaY = this.entity.getY() + this.entity.getEyeHeight() - (pos.getY() + 0.5);
			double deltaZ = this.entity.getZ() - (pos.getZ() + 0.5);
			return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
		}
	}
}
