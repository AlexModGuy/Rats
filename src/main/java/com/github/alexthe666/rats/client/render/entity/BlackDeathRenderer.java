package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.entity.BlackDeathModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class BlackDeathRenderer extends MobRenderer<BlackDeath, BlackDeathModel<BlackDeath>> {

	private static final ResourceLocation BLACK_DEATH_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/black_death.png");
	private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/black_death_overlay.png");

	public BlackDeathRenderer(EntityRendererProvider.Context context) {
		super(context, new BlackDeathModel<>(context.bakeLayer(RatsModelLayers.BLACK_DEATH)), 0.5F);
		this.addLayer(new GlowingOverlayLayer<>(this, GLOW_TEXTURE));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack stack, MultiBufferSource buffer, int light, BlackDeath entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.isSummoning() || entity.isMeleeAttacking()) {
					super.render(stack, buffer, light, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	public ResourceLocation getTextureLocation(BlackDeath entity) {
		return BLACK_DEATH_TEXTURE;
	}
}
