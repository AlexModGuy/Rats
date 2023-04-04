package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class StriderMountRatUpgradeItem<T extends Mob & RatMount> extends MountRatUpgradeItem<T> implements HoldsItemUpgrade {
	public StriderMountRatUpgradeItem(Properties properties, int rarity, int textLength, RegistryObject<EntityType<T>> entityType) {
		super(properties, rarity, textLength, entityType);
	}

	@Override
	public boolean playIdleAnimation(TamedRat rat) {
		return false;
	}

	@Override
	public void renderHeldItem(TamedRat rat, RatModel<?> model, PoseStack stack, MultiBufferSource buffer, int light, float ageInTicks) {
		stack.pushPose();
		this.translateToHand(model, false, stack);
		stack.mulPose(Axis.ZP.rotationDegrees(180F));
		stack.translate(0.0F, 0.0F, -0.15F);
		stack.scale(0.65F, 0.65F, 0.65F);
		Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(Items.WARPED_FUNGUS_ON_A_STICK), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
		stack.popPose();
	}
}
