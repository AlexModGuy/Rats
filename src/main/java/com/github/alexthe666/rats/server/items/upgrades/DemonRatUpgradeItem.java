package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DemonRatUpgradeItem extends StatBoostingRatUpgradeItem implements GlowingEyesUpgrade, DamageImmunityUpgrade, PostAttackUpgrade, ChangesOverlayUpgrade {
	public DemonRatUpgradeItem(Properties properties) {
		super(properties, 1, 2, () -> Map.of(Attributes.MAX_HEALTH, RatConfig.demonHealthUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.demonDamageUpgrade), false);
	}

	public static boolean isSoulVersion(ItemStack stack) {
		return stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBoolean("Soul");
	}

	public static ItemStack getDemonUpgrade(boolean soul) {
		ItemStack stack = new ItemStack(RatsItemRegistry.RAT_UPGRADE_DEMON.get());
		net.minecraft.nbt.CompoundTag tag = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
		tag.putBoolean("Soul", soul);
		stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
		return stack;
	}

	@Override
	public RenderType getEyeTexture(ItemStack stack) {
		if (isSoulVersion(stack)) {
			return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/demon_rat/soul_demon_rat_eye.png"));
		}
		return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/demon_rat/demon_rat_eye.png"));
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypeTags.IS_FIRE);
	}

	@Override
	public void afterHit(TamedRat rat, LivingEntity target) {
		if (!target.fireImmune()) target.igniteForSeconds(10);
	}

	@Override
	public @Nullable RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks) {
		if (isSoulVersion(stack)) {
			return RenderType.entitySmoothCutout(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/demon_rat/soul_demon_rat.png"));
		}
		return RenderType.entitySmoothCutout(ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/entity/demon_rat/demon_rat.png"));
	}
}
