package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.items.HatItem;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class RatHelmetLayer<T extends AbstractRat> extends RenderLayer<T, RatModel<T>> {
	private final HumanoidModel<?> ratArmorModel;

	public RatHelmetLayer(RenderLayerParent<T, RatModel<T>> parent, HumanoidModel<?> armorModel) {
		super(parent);
		this.ratArmorModel = armorModel;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = rat.getItemBySlot(EquipmentSlot.HEAD);
		if (!rat.isBaby() && !itemstack.isEmpty()) {
			stack.pushPose();
			if (itemstack.getItem() instanceof ArmorItem armoritem) {
				if (armoritem.getEquipmentSlot() == EquipmentSlot.HEAD) {
					this.ratArmorModel.setAllVisible(false);
					this.ratArmorModel.head.visible = true;
					this.ratArmorModel.hat.visible = true;
					Model model = ForgeHooksClient.getArmorModel(rat, itemstack, EquipmentSlot.HEAD, this.ratArmorModel);
					this.getParentModel().body1.translateRotate(stack);
					this.getParentModel().body2.translateRotate(stack);
					this.getParentModel().neck.translateRotate(stack);
					this.getParentModel().head.translateRotate(stack);
					stack.translate(0, -0.375F, -0.045F);
					stack.scale(0.55F, 0.55F, 0.55F);
					if (itemstack.getItem() instanceof HatItem hat) {
						hat.transformOnHead(rat, stack);
					}
					boolean flag1 = itemstack.hasFoil();
					if (armoritem instanceof DyeableArmorItem dyeable) { // Allow this for anything, not only cloth
						int i = dyeable.getColor(itemstack);
						float f = (float) (i >> 16 & 255) / 255.0F;
						float f1 = (float) (i >> 8 & 255) / 255.0F;
						float f2 = (float) (i & 255) / 255.0F;
						this.renderArmor(stack, buffer, light, flag1, model, f, f1, f2, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, null));
						this.renderArmor(stack, buffer, light, flag1, model, 1.0F, 1.0F, 1.0F, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, "overlay"));
					} else {
						this.renderArmor(stack, buffer, light, flag1, model, 1.0F, 1.0F, 1.0F, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, null));
					}

				}
			} else {
				this.getParentModel().body1.translateRotate(stack);
				this.getParentModel().body2.translateRotate(stack);
				this.getParentModel().neck.translateRotate(stack);
				this.getParentModel().head.translateRotate(stack);
				stack.translate(0, 0.025F, -0.15F);
				stack.mulPose(Axis.XP.rotationDegrees(180));
				stack.mulPose(Axis.YP.rotationDegrees(180));
				stack.scale(0.5F, 0.5F, 0.5F);
				if (itemstack.is(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get().asItem())) {
					stack.translate(0, -0.1F, 0.1F);
					stack.mulPose(Axis.XP.rotationDegrees(15));
				} else if (itemstack.is(Tags.Items.HEADS) && ForgeRegistries.ITEMS.getKey(itemstack.getItem()).getNamespace().equals("minecraft")) {
					stack.mulPose(Axis.YP.rotationDegrees(180));
					stack.translate(0.0D, 0.6D, -0.05D);
					stack.scale(1.8F, 1.8F, 1.8F);
				}
				Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
			}
			stack.popPose();

			stack.pushPose();
			ItemStack banner = rat.getItemBySlot(EquipmentSlot.OFFHAND);
			if (banner.getItem() instanceof BannerItem) {
				this.getParentModel().body1.translateAndRotate(stack);
				this.getParentModel().body2.translateAndRotate(stack);
				stack.translate(0.0D, -0.5D, -0.2D);
				stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
				float sitProgress = rat.sitProgress / 20.0F;
				stack.mulPose(Axis.XP.rotationDegrees(sitProgress * -40.0F));
				stack.translate(0.0D, 0.0D, -sitProgress * 0.04F);
				stack.scale(1.7F, 1.7F, 1.7F);
				Minecraft.getInstance().getItemRenderer().renderStatic(banner, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
			}
			stack.popPose();
		}
	}


	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

	private void renderArmor(PoseStack stack, MultiBufferSource buffer, int light, boolean glint, Model modelIn, float red, float green, float blue, ResourceLocation armorResource) {
		VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(armorResource), false, glint);
		modelIn.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
	}

	public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @org.jetbrains.annotations.Nullable String type) {
		ArmorItem item = (ArmorItem) stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (1), type == null ? "" : String.format("_%s", type));

		s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
}