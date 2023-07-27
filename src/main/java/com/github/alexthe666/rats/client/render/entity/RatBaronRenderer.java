package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.entity.RatModel;
import com.github.alexthe666.rats.client.render.entity.layer.RatEyesLayer;
import com.github.alexthe666.rats.server.entity.monster.boss.RatBaron;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RatBaronRenderer extends AbstractRatRenderer<RatBaron, RatModel<RatBaron>> {
	public RatBaronRenderer(EntityRendererProvider.Context context) {
		super(context, new RatModel<>());
		this.addLayer(new RatEyesLayer<>(this));
	}
}
