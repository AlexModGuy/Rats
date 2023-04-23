package com.github.alexthe666.rats.client.render.entity.layer;

import com.github.alexthe666.rats.client.model.entity.RatGolemMountModel;
import com.github.alexthe666.rats.server.entity.mount.RatGolemMount;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class RatGolemMountCracksLayer extends RenderLayer<RatGolemMount, RatGolemMountModel<RatGolemMount>> {
	private static final Map<RatGolemMount.Cracks, ResourceLocation> CRACK_MAP = ImmutableMap.of(RatGolemMount.Cracks.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"), RatGolemMount.Cracks.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"), RatGolemMount.Cracks.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));

	public RatGolemMountCracksLayer(RenderLayerParent<RatGolemMount, RatGolemMountModel<RatGolemMount>> parent) {
		super(parent);
	}

	public void render(PoseStack stack, MultiBufferSource buffer, int light, RatGolemMount entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!entity.isInvisible()) {
			RatGolemMount.Cracks cracks = entity.getCracks();
			if (cracks != RatGolemMount.Cracks.NONE) {
				ResourceLocation resourcelocation = CRACK_MAP.get(cracks);
				renderColoredCutoutModel(this.getParentModel(), resourcelocation, stack, buffer, light, entity, 1.0F, 1.0F, 1.0F);
			}
		}
	}
}