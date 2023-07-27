package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.AbstractRatModel;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
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
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RatHelmetLayer<T extends AbstractRat, M extends AbstractRatModel<T>> extends RenderLayer<T, M> {
	private final HumanoidModel<?> ratArmorModel;
	private final TextureAtlas armorTrimAtlas;
	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

	public RatHelmetLayer(RenderLayerParent<T, M> parent, HumanoidModel<?> armorModel) {
		super(parent);
		this.ratArmorModel = armorModel;
		this.armorTrimAtlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
	}

	//for the sake of easier maintenance in the future, I will be commenting what each sections of this does.
	//this is partially a [VanillaCopy] of HumanoidArmorLayer.renderArmorPiece unless specified otherwise
	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int light, T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		ItemStack itemstack = rat.getItemBySlot(EquipmentSlot.HEAD);
		//Rats: always render a halo on top of dead rats
		if (rat instanceof TamedRat tamed && tamed.getRespawnCountdown() > 0) {
			itemstack = new ItemStack(RatsItemRegistry.HALO_HAT.get());
		}
		if (!itemstack.isEmpty()) {
			stack.pushPose();
			if (itemstack.getItem() instanceof ArmorItem armoritem) {
				if (armoritem.getEquipmentSlot() == EquipmentSlot.HEAD) {
					//Rats: instead of using setPartVisibility, just toggle the helmet on. Its the only piece of armor we care about rendering anyway.
					this.ratArmorModel.setAllVisible(false);
					this.ratArmorModel.head.visible = true;
					this.ratArmorModel.hat.visible = true;
					Model model = ForgeHooksClient.getArmorModel(rat, itemstack, EquipmentSlot.HEAD, this.ratArmorModel);
					//Rats: do some extra transforms based on which model is being used and what item is rendering.
					this.getParentModel().translateToHead(stack);
					if (rat.isBaby()) {
						stack.translate(0.0D, 0.025D, -0.05D);
						stack.scale(0.65F, 0.65F, 0.65F);
					}
					stack.translate(0, -0.375F, -0.045F);
					stack.scale(0.55F, 0.55F, 0.55F);
					if (itemstack.getItem() instanceof HatItem hat) {
						hat.transformOnHead(rat, stack);
					}
					if (armoritem instanceof DyeableArmorItem dyeable) {
						int i = dyeable.getColor(itemstack);
						float f = (float) (i >> 16 & 255) / 255.0F;
						float f1 = (float) (i >> 8 & 255) / 255.0F;
						float f2 = (float) (i & 255) / 255.0F;
						this.renderModel(stack, buffer, light, model, f, f1, f2, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, null));
						this.renderModel(stack, buffer, light, model, 1.0F, 1.0F, 1.0F, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, "overlay"));
					} else {
						this.renderModel(stack, buffer, light, model, 1.0F, 1.0F, 1.0F, getArmorResource(rat, itemstack, EquipmentSlot.HEAD, null));
					}
					ArmorTrim.getTrim(rat.level().registryAccess(), itemstack).ifPresent(trim ->
							this.renderTrim(armoritem.getMaterial(), stack, buffer, light, trim, model));
					if (itemstack.hasFoil()) {
						this.renderGlint(stack, buffer, light, model);
					}

				}
			} else {
				//Rats: handle some special case hats in the mod
				if (!itemstack.is(RatsItemRegistry.PARTY_HAT.get())) {
					this.getParentModel().translateToHead(stack);
					stack.translate(0, 0.025F, -0.15F);
					stack.mulPose(Axis.XP.rotationDegrees(180));
					stack.mulPose(Axis.YP.rotationDegrees(180));
					stack.scale(0.5F, 0.5F, 0.5F);
					if (itemstack.is(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get().asItem())) {
						stack.translate(0, -0.1F, 0.1F);
						stack.mulPose(Axis.XP.rotationDegrees(15));
						if (rat.isBaby()) {
							stack.scale(0.4F, 0.4F, 0.4F);
							stack.translate(0.0D, 0.25D, 0.0D);
						}
					} else if (itemstack.is(Tags.Items.HEADS) && ForgeRegistries.ITEMS.getKey(itemstack.getItem()).getNamespace().equals("minecraft")) {
						stack.mulPose(Axis.YP.rotationDegrees(180));
						stack.translate(0.0D, 0.6D, -0.05D);
						stack.scale(1.8F, 1.8F, 1.8F);
						if (rat.isBaby()) {
							stack.scale(0.3F, 0.3F, 0.3F);
							stack.translate(0.0D, -0.75D, -0.25D);
						}
					}
					Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY, stack, buffer, null, rat.getId());
				}
			}
			stack.popPose();

			//Rats: render a banner if the rat has one in its banner slot
			stack.pushPose();
			ItemStack banner = rat.getItemBySlot(EquipmentSlot.OFFHAND);
			if (banner.getItem() instanceof BannerItem) {
				this.getParentModel().translateToBody(stack);
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

	private void renderModel(PoseStack stack, MultiBufferSource buffer, int light, Model model, float red, float green, float blue, ResourceLocation armorResource) {
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		model.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
	}

	private void renderTrim(ArmorMaterial material, PoseStack stack, MultiBufferSource buffer, int light, ArmorTrim trim, Model model) {
		TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(trim.outerTexture(material));
		VertexConsumer vertexconsumer = textureatlassprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet()));
		model.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderGlint(PoseStack stack, MultiBufferSource buffer, int light, Model model) {
		model.renderToBuffer(stack, buffer.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	//copy of HumanoidArmorLayer.getArmorResource, a method provided by forge
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
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