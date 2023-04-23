package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.monster.boss.Dutchrat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.entity.HumanoidArm;

public class FlyingDutchratModel<T extends Dutchrat> extends AdvancedEntityModel<T> implements ArmedModel {
	public final AdvancedModelBox body1;
	public final AdvancedModelBox body2;
	public final AdvancedModelBox neck;
	public final AdvancedModelBox leftArm1;
	public final AdvancedModelBox rightArm1;
	public final AdvancedModelBox coat;
	public final AdvancedModelBox body3;
	public final AdvancedModelBox body4;
	public final AdvancedModelBox tail1;
	public final AdvancedModelBox head;
	public final AdvancedModelBox upperJaw;
	public final AdvancedModelBox lowerJaw;
	public final AdvancedModelBox eyebrowLeft;
	public final AdvancedModelBox eyebrowRight;
	public final AdvancedModelBox earLeft;
	public final AdvancedModelBox earRight;
	public final AdvancedModelBox nose;
	public final AdvancedModelBox wiskers1;
	public final AdvancedModelBox wiskers2;
	public final AdvancedModelBox beard;
	public final AdvancedModelBox leftArm2;
	public final AdvancedModelBox hookBase;
	public final AdvancedModelBox hook1;
	public final AdvancedModelBox hook2;
	public final AdvancedModelBox rightArm2;
	public final AdvancedModelBox paw;
	public final ModelAnimator animator;

