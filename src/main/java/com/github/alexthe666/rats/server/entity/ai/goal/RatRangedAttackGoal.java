package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.SmallArrow;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.EnumSet;

public class RatRangedAttackGoal extends Goal implements RatWorkGoal {
	private final TamedRat rat;
	private final double speedModifier;
	private final int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;
	private int useTime = 0;

	public RatRangedAttackGoal(TamedRat mob, double speedModifier, int attackCooldown, float attackDistance) {
		this.rat = mob;
		this.speedModifier = speedModifier;
		this.attackCooldown = attackCooldown;
		this.maxAttackDistance = attackDistance * attackDistance;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	public boolean canUse() {
		return this.rat.getTarget() != null;
	}

	public boolean canContinueToUse() {
		return (this.canUse() || !this.rat.getNavigation().isDone());
	}

	public void start() {
		super.start();
		this.rat.setAggressive(true);
	}

	public void stop() {
		super.stop();
		this.rat.setAggressive(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.useTime = 0;
		this.rat.stopUsingItem();
	}

	public void tick() {
		LivingEntity entity = this.rat.getTarget();
		if (entity != null) {
			double d0 = this.rat.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
			boolean flag = this.rat.getSensing().hasLineOfSight(entity);
			boolean flag1 = this.seeTime > 0;
			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
				this.rat.getNavigation().stop();
				++this.strafingTime;
			} else {
				this.rat.getNavigation().moveTo(entity, this.speedModifier);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.rat.getRandom().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.rat.getRandom().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.rat.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
				this.rat.lookAt(entity, 30.0F, 30.0F);
			} else {
				this.rat.getLookControl().setLookAt(entity, 30.0F, 30.0F);
			}

			if (this.useTime > 0) {
				if (!flag && this.seeTime < -60) {
					this.useTime = 0;
				} else if (flag) {
					++this.useTime;
					if (this.useTime >= (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_CROSSBOW.get()) ? 40 : 20)) {
						this.performRangedAttack(entity);
						this.useTime = 0;
						this.attackTime = this.attackCooldown;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				++this.useTime;
			}
		}

	}

	public void performRangedAttack(LivingEntity target) {
		new ItemStack(Items.ARROW);
		AbstractArrow arrow = new SmallArrow(this.rat.getLevel(), this.rat);
		double d0 = target.getX() - this.rat.getX();
		double d1 = target.getY(0.3333333333333333D) - arrow.getY();
		double d2 = target.getZ() - this.rat.getZ();
		double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
		arrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.rat.getLevel().getDifficulty().getId() * 4));
		if (RatUpgradeUtils.hasUpgrade(this.rat, RatsItemRegistry.RAT_UPGRADE_CROSSBOW.get())) {
			arrow.setBaseDamage(arrow.getBaseDamage() + 2.5D);
		}

		this.rat.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.rat.getRandom().nextFloat() * 0.4F + 0.8F));
		this.rat.getLevel().addFreshEntity(arrow);
	}

	@Override
	public TaskType getRatTaskType() {
		return TaskType.ATTACK;
	}
}
