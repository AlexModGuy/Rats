package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IForgeShearable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RatUseShearsGoal extends BaseRatHarvestGoal {
	private static final ItemStack SHEAR_STACK = new ItemStack(Items.SHEARS);
	private final TamedRat rat;
	private final Predicate<LivingEntity> SHEAR_PREDICATE = (com.google.common.base.Predicate<LivingEntity>) entity -> entity instanceof IForgeShearable && ((IForgeShearable) entity).isShearable(SHEAR_STACK, entity.getLevel(), entity.blockPosition());

	public RatUseShearsGoal(TamedRat rat) {
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
		return this.getTargetEntity() != null;
	}

	@Override
	public void tick() {
		if (this.getTargetEntity() != null && this.getTargetEntity().isAlive() && this.rat.getMainHandItem().isEmpty()) {
			this.rat.getNavigation().moveTo(this.getTargetEntity(), 1.25D);
			if (this.rat.distanceToSqr(this.getTargetEntity()) < 1.5D * this.rat.getRatDistanceModifier()) {
				if (this.getTargetEntity() instanceof IForgeShearable shearable) {
					List<ItemStack> drops = shearable.onSheared(null, SHEAR_STACK, this.rat.getLevel(), this.getTargetEntity().blockPosition(), 0);
					for (ItemStack stack : drops) {
						this.getTargetEntity().spawnAtLocation(stack, 0.0F);
					}
				}
				this.setTargetEntity(null);
				this.stop();
			}
		} else {
			this.stop();
		}
	}

	private void resetTarget() {
		int radius = this.rat.getRadius();
		AABB bb = new AABB(-radius, -radius, -radius, radius, radius, radius).move(this.rat.getSearchCenter());
		List<LivingEntity> list = this.rat.getLevel().getEntitiesOfClass(LivingEntity.class, bb, SHEAR_PREDICATE);
		LivingEntity closestSheep = null;
		for (LivingEntity base : list) {
			if (closestSheep == null || base.distanceToSqr(this.rat) < closestSheep.distanceToSqr(this.rat)) {
				closestSheep = base;
			}
		}
		if (closestSheep != null) {
			this.setTargetEntity(closestSheep);
		}
	}
}