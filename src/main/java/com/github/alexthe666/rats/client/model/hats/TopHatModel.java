package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TopHatModel extends AbstractHatModel {

	public TopHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 15)
						.addBox(-5.0F, 5.0F, 1.0F, 10.0F, 1.0F, 10.0F)
						.texOffs(0, 0)
						.addBox(-3.5F, -3.0F, 2.5F, 7.0F, 8.0F, 7.0F),
				PartPose.offset(0.0F, -13.0F, -6.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
