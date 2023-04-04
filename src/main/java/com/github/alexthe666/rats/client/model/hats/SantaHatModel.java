package com.github.alexthe666.rats.client.model.hats;


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SantaHatModel extends AbstractHatModel {

	public SantaHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-5.0F, 4.0F, 1.0F, 10.0F, 2.0F, 10.0F),
				PartPose.offset(0.0F, -14.0F, -6.0F));

		PartDefinition hat1 = hat.addOrReplaceChild("hat1", CubeListBuilder.create()
						.texOffs(0, 12)
						.addBox(-4.0F, -4.8F, -3.0F, 8.0F, 5.0F, 7.0F),
				PartPose.offsetAndRotation(0.0F, 5.0F, 5.0F, -0.2618F, 0.0F, 0.0873F));

		PartDefinition hat2 = hat1.addOrReplaceChild("hat2", CubeListBuilder.create()
						.texOffs(32, 12)
						.addBox(-3.0F, -4.0F, -2.0F, 6.0F, 6.0F, 6.0F),
				PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -0.2182F, 0.0F, 0.2618F));

		PartDefinition hat3 = hat2.addOrReplaceChild("hat3", CubeListBuilder.create()
						.texOffs(48, 0)
						.addBox(-2.0F, -3.0F, -2.0F, 4.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, -0.2182F, 0.0F, 0.2618F));

		PartDefinition hat4 = hat3.addOrReplaceChild("hat4", CubeListBuilder.create()
						.texOffs(40, 8)
						.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.2182F, 0.0F, 0.2618F));

		hat4.addOrReplaceChild("ball", CubeListBuilder.create()
						.texOffs(30, 0)
						.addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.7854F, 0.1745F));

		return LayerDefinition.create(mesh, 64, 32);
	}
}
