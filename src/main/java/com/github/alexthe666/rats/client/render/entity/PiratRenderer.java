package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatEyesLayer;
import com.github.alexthe666.rats.server.entity.monster.Pirat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PiratRenderer extends AbstractRatRenderer<Pirat, RatModel<Pirat>> {

	public PiratRenderer(EntityRendererProvider.Context context) {
		super(context, new RatModel<>());
		this.addLayer(new RatEyesLayer<>(this));
	}

	protected void scale(Pirat rat, PoseStack stack, float partialTickTime) {
		super.scale(rat, stack, partialTickTime);
		stack.scale(1.6F, 1.6F, 1.6F);
	}
}
