package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PartyHatModel extends AbstractHatModel {

	public PartyHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F)
						.texOffs(0, 8)
						.addBox(-2.5F, -4.0F, -2.5F, 5.0F, 2.0F, 5.0F)
						.texOffs(0, 15)
						.addBox(-2.0F, -6.0F, -2.0F, 4.0F, 2.0F, 4.0F)
						.texOffs(18, 0)
						.addBox(-1.5F, -8.0F, -1.5F, 3.0F, 2.0F, 3.0F)
						.texOffs(18, 8)
						.addBox(-1.0F, -9.0F, -1.0F, 2.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -8.0F, 0.0F));

		hat.addOrReplaceChild("bow", CubeListBuilder.create()
						.texOffs(40, 2).mirror()
						.addBox(-2.5F, -12.0F, 0.0F, 5.0F, 3.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		hat.addOrReplaceChild("bow2", CubeListBuilder.create()
						.texOffs(40, 2)
						.addBox(-2.5F, -12.0F, 0.0F, 5.0F, 3.0F, 0.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));


		return LayerDefinition.create(mesh, 64, 32);
	}
}