package com.github.alexthe666.rats.server.entity.misc;

import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.Ratlanteans;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Ratfish extends AbstractSchoolingFish implements Ratlanteans {
	public Ratfish(EntityType<? extends Ratfish> type, Level world) {
		super(type, world);
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(RatlantisItemRegistry.RATFISH_BUCKET.get());
	}

	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RATFISH_AMBIENT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RATFISH_DEATH.get();
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return RatsSoundRegistry.RATFISH_HURT.get();
	}

	protected SoundEvent getFlopSound() {
		return RatsSoundRegistry.RATFISH_FLOP.get();
	}
}