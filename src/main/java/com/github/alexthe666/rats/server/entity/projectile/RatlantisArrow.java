package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RatlantisArrow extends AbstractArrow {

	public RatlantisArrow(EntityType<? extends AbstractArrow> arrow, Level level) {
		super(arrow, level);
		this.setBaseDamage(4.0D);
	}

	public RatlantisArrow(Level level, LivingEntity shooter) {
		this(level, shooter, null);
	}

	public RatlantisArrow(Level level, LivingEntity shooter, ItemStack weapon) {
		// 1.21: AbstractArrow ctor takes (type, shooter, level, pickup, weapon). Threading the source bow
		// through as `weapon` is what lets vanilla apply Power/Punch/Flame enchant effects on hit (via
		// EnchantmentHelper.modifyDamage / modifyKnockback / etc.).
		super(RatlantisEntityRegistry.RATLANTIS_ARROW.get(), shooter, level, new ItemStack(Items.ARROW), weapon);
		this.setBaseDamage(4.0D);
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return new ItemStack(Items.ARROW);
	}
}
