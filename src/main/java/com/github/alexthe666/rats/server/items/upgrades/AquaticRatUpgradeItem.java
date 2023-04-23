package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;

public class AquaticRatUpgradeItem extends BaseRatUpgradeItem implements ChangesTextureUpgrade, DamageImmunityUpgrade {
	public AquaticRatUpgradeItem(Properties properties) {
		super(properties, 0, 3);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/aquatic.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return true;
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_DROWNING);
	}
}
