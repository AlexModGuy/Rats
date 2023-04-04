package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class ChristmasChestModel extends HierarchicalModel<Entity> {

	private final ModelPart root;

	public ChristmasChestModel(ModelPart root) {
		this.root = root;
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
