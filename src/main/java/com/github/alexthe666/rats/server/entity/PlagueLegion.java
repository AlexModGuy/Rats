package com.github.alexthe666.rats.server.entity;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public interface PlagueLegion {
	Predicate<LivingEntity> NOT_PLAGUE = entity -> entity.isAlive() && !(entity instanceof PlagueLegion);
}
