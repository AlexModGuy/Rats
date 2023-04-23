package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.RatsModelLayers;
import com.github.alexthe666.rats.client.model.entity.PlagueDoctorModel;
import com.github.alexthe666.rats.server.entity.misc.PlagueDoctor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.resources.ResourceLocation;

public class PlagueDoctorRenderer extends MobRenderer<PlagueDoctor, PlagueDoctorModel<PlagueDoctor>> {
	private static final ResourceLocation DOCTOR = new ResourceLocation(RatsMod.MODID, "textures/entity/plague_doctor.png");

	public PlagueDoctorRenderer(EntityRendererProvider.Context context) {
		super(context, new PlagueDoctorModel<>(context.bakeLayer(RatsModelLayers.PLAGUE_DOCTOR)), 0.5F);
		this.addLayer(new CrossedArmsItemLayer<>(this, context.getItemInHandRenderer()));
	}

	public ResourceLocation getTextureLocation(PlagueDoctor entity) {
		return DOCTOR;
	}

	protected void scale(PlagueDoctor living, PoseStack stack, float partialTickTime) {
		float f = 0.9375F;
		if (living.getAge() < 0) {
			f = (float) ((double) f * 0.5D);
			this.shadowRadius = 0.25F;
		} else {
			this.shadowRadius = 0.5F;
		}
		stack.scale(f, f, f);
	}
}