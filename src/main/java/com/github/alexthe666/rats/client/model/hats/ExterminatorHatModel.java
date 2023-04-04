package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ExterminatorHatModel extends AbstractHatModel {
	public ExterminatorHatModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-3.5F, -35.5F, -3.0F, 7.0F, 3.0F, 8.0F)
				.texOffs(18, 0).addBox(-4.5F, -32.5F, -7.0F, 9.0F, 1.0F, 14.0F)
				.texOffs(0, 16).addBox(-4.0F, -29.0F, -4.5F, 8.0F, 3.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		hat.addOrReplaceChild("cig", CubeListBuilder.create().texOffs(54, 0).addBox(-0.5F, -24.5F, 0.0F, 1.0F, 1.0F, 4.0F), PartPose.offsetAndRotation(-1.5F, -1.0F, -4.0F, 0.1745F, 0.2618F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}
}
