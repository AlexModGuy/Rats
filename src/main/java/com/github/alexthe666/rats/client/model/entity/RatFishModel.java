package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.rats.server.entity.ratlantis.Ratfish;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class RatFishModel<T extends Ratfish> extends AdvancedEntityModel<T> {
	public final AdvancedModelBox head;
	public final AdvancedModelBox snout;
	public final AdvancedModelBox leftEar;
	public final AdvancedModelBox rightEar;
	public final AdvancedModelBox leftEye;
	public final AdvancedModelBox rightEye;
	public final AdvancedModelBox nose;
	public final AdvancedModelBox wisker1;
	public final AdvancedModelBox wisker2;
	private final AdvancedModelBox body;
	private final AdvancedModelBox tail;
	private final AdvancedModelBox finRight;
	private final AdvancedModelBox finLeft;
	private final AdvancedModelBox finTop;

	public RatFishModel(float scale) {
		this.texWidth = 64;
		this.texHeight = 32;
		this.nose = new AdvancedModelBox(this, 32, 26);
		this.nose.setRotationPoint(0.0F, -0.6F, -2.1F);
		this.nose.addBox(-0.5F, -0.3F, -1.0F, 1, 1, 2, 0.0F);
		this.rightEye = new AdvancedModelBox(this, 37, 20);
		this.rightEye.setRotationPoint(-2.0F, -0.5F, -1.6F);
		this.rightEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, scale);
		this.wisker2 = new AdvancedModelBox(this, 35, 3);
		this.wisker2.setRotationPoint(-1.0F, 0.0F, -1.0F);
		this.wisker2.addBox(-2.5F, -1.5F, 0.0F, 2, 3, 0, scale);
		this.wisker1 = new AdvancedModelBox(this, 40, 3);
		this.wisker1.setRotationPoint(1.0F, 0.0F, -1.0F);
		this.wisker1.addBox(0.5F, -1.5F, 0.0F, 2, 3, 0, scale);
		this.leftEye = new AdvancedModelBox(this, 37, 20);
		this.leftEye.setRotationPoint(2.0F, -0.5F, -1.6F);
		this.leftEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, scale);
		this.head = new AdvancedModelBox(this, 22, 20);
		this.head.setRotationPoint(0.0F, 21.5F, -1.0F);
		this.head.addBox(-2.0F, -1.5F, -2.5F, 4, 3, 3, scale);
		this.setRotateAngle(this.head, 0.18656417346763232F, 0.0F, 0.0F);
		this.snout = new AdvancedModelBox(this, 22, 26);
		this.snout.setRotationPoint(0.0F, 0.5F, -2.5F);
		this.snout.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, scale);
		this.leftEar = new AdvancedModelBox(this, 33, 20);
		this.leftEar.mirror = true;
		this.leftEar.setRotationPoint(1.5F, -0.5F, 0.0F);
		this.leftEar.addBox(0.0F, -2.0F, 0.0F, 2, 2, 0, scale);
		this.setRotateAngle(this.leftEar, 0.0F, -0.7853981633974483F, 0.0F);
		this.rightEar = new AdvancedModelBox(this, 33, 20);
		this.rightEar.setRotationPoint(-1.5F, -0.5F, 0.0F);
		this.rightEar.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 0, scale);
		this.setRotateAngle(this.rightEar, 0.0F, 0.7853981633974483F, 0.0F);

		this.body = new AdvancedModelBox(this, 0, 0);
		this.body.addBox(-1.0F, -1.5F, -3.0F, 2, 3, 6, scale);
		this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
		this.tail = new AdvancedModelBox(this, 22, -6);
		this.tail.addBox(0.0F, -1.5F, 0.0F, 0, 6, 6, scale);
		this.tail.setRotationPoint(0.0F, 22.0F, 3.0F);
		this.finRight = new AdvancedModelBox(this, 2, 16);
		this.finRight.addBox(-2.0F, -1.0F, 0.0F, 2, 2, 0, scale);
		this.finRight.setRotationPoint(-1.0F, 22.5F, 0.0F);
		this.finRight.rotateAngleY = ((float) Math.PI / 4F);
		this.finLeft = new AdvancedModelBox(this, 2, 12);
		this.finLeft.addBox(0.0F, -1.0F, 0.0F, 2, 2, 0, scale);
		this.finLeft.setRotationPoint(1.0F, 22.5F, 0.0F);
		this.finLeft.rotateAngleY = (-(float) Math.PI / 4F);
		this.finTop = new AdvancedModelBox(this, 10, -5);
		this.finTop.addBox(0.0F, -3.0F, 0.0F, 0, 3, 6, scale);
		this.finTop.setRotationPoint(0.0F, 20.5F, -3.0F);
		this.snout.addChild(this.nose);
		this.head.addChild(this.rightEye);
		this.snout.addChild(this.wisker1);
		this.snout.addChild(this.wisker2);
		this.head.addChild(this.leftEye);
		this.head.addChild(this.leftEar);
		this.head.addChild(this.rightEar);
		this.head.addChild(this.snout);
		this.updateDefaultPose();
	}


	@Override
	public Iterable<BasicModelPart> parts() {
		return ImmutableList.of(this.head, this.body, this.tail, this.finLeft, this.finRight, this.finTop);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.resetToDefaultPose();
		float speedIdle = 0.75F;
		float degreeIdle = 0.15F;
		float f = 1.0F;
		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.tail.rotateAngleY = -f * 0.45F * Mth.sin(0.6F * ageInTicks);
		float ulatingScale = 0.9F + (float) Math.sin(ageInTicks * 0.75F) * 0.1F;
		this.swing(this.wisker2, speedIdle, degreeIdle, false, 0, 0, ageInTicks, 1);
		this.swing(this.wisker1, speedIdle, degreeIdle, true, 0, 0, ageInTicks, 1);
		this.flap(this.wisker2, speedIdle, degreeIdle, false, 1, 0, ageInTicks, 1);
		this.flap(this.wisker1, speedIdle, degreeIdle, false, 1, 0, ageInTicks, 1);
		this.walk(this.wisker2, speedIdle, degreeIdle, false, 2, 0, ageInTicks, 1);
		this.walk(this.wisker1, speedIdle, degreeIdle, false, 2, 0, ageInTicks, 1);
		this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
	}

	@Override
	public Iterable<AdvancedModelBox> getAllParts() {
		return ImmutableList.of(this.head,
				this.snout,
				this.leftEar,
				this.rightEar,
				this.leftEye,
				this.rightEye,
				this.nose,
				this.wisker1,
				this.wisker2,
				this.body,
				this.tail,
				this.finRight,
				this.finLeft,
				this.finTop);
	}
}
