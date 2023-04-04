package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class EmptyModel<T extends Entity> extends AdvancedEntityModel<T> {

	public EmptyModel() {
		super();
		this.texWidth = 16;
		this.texHeight = 16;
		this.updateDefaultPose();
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of();
	}

	public void setupAnim(Entity entity, float f, float f1, float f2, float f3, float f4) {

	}
}