	public FlyingDutchratModel() {
		this.texWidth = 128;
		this.texHeight = 128;
		this.tail1 = new AdvancedModelBox(this, 113, 13);
		this.tail1.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.tail1.addBox(-1.5F, -1.0F, -1.5F, 3, 9, 3, 0.0F);
		this.setRotateAngle(this.tail1, 0.22759093446006054F, 0.0F, 0.0F);
		this.upperJaw = new AdvancedModelBox(this, 105, 25);
		this.upperJaw.setRotationPoint(0.0F, -3.0F, -6.5F);
		this.upperJaw.addBox(-2.0F, -1.5F, -4.0F, 4, 4, 6, 0.0F);
		this.setRotateAngle(this.upperJaw, 0.18203784098300857F, 0.0F, 0.0F);
		this.eyebrowLeft = new AdvancedModelBox(this, 76, 30);
		this.eyebrowLeft.setRotationPoint(3.0F, -4.0F, -2.0F);
		this.eyebrowLeft.addBox(-1.0F, -1.0F, -2.0F, 2, 1, 7, 0.0F);
		this.setRotateAngle(this.eyebrowLeft, 0.12217304763960307F, 0.17453292519943295F, 0.0F);
		this.nose = new AdvancedModelBox(this, 116, 0);
		this.nose.setRotationPoint(0.0F, -1.5F, -4.0F);
		this.nose.addBox(-1.0F, -0.5F, -1.0F, 2, 2, 3, 0.0F);
		this.setRotateAngle(this.nose, 0.14207500488242633F, 0.0F, 0.0F);
		this.body4 = new AdvancedModelBox(this, 0, 18);
		this.body4.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.body4.addBox(-2.5F, -2.0F, -2.0F, 5, 9, 4, 0.0F);
		this.setRotateAngle(this.body4, 0.22759093446006054F, 0.0F, 0.0F);
		this.earLeft = new AdvancedModelBox(this, 0, 51);
		this.earLeft.setRotationPoint(3.0F, -3.0F, 2.0F);
		this.earLeft.addBox(-1.0F, -3.0F, 0.0F, 4, 4, 0, 0.0F);
		this.setRotateAngle(this.earLeft, -0.6981317007977318F, -0.17453292519943295F, 0.7853981633974483F);
		this.neck = new AdvancedModelBox(this, 37, 0);
		this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.neck.addBox(-2.0F, -2.0F, -2.0F, 4, 2, 4, 0.0F);
		this.beard = new AdvancedModelBox(this, 0, 62);
		this.beard.setRotationPoint(0.0F, 0.5F, -4.0F);
		this.beard.addBox(-3.5F, 0.5F, 0.0F, 7, 7, 0, 0.0F);
		this.lowerJaw = new AdvancedModelBox(this, 18, 18);
		this.lowerJaw.setRotationPoint(0.0F, 0.0F, -4.0F);
		this.lowerJaw.addBox(-2.0F, -1.5F, -5.0F, 4, 2, 5, 0.0F);
		this.setRotateAngle(this.lowerJaw, -0.091106186954104F, 0.0F, 0.0F);
		this.wiskers1 = new AdvancedModelBox(this, 0, 55);
		this.wiskers1.mirror = true;
		this.wiskers1.setRotationPoint(2.0F, 0.0F, -2.0F);
		this.wiskers1.addBox(0.0F, -2.5F, 0.0F, 7, 7, 0, 0.0F);
		this.setRotateAngle(this.wiskers1, 0.0F, -0.3490658503988659F, 0.0F);
		this.leftArm2 = new AdvancedModelBox(this, 38, 37);
		this.leftArm2.setRotationPoint(8.0F, 0.0F, 0.0F);
		this.leftArm2.addBox(-0.5F, -1.5F, -1.5F, 15, 3, 3, 0.0F);
		this.setRotateAngle(this.leftArm2, -0.27314402793711257F, 0.7285004297824331F, 0.0F);
		this.coat = new AdvancedModelBox(this, 37, 16);
		this.coat.setRotationPoint(0.0F, 9.0F, 0.0F);
		this.coat.addBox(-7.0F, 0.0F, -4.5F, 14, 12, 9, 0.0F);
		this.rightArm2 = new AdvancedModelBox(this, 38, 37);
		this.rightArm2.mirror = true;
		this.rightArm2.setRotationPoint(-8.0F, 0.0F, 0.0F);
		this.rightArm2.addBox(-14.5F, -1.5F, -1.5F, 15, 3, 3, 0.0F);
		this.setRotateAngle(this.rightArm2, -0.27314402793711257F, -0.7285004297824331F, 0.0F);
		this.head = new AdvancedModelBox(this, 10, 29);
		this.head.setRotationPoint(0.0F, -1.7F, 0.0F);
		this.head.addBox(-3.0F, -5.0F, -5.0F, 6, 6, 8, 0.0F);
		this.setRotateAngle(this.head, 0.07505881616829756F, 0.0F, 0.0F);
		this.paw = new AdvancedModelBox(this, 0, 43);
		this.paw.setRotationPoint(-14.4F, 0.0F, 0.0F);
		this.paw.addBox(-4.0F, -1.5F, -2.5F, 4, 3, 5, 0.0F);
		this.hook2 = new AdvancedModelBox(this, 74, 16);
		this.hook2.setRotationPoint(2.0F, 0.0F, 0.0F);
		this.hook2.addBox(1.0F, -1.0F, -4.0F, 2, 2, 3, 0.0F);
		this.rightArm1 = new AdvancedModelBox(this, 84, 0);
		this.rightArm1.mirror = true;
		this.rightArm1.setRotationPoint(-7.0F, 4.0F, 0.0F);
		this.rightArm1.addBox(-9.0F, -2.0F, -2.0F, 12, 4, 4, 0.0F);
		this.setRotateAngle(this.rightArm1, 0.0F, -0.3490658503988659F, -0.8726646259971648F);
		this.body3 = new AdvancedModelBox(this, 83, 16);
		this.body3.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.body3.addBox(-4.5F, -2.0F, -2.5F, 9, 9, 5, 0.0F);
		this.setRotateAngle(this.body3, 0.18203784098300857F, 0.0F, 0.0F);
		this.body2 = new AdvancedModelBox(this, 46, 0);
		this.body2.setRotationPoint(0.0F, 9.0F, 0.0F);
		this.body2.addBox(-6.0F, -2.0F, -3.5F, 12, 9, 7, 0.0F);
		this.setRotateAngle(this.body2, 0.091106186954104F, 0.0F, 0.0F);
		this.earRight = new AdvancedModelBox(this, 0, 51);
		this.earRight.mirror = true;
		this.earRight.setRotationPoint(-3.0F, -3.0F, 2.0F);
		this.earRight.addBox(-3.0F, -3.0F, 0.0F, 4, 4, 0, 0.0F);
		this.setRotateAngle(this.earRight, -0.6981317007977318F, 0.17453292519943295F, -0.7853981633974483F);
		this.hookBase = new AdvancedModelBox(this, 107, 35);
		this.hookBase.setRotationPoint(14.0F, 0.0F, 0.0F);
		this.hookBase.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
		this.setRotateAngle(this.hookBase, 0.6981317007977318F, 0.0F, 0.136659280431156F);
		this.leftArm1 = new AdvancedModelBox(this, 84, 0);
		this.leftArm1.setRotationPoint(7.0F, 4.0F, 0.0F);
		this.leftArm1.addBox(-3.0F, -2.0F, -2.0F, 12, 4, 4, 0.0F);
		this.setRotateAngle(this.leftArm1, 0.0F, 0.3490658503988659F, 0.8726646259971648F);
		this.hook1 = new AdvancedModelBox(this, 112, 8);
		this.hook1.setRotationPoint(2.0F, 0.0F, 0.0F);
		this.hook1.addBox(0.0F, -1.0F, -1.0F, 5, 2, 2, 0.0F);
		this.eyebrowRight = new AdvancedModelBox(this, 94, 30);
		this.eyebrowRight.setRotationPoint(-3.0F, -4.0F, -2.0F);
		this.eyebrowRight.addBox(-1.0F, -1.0F, -2.0F, 2, 1, 7, 0.0F);
		this.setRotateAngle(this.eyebrowRight, 0.12217304763960307F, -0.17453292519943295F, 0.0F);
		this.wiskers2 = new AdvancedModelBox(this, 0, 55);
		this.wiskers2.setRotationPoint(-2.0F, 0.0F, -2.0F);
		this.wiskers2.addBox(-7.0F, -2.5F, 0.0F, 7, 7, 0, 0.0F);
		this.setRotateAngle(this.wiskers2, 0.0F, 0.3490658503988659F, 0.0F);
		this.body1 = new AdvancedModelBox(this, 0, 0);
		this.body1.setRotationPoint(0.0F, -13.0F, 0.0F);
		this.body1.addBox(-7.0F, 0.0F, -4.5F, 14, 9, 9, 0.0F);
		this.body4.addChild(this.tail1);
		this.head.addChild(this.upperJaw);
		this.head.addChild(this.eyebrowLeft);
		this.upperJaw.addChild(this.nose);
		this.body3.addChild(this.body4);
		this.head.addChild(this.earLeft);
		this.body1.addChild(this.neck);
		this.lowerJaw.addChild(this.beard);
		this.head.addChild(this.lowerJaw);
		this.upperJaw.addChild(this.wiskers1);
		this.leftArm1.addChild(this.leftArm2);
		this.body1.addChild(this.coat);
		this.rightArm1.addChild(this.rightArm2);
		this.neck.addChild(this.head);
		this.rightArm2.addChild(this.paw);
		this.hook1.addChild(this.hook2);
		this.body1.addChild(this.rightArm1);
		this.body2.addChild(this.body3);
		this.body1.addChild(this.body2);
		this.head.addChild(this.earRight);
		this.leftArm2.addChild(this.hookBase);
		this.body1.addChild(this.leftArm1);
		this.hookBase.addChild(this.hook1);
		this.head.addChild(this.eyebrowRight);
		this.upperJaw.addChild(this.wiskers2);
		this.lowerJaw.setScale(0.99F, 0.99F, 0.99F);
		this.updateDefaultPose();
		this.animator = ModelAnimator.create();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.body1);
	}

	public void animate(Dutchrat entity) {
		this.resetToDefaultPose();
		this.animator.update(entity);
		this.animator.setAnimation(Dutchrat.ANIMATION_SLASH);
		this.animator.startKeyframe(5);
		this.rotate(this.body1, 0, 20, 0);
		this.rotate(this.rightArm1, -80, 80, 30);
		this.rotate(this.rightArm2, -10, 0, 0);
		this.rotate(this.leftArm1, 0, -20, 0);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.beard, -50, 0, 0);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.body1, 0, -60, 0);
		this.rotate(this.rightArm1, 40, 0, 30);
		this.rotate(this.leftArm1, 0, 20, 0);
		this.rotate(this.paw, 0, 30, 0);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.body1, 0, -20, 0);
		this.rotate(this.rightArm2, -10, 0, 0);
		this.rotate(this.leftArm1, -40, -20, 30);
		this.rotate(this.leftArm2, 0, 60, 0);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.body1, 0, 60, 0);
		this.rotate(this.rightArm1, 40, 0, -30);
		this.rotate(this.leftArm1, 0, -20, 0);
		this.rotate(this.leftArm2, 0, 20, 0);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(Dutchrat.ANIMATION_STAB);
		this.animator.startKeyframe(5);
		this.rotate(this.body1, 0, 20, 0);
		this.rotate(this.rightArm1, 30, 100, 30);
		this.rotate(this.leftArm1, 0, -20, 0);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotate(this.body1, 0, -30, 0);
		this.rotate(this.rightArm1, 20, -30, 0);
		this.rotate(this.rightArm2, 20, 30, 0);
		this.rotate(this.paw, 0, 20, 0);
		this.animator.endKeyframe();
		this.animator.setStaticKeyframe(2);
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(Dutchrat.ANIMATION_SPEAK);
		this.animator.startKeyframe(5);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(Dutchrat.ANIMATION_THROW);
		this.animator.startKeyframe(5);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.body1, 0, 60, 0);
		this.rotate(this.rightArm1, 0, -180, -70);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.rotate(this.beard, -50, 0, 0);
		this.rotate(this.lowerJaw, 50, 0, 0);
		this.rotate(this.body1, 0, -20, 0);
		this.rotate(this.rightArm1, 0, 20, 10);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
	}

	private void rotate(AdvancedModelBox renderer, float degX, float degY, float degZ) {
		this.animator.rotate(renderer, (float) Math.toRadians(degX), (float) Math.toRadians(degY), (float) Math.toRadians(degZ));
	}

	@Override
	public void setupAnim(T rat, float f, float f1, float f2, float f3, float f4) {
		this.animate(rat);
		this.handleSpawningAnim(rat);
		float idleSpeed = 0.1F;
		float idleDegree = 0.1F;
		float walkSpeed = 0.6F;
		float walkDegree = 0.3F;
		this.bob(this.body1, idleSpeed, idleDegree * 7F, false, f2, 1);
		this.bob(this.rightArm1, idleSpeed, idleDegree * 3.5F, false, f2, 1);
		this.bob(this.leftArm1, idleSpeed, idleDegree * 3.5F, false, f2, 1);
		this.walk(this.head, idleSpeed * 0.5F, idleDegree, false, 0, 0, f2, 1);
		float ulatingScale = 0.9F + (float) Math.sin(f2 * 0.15F) * 0.1F;
		this.swing(this.wiskers1, idleSpeed, idleDegree, false, 0, 0, f2, 1);
		this.swing(this.wiskers2, idleSpeed, idleDegree, true, 0, 0, f2, 1);
		this.flap(this.wiskers2, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.flap(this.wiskers1, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.walk(this.wiskers2, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.walk(this.wiskers1, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.walk(this.beard, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.swing(this.rightArm1, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.swing(this.leftArm1, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.swing(this.rightArm2, idleSpeed, idleDegree, false, 3, -0.1F, f2, 1);
		this.swing(this.leftArm2, idleSpeed, idleDegree, false, 3, 0.1F, f2, 1);
		this.walk(this.paw, idleSpeed, idleDegree, false, 3, 0.1F, f2, 1);
		this.swing(this.body2, idleSpeed, idleDegree, false, 0, 0, f2, 1);
		this.swing(this.body3, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.walk(this.body3, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.swing(this.body4, idleSpeed, idleDegree, false, 3, 0, f2, 1);
		this.walk(this.body4, idleSpeed, idleDegree, false, 4, 0, f2, 1);
		this.walk(this.tail1, idleSpeed, idleDegree, false, 5, 0, f2, 1);
		this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
		this.swing(this.rightArm1, walkSpeed, walkDegree * 1.5F, true, 1, -0.5F, f, f1);
		this.swing(this.leftArm1, walkSpeed, walkDegree * 1.5F, true, 1, 0.5F, f, f1);
		this.swing(this.rightArm2, walkSpeed, walkDegree, false, 2, -0.1F, f, f1);
		this.swing(this.leftArm2, walkSpeed, walkDegree, false, 2, 0.1F, f, f1);
		this.walk(this.body3, walkSpeed, walkDegree, false, 3, 0, f, f1);
	}

	private void handleSpawningAnim(T rat) {
		this.tail1.showModel = this.showWithTickDelay(rat, 0);
		this.body4.showModel = this.showWithTickDelay(rat, 2);
		this.body3.showModel = this.showWithTickDelay(rat, 5);
		this.body2.showModel = this.showWithTickDelay(rat, 10);
		this.coat.showModel = this.showWithTickDelay(rat, 10);
		this.body1.showModel = this.showWithTickDelay(rat, 20);
		this.neck.showModel = this.showWithTickDelay(rat, 40);
		this.leftArm1.showModel = this.showWithTickDelay(rat, 40);
		this.rightArm1.showModel = this.showWithTickDelay(rat, 40);
		this.leftArm2.showModel = this.showWithTickDelay(rat, 50);
		this.rightArm2.showModel = this.showWithTickDelay(rat, 50);
		this.paw.showModel = this.showWithTickDelay(rat, 60);
		this.hookBase.showModel = this.showWithTickDelay(rat, 60);
		this.hook1.showModel = this.showWithTickDelay(rat, 60);
		this.hook2.showModel = this.showWithTickDelay(rat, 60);
		this.head.showModel = this.showWithTickDelay(rat, 70);
		this.eyebrowLeft.showModel = this.showWithTickDelay(rat, 70);
		this.eyebrowRight.showModel = this.showWithTickDelay(rat, 70);
		this.earLeft.showModel = this.showWithTickDelay(rat, 75);
		this.earRight.showModel = this.showWithTickDelay(rat, 75);
		this.upperJaw.showModel = this.showWithTickDelay(rat, 80);
		this.nose.showModel = this.showWithTickDelay(rat, 80);
		this.wiskers1.showModel = this.showWithTickDelay(rat, 82);
		this.wiskers2.showModel = this.showWithTickDelay(rat, 82);
		this.lowerJaw.showModel = this.showWithTickDelay(rat, 85);
		this.beard.showModel = this.showWithTickDelay(rat, 85);
	}

	private boolean showWithTickDelay(T rat, int delay) {
		int smokeEndTime = 85;
		return rat.getBellSummonTicks() - smokeEndTime + delay <= 0;
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.body1,
				this.body2,
				this.neck,
				this.leftArm1,
				this.rightArm1,
				this.coat,
				this.body3,
				this.body4,
				this.tail1,
				this.head,
				this.upperJaw,
				this.lowerJaw,
				this.eyebrowLeft,
				this.eyebrowRight,
				this.earLeft,
				this.earRight,
				this.nose,
				this.wiskers1,
				this.wiskers2,
				this.beard,
				this.leftArm2,
				this.hookBase,
				this.hook1,
				this.hook2,
				this.rightArm2,
				this.paw);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack stack) {
		this.body1.translateRotate(stack);
		this.rightArm1.translateRotate(stack);
		this.rightArm2.translateRotate(stack);
		this.paw.translateRotate(stack);
		stack.mulPose(Axis.ZP.rotationDegrees(90));
		stack.translate(0.1F, -0.45F, 0);
	}
}
