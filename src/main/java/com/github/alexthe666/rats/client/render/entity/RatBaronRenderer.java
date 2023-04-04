package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.entity.layer.RatEyesLayer;
import com.github.alexthe666.rats.server.entity.ratlantis.RatBaron;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RatBaronRenderer extends AbstractRatRenderer<RatBaron> {
	public RatBaronRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new RatEyesLayer<>(this));
	}
}
