package com.github.alexthe666.rats.client.model.entity;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.npc.AbstractVillager;

public class PlagueDoctorModel<T extends AbstractVillager> extends VillagerModel<T> {

	public PlagueDoctorModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = VillagerModel.createBodyModel();
		PartDefinition partdefinition = mesh.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F)
						.texOffs(16, 50)
						.addBox(-7.0F, -7.0F, -7.0F, 14.0F, 0.0F, 14.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		hat.addOrReplaceChild("hat_rim", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1.5F, -1.5F, -4.75F, 3.0F, 3.0F, 5.0F),
				PartPose.offsetAndRotation(0.0F, -2.5F, -4.0F, 0.1745F, 0.0F, 0.0F));

		nose.addOrReplaceChild("nose2", CubeListBuilder.create()
						.texOffs(40, 0)
						.addBox(-1.0F, -1.0F, -3.5F, 2.0F, 2.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, 0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}
}
