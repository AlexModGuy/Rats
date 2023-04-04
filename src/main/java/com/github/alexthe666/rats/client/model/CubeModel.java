package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class CubeModel<T extends Entity> extends HierarchicalModel<T> {
	public final ModelPart root;

	public CubeModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partDefinition = mesh.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, new CubeDeformation(1.1F)),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 16, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}
}