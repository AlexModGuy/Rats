package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.ArrowlikeProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class GolemBeam extends ArrowlikeProjectile {

	public GolemBeam(EntityType<? extends ArrowlikeProjectile> type, Level level) {
		super(type, level);
		this.setBaseDamage(8F);
	}

	public GolemBeam(EntityType<? extends ArrowlikeProjectile> type, Level level, LivingEntity shooter) {
		super(type, shooter, level);
		this.setBaseDamage(6.0F * 0.5F + 0.5F);
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public boolean explodesOnHit() {
		return true;
	}
}
