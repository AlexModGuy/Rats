package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;

public class UnderwaterRatUpgradeItem extends BaseRatUpgradeItem implements DamageImmunityUpgrade {
	public UnderwaterRatUpgradeItem(Properties properties) {
		super(properties, 0, 3);
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.WITCH_RESISTANT_TO) || source.is(DamageTypes.DROWN);
	}
}
