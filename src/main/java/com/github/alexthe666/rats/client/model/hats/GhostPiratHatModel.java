package com.github.alexthe666.rats.client.model.hats;

import com.github.alexthe666.rats.client.render.RatsRenderType;
import net.minecraft.client.model.geom.ModelPart;

public class GhostPiratHatModel extends PiratHatModel {
	public GhostPiratHatModel(ModelPart root) {
		super(root, RatsRenderType::getGlowingTranslucent);
	}
}
