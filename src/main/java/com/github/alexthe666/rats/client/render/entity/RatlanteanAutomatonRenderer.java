package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.model.entity.RatlanteanAutomatonModel;
import com.github.alexthe666.rats.client.render.entity.layer.GlowingOverlayLayer;
import com.github.alexthe666.rats.server.entity.monster.boss.RatlanteanAutomaton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RatlanteanAutomatonRenderer extends MobRenderer<RatlanteanAutomaton, RatlanteanAutomatonModel<RatlanteanAutomaton>> {

	private static final ResourceLocation MARBLED_CHEESE_GOLEM_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/marble_cheese_golem.png");
	private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/entity/ratlantis/marble_cheese_golem_glow.png");

	public RatlanteanAutomatonRenderer(EntityRendererProvider.Context context) {
		super(context, new RatlanteanAutomatonModel<>(false), 0.95F);
		this.addLayer(new GlowingOverlayLayer<>(this, GLOW_TEXTURE));
	}

	@Override
	protected void scale(RatlanteanAutomaton automaton, PoseStack stack, float partialTicks) {
		stack.scale(1.2F, 1.2F, 1.2F);
	}

	@Override
	public Vec3 getRenderOffset(RatlanteanAutomaton rat, float partialTickTime) {
		return new Vec3(0, 0.35F, 0);
	}

	@Override
	public ResourceLocation getTextureLocation(RatlanteanAutomaton entity) {
		return MARBLED_CHEESE_GOLEM_TEXTURE;
	}
}
