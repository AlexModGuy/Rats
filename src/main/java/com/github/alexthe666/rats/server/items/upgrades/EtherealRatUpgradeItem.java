package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class EtherealRatUpgradeItem extends BaseRatUpgradeItem implements ChangesTextureUpgrade, ChangesOverlayUpgrade {
	public EtherealRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public @Nullable RenderType getOverlayTexture(TamedRat rat, float partialTicks) {
		float f = (float) rat.tickCount + partialTicks;
		return RenderType.energySwirl(new ResourceLocation(RatsMod.MODID, "textures/entity/psychic.png"), f * 0.01F, f * 0.01F);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/ghost_pirat/ghost_pirat.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return false;
	}
}
