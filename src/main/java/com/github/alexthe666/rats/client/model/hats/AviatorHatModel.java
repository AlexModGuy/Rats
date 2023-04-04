package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AviatorHatModel extends AbstractHatModel {

	public AviatorHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		PartDefinition hat = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.5F, -11.0F, -4.5F, 9.0F, 4.0F, 9.0F)
				.texOffs(14, 14)
				.addBox(-5.0F, -8.0F, -5.0F, 10.0F, 3.0F, 10.0F), PartPose.offset(16.0F, 32.0F, 16.0F));

		hat.addOrReplaceChild("right_flap", CubeListBuilder.create()
						.texOffs(0, 14).mirror()
						.addBox(-1.0F, 0.0F, -2.5F, 1.0F, 7.0F, 5.0F),
				PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		hat.addOrReplaceChild("left_flap", CubeListBuilder.create()
						.texOffs(0, 14)
						.addBox(0.0F, 0.0F, -2.5F, 1.0F, 7.0F, 5.0F),
				PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}
}
