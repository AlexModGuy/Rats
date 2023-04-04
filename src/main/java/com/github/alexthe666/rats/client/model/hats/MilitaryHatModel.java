package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MilitaryHatModel extends AbstractHatModel {

	public MilitaryHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.0F, -4.0F, -4.0F, 2.0F, 2.0F, 1.0F)
						.texOffs(0, 23)
						.addBox(-4.0F, -1.0F, -4.0F, 8.0F, 1.0F, 8.0F)
						.texOffs(0, 0)
						.addBox(-3.5F, -5.0F, -3.5F, 7.0F, 4.0F, 7.0F)
						.texOffs(36, 29)
						.addBox(-2.5F, -1.0F, -6.0F, 5.0F, 1.0F, 2.0F),
				PartPose.offset(0.0F, -8.0F, 0.0F));

		hat.addOrReplaceChild("top", CubeListBuilder.create()
						.texOffs(0, 11)
						.addBox(-5.0F, -3.0F, -5.0F, 10.0F, 2.0F, 10.0F),
				PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.2618F, 0.0F, 0.0F));


		return LayerDefinition.create(mesh, 64, 32);
	}
}
