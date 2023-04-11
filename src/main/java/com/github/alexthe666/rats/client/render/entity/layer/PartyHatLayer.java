package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.entity.StaticRatModel;
import com.github.alexthe666.rats.client.model.hats.PartyHatModel;
import com.github.alexthe666.rats.server.items.PartyHatItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class PartyHatLayer<T extends LivingEntity, M extends EntityModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
	private final A outerModel;
	private final PartyHatModel partyHat = new PartyHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(RatsModelLayers.PARTY_HAT));

	public PartyHatLayer(RenderLayerParent<T, M> parent, A outerModel) {
		super(parent);
		this.outerModel = outerModel;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource source, int light, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.HEAD);
		if (itemstack.getItem() instanceof PartyHatItem hat) {
			stack.pushPose();
			this.getParentModel().copyPropertiesTo(this.outerModel);
			this.outerModel.setAllVisible(false);
			if (this.getParentModel() instanceof HumanoidModel<?> human){
				human.head.translateAndRotate(stack);
				stack.translate(0.0F, -0.875F, 0.0F);
			} else if (!entity.isBaby() && this.getParentModel() instanceof StaticRatModel<?> rat) {
				rat.body1.translateRotate(stack);
				rat.body2.translateRotate(stack);
				rat.neck.translateRotate(stack);
				rat.head.translateRotate(stack);
				stack.translate(0.0F, -0.35F, 0.0F);
				stack.scale(0.75F, 0.75F, 0.75F);
			}
			boolean flag1 = itemstack.hasFoil();
			int i = hat.getColor(itemstack);
			this.renderModel(stack, source, light, flag1, this.partyHat, (float) (i >> 16 & 255) / 255.0F, (float) (i >> 8 & 255) / 255.0F, (float) (i & 255) / 255.0F, new ResourceLocation(RatsMod.MODID, "textures/model/party_hat_layer_1.png"));
			i = this.invertColor(hat.getColor(itemstack));
			this.renderModel(stack, source, light, flag1, this.partyHat, (float) (i >> 16 & 255) / 255.0F, (float) (i >> 8 & 255) / 255.0F, (float) (i & 255) / 255.0F, new ResourceLocation(RatsMod.MODID, "textures/model/party_hat_layer_2.png"));
			stack.popPose();
		}
	}

	private int invertColor(int color) {
		int a = (color >> 24) & 0xff;
		int r = (color >> 16) & 0xff;
		int g = (color >> 8) & 0xff;
		int b = color & 0xff;

		r = 255 - r;
		g = 255 - g;
		b = 255 - b;
		return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
	}

	private void renderModel(PoseStack stack, MultiBufferSource source, int light, boolean glint, Model model, float red, float green, float blue, ResourceLocation texture) {
		VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(texture), false, glint);
		model.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
	}
}
