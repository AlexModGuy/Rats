package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;

public class SupportRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade, HoldsItemUpgrade {

	public SupportRatUpgradeItem(Properties properties) {
		super(properties, 1, 2);
	}

	@Override
	public void tick(TamedRat rat) {
		ItemStack potionToThrow;
		if (rat.tickCount % 300 == 0) {
			if (rat.getOwner() instanceof Player player && EntitySelector.LIVING_ENTITY_STILL_ALIVE.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR).test(player)) {
				if (player.getHealth() < player.getMaxHealth()) {
					potionToThrow = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), rat.getRandom().nextBoolean() ? Potions.STRONG_HEALING : Potions.HEALING);
				} else {
					potionToThrow = switch (rat.getRandom().nextInt(20)) {
						case 0 -> PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRENGTH);
						case 3 -> PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.REGENERATION);
						case 9 -> PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LONG_SWIFTNESS);
						case 11 ->
								PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.STRONG_STRENGTH);
						case 17 -> PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), Potions.LONG_SWIFTNESS);
						default -> null;
					};
				}
				if (potionToThrow != null) {
					Vec3 vec3 = player.getDeltaMovement();
					double d0 = player.getX() + vec3.x() - rat.getX();
					double d1 = player.getEyeY() - (double) 1.1F - rat.getY();
					double d2 = player.getZ() + vec3.z() - rat.getZ();
					double d3 = Math.sqrt(d0 * d0 + d2 * d2);

					ThrownPotion potion = new ThrownPotion(rat.level(), rat);
					potion.setItem(potionToThrow);
					potion.setXRot(potion.getXRot() - -20.0F);
					potion.shoot(d0, d1 + d3 * 0.2D, d2, 0.75F, 0.0F);
					if (!rat.isSilent()) {
						rat.level().playSound(null, rat.getX(), rat.getY(), rat.getZ(), SoundEvents.WITCH_THROW, rat.getSoundSource(), 1.0F, 0.8F + rat.getRandom().nextFloat() * 0.4F);
					}

					rat.level().addFreshEntity(potion);
				}
			}
		}
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		model.translateToBody(stack);
		stack.pushPose();
		stack.scale(0.5F, 0.5F, 0.5F);
		stack.mulPose(Axis.XP.rotationDegrees(-90));
		stack.mulPose(Axis.YP.rotationDegrees(90));
		stack.mulPose(Axis.ZP.rotationDegrees(-90));
		stack.translate(0.0F, -0.1F, 0.275F);
		Minecraft.getInstance().getItemRenderer().renderStatic(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HEALING), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, rat.level(), rat.getId());
		stack.translate(0.35F, 0.1F, 0.0F);
		Minecraft.getInstance().getItemRenderer().renderStatic(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SLOW_FALLING), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, rat.level(), rat.getId());
		stack.popPose();
		stack.pushPose();
		stack.scale(0.5F, 0.5F, 0.5F);
		stack.mulPose(Axis.XP.rotationDegrees(-90));
		stack.mulPose(Axis.YP.rotationDegrees(90));
		stack.mulPose(Axis.ZP.rotationDegrees(-90));
		stack.translate(0.0F, -0.1F, -0.4F);
		Minecraft.getInstance().getItemRenderer().renderStatic(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.SWIFTNESS), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, rat.level(), rat.getId());
		stack.translate(0.35F, 0.1F, 0.0F);
		Minecraft.getInstance().getItemRenderer().renderStatic(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.LUCK), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, rat.level(), rat.getId());
		stack.popPose();
	}

	@Override
	public boolean isFakeHandRender() {
		return true;
	}
}
