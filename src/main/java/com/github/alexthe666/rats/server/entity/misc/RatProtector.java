package com.github.alexthe666.rats.server.entity.misc;

import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.EtherealRatMoveControl;
import com.github.alexthe666.rats.server.entity.ai.navigation.navigation.EtherealRatNavigation;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class RatProtector extends AbstractRat {

	public RatProtector(EntityType<? extends AbstractRat> type, Level level) {
		super(type, level);
		this.moveControl = new EtherealRatMoveControl(this);
		this.navigation = new EtherealRatNavigation(this, this.getLevel());
	}

	@Override
	public int getExperienceReward() {
		return 0;
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
		if (flag) {
			this.kill();
		}
		return flag;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0D)
				.add(Attributes.MOVEMENT_SPEED, 1.65D)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 10) {
			this.discard();
			if (this.getLevel().isClientSide()) {
				for (int i = 0; i < 15; i++) {
					this.getLevel().addParticle(RatsParticleRegistry.RAT_GHOST.get(),
							this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
							this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()),
							this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
							0.92F, 0.82F, 0.0F);
				}
			}
		}
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	@Override
	public void tick() {
		super.tick();
		this.noPhysics = true;
		if (!this.getLevel().isClientSide()) {
			if (this.getTarget() == null || !this.getTarget().isAlive()) {
				this.kill();
			} else {
				LivingEntity target = this.getTarget();
				this.getMoveControl().setWantedPosition(target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 1);
			}
		}
	}

	@Override
	public boolean isTame() {
		return true;
	}
}
