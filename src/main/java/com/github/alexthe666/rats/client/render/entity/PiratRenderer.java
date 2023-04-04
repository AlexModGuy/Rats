package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.entity.layer.RatEyesLayer;
import com.github.alexthe666.rats.server.entity.ratlantis.Pirat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PiratRenderer extends AbstractRatRenderer<Pirat> {

	public PiratRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new RatEyesLayer<>(this));
	}

	protected void scale(Pirat rat, PoseStack stack, float partialTickTime) {
		super.scale(rat, stack, partialTickTime);
		stack.scale(1.6F, 1.6F, 1.6F);
	}
}
