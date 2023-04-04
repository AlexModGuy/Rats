package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.RatGolemMount;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class RatGolemMountModel<T extends RatGolemMount> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox chest;
	public final AdvancedModelBox waist;
	public final AdvancedModelBox leftArm;
	public final AdvancedModelBox rightArm;
	public final AdvancedModelBox rightLeg;
	public final AdvancedModelBox leftLeg;
	public final AdvancedModelBox dome;

	public RatGolemMountModel() {
		this.texWidth = 128;
		this.texHeight = 128;
		this.leftLeg = new AdvancedModelBox(this, 60, 0);
		this.leftLeg.mirror = true;
		this.leftLeg.setRotationPoint(5.0F, 11.0F, 0.0F);
		this.leftLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, 0.0F);
		this.leftArm = new AdvancedModelBox(this, 60, 58);
		this.leftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.leftArm.addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, 0.0F);
		this.chest = new AdvancedModelBox(this, 0, 40);
		this.chest.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.chest.addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, 0.0F);
		this.rightArm = new AdvancedModelBox(this, 60, 21);
		this.rightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.rightArm.addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, 0.0F);
		this.rightLeg = new AdvancedModelBox(this, 37, 0);
		this.rightLeg.setRotationPoint(-4.0F, 11.0F, 0.0F);
		this.rightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, 0.0F);
		this.dome = new AdvancedModelBox(this, 0, 82);
		this.dome.setRotationPoint(0.0F, -9.0F, 0.0F);
		this.dome.addBox(-5.0F, -14.0F, -5.0F, 10, 14, 10, 0.0F);
		this.waist = new AdvancedModelBox(this, 0, 70);
		this.waist.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.waist.addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, 0.5F);
		this.updateDefaultPose();
	}

	@Override
	public void setupAnim(T entity, float f, float f1, float ageInTicks, float netHeadYaw, float headPitch) {
		this.resetToDefaultPose();
		this.leftLeg.rotateAngleX = -1.5F * this.triangleWave(f, 13.0F) * f1;
		this.rightLeg.rotateAngleX = 1.5F * this.triangleWave(f, 13.0F) * f1;
		this.leftLeg.rotateAngleY = 0.0F;
		this.rightLeg.rotateAngleY = 0.0F;
		int i = entity.getAttackTimer();
		if (i > 0) {
			float partialTicks = Minecraft.getInstance().getPartialTick();
			this.leftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) i - partialTicks, 10.0F);
			this.rightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) i - partialTicks, 10.0F);
		} else {
			this.leftArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(f, 13.0F)) * f1;
			this.rightArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(f, 13.0F)) * f1;
		}
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(chest, waist, dome, leftArm, leftLeg, rightArm, rightLeg);
	}

	private float triangleWave(float time, float interval) {
		return (Math.abs(time % interval - interval * 0.5F) - interval * 0.25F) / (interval * 0.25F);
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(chest, waist, dome, leftArm, leftLeg, rightArm, rightLeg);
	}
}
