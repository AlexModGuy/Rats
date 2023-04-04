package com.github.alexthe666.rats.client.model.hats;


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PlagueDoctorMaskModel extends AbstractHatModel {

	public PlagueDoctorMaskModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partDefinition = mesh.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		var hat = partDefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -11.0F, -4.0F, 8, 11, 8, new CubeDeformation(0.1F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		var nose = hat.addOrReplaceChild("nose1", CubeListBuilder.create()
						.texOffs(0, 19)
						.addBox(-1.5F, -1.5F, -3.0F, 3, 3, 5, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, 0.17453292519943295F, 0.0F, 0.0F));

		nose.addOrReplaceChild("nose2", CubeListBuilder.create()
						.texOffs(16, 19)
						.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 4, new CubeDeformation(0.1F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, -3.5F, 0.17453292519943295F, 0.0F, 0.0F));
		hat.addOrReplaceChild("brim", CubeListBuilder.create()
						.texOffs(18, 0)
						.addBox(-7.0F, 0.0F, -7.0F, 14, 0, 14),
				PartPose.offset(0.0F, -8.1F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}


}
