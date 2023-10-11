package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.StatBoostingUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SculkedRatUpgradeItem extends BaseRatUpgradeItem implements StatBoostingUpgrade, ChangesTextureUpgrade, GlowingEyesUpgrade {
	public SculkedRatUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/sculked.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return true;
	}

	@Override
	public RenderType getEyeTexture(ItemStack stack) {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/eyes/sculked.png"));
	}

	@Override
	public Map<Attribute, Double> getAttributeBoosts() {
		return Map.of(Attributes.MAX_HEALTH, 18.0D);
	}
}
