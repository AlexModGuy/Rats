package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.entity.layer.RatEyesLayer;
import com.github.alexthe666.rats.client.render.entity.layer.RatOverlayLayer;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RatRenderer extends AbstractRatRenderer<Rat> {
	public RatRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.addLayer(new RatOverlayLayer(this));
		this.addLayer(new RatEyesLayer<>(this));
	}
}
