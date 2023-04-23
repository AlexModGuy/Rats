package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.entity.PiedPiperModel;
import com.github.alexthe666.rats.server.entity.monster.PiedPiper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class PiedPiperRenderer extends MobRenderer<PiedPiper, PiedPiperModel<PiedPiper>> {
	private static final ResourceLocation PIPER = new ResourceLocation(RatsMod.MODID, "textures/entity/pied_piper.png");

	public PiedPiperRenderer(EntityRendererProvider.Context context) {
		super(context, new PiedPiperModel<>(context.bakeLayer(RatsModelLayers.PIPER)), 0.5F);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	public ResourceLocation getTextureLocation(PiedPiper entity) {
		return PIPER;
	}
}