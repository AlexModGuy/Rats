package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.FlyingDutchratModel;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Map;

public class DutchratHelmetLayer<T extends Dutchrat, M extends FlyingDutchratModel<T>> extends RenderLayer<T, M> {
	private final HumanoidModel<?> backup;
	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

	public DutchratHelmetLayer(RenderLayerParent<T, M> parent, HumanoidModel<?> armorModel) {
		super(parent);
		this.backup = armorModel;
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (rat.getBellSummonTicks() <= 0) {
			stack.pushPose();
			this.getParentModel().body1.translateRotate(stack);
			this.getParentModel().neck.translateRotate(stack);
			this.getParentModel().head.translateRotate(stack);
			stack.translate(0, -0.77F, 0);
			ItemStack itemstack = rat.getItemBySlot(EquipmentSlot.HEAD);
			if (itemstack.getItem() instanceof ArmorItem) {
				Model model = ForgeHooksClient.getArmorModel(rat, itemstack, EquipmentSlot.HEAD, this.backup);
				ResourceLocation tex = getArmorResource(rat, itemstack, EquipmentSlot.HEAD, null);
				VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(tex), false, false);
				model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			}
			stack.popPose();
		}
	}

	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @org.jetbrains.annotations.Nullable String type) {
		ArmorItem item = (ArmorItem) stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (1), type == null ? "" : String.format("_%s", type));

		s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s1);

		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
		}

		return resourcelocation;
	}

}
