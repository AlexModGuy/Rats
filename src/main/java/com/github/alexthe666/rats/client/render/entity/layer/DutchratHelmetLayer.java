package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.FlyingDutchratModel;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
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
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.List;

public class DutchratHelmetLayer<T extends Dutchrat, M extends FlyingDutchratModel<T>> extends RenderLayer<T, M> {
	private final HumanoidModel<?> backup;

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
				Model model = ClientHooks.getArmorModel(rat, itemstack, EquipmentSlot.HEAD, this.backup);
				ResourceLocation tex = getArmorResource(rat, itemstack, EquipmentSlot.HEAD);
				VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(tex), false, false);
				model.renderToBuffer(stack, consumer, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
			}
			stack.popPose();
		}
	}

	// 1.21: route through ClientHooks.getArmorTexture so per-stack IItemExtension.getArmorTexture overrides
	// (e.g. HatItem returning rats:textures/model/hat/<id>.png) are honored. Falls back to the ArmorMaterial
	// layer's default texture path when no override is registered.
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot) {
		ArmorItem item = (ArmorItem) stack.getItem();
		ArmorMaterial material = item.getMaterial().value();
		List<ArmorMaterial.Layer> layers = material.layers();
		ArmorMaterial.Layer layer = layers.isEmpty()
				? new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("iron"))
				: layers.get(0);
		return ClientHooks.getArmorTexture(entity, stack, layer, false, slot);
	}

}
