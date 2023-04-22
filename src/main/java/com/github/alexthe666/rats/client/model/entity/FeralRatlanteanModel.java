package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.ratlantis.FeralRatlantean;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Mob;

public class FeralRatlanteanModel<T extends Mob & IAnimatedEntity> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox lowerbody;
	public final AdvancedModelBox midBody;
	public final AdvancedModelBox backLeftThigh;
	public final AdvancedModelBox backRightThigh;
	public final AdvancedModelBox tail1;
	public final AdvancedModelBox tatters3;
	public final AdvancedModelBox frontBody;
	public final AdvancedModelBox tatters1;
	public final AdvancedModelBox tatters2;
	public final AdvancedModelBox frontRightLeg;
	public final AdvancedModelBox frontLeftLeg;
	public final AdvancedModelBox neck;
	public final AdvancedModelBox frontRightHeel;
	public final AdvancedModelBox frontRightFoot;
	public final AdvancedModelBox frontLeftHeel;
	public final AdvancedModelBox frontLeftFoot;
	public final AdvancedModelBox head1;
	public final AdvancedModelBox tatters4;
	public final AdvancedModelBox snoutUpper;
	public final AdvancedModelBox mouth1;
	public final AdvancedModelBox ear1;
	public final AdvancedModelBox ear2;
	public final AdvancedModelBox nose;
	public final AdvancedModelBox teeth;
	public final AdvancedModelBox wisker1;
	public final AdvancedModelBox wisker2;
	public final AdvancedModelBox backLeftLeg;
	public final AdvancedModelBox backLeftHeel;
	public final AdvancedModelBox backLeftFoot;
	public final AdvancedModelBox backRightLeg;
	public final AdvancedModelBox backRightHeel;
	public final AdvancedModelBox backRightFoot;
	public final AdvancedModelBox tail2;
	public final AdvancedModelBox tail3;
	public final AdvancedModelBox tail4;
	public final ModelAnimator animator;

	public FeralRatlanteanModel() {
		this.texWidth = 128;
		this.texHeight = 64;
		this.tail2 = new AdvancedModelBox(this, "tail2");
		this.tail2.setTextureOffset(15, 50);
		this.tail2.setRotationPoint(0.0F, 0.1F, 7.5F);
		this.tail2.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 8, 0.0F);
		this.setRotateAngle(this.tail2, 0.2617993877991494F, 0.0F, 0.0F);
		this.frontLeftHeel = new AdvancedModelBox(this, "front_left_heel");
		this.frontLeftHeel.setTextureOffset(34, 0);
		this.frontLeftHeel.setRotationPoint(1.0F, 4.7F, 1.0F);
		this.frontLeftHeel.addBox(-0.5F, 0.0F, -2.0F, 1, 6, 2, 0.0F);
		this.setRotateAngle(this.frontLeftHeel, -0.9599310885968813F, 0.0F, 0.0F);
		this.frontRightHeel = new AdvancedModelBox(this, "front_right_heel");
		this.frontRightHeel.setTextureOffset(34, 0);
		this.frontRightHeel.setRotationPoint(-1.0F, 4.7F, 1.0F);
		this.frontRightHeel.addBox(-0.5F, 0.0F, -2.0F, 1, 6, 2, 0.0F);
		this.setRotateAngle(this.frontRightHeel, -0.9599310885968813F, 0.0F, 0.0F);
		this.nose = new AdvancedModelBox(this, "nose");
		this.nose.setTextureOffset(36, 27);
		this.nose.setRotationPoint(0.0F, 0.5F, -4.0F);
		this.nose.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 3, 0.0F);
		this.setRotateAngle(this.nose, 0.31869712141416456F, 0.0F, 0.0F);
		this.teeth = new AdvancedModelBox(this, "teeth");
		this.teeth.setTextureOffset(24, 43);
		this.teeth.setRotationPoint(0.0F, 3.0F, 0.0F);
		this.teeth.addBox(-2.0F, -0.1F, -5.0F, 4, 2, 5, 0.0F);
		this.tail4 = new AdvancedModelBox(this, "tail4");
		this.tail4.setTextureOffset(0, 53);
		this.tail4.setRotationPoint(0.0F, 0.0F, 7.5F);
		this.tail4.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8, 0.0F);
		this.setRotateAngle(this.tail4, 0.2617993877991494F, 0.0F, 0.0F);
		this.lowerbody = new AdvancedModelBox(this, "lower_body");
		this.lowerbody.setTextureOffset(0, 0);
		this.lowerbody.setRotationPoint(0.0F, 12.3F, 4.0F);
		this.lowerbody.addBox(-3.5F, -4.0F, -1.0F, 7, 8, 10, 0.0F);
		this.setRotateAngle(this.lowerbody, -0.08726646259971647F, 0.0F, 0.0F);
		this.tatters4 = new AdvancedModelBox(this, "tatters4");
		this.tatters4.setTextureOffset(89, 0);
		this.tatters4.setRotationPoint(-2.0F, -1.0F, -1.0F);
		this.tatters4.addBox(0.0F, 0.0F, -3.0F, 0, 9, 6, 0.0F);
		this.setRotateAngle(this.tatters4, 0.0F, 0.0F, 0.2617993877991494F);
		this.frontBody = new AdvancedModelBox(this, "front_body");
		this.frontBody.setTextureOffset(15, 27);
		this.frontBody.setRotationPoint(0.0F, 0.0F, -6.0F);
		this.frontBody.addBox(-3.5F, -3.5F, -7.0F, 7, 7, 7, 0.0F);
		this.setRotateAngle(this.frontBody, 0.2617993877991494F, 0.0F, 0.0F);
		this.frontLeftFoot = new AdvancedModelBox(this, "front_left_foot");
		this.frontLeftFoot.setTextureOffset(0, 29);
		this.frontLeftFoot.setRotationPoint(0.0F, 5.1F, -0.7F);
		this.frontLeftFoot.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 5, 0.0F);
		this.setRotateAngle(this.frontLeftFoot, 0.08726646259971647F, 0.0F, 0.0F);
		this.mouth1 = new AdvancedModelBox(this, "mouth1");
		this.mouth1.setTextureOffset(13, 41);
		this.mouth1.setRotationPoint(0.0F, 1.5F, -5.0F);
		this.mouth1.addBox(-1.5F, 0.0F, -5.7F, 3, 1, 5, 0.0F);
		this.backLeftHeel = new AdvancedModelBox(this, "back_left_heel");
		this.backLeftHeel.setTextureOffset(0, 44);
		this.backLeftHeel.setRotationPoint(0.0F, 6.0F, 0.2F);
		this.backLeftHeel.addBox(-0.5F, 0.0F, -2.0F, 1, 6, 2, 0.0F);
		this.setRotateAngle(this.backLeftHeel, -2.007128639793479F, 0.0F, 0.0F);
		this.backRightFoot = new AdvancedModelBox(this, "back_right_foot");
		this.backRightFoot.setTextureOffset(1, 47);
		this.backRightFoot.setRotationPoint(0.0F, 5.1F, -0.7F);
		this.backRightFoot.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 5, 0.0F);
		this.setRotateAngle(this.backRightFoot, 0.6108652381980153F, 0.0F, 0.0F);
		this.tail3 = new AdvancedModelBox(this, "tail3");
		this.tail3.setTextureOffset(0, 53);
		this.tail3.setRotationPoint(0.0F, 0.0F, 7.5F);
		this.tail3.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8, 0.0F);
		this.setRotateAngle(this.tail3, 0.2617993877991494F, 0.0F, 0.0F);
		this.tatters3 = new AdvancedModelBox(this, "tatters3");
		this.tatters3.setTextureOffset(102, 0);
		this.tatters3.setRotationPoint(3.5F, -4.0F, 7.0F);
		this.tatters3.addBox(0.0F, 0.0F, -3.0F, 0, 9, 6, 0.0F);
		this.setRotateAngle(this.tatters3, 0.0F, 0.0F, -0.9105382707654417F);
		this.backLeftThigh = new AdvancedModelBox(this, "back_left_thigh");
		this.backLeftThigh.setTextureOffset(30, 15);
		this.backLeftThigh.setRotationPoint(3.5F, 0.0F, 5.0F);
		this.backLeftThigh.addBox(0.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
		this.setRotateAngle(this.backLeftThigh, -0.7853981633974483F, 0.0F, 0.0F);
		this.ear1 = new AdvancedModelBox(this, "left_ear");
		this.ear1.setTextureOffset(71, -3);
		this.ear1.setRotationPoint(2.5F, -2.0F, -2.0F);
		this.ear1.addBox(0.0F, 0.0F, 0.0F, 0, 3, 3, 0.0F);
		this.setRotateAngle(this.ear1, 0.7853981633974483F, 0.7853981633974483F, 0.0F);
		this.frontRightFoot = new AdvancedModelBox(this, "front_left_foot");
		this.frontRightFoot.setTextureOffset(0, 29);
		this.frontRightFoot.setRotationPoint(0.0F, 5.1F, -0.7F);
		this.frontRightFoot.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 5, 0.0F);
		this.setRotateAngle(this.frontRightFoot, 0.08726646259971647F, 0.0F, 0.0F);
		this.tatters2 = new AdvancedModelBox(this, "tatters2");
		this.tatters2.setTextureOffset(76, 0);
		this.tatters2.setRotationPoint(-3.0F, -3.0F, -6.0F);
		this.tatters2.addBox(0.0F, 0.0F, -3.0F, 0, 8, 6, 0.0F);
		this.setRotateAngle(this.tatters2, 0.0F, 0.0F, 0.5235987755982988F);
		this.frontLeftLeg = new AdvancedModelBox(this, "front_left_leg");
		this.frontLeftLeg.setTextureOffset(0, 0);
		this.frontLeftLeg.setRotationPoint(3.5F, 2.0F, -4.0F);
		this.frontLeftLeg.addBox(0.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
		this.setRotateAngle(this.frontLeftLeg, 0.5235987755982988F, 0.0F, 0.0F);
		this.neck = new AdvancedModelBox(this, "neck");
		this.neck.setTextureOffset(43, 27);
		this.neck.setRotationPoint(0.0F, -0.5F, -7.0F);
		this.neck.addBox(-2.0F, -2.5F, -4.0F, 4, 5, 5, 0.0F);
		this.setRotateAngle(this.neck, -0.2617993877991494F, 0.0F, 0.0F);
		this.tail1 = new AdvancedModelBox(this, "tail1");
		this.tail1.setTextureOffset(0, 18);
		this.tail1.setRotationPoint(0.0F, -1.0F, 8.0F);
		this.tail1.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 8, 0.0F);
		this.setRotateAngle(this.tail1, -0.6981317007977318F, 0.0F, 0.0F);
		this.frontRightLeg = new AdvancedModelBox(this, "front_left_leg");
		this.frontLeftLeg.setTextureOffset(0, 0);
		this.frontRightLeg.setRotationPoint(-3.5F, 2.0F, -4.0F);
		this.frontRightLeg.addBox(-2.0F, -1.0F, -1.5F, 2, 6, 3, 0.0F);
		this.setRotateAngle(this.frontRightLeg, 0.5235987755982988F, 0.0F, 0.0F);
		this.midBody = new AdvancedModelBox(this, "mid_body");
		this.midBody.setTextureOffset(34, 0);
		this.midBody.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.midBody.addBox(-3.0F, -3.0F, -7.0F, 6, 7, 8, 0.0F);
		this.setRotateAngle(this.midBody, 0.17453292519943295F, 0.0F, 0.0F);
		this.snoutUpper = new AdvancedModelBox(this, "snout_upper");
		this.snoutUpper.setTextureOffset(0, 36);
		this.snoutUpper.setRotationPoint(0.0F, -1.3F, -6.0F);
		this.snoutUpper.addBox(-2.0F, 0.0F, -5.0F, 4, 3, 5, 0.0F);
		this.ear2 = new AdvancedModelBox(this, "right_ear");
		this.ear2.setTextureOffset(71, -3);
		this.ear2.setRotationPoint(-2.5F, -2.0F, -2.0F);
		this.ear2.addBox(0.0F, 0.0F, 0.0F, 0, 3, 3, 0.0F);
		this.setRotateAngle(this.ear2, 0.7853981633974483F, -0.7853981633974483F, 0.0F);
		this.wisker1 = new AdvancedModelBox(this, "whisker1");
		this.wisker1.setTextureOffset(63, 0);
		this.wisker1.mirror = true;
		this.wisker1.setRotationPoint(2.0F, 1.0F, -4.0F);
		this.wisker1.addBox(0.0F, -2.0F, 0.0F, 4, 4, 0, 0.0F);
		this.setRotateAngle(this.wisker1, 0.0F, -0.8726646259971648F, 0.0F);
		this.backLeftFoot = new AdvancedModelBox(this, "back_left_foot");
		this.backLeftFoot.setTextureOffset(1, 47);
		this.backLeftFoot.mirror = true;
		this.backLeftFoot.setRotationPoint(0.0F, 5.1F, -0.7F);
		this.backLeftFoot.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 5, 0.0F);
		this.setRotateAngle(this.backLeftFoot, 0.6108652381980153F, 0.0F, 0.0F);
		this.backLeftLeg = new AdvancedModelBox(this, "back_left_leg");
		this.backLeftLeg.setTextureOffset(0, 18);
		this.backLeftLeg.setRotationPoint(1.2F, 7.0F, 1.5F);
		this.backLeftLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
		this.setRotateAngle(this.backLeftLeg, 2.2689280275926285F, 0.0F, 0.0F);
		this.tatters1 = new AdvancedModelBox(this, "tatters1");
		this.tatters1.setTextureOffset(63, 0);
		this.tatters1.setRotationPoint(3.0F, -1.0F, -3.0F);
		this.tatters1.addBox(0.0F, 0.0F, -3.0F, 0, 9, 6, 0.0F);
		this.setRotateAngle(this.tatters1, 0.0F, 0.0F, -0.2617993877991494F);
		this.head1 = new AdvancedModelBox(this, "head");
		this.head1.setTextureOffset(37, 37);
		this.head1.setRotationPoint(0.0F, 0.0F, -2.5F);
		this.head1.addBox(-3.0F, -2.5F, -6.0F, 6, 5, 6, 0.0F);
		this.setRotateAngle(this.head1, -0.08726646259971647F, 0.0F, 0.0F);
		this.backRightThigh = new AdvancedModelBox(this, "back_right_thigh");
		this.backRightThigh.setTextureOffset(30, 15);
		this.backRightThigh.setRotationPoint(-3.5F, 0.0F, 5.0F);
		this.backRightThigh.addBox(-3.0F, -1.0F, -2.0F, 3, 8, 4, 0.0F);
		this.setRotateAngle(this.backRightThigh, -0.7853981633974483F, 0.0F, 0.0F);
		this.backRightHeel = new AdvancedModelBox(this, "back_right_heel");
		this.backRightHeel.setTextureOffset(0, 44);
		this.backRightHeel.setRotationPoint(0.0F, 6.0F, 0.2F);
		this.backRightHeel.addBox(-0.5F, 0.0F, -2.0F, 1, 6, 2, 0.0F);
		this.setRotateAngle(this.backRightHeel, -2.007128639793479F, 0.0F, 0.0F);
		this.wisker2 = new AdvancedModelBox(this, "whisker2");
		this.wisker2.setTextureOffset(63, 0);
		this.wisker2.setRotationPoint(-2.0F, 1.0F, -4.0F);
		this.wisker2.addBox(-4.0F, -2.0F, 0.0F, 4, 4, 0, 0.0F);
		this.setRotateAngle(this.wisker2, 0.0F, 0.8726646259971648F, 0.0F);
		this.backRightLeg = new AdvancedModelBox(this, "back_right_leg");
		this.backRightLeg.setTextureOffset(0, 18);
		this.backRightLeg.setRotationPoint(-1.2F, 7.0F, 1.5F);
		this.backRightLeg.addBox(-1.0F, 0.0F, 0.0F, 2, 6, 2, 0.0F);
		this.setRotateAngle(this.backRightLeg, 2.2689280275926285F, 0.0F, 0.0F);
		this.tail1.addChild(this.tail2);
		this.frontLeftLeg.addChild(this.frontLeftHeel);
		this.frontRightLeg.addChild(this.frontRightHeel);
		this.snoutUpper.addChild(this.nose);
		this.snoutUpper.addChild(this.teeth);
		this.tail3.addChild(this.tail4);
		this.neck.addChild(this.tatters4);
		this.midBody.addChild(this.frontBody);
		this.frontLeftHeel.addChild(this.frontLeftFoot);
		this.head1.addChild(this.mouth1);
		this.backLeftLeg.addChild(this.backLeftHeel);
		this.backRightHeel.addChild(this.backRightFoot);
		this.tail2.addChild(this.tail3);
		this.lowerbody.addChild(this.tatters3);
		this.lowerbody.addChild(this.backLeftThigh);
		this.head1.addChild(this.ear1);
		this.frontRightHeel.addChild(this.frontRightFoot);
		this.midBody.addChild(this.tatters2);
		this.frontBody.addChild(this.frontLeftLeg);
		this.frontBody.addChild(this.neck);
		this.lowerbody.addChild(this.tail1);
		this.frontBody.addChild(this.frontRightLeg);
		this.lowerbody.addChild(this.midBody);
		this.head1.addChild(this.snoutUpper);
		this.head1.addChild(this.ear2);
		this.snoutUpper.addChild(this.wisker1);
		this.backLeftHeel.addChild(this.backLeftFoot);
		this.backLeftThigh.addChild(this.backLeftLeg);
		this.midBody.addChild(this.tatters1);
		this.neck.addChild(this.head1);
		this.lowerbody.addChild(this.backRightThigh);
		this.backRightLeg.addChild(this.backRightHeel);
		this.snoutUpper.addChild(this.wisker2);
		this.backRightThigh.addChild(this.backRightLeg);
		this.animator = ModelAnimator.create();
		this.teeth.setScale(0.99F, 0.99F, 0.99F);
		this.tail3.setScale(0.99F, 0.99F, 0.99F);
		this.tail4.setScale(0.98F, 0.98F, 0.98F);
		this.updateDefaultPose();
	}

	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.lowerbody);
	}

	public void animate(IAnimatedEntity entity) {
		this.resetToDefaultPose();
		this.animator.update(entity);
		this.animator.setAnimation(FeralRatlantean.ANIMATION_BITE);
		this.animator.startKeyframe(5);
		this.rotateFrom(this.lowerbody, 0, 10, 0);
		this.rotateFrom(this.midBody, 0, 10, 0);
		this.rotateFrom(this.frontBody, 0, -30, 0);
		this.rotateFrom(this.neck, 0, -30, 0);
		this.rotateFrom(this.head1, 0, 60, 0);
		this.rotateFrom(this.frontRightFoot, 20, 0, 0);
		this.rotateFrom(this.frontLeftFoot, 20, 0, 0);
		this.animator.move(this.frontRightLeg, 0, 1, 0);
		this.animator.move(this.frontLeftLeg, 0, 1, 0);
		this.animator.move(this.lowerbody, 0, 0, 2);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(lowerbody, 0, 0, -4);
		this.rotateFrom(this.neck, -5, -5, 0);
		this.rotateFrom(this.head1, -30, 5, 0);
		this.rotateFrom(this.mouth1, 40, 5, 0);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.move(this.lowerbody, 0, 0, -1);
		this.rotateFrom(this.neck, -5, -5, 0);
		this.rotateFrom(this.head1, -20, 5, 0);
		this.rotateFrom(this.mouth1, -10, 5, 0);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(3);
		this.animator.setAnimation(FeralRatlantean.ANIMATION_SNIFF);
		this.animator.startKeyframe(5);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(-5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 10, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, -20, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(-5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 20, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, -20, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(-5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 20, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(2);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(5));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, -20, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(FeralRatlantean.ANIMATION_SLASH);
		this.animator.startKeyframe(5);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(-25));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftLeg, -5, 0, -75);
		this.rotateFrom(this.frontLeftHeel, -30, 0, 0);
		this.rotateFrom(this.frontRightLeg, 50, 0, 15);
		this.animator.rotate(this.frontLeftFoot, (float) Math.toRadians(-10), (float) Math.toRadians(-45), (float) Math.toRadians(45));
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 60, 0, 10);
		this.rotateFrom(this.mouth1, 30, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(25));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontLeftLeg, -5, 0, 45);
		this.rotateFrom(this.frontLeftHeel, -30, 0, 0);
		this.rotateFrom(this.frontRightLeg, 50, 0, 15);
		this.animator.rotate(this.frontLeftFoot, (float) Math.toRadians(-10), (float) Math.toRadians(-45), (float) Math.toRadians(45));
		this.rotateFrom(this.frontRightFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 60, 0, -10);
		this.rotateFrom(this.mouth1, 30, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(25));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontRightLeg, -5, 0, 75);
		this.rotateFrom(this.frontRightHeel, -30, 0, 0);
		this.rotateFrom(this.frontLeftLeg, 50, 0, -15);
		this.animator.rotate(this.frontRightFoot, (float) Math.toRadians(-10), (float) Math.toRadians(45), (float) Math.toRadians(-45));
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 30, 0, -10);
		this.rotateFrom(this.mouth1, 60, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.animator.rotate(this.lowerbody, (float) Math.toRadians(-40), 0, 0);
		this.animator.rotate(this.midBody, (float) Math.toRadians(-15), 0, 0);
		this.animator.rotate(this.frontBody, (float) Math.toRadians(-5), 0, (float) Math.toRadians(-25));
		this.animator.rotate(this.backLeftThigh, (float) Math.toRadians(40), 0, 0);
		this.animator.rotate(this.backRightThigh, (float) Math.toRadians(40), 0, 0);
		this.rotateFrom(this.frontRightLeg, -5, 0, -45);
		this.rotateFrom(this.frontRightHeel, -30, 0, 0);
		this.rotateFrom(this.frontLeftLeg, 50, 0, -15);
		this.animator.rotate(this.frontRightFoot, (float) Math.toRadians(-10), (float) Math.toRadians(45), (float) Math.toRadians(-45));
		this.rotateFrom(this.frontLeftFoot, 150, 0, 0);
		this.rotateFrom(this.tail1, 20, 0, 0);
		this.rotateFrom(this.head1, 60, 0, 10);
		this.rotateFrom(this.mouth1, 30, 0, 0);
		this.animator.move(this.lowerbody, 0, -3, -1);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(9);
	}

	protected void rotateFrom(AdvancedModelBox renderer, float degX, float degY, float degZ) {
		this.animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
	}

	@Override
	public void setupAnim(T rat, float f, float f1, float f2, float f3, float f4) {
		this.animate(rat);
		float idleSpeed = 0.3F;
		float idleDegree = 0.1F;
		float walkSpeed = 0.4F;
		float walkDegree = 0.3F;
		AdvancedModelBox[] tailParts = new AdvancedModelBox[]{this.tail1, this.tail2, this.tail3, this.tail4};
		this.bob(this.lowerbody, idleSpeed, idleDegree * 1.5F, false, f2, 1);
		this.bob(this.backLeftThigh, idleSpeed, -idleDegree * 1.5F, false, f2, 1);
		this.bob(this.backRightThigh, idleSpeed, -idleDegree * 1.5F, false, f2, 1);
		this.bob(this.frontLeftLeg, idleSpeed, -idleDegree * 1.5F, false, f2, 1);
		this.bob(this.frontRightLeg, idleSpeed, -idleDegree * 1.5F, false, f2, 1);
		this.walk(this.backLeftThigh, idleSpeed, -idleDegree * 0.25F, false, 3.0F, 0, f2, 1);
		this.walk(this.backLeftLeg, idleSpeed, idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.backLeftHeel, idleSpeed, idleDegree * 0.1F, false, 3.0F, 0, f2, 1);
		this.walk(this.backRightThigh, idleSpeed, -idleDegree * 0.25F, false, 3.0F, 0, f2, 1);
		this.walk(this.backRightLeg, idleSpeed, idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.backRightHeel, idleSpeed, idleDegree * 0.1F, false, 3.0F, 0, f2, 1);
		this.walk(this.frontLeftLeg, idleSpeed, idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.frontLeftHeel, idleSpeed, -idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.frontRightLeg, idleSpeed, idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.frontRightHeel, idleSpeed, -idleDegree * 0.15F, false, 3.0F, 0, f2, 1);
		this.walk(this.frontRightFoot, idleSpeed, idleDegree * 0.25F, false, 2.0F, 0, f2, 1);
		this.walk(this.frontLeftFoot, idleSpeed, idleDegree * 0.25F, false, 2.0F, 0, f2, 1);
		this.walk(this.frontBody, idleSpeed, -idleDegree * 0.25F, false, 2.0F, 0, f2, 1);
		this.walk(this.neck, idleSpeed, idleDegree * 0.15F, false, 2.0F, 0, f2, 1);
		this.chainSwing(tailParts, idleSpeed, idleDegree * 0.5F, 1, f2, 1);
		this.swing(this.backLeftThigh, idleSpeed, -idleDegree * 0.95F, true, 1.0F, -0.1F, f2, 1);
		this.swing(this.backRightThigh, idleSpeed, -idleDegree * 0.95F, false, 1.0F, 0.1F, f2, 1);
		this.swing(this.frontLeftLeg, idleSpeed, -idleDegree * 0.95F, true, 1.0F, -0.1F, f2, 1);
		this.swing(this.frontRightLeg, idleSpeed, -idleDegree * 0.95F, false, 1.0F, 0.1F, f2, 1);

		this.swing(this.backLeftFoot, idleSpeed, idleDegree * 0.95F, true, 1.0F, -0.1F, f2, 1);
		this.swing(this.backRightFoot, idleSpeed, idleDegree * 0.95F, false, 1.0F, 0.1F, f2, 1);
		this.swing(this.frontLeftFoot, idleSpeed, idleDegree * 0.95F, true, 1.0F, -0.1F, f2, 1);
		this.swing(this.frontRightFoot, idleSpeed, idleDegree * 0.95F, false, 1.0F, 0.1F, f2, 1);

		float ulatingScale = 0.9F + (float) Math.sin(f2 * 0.75F) * 0.1F;
		this.swing(this.wisker2, idleSpeed, idleDegree, false, 0, 0, f2, 1);
		this.swing(this.wisker1, idleSpeed, idleDegree, true, 0, 0, f2, 1);
		this.flap(this.wisker2, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.flap(this.wisker1, idleSpeed, idleDegree, false, 1, 0, f2, 1);
		this.walk(this.wisker2, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.walk(this.wisker1, idleSpeed, idleDegree, false, 2, 0, f2, 1);
		this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
		this.walk(this.mouth1, idleSpeed * 0.75F, idleDegree, true, 4, -0.1F, f2, 1);
		this.flap(this.tatters1, idleSpeed * 0.25F, idleDegree, true, 3, -0.1F, f2, 1);
		this.flap(this.tatters2, idleSpeed * 0.25F, idleDegree, true, 3, -0.1F, f2, 1);
		this.flap(this.tatters3, idleSpeed * 0.5F, idleDegree, true, 3, 0.1F, f2, 1);
		this.flap(this.tatters4, idleSpeed * 0.5F, idleDegree, true, 3, 0.1F, f2, 1);
		this.bob(this.lowerbody, walkSpeed, walkDegree * 5F, false, f, f1);
		this.walk(this.midBody, walkSpeed, walkDegree, false, 0, 0, f, f1);
		this.walk(this.frontBody, walkSpeed, walkDegree * 0.5F, false, 1, -0.1F, f, f1);
		this.walk(this.neck, walkSpeed, walkDegree * 0.5F, false, 2, 0, f, f1);
		this.walk(this.tail1, walkSpeed, walkDegree, false, -1, 0.3F, f, f1);
		this.walk(this.tail2, walkSpeed, walkDegree * 0.5F, false, -2, 0.1F, f, f1);
		this.walk(this.tail3, walkSpeed, walkDegree * 0.5F, false, -3, 0.0F, f, f1);
		this.walk(this.tail4, walkSpeed, walkDegree * 0.5F, false, -4, 0.1F, f, f1);
		this.walk(this.backLeftThigh, walkSpeed, walkDegree * 2F, true, 0, 0, f, f1);
		this.walk(this.backRightThigh, walkSpeed, walkDegree * 2F, true, 0, 0, f, f1);
		this.walk(this.backLeftLeg, walkSpeed, walkDegree * 2F, true, 0, 0, f, f1);
		this.walk(this.backRightLeg, walkSpeed, walkDegree * 2F, true, 0, 0, f, f1);
		this.walk(this.backLeftHeel, walkSpeed, walkDegree, true, 1, 0, f, f1);
		this.walk(this.backRightHeel, walkSpeed, walkDegree, true, 1, 0, f, f1);
		this.walk(this.backLeftFoot, walkSpeed, walkDegree * 6F, false, 0.7F, 0.4F, f, f1);
		this.walk(this.backRightFoot, walkSpeed, walkDegree * 6F, false, 0.7F, 0.4F, f, f1);
		this.walk(this.frontLeftLeg, walkSpeed, walkDegree * 2F, true, 2, 0.1F, f, f1);
		this.walk(this.frontRightLeg, walkSpeed, walkDegree * 2F, true, 2, 0.1F, f, f1);
		this.walk(this.frontLeftHeel, walkSpeed, walkDegree * 2F, true, 2, -0.2F, f, f1);
		this.walk(this.frontRightHeel, walkSpeed, walkDegree * 2F, true, 2, -0.2F, f, f1);
		this.walk(this.frontLeftFoot, walkSpeed, walkDegree * 4F, false, 2.6F, 0.01F, f, f1);
		this.walk(this.frontRightFoot, walkSpeed, walkDegree * 4F, false, 2.6F, 0.01F, f, f1);
		this.swing(this.frontLeftLeg, walkSpeed, -walkDegree, false, 1.5F, -0.25F, f, f1);
		this.swing(this.frontRightLeg, walkSpeed, -walkDegree, true, 1.5F, -0.25F, f, f1);
		this.swing(this.backLeftThigh, walkSpeed, -walkDegree * 1.25F, false, 0F, -0.25F, f, f1);
		this.swing(this.backRightThigh, walkSpeed, -walkDegree * 1.25F, true, 0F, -0.25F, f, f1);
		this.faceTarget(f3, f4, 2, this.neck, this.head1);
	}

	public void setRotateAngle(AdvancedModelBox box, float x, float y, float z) {
		box.rotateAngleX = x;
		box.rotateAngleY = y;
		box.rotateAngleZ = z;
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.lowerbody,
				this.midBody,
				this.backLeftThigh,
				this.backRightThigh,
				this.tail1,
				this.tatters3,
				this.frontBody,
				this.tatters1,
				this.tatters2,
				this.frontRightLeg,
				this.frontLeftLeg,
				this.neck,
				this.frontRightHeel,
				this.frontRightFoot,
				this.frontLeftHeel,
				this.frontLeftFoot,
				this.head1,
				this.tatters4,
				this.snoutUpper,
				this.mouth1,
				this.ear1,
				this.ear2,
				this.nose,
				this.teeth,
				this.wisker1,
				this.wisker2,
				this.backLeftLeg,
				this.backLeftHeel,
				this.backLeftFoot,
				this.backRightLeg,
				this.backRightHeel,
				this.backRightFoot,
				this.tail2,
				this.tail3,
				this.tail4);
	}
}
