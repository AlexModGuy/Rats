package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class CreativeRatUpgradeItem extends BaseRatUpgradeItem implements DamageImmunityUpgrade {
	public CreativeRatUpgradeItem(Properties properties) {
		super(properties, 3, 1);
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.getEntity() == null || source.getEntity() instanceof LivingEntity living && !rat.isOwnedBy(living);
	}
}
