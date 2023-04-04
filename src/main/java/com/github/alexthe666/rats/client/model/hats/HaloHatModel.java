package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class HaloHatModel extends AbstractHatModel {

	public HaloHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partDefinition = mesh.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		partDefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -4.0F, -4.0F, 8, 4, 8),
				PartPose.offset(0.0F, -10.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
