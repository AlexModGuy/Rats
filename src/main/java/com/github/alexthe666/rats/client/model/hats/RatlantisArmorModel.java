package com.github.alexthe666.rats.client.model.hats;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class RatlantisArmorModel extends AbstractHatModel {
	public final ModelPart leftEar;
	public final ModelPart rightEar;
	public final ModelPart jaw;

	public RatlantisArmorModel(ModelPart root) {
		super(root);
		this.leftEar = root.getChild("head").getChild("left_ear");
		this.rightEar = root.getChild("head").getChild("right_ear");
		this.jaw = root.getChild("head").getChild("mouth");
	}

	public static LayerDefinition create(CubeDeformation deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(deformation, 0.0F);
		PartDefinition partDefinition = mesh.getRoot();

		partDefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		partDefinition.getChild("head").addOrReplaceChild("nose", CubeListBuilder.create().texOffs(54, 15).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 3.0F), PartPose.offsetAndRotation(0.0F, -4.0F, -8.0F, 0.2618F, 0.0F, 0.0F));
		partDefinition.getChild("head").addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 0).addBox(1.0F, -4.0F, -1.0F, 7.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(4.0F, -8.0F, 4.0F, 0.0F, -0.7854F, 0.0F));
		partDefinition.getChild("head").addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 0).addBox(-8.0F, -4.0F, -1.0F, 7.0F, 7.0F, 1.0F), PartPose.offsetAndRotation(-4.0F, -8.0F, 4.0F, 0.0F, 0.7854F, 0.0F));
		partDefinition.getChild("head").addOrReplaceChild("snout", CubeListBuilder.create().texOffs(34, 2).addBox(-2.5F, 0.0F, -6.0F, 5.0F, 4.0F, 6.0F), PartPose.offset(0.0F, -4.0F, -3.0F));
		partDefinition.getChild("head").addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(46, 7).addBox(-2.0F, 0.0F, -6.0F, 4.0F, 2.0F, 6.0F), PartPose.offset(0.0F, -1.0F, -2.0F));
		partDefinition.getChild("left_arm").addOrReplaceChild("left_rat", CubeListBuilder.create().texOffs(54, 0).addBox(-2.0F, -5.0F, 0.0F, 5.0F, 9.0F, 0.0F), PartPose.offsetAndRotation(2.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.3927F));
		partDefinition.getChild("right_arm").addOrReplaceChild("right_rat", CubeListBuilder.create().texOffs(54, 0).addBox(-3.0F, -5.0F, 0.0F, 5.0F, 9.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.jaw.xRot = (float) (Math.sin(limbSwing * (float) Math.PI * 0.2F) + 1.0D) * 0.2F;

		this.leftEar.y = Mth.sin((ageInTicks / 60.0F) * 40.0F) + 0.4F;
		AnimationUtils.bobModelPart(this.leftEar, ageInTicks, 1.0F);
		this.rightEar.y = Mth.sin((ageInTicks / 60.0F) * 40.0F) + 0.4F;
		AnimationUtils.bobModelPart(this.rightEar, ageInTicks, -1.0F);
	}
}
