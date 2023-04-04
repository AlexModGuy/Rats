package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.PostAttackUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DemonRatUpgradeItem extends StatBoostingRatUpgradeItem implements GlowingEyesUpgrade, DamageImmunityUpgrade, PostAttackUpgrade, ChangesOverlayUpgrade {
	public DemonRatUpgradeItem(Properties properties) {
		super(properties, 1, 2, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.demonHealthUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.demonDamageUpgrade), false);
	}

	@Override
	public RenderType getEyeTexture() {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/demon_rat_eye.png"));
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_FIRE);
	}

	@Override
	public void afterHit(TamedRat rat, LivingEntity target) {
		if (!target.fireImmune()) target.setSecondsOnFire(10);
	}

	@Override
	public @Nullable RenderType getOverlayTexture(TamedRat rat, float partialTicks) {
		return RenderType.entitySmoothCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/demon_rat.png"));
	}
}
