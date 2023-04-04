package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ArcheologistHatModel extends AbstractHatModel {

	public ArcheologistHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partDefinition = mesh.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -13.5F, -1.0F, 2.0F, 1.0F, 2.0F)
				.texOffs(0, 15).addBox(-3.5F, -13.0F, -3.5F, 7.0F, 4.0F, 7.0F)
				.texOffs(0, 0).addBox(-5.0F, -9.0F, -7.0F, 10.0F, 1.0F, 14.0F), PartPose.offset(0.0F, 32.0F, 0.0F));

		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}
}
