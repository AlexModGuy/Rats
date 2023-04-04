package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PiperHatModel extends AbstractHatModel {

	public PiperHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-5.0F, 24.0F, -11.0F, 9.0F, 5.0F, 12.0F, new CubeDeformation(0.25F))
						.texOffs(38, 0).addBox(-3.0F, 22.0F, -9.0F, 5.0F, 2.0F, 8.0F),
				PartPose.offsetAndRotation(0.5F, -36.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		hat.addOrReplaceChild("feather", CubeListBuilder.create()
						.texOffs(0, 7)
						.addBox(0.0F, -9.0F, -4.0F, 0.0F, 10.0F, 10.0F),
				PartPose.offsetAndRotation(-3.0F, 24.0F, -3.0F, 0.0F, 0.0F, -0.3927F));

		return LayerDefinition.create(mesh, 64, 32);
	}

}
