package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class NonbelieverRatUpgradeItem extends StatBoostingRatUpgradeItem implements ChangesOverlayUpgrade, GlowingEyesUpgrade {
	public NonbelieverRatUpgradeItem(Properties properties) {
		super(properties, 3, 0, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.nonbelieverHealthUpgrade, Attributes.ARMOR, RatConfig.nonbelieverArmorUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.nonbelieverDamageUpgrade), true);
	}

	@Override
	public @Nullable RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks) {
		return RatsRenderType.getGreenGlint();
	}

	@Override
	public RenderType getEyeTexture(ItemStack stack) {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/eyes/nonbeliever.png"));
	}
}
