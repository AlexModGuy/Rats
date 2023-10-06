package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.ChristmasChestModel;
import com.github.alexthe666.rats.client.model.entity.AbstractRatModel;
import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.HoldsItemUpgrade;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RatHeldItemLayer<T extends AbstractRat, M extends AbstractRatModel<T>> extends RenderLayer<T, M> {

	public static final ChristmasChestModel CHRISTMAS_CHEST_MODEL = new ChristmasChestModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.CHEST));

	public RatHeldItemLayer(RenderLayerParent<T, M> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isBaby() && this.getParentModel() instanceof RatModel<?> model) {
			ItemStack itemstack = entity.getItemInHand(InteractionHand.MAIN_HAND);
			if (!itemstack.isEmpty()) {
				stack.pushPose();
				if (this.getParentModel().young) {
					stack.translate(0.0F, 0.625F, 0.0F);
					stack.mulPose(Axis.XP.rotationDegrees(20));
					stack.scale(0.5F, 0.5F, 0.5F);
				}
				if (!entity.isHoldingItemInHands()) {
					this.getParentModel().translateToHead(stack);
					stack.mulPose(Axis.ZP.rotationDegrees(180));
					stack.mulPose(Axis.YP.rotationDegrees(180));
					stack.mulPose(Axis.XP.rotationDegrees(90));
					stack.translate(0F, 0.35F, 0.05F);
					stack.scale(0.5F, 0.5F, 0.5F);
					Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, entity.getId());
				} else {
					boolean flag = false;
					boolean model3d = Minecraft.getInstance().getItemRenderer().getModel(itemstack, Minecraft.getInstance().level, entity, 0).isGui3d();
					if (entity instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get())) {
						this.translateToHand(model, true, stack);
						stack.translate(-0.125F, -0.15F, -0.2F);
						if (model3d) {
							stack.mulPose(Axis.XP.rotationDegrees(110));
							stack.mulPose(Axis.YP.rotationDegrees(10));
							stack.mulPose(Axis.ZN.rotationDegrees(3));
							stack.translate(0.0F, 0.2F, 0.025F);
						} else {
							stack.mulPose(Axis.XP.rotationDegrees(20));
							stack.mulPose(Axis.YP.rotationDegrees(3));
							stack.mulPose(Axis.ZP.rotationDegrees(10));
							stack.translate(0.0F, -0.025F, 0.09F);
							stack.scale(0.75F, 0.75F, 0.75F);
						}
						stack.scale(0.5F, 0.5F, 0.5F);
						flag = true;
					} else {
						this.translateToHand(model, true, stack);
						stack.mulPose(Axis.ZP.rotationDegrees(180));
						stack.mulPose(Axis.XP.rotationDegrees(20));
						stack.scale(0.65F, 0.65F, 0.65F);
						stack.translate(0.0F, -0.075F, -0.2F);
					}
					Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, flag ? ItemDisplayContext.FIXED : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, entity.getId());
					if (flag && !model3d && itemstack.getCount() > 5) {
						//this ensures our stacks dont constantly rotate around either when rendering on the plate or when the stack size changes
						RandomSource random = RandomSource.create(itemstack.getItem().hashCode());
						for (int i = 0; i < itemstack.getCount() / 5; i++) {
							stack.translate(0.0D, 0.025D, 0.0D);
							stack.mulPose(Axis.ZP.rotationDegrees(random.nextIntBetweenInclusive(-90, 90)));
							stack.translate(0.0D, -0.025D, 0.05D);
							Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, entity.getId());
						}
					}
				}
				stack.popPose();
			}

			if (entity instanceof TamedRat rat) {
				if (rat.getRespawnCountdown() > 0) {
					stack.pushPose();
					Minecraft minecraft = Minecraft.getInstance();
					float wingAngle = 0;
					float wingFold = -45.0F;
					this.getParentModel().translateToBody(stack);
					stack.pushPose();
					stack.mulPose(Axis.ZN.rotationDegrees(wingAngle));
					stack.mulPose(Axis.YP.rotationDegrees(wingFold));
					stack.mulPose(Axis.XN.rotationDegrees(90));
					stack.translate(0.2F, 0.0F, -0.15F);
					minecraft.getItemRenderer().renderStatic(new ItemStack(RatsItemRegistry.FEATHERY_WING.get()), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
					stack.popPose();
					stack.pushPose();

					stack.mulPose(Axis.ZP.rotationDegrees(wingAngle));
					stack.mulPose(Axis.YN.rotationDegrees(wingFold));
					stack.mulPose(Axis.XN.rotationDegrees(90));
					stack.mulPose(Axis.YP.rotationDegrees(180));
					stack.translate(0.2F, -0.0F, 0.15F);
					minecraft.getItemRenderer().renderStatic(new ItemStack(RatsItemRegistry.FEATHERY_WING.get()), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
					stack.popPose();
					stack.popPose();
				} else {
					RatUpgradeUtils.forEachUpgrade(rat, item -> item instanceof HoldsItemUpgrade, (stack1, slot) -> {
						if (rat.isSlotVisible(slot)) {
							stack.pushPose();
							((HoldsItemUpgrade) stack1.getItem()).renderHeldItem(rat, model, stack, buffer, light, ageInTicks);
							stack.popPose();
						}
					});
				}
			}
		}
	}

	protected void translateToHand(RatModel<?> model, boolean left, PoseStack stack) {
		model.body1.translateRotate(stack);
		model.body2.translateRotate(stack);
		if (left) {
			model.leftArm.translateRotate(stack);
			model.leftHand.translateRotate(stack);
		} else {
			model.rightArm.translateRotate(stack);
			model.rightHand.translateRotate(stack);
		}
	}
}
