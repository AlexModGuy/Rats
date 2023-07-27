package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.projectile.RatShot;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;

public class StaticRatModel<T extends Entity> extends AbstractRatModel<T> {
	public AdvancedModelBox body1;
	public AdvancedModelBox body2;
	public AdvancedModelBox tail1;
	public AdvancedModelBox leftThigh;
	public AdvancedModelBox rightThigh;
	public AdvancedModelBox neck;
	public AdvancedModelBox leftArm;
	public AdvancedModelBox rightArm;
	public AdvancedModelBox head;
	public AdvancedModelBox snout;
	public AdvancedModelBox leftEar;
	public AdvancedModelBox rightEar;
	public AdvancedModelBox leftEye;
	public AdvancedModelBox rightEye;
	public AdvancedModelBox nose;
	public AdvancedModelBox wisker1;
	public AdvancedModelBox wisker2;
	public AdvancedModelBox leftHand;
	public AdvancedModelBox rightHand;
	public AdvancedModelBox tail2;
	public AdvancedModelBox leftFoot;
	public AdvancedModelBox rightFoot;
	protected ModelAnimator animator;

	public StaticRatModel() {
		this.texWidth = 64;
		this.texHeight = 32;
		this.neck = new AdvancedModelBox(this, "neck");
		this.neck.setTextureOffset(32, 17);
		this.neck.setRotationPoint(0.0F, 0.0F, -5.0F);
		this.neck.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 1);
		this.nose = new AdvancedModelBox(this, "nose");
		this.nose.setTextureOffset(32, 26);
		this.nose.setRotationPoint(0.0F, -0.6F, -2.1F);
		this.nose.addBox(-0.5F, -0.3F, -1.0F, 1, 1, 2, 0.0F);
		this.rightEye = new AdvancedModelBox(this, 37, 20);
		this.rightEye.setRotationPoint(-2.0F, -0.5F, -1.6F);
		this.rightEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1);
		this.leftFoot = new AdvancedModelBox(this, "left_foot");
		this.leftFoot.setTextureOffset(22, 15);
		this.leftFoot.mirror = true;
		this.leftFoot.setRotationPoint(0.0F, 3.0F, 0.5F);
		this.leftFoot.addBox(-0.5F, 0.0F, -3.5F, 1, 1, 4);
		this.wisker2 = new AdvancedModelBox(this, "right_whisker");
		this.wisker2.setTextureOffset(35, 0);
		this.wisker2.setRotationPoint(-1.0F, 0.0F, -1.0F);
		this.wisker2.addBox(-2.5F, -1.5F, 0.0F, 2, 3, 0);
		this.wisker1 = new AdvancedModelBox(this, "left_whisker");
		this.wisker1.setTextureOffset(40, 0);
		this.wisker1.setRotationPoint(1.0F, 0.0F, -1.0F);
		this.wisker1.addBox(0.5F, -1.5F, 0.0F, 2, 3, 0);
		this.tail2 = new AdvancedModelBox(this, "lower_tail");
		this.tail2.setTextureOffset(0, 11);
		this.tail2.setRotationPoint(0.0F, 0.0F, 8.0F);
		this.tail2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8);
		this.leftEye = new AdvancedModelBox(this, "left_eye");
		this.leftEye.setTextureOffset(37, 20);
		this.leftEye.setRotationPoint(2.0F, -0.5F, -1.6F);
		this.leftEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1);
		this.leftArm = new AdvancedModelBox(this, "left_arm");
		this.leftArm.setTextureOffset(32, 9);
		this.leftArm.mirror = true;
		this.leftArm.setRotationPoint(2.0F, 1.5F, -3.0F);
		this.leftArm.addBox(0.0F, 0.0F, -1.0F, 1, 2, 2);
		this.body2 = new AdvancedModelBox(this, "lower_body");
		this.body2.setTextureOffset(22, 0);
		this.body2.setRotationPoint(0.0F, -0.4F, 0.5F);
		this.body2.addBox(-2.0F, -2.0F, -5.0F, 4, 4, 5);
		this.rightThigh = new AdvancedModelBox(this, "right_thigh");
		this.rightThigh.setTextureOffset(22, 9);
		this.rightThigh.setRotationPoint(-2.5F, 0.0F, 5.0F);
		this.rightThigh.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3);
		this.head = new AdvancedModelBox(this, "head");
		this.head.setTextureOffset(22, 20);
		this.head.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.head.setTextureOffset(22, 20).addBox(-2.0F, -1.5F, -2.5F, 4, 3, 3);
		this.setRotateAngle(head, 0.18656417346763232F, 0.0F, 0.0F);
		this.body1 = new AdvancedModelBox(this, "body");
		this.body1.setTextureOffset(0, 0);
		this.body1.setRotationPoint(0.0F, 20.0F, 0.0F);
		this.body1.addBox(-2.5F, -3.0F, 0.0F, 5, 5, 6);
		this.rightFoot = new AdvancedModelBox(this, "right_foot");
		this.rightFoot.setTextureOffset(22, 15);
		this.rightFoot.setRotationPoint(0.0F, 3.0F, 0.5F);
		this.rightFoot.addBox(-0.5F, 0.0F, -3.5F, 1, 1, 4);
		this.tail1 = new AdvancedModelBox(this, "upper_tail");
		this.tail1.setTextureOffset(0, 11);
		this.tail1.setRotationPoint(0.0F, 0.0F, 6.0F);
		this.tail1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8);
		this.rightHand = new AdvancedModelBox(this, "right_hand");
		this.rightHand.setTextureOffset(32, 13);
		this.rightHand.setRotationPoint(-0.4F, 2.0F, -0.3F);
		this.rightHand.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 2);
		this.leftEar = new AdvancedModelBox(this, "left_ear");
		this.leftEar.setTextureOffset(33, 20);
		this.leftEar.mirror = true;
		this.leftEar.setRotationPoint(1.5F, -0.5F, 0.0F);
		this.leftEar.addBox(0.0F, -2.0F, 0.0F, 2, 2, 0);
		this.setRotateAngle(leftEar, 0.0F, -0.7853981633974483F, 0.0F);
		this.rightEar = new AdvancedModelBox(this, "right_ear");
		this.rightEar.setTextureOffset(33, 20);
		this.rightEar.setRotationPoint(-1.5F, -0.5F, 0.0F);
		this.rightEar.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 0);
		this.setRotateAngle(rightEar, 0.0F, 0.7853981633974483F, 0.0F);
		this.rightArm = new AdvancedModelBox(this, "right_arm");
		this.rightArm.setTextureOffset(32, 9);
		this.rightArm.setRotationPoint(-2.0F, 1.5F, -3.0F);
		this.rightArm.addBox(-1.0F, 0.0F, -1.0F, 1, 2, 2);
		this.leftThigh = new AdvancedModelBox(this, "left_thigh");
		this.leftThigh.setTextureOffset(22, 9);
		this.leftThigh.mirror = true;
		this.leftThigh.setRotationPoint(2.5F, 0.0F, 5.0F);
		this.leftThigh.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3);
		this.snout = new AdvancedModelBox(this, "snout");
		this.snout.setTextureOffset(22, 26);
		this.snout.setRotationPoint(0.0F, 0.5F, -2.5F);
		this.snout.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2);
		this.leftHand = new AdvancedModelBox(this, "left_hand");
		this.leftHand.setTextureOffset(32, 13);
		this.leftHand.mirror = true;
		this.leftHand.setRotationPoint(0.4F, 2.0F, -0.3F);
		this.leftHand.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 2);
		this.body2.addChild(this.neck);
		this.snout.addChild(this.nose);
		this.head.addChild(this.rightEye);
		this.leftThigh.addChild(this.leftFoot);
		this.snout.addChild(this.wisker1);
		this.snout.addChild(this.wisker2);
		this.tail1.addChild(this.tail2);
		this.head.addChild(this.leftEye);
		this.body2.addChild(this.leftArm);
		this.body1.addChild(this.body2);
		this.body1.addChild(this.rightThigh);
		this.neck.addChild(this.head);
		this.rightThigh.addChild(this.rightFoot);
		this.body1.addChild(this.tail1);
		this.rightArm.addChild(this.rightHand);
		this.head.addChild(this.leftEar);
		this.head.addChild(this.rightEar);
		this.body2.addChild(this.rightArm);
		this.body1.addChild(this.leftThigh);
		this.head.addChild(this.snout);
		this.leftArm.addChild(this.leftHand);
		this.animator = ModelAnimator.create();
		this.updateDefaultPose();

	}

	@Override
	public void setupAnim(T rat, float f, float f1, float f2, float f3, float f4) {
		this.resetToDefaultPose();
		float speedWalk = 2F;
		float degreeWalk = 0.3F;
		float speedRun = 2F;
		float degreeRun = 0.4F;
		float speedIdle = 0.75F;
		float degreeIdle = 0.15F;

		float ulating0 = 0.9F + (float) Math.sin(f2 * 0.75F) * 0.1F;
		this.faceTarget(f3, f4, 2, this.neck, this.head);
		this.head.rotateAngleX = f4 * ((float) Math.PI / 180F);
		this.head.rotateAngleY = f3 * ((float) Math.PI / 180F);

		this.swing(this.wisker2, speedIdle, degreeIdle, false, 0, 0, f2, 1);
		this.swing(this.wisker1, speedIdle, degreeIdle, true, 0, 0, f2, 1);
		this.flap(this.wisker2, speedIdle, degreeIdle, false, 1, 0, f2, 1);
		this.flap(this.wisker1, speedIdle, degreeIdle, false, 1, 0, f2, 1);
		this.walk(this.wisker2, speedIdle, degreeIdle, false, 2, 0, f2, 1);
		this.walk(this.wisker1, speedIdle, degreeIdle, false, 2, 0, f2, 1);
		this.nose.setScale(ulating0, ulating0, ulating0);
		if (rat instanceof RatShot) {
			this.walk(this.tail1, speedRun, degreeRun, false, -1, 0, f, f1);
			this.walk(this.tail2, speedRun, degreeRun * 0.5F, false, -2, 0, f, f1);
			this.walk(this.rightThigh, speedWalk, degreeWalk * 4F, false, 1, 0, f, f1);
			this.walk(this.rightFoot, speedWalk, degreeWalk * 2F, false, 3.5F, -0.1F, f, f1);
			this.walk(this.leftThigh, speedWalk, degreeWalk * 4F, true, 1, 0, f, f1);
			this.walk(this.leftFoot, speedWalk, degreeWalk * 2F, true, 3.5F, 0.1F, f, f1);
			this.walk(this.rightArm, speedWalk, degreeWalk * 4F, true, 1, 0, f, f1);
			this.walk(this.rightHand, speedWalk, degreeWalk * 2F, true, 3F, 0.1F, f, f1);
			this.walk(this.leftArm, speedWalk, degreeWalk * 4F, false, 1, 0, f, f1);
			this.walk(this.leftHand, speedWalk, degreeWalk * 2F, false, 3F, -0.1F, f, f1);
		} else {
			this.bob(this.body1, speedRun, degreeRun * 5F, false, f, f1);
			this.walk(this.body1, speedRun, degreeRun, false, 0, 0, f, f1);
			this.walk(this.body2, speedRun, degreeRun * 0.5F, false, 1, -0.1F, f, f1);
			this.walk(this.neck, speedRun, degreeRun * 0.5F, false, 2, 0, f, f1);
			this.walk(this.tail1, speedRun, degreeRun, false, -1, 0, f, f1);
			this.walk(this.tail2, speedRun, degreeRun * 0.5F, false, -2, 0, f, f1);
			this.walk(this.leftThigh, speedRun, degreeRun * 2F, true, 0, 0, f, f1);
			this.walk(this.rightThigh, speedRun, degreeRun * 2F, true, 0, 0, f, f1);
			this.walk(this.rightFoot, speedRun, degreeRun * 2F, true, 3, -0.1F, f, f1);
			this.walk(this.leftFoot, speedRun, degreeRun * 2F, true, 3, -0.1F, f, f1);
			this.walk(this.leftArm, speedRun, degreeRun * 2F, true, 2, 0, f, f1);
			this.walk(this.rightArm, speedRun, degreeRun * 2F, true, 2, 0, f, f1);
			this.walk(this.rightHand, speedRun, degreeRun * 2F, true, 5, -0.1F, f, f1);
			this.walk(this.leftHand, speedRun, degreeRun * 2F, true, 5, -0.1F, f, f1);
		}
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	public void progressRotation(AdvancedModelBox model, float progress, float rotX, float rotY, float rotZ, float divisor) {
		model.rotateAngleX += progress * (rotX - model.defaultRotationX) / divisor;
		model.rotateAngleY += progress * (rotY - model.defaultRotationY) / divisor;
		model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / divisor;
	}

	public void progressPosition(AdvancedModelBox model, float progress, float x, float y, float z, float divisor) {
		model.rotationPointX += progress * (x - model.defaultPositionX) / divisor;
		model.rotationPointY += progress * (y - model.defaultPositionY) / divisor;
		model.rotationPointZ += progress * (z - model.defaultPositionZ) / divisor;
	}

	protected void scratchPosition(AbstractRat rat) {
		if (!rat.isHoldingItemInHands()) {
			this.rotateFrom(this.neck, 38, 0, 0);
			this.rotateFrom(this.head, 58, 0, 0);
			this.rotateFrom(this.rightHand, 25, 10, -30);
			this.rotateFrom(this.leftHand, 25, -10, 30);
		}
	}

	protected void rotateFrom(AdvancedModelBox renderer, float degX, float degY, float degZ) {
		this.animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.body1);
	}

	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.body1, this.body2, this.nose, this.leftEye, this.rightEye, this.head, this.leftArm, this.leftEar, this.leftFoot, this.leftHand, this.leftThigh, this.neck, this.rightArm, this.rightEar, this.rightFoot, this.rightHand, this.rightThigh, this.snout, this.tail1, this.tail1, this.tail2, this.wisker1, this.wisker2);
	}

	@Override
	public void translateToHead(PoseStack stack) {
		this.body1.translateRotate(stack);
		this.body2.translateRotate(stack);
		this.neck.translateRotate(stack);
		this.head.translateRotate(stack);
	}

	@Override
	public void translateToBody(PoseStack stack) {
		this.body1.translateRotate(stack);
		this.body2.translateRotate(stack);
	}
}
