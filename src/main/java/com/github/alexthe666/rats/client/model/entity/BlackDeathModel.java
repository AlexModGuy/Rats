package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BlackDeathModel<T extends BlackDeath> extends HumanoidModel<T> {
	public final ModelPart arms;

	public BlackDeathModel(ModelPart root) {
		super(root);
		this.arms = root.getChild("arms");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F)
						.texOffs(18, 6)
						.addBox(-7.0F, -7.0F, -7.0F, 14.0F, 0.0F, 14.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create()
						.texOffs(28, 42)
						.addBox(-1.5F, -2.0F, -5.5F, 3.0F, 3.0F, 5.0F),
				PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.1745F, 0.0F, 0.0F));

		nose.addOrReplaceChild("nose2", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, -0.5F, -5.0F, 0.1745F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(16, 20)
						.addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F)
						.texOffs(0, 38)
						.addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("arms", CubeListBuilder.create()
						.texOffs(44, 22)
						.addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
						.texOffs(44, 22)
						.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
						.texOffs(40, 38)
						.addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
				PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(40, 46).mirror()
						.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(40, 46)
						.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(0, 22).mirror()
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(2.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(0, 22)
						.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
				PartPose.offset(-2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;
		if (entity.isSummoning()) {
			this.rightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.leftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
			this.rightArm.zRot = 135.0F * Mth.DEG_TO_RAD;
			this.leftArm.zRot = -135.0F * Mth.DEG_TO_RAD;
			this.rightArm.yRot = Mth.PI;
			this.leftArm.yRot = Mth.PI;
		} else {
			if (entity.isMeleeAttacking()) {
				AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, entity, this.attackTime, ageInTicks);
			} else {
				this.rightArm.setRotation(0.0F, 0.0F, 0.0F);
				this.leftArm.setRotation(0.0F, 0.0F, 0.0F);
			}
		}
		boolean flag = entity.isSummoning() || entity.isMeleeAttacking();
		this.arms.visible = !flag;
		this.leftArm.visible = flag;
		this.rightArm.visible = flag;
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Iterables.concat(super.bodyParts(), ImmutableList.of(this.arms));
	}
}