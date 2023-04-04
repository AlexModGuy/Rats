package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

public class AsbestosRatUpgradeItem extends BaseRatUpgradeItem implements DamageImmunityUpgrade {
	public AsbestosRatUpgradeItem(Properties properties) {
		super(properties, 0, 4);
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_FIRE);
	}
}
