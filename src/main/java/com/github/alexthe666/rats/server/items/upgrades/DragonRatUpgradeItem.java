package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.projectile.RatDragonFire;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.RatSackItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.StatBoostingUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DragonRatUpgradeItem extends BaseFlightRatUpgradeItem implements StatBoostingUpgrade, ChangesTextureUpgrade, TickRatUpgrade, GlowingEyesUpgrade {
	public DragonRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public ItemStack getWing() {
		return new ItemStack(RatsItemRegistry.DRAGON_WING.get());
	}

	@Override
	public Map<Attribute, Double> getAttributeBoosts() {
		return Map.of(Attributes.MAX_HEALTH, RatConfig.dragonHealthUpgrade, Attributes.ARMOR, RatConfig.dragonArmorUpgrade, Attributes.ATTACK_DAMAGE, RatConfig.dragonDamageUpgrade);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		this.getAttributeBoosts().forEach((attribute, aDouble) -> tooltip.add(Component.translatable(RatsLangConstants.RAT_UPGRADE_STAT_BOOST, aDouble.toString(), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.AQUA)));
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/dragon.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return true;
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.getVisualFlag() && rat.visualCooldown == 0) {
			rat.setVisualFlag(false);
		}
		if (rat.rangedAttackCooldown == 0 && rat.getTarget() != null) {
			rat.rangedAttackCooldown = 5;
			float radius = 0.3F;
			float angle = (0.01745329251F * (rat.yBodyRot));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + rat.getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + rat.getZ();
			double extraY = 0.2 + rat.getY();
			double targetRelativeX = rat.getTarget().getX() - extraX;
			double targetRelativeY = rat.getTarget().getY() + rat.getTarget().getBbHeight() / 2 - extraY;
			double targetRelativeZ = rat.getTarget().getZ() - extraZ;
			rat.playSound(RatsSoundRegistry.RAT_SHOOT.get(), 1.0F, 1.25F + rat.getRandom().nextFloat() * 0.5F);
			RatDragonFire beam = new RatDragonFire(RatsEntityRegistry.RAT_DRAGON_FIRE.get(), rat, rat.level(), targetRelativeX, targetRelativeY, targetRelativeZ);
			beam.setPos(extraX, extraY, extraZ);
			if (!rat.level().isClientSide()) {
				rat.level().addFreshEntity(beam);
			}
		}
		rat.clearFire();
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return super.isImmuneToDamageSource(rat, source) || source.is(DamageTypeTags.IS_FIRE);
	}

	@Override
	public RenderType getEyeTexture() {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/eyes/dragon.png"));
	}
}
