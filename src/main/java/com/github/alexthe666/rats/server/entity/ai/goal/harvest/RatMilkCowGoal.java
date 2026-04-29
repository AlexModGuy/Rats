package com.github.alexthe666.rats.server.entity.ai.goal.harvest;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.message.UpdateRatFluidPacket;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RatMilkCowGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;
	private final Predicate<LivingEntity> COW_PREDICATE = entity -> entity != null && RatUtils.isCow(entity) && !entity.isBaby();

	public RatMilkCowGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!super.canUse() || !this.checkTheBasics(false, false)) {
			return false;
		}
		if (!this.rat.transportingFluid.isEmpty() && this.rat.transportingFluid.getAmount() >= this.rat.getMBTransferRate()) {
			return false;
		}
		this.resetTarget();
		return this.getTargetEntity() != null;
	}

	@Override
	public boolean canContinueToUse() {
		return this.getTargetEntity() != null && (this.rat.transportingFluid.isEmpty() || this.rat.transportingFluid.getAmount() < this.rat.getMBTransferRate());
	}

	@Override
	public void tick() {
		if (this.getTargetEntity() != null && this.getTargetEntity().isAlive() && (this.rat.transportingFluid.isEmpty() || this.rat.transportingFluid.getAmount() < this.rat.getMBTransferRate())) {
			this.rat.getNavigation().moveTo(this.getTargetEntity(), 1.25D);
			if (this.rat.distanceToSqr(this.getTargetEntity()) < this.rat.getRatHarvestDistance(0.0D)) {
				if (this.rat.transportingFluid.isEmpty()) {
					FluidBucketWrapper milkWrapper = new FluidBucketWrapper(new ItemStack(Items.MILK_BUCKET));
					// 1.21: FluidStack(FluidStack, int) replaced by copyWithAmount(int).
					FluidStack milkFluid = milkWrapper.getFluid().copyWithAmount(1000);
					if (milkFluid.isEmpty()) {
						milkFluid = new FluidStack(NeoForgeMod.MILK.get(), 1000);
					}
					if (this.rat.transportingFluid.isEmpty() || this.rat.transportingFluid.getAmount() < this.rat.getMBTransferRate()) {
						this.rat.transportingFluid = milkFluid.copy();
						if (!this.rat.level().isClientSide()) {
							PacketDistributor.sendToAllPlayers(new UpdateRatFluidPacket(this.rat.getId(), this.rat.transportingFluid));
						}
						this.rat.playSound(SoundEvents.COW_MILK, 1, 1);
						this.rat.gameEvent(GameEvent.ENTITY_INTERACT);
						this.stop();
					}
				}
			}
		} else {
			this.stop();
		}
	}

	private void resetTarget() {
		int radius = this.rat.getRadius();
		AABB bb = new AABB(-radius, -radius, -radius, radius, radius, radius).move(this.rat.getSearchCenter());
		List<LivingEntity> list = this.rat.level().getEntitiesOfClass(LivingEntity.class, bb, COW_PREDICATE);
		LivingEntity closestCow = null;
		for (LivingEntity base : list) {
			if (closestCow == null || base.distanceToSqr(this.rat) < closestCow.distanceToSqr(this.rat)) {
				closestCow = base;
			}
		}
		if (closestCow != null) {
			this.setTargetEntity(closestCow);
		}
	}
}
