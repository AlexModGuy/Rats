package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class PiratHatModel extends AbstractHatModel {

	public PiratHatModel(ModelPart root) {
		super(root);
	}

	public PiratHatModel(ModelPart root, Function<ResourceLocation, RenderType> function) {
		super(root, function);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = mesh.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition hat = partdefinition.getChild("head").addOrReplaceChild("main_hat", CubeListBuilder.create().texOffs(36, 0).addBox(-4.0F, -5.0F, -3.0F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(23, 3).addBox(6.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(-0.01F))
				.texOffs(23, 3).addBox(-7.0F, -3.0F, -4.0F, 1.0F, 3.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		hat.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 5).addBox(-7.0F, -4.0F, -1.0F, 14.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 22).addBox(-5.0F, -7.0F, -1.0F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(1, 26).addBox(-3.0F, -8.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.1745F, 0.0F, 0.0F));

		hat.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -4.0F, 0.0F, 14.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(30, 15).addBox(-5.0F, -6.0F, 0.0F, 10.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.1745F, 0.0F, 0.0F));


		return LayerDefinition.create(mesh, 64, 32);
	}
}
