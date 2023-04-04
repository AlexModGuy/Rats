package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import net.minecraft.resources.ResourceLocation;

public class UndeadRatUpgradeItem extends BaseRatUpgradeItem implements ChangesTextureUpgrade {
	public UndeadRatUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_skeleton.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return false;
	}
}
