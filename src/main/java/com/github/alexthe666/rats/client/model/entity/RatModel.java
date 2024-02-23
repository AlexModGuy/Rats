package com.github.alexthe666.rats.client.model.entity;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.data.tags.RatsItemTags;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.AdjustsRatTail;
import com.github.alexthe666.rats.server.entity.monster.Pirat;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class RatModel<T extends AbstractRat> extends StaticRatModel<T> {

	public void animate(IAnimatedEntity entity) {
		this.resetToDefaultPose();

		AbstractRat rat = (AbstractRat) entity;
		this.animator.update(entity);
		this.animator.setAnimation(AbstractRat.ANIMATION_IDLE_SCRATCH);
		this.animator.startKeyframe(5);
		this.scratchPosition(rat);
		this.rotateFrom(this.rightArm, -90.0F, 0.0F, 0.0F);
		this.rotateFrom(this.leftArm, -90.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.scratchPosition(rat);
		this.rotateFrom(this.rightArm, -25.0F, 30.0F, 0.0F);
		this.rotateFrom(this.leftArm, -25.0F, -30.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.scratchPosition(rat);
		this.rotateFrom(this.rightArm, -90.0F, 0.0F, 0.0F);
		this.rotateFrom(this.leftArm, -90.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.scratchPosition(rat);
		this.rotateFrom(this.rightArm, -25.0F, 30.0F, 0.0F);
		this.rotateFrom(this.leftArm, -25.0F, -30.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(AbstractRat.ANIMATION_IDLE_SNIFF);
		this.animator.startKeyframe(3);
		this.rotateFrom(this.neck, -30.0F, 0.0F, 0.0F);
		this.rotateFrom(this.nose, 15.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.rotateFrom(this.neck, -60.0F, 0.0F, 0.0F);
		this.rotateFrom(this.nose, -15.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.rotateFrom(this.neck, -30.0F, 0.0F, 0.0F);
		this.rotateFrom(this.nose, 15.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.rotateFrom(this.neck, -60.0F, 0.0F, 0.0F);
		this.rotateFrom(this.nose, -15.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(3);
		this.rotateFrom(this.neck, -30.0F, 0.0F, 0.0F);
		this.rotateFrom(this.nose, 15.0F, 0.0F, 0.0F);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
		this.animator.setAnimation(AbstractRat.ANIMATION_DANCE);
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, 25.0F, 0.0F);
		this.rotateFrom(this.leftArm, -100.0F, 0.0F, -65.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, -25.0F, 0.0F);
		this.rotateFrom(this.rightArm, -100.0F, 0.0F, 65.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, 25.0F, 0.0F);
		this.rotateFrom(this.leftArm, -100.0F, 0.0F, -65.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, -25.0F, 0.0F);
		this.rotateFrom(this.rightArm, -100.0F, 0.0F, 65.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, 25.0F, 0.0F);
		this.rotateFrom(this.leftArm, -100.0F, 0.0F, -65.0F);
		this.animator.endKeyframe();
		this.animator.startKeyframe(5);
		this.animator.move(this.body2, 0.0F, 0.0F, 0.5F);
		this.rotateFrom(this.body2, 10.0F, -25.0F, 0.0F);
		this.rotateFrom(this.rightArm, -100.0F, 0.0F, 65.0F);
		this.animator.endKeyframe();
		this.animator.resetKeyframe(5);
	}

	@Override
	public void setupAnim(T rat, float f, float f1, float f2, float f3, float f4) {
		this.animate(rat);
		float speedWalk = 1.0F;
		float degreeWalk = 0.3F;
		float speedRun = 1.0F;
		float degreeRun = 0.4F;
		float speedIdle = 0.75F;
		float degreeIdle = 0.15F;
		float speedDance = 0.75F;
		float degreeDance = 0.4F;
		boolean running = rat.isSprinting();
		boolean holdingInHands = !rat.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && (rat instanceof TamedRat realRat && realRat.cookingProgress > 0) || rat.getAnimation() == AbstractRat.ANIMATION_EAT || (rat instanceof TamedRat actualRat && (actualRat.holdsItemInHandUpgrade() || actualRat.getMBTransferRate() > 0));
		float maxTailRotation = (float) Math.toRadians(15.0D);
		float f12 = (float) Math.toRadians(-15.0D) + f1;
		if (rat.getVehicle() != null && rat.getVehicle() instanceof LivingEntity rider && !(rider instanceof AdjustsRatTail)) {
			maxTailRotation = (float) Math.toRadians(30.0D);
			f12 = (float) Math.toRadians(-15.0D) + rider.walkAnimation.speed();
			this.walk(this.tail1, speedIdle, degreeIdle, false, -1.0F, 0.0F, rider.walkAnimation.position(), rider.walkAnimation.speed());
			this.walk(this.tail2, speedIdle, degreeIdle * 0.5F, false, -2.0F, 0.0F, rider.walkAnimation.position(), rider.walkAnimation.speed());
		}

		if (f12 > maxTailRotation) {
			f12 = maxTailRotation;
		}

		if (f12 < (float) Math.toRadians(-15.0D)) {
			f12 = (float) Math.toRadians(-15.0D);
		}

		if (!rat.isDeadInTrap() && rat.getAnimation() != AbstractRat.ANIMATION_IDLE_SCRATCH && (!(rat instanceof TamedRat realRat) || !realRat.isInWheel())) {
			this.head.rotateAngleX = f4 * Mth.DEG_TO_RAD;
			this.head.rotateAngleY = f3 * Mth.DEG_TO_RAD;
		}

		this.wisker1.showModel = this.wisker2.showModel = !rat.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemTags.HIDES_RAT_WHISKERS);
		this.progressRotation(this.leftThigh, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, (float) Math.toRadians(-60.0D), 5.0F);
		this.progressRotation(this.rightThigh, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, (float) Math.toRadians(60.0D), 5.0F);
		this.progressRotation(this.leftFoot, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, 0.0F, 5.0F);
		this.progressRotation(this.rightFoot, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, 0.0F, 5.0F);
		this.progressRotation(this.leftArm, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, (float) Math.toRadians(-60.0D), 5.0F);
		this.progressRotation(this.rightArm, rat.deadInTrapProgress, (float) Math.toRadians(20.0D), 0.0F, (float) Math.toRadians(60.0D), 5.0F);
		this.progressRotation(this.head, rat.deadInTrapProgress, (float) Math.toRadians(-20.0D), 0.0F, 0.0F, 5.0F);
		this.progressRotation(this.tail1, rat.deadInTrapProgress, 0.0F, (float) Math.toRadians(10.0D), 0.0F, 5.0F);
		this.progressRotation(this.tail2, rat.deadInTrapProgress, 0.0F, (float) Math.toRadians(30.0D), 0.0F, 5.0F);
		this.progressRotation(this.rightFoot, rat.sitProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightEar, rat.sitProgress, -0.17453292F, 0.7853982F, -0.7853982F, 20.0F);
		this.progressRotation(this.leftFoot, rat.sitProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftThigh, rat.sitProgress, 1.134464F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.body1, rat.sitProgress, -1.134464F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightThigh, rat.sitProgress, 1.134464F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.neck, rat.sitProgress, 0.091106184F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.head, rat.sitProgress, 0.8285004F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftEar, rat.sitProgress, -0.17453292F, -0.7853982F, 0.7853982F, 20.0F);
		this.progressRotation(this.leftArm, rat.sitProgress, 1.3089969F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightArm, rat.sitProgress, 1.3089969F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightHand, rat.sitProgress, 0.9599311F, -0.17453292F, 0.08726646F, 20.0F);
		this.progressRotation(this.leftHand, rat.sitProgress, 0.9599311F, 0.17453292F, -0.08726646F, 20.0F);
		this.progressRotation(this.body2, rat.sitProgress, 0.34906584F, 0.0F, 0.0F, 20.0F);
		if (!rat.isPassenger()) {
			this.progressRotation(this.tail2, rat.sitProgress, 0.20943952F, 0.61086524F, 0.0F, 20.0F);
			this.progressRotation(this.tail1, rat.sitProgress, 1.2F, 0.17453292F, 0.6981317F, 20.0F);
		} else if (rat instanceof Pirat || (rat instanceof TamedRat realRat && realRat.isDancing())) {
			this.progressRotation(this.tail1, rat.sitProgress, 1.1F, 0.0F, 0.0F, 20.0F);
		}

		if (rat.getVehicle() instanceof AdjustsRatTail adjuster) {
			adjuster.adjustRatTailRotation(rat, this.tail1, this.tail2);
		}

		this.progressPosition(this.body1, rat.sitProgress, 0.0F, 16.0F, 0.0F, 20.0F);
		this.progressPosition(this.leftThigh, rat.sitProgress, 2.5F, 0.0F, 4.5F, 20.0F);
		this.progressPosition(this.rightThigh, rat.sitProgress, -2.5F, 0.0F, 4.5F, 20.0F);
		this.progressRotation(this.rightFoot, rat.sleepProgress, 1.5707964F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftThigh, rat.sleepProgress, -1.5707964F, -0.37018433F, 0.0F, 20.0F);
		this.progressRotation(this.leftArm, rat.sleepProgress, -1.5707964F, -0.3642502F, 0.0F, 20.0F);
		this.progressRotation(this.head, rat.sleepProgress, 0.18656418F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.body1, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.tail1, rat.sleepProgress, -0.119270824F, 1.3203416F, 0.0F, 20.0F);
		this.progressRotation(this.leftFoot, rat.sleepProgress, 1.5707964F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.wisker2, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightEar, rat.sleepProgress, 0.0F, 0.7853982F, 0.0F, 20.0F);
		this.progressRotation(this.tail2, rat.sleepProgress, 0.0F, 1.5635244F, 0.0F, 20.0F);
		this.progressRotation(this.rightEye, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.wisker1, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.nose, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.rightArm, rat.sleepProgress, -1.5707964F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.snout, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftEar, rat.sleepProgress, 0.0F, -0.62699854F, 0.0F, 20.0F);
		this.progressRotation(this.rightHand, rat.sleepProgress, 1.5707964F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.neck, rat.sleepProgress, 0.0F, -0.70540404F, 0.0F, 20.0F);
		this.progressRotation(this.body2, rat.sleepProgress, 0.0F, -0.42906016F, 0.0F, 20.0F);
		this.progressRotation(this.rightThigh, rat.sleepProgress, -1.5707964F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftEye, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20.0F);
		this.progressRotation(this.leftHand, rat.sleepProgress, 1.6470628F, 0.0F, 0.0F, 20.0F);
		if (rat instanceof TamedRat realRat && realRat.isInWheel()) {
			f = f2;
			f1 = 1.0F;
			this.progressRotationPrev(this.body1, 5.0F, (float) Math.toRadians(20.0D), 0.0F, 0.0F, 5.0F);
			this.progressRotationPrev(this.body2, 5.0F, (float) Math.toRadians(-50.0D), 0.0F, 0.0F, 5.0F);
			this.progressRotationPrev(this.head, 5.0F, (float) Math.toRadians(-30.0D), 0.0F, 0.0F, 5.0F);
			this.progressRotationPrev(this.tail1, 5.0F, (float) Math.toRadians(70.0D), 0.0F, 0.0F, 5.0F);
			this.progressRotationPrev(this.tail2, 5.0F, (float) Math.toRadians(60.0D), 0.0F, 0.0F, 5.0F);
			this.progressRotationPrev(this.rightEar, 5.0F, 0.0F, (float) Math.toRadians(-20.0D), 0.0F, 5.0F);
			this.progressRotationPrev(this.leftEar, 5.0F, 0.0F, (float) Math.toRadians(20.0D), 0.0F, 5.0F);
		}

		if (rat.getAnimation() == AbstractRat.ANIMATION_EAT) {
			this.walk(this.neck, speedIdle * 1.5F, degreeIdle * 1.5F, true, 2.0F, -0.4F, f2, 1.0F);
			this.walk(this.head, speedIdle * 1.5F, degreeIdle * 1.5F, true, 2.0F, -0.2F, f2, 1.0F);
			if (!(rat instanceof TamedRat realRat) || (!RatUpgradeUtils.hasUpgrade(realRat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) && !RatUpgradeUtils.hasUpgrade(realRat, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get()))) {
				this.walk(this.leftArm, speedIdle, degreeIdle * 0.5F, true, 1.0F, 0.0F, f2, 1.0F);
				this.walk(this.rightArm, speedIdle, degreeIdle * 0.5F, true, 1.0F, 0.0F, f2, 1.0F);
				this.walk(this.rightHand, speedIdle, degreeIdle * 0.5F, true, 0.0F, -0.1F, f2, 1.0F);
				this.walk(this.leftHand, speedIdle, degreeIdle * 0.5F, true, 0.0F, -0.1F, f2, 1.0F);
			}
		}

		if (rat.getVehicle() == null || !(rat.getVehicle() instanceof AdjustsRatTail)) {
			this.tail1.rotateAngleX += f12;
			this.tail2.rotateAngleX -= f12 / 2F;
		}

		float ulatingScale = 0.9F + (float) Math.sin(f2 * 0.75F) * 0.1F;
		if (!rat.isDeadInTrap()) {
			this.swing(this.wisker2, speedIdle, degreeIdle, false, 0.0F, 0.0F, f2, 1.0F);
			this.swing(this.wisker1, speedIdle, degreeIdle, true, 0.0F, 0.0F, f2, 1.0F);
			this.flap(this.wisker2, speedIdle, degreeIdle, false, 1.0F, 0.0F, f2, 1.0F);
			this.flap(this.wisker1, speedIdle, degreeIdle, false, 1.0F, 0.0F, f2, 1.0F);
			this.walk(this.wisker2, speedIdle, degreeIdle, false, 2.0F, 0.0F, f2, 1.0F);
			this.walk(this.wisker1, speedIdle, degreeIdle, false, 2.0F, 0.0F, f2, 1.0F);
			this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
		}

		if (rat instanceof TamedRat realRat && RatUpgradeUtils.hasUpgrade(realRat, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get()) && rat.isInWater()) {
			AdvancedModelBox[] body = new AdvancedModelBox[]{this.body1, this.tail1, this.tail2};
			this.chainSwing(body, speedRun * 0.3F, degreeRun, -2.0D, f, f1);
			this.bob(this.body1, speedRun * 0.6F, degreeRun * 6.0F, false, f, f1);
		}

		if (running) {
			this.bob(this.body1, speedRun, degreeRun * 5.0F, false, f, f1);
			this.walk(this.body1, speedRun, degreeRun, false, 0.0F, 0.0F, f, f1);
			this.walk(this.body2, speedRun, degreeRun * 0.5F, false, 1.0F, -0.1F, f, f1);
			this.walk(this.neck, speedRun, degreeRun * 0.5F, false, 2.0F, 0.0F, f, f1);
			this.walk(this.tail1, speedRun, degreeRun, false, -1.0F, 0.0F, f, f1);
			this.walk(this.tail2, speedRun, degreeRun * 0.5F, false, -2.0F, 0.0F, f, f1);
			this.walk(this.leftThigh, speedRun, degreeRun * 2.0F, true, 0.0F, 0.0F, f, f1);
			this.walk(this.rightThigh, speedRun, degreeRun * 2.0F, true, 0.0F, 0.0F, f, f1);
			this.walk(this.rightFoot, speedRun, degreeRun * 2.0F, true, 3.0F, -0.1F, f, f1);
			this.walk(this.leftFoot, speedRun, degreeRun * 2.0F, true, 3.0F, -0.1F, f, f1);
			if (!holdingInHands) {
				this.walk(this.leftArm, speedRun, degreeRun * 2.0F, true, 2.0F, 0.0F, f, f1);
				this.walk(this.rightArm, speedRun, degreeRun * 2.0F, true, 2.0F, 0.0F, f, f1);
				this.walk(this.rightHand, speedRun, degreeRun * 2.0F, true, 5.0F, -0.1F, f, f1);
				this.walk(this.leftHand, speedRun, degreeRun * 2.0F, true, 5.0F, -0.1F, f, f1);
			}
		} else {
			this.walk(this.body1, speedWalk, degreeWalk * 0.25F, false, 0.0F, 0.0F, f, f1);
			this.walk(this.body2, speedWalk, degreeWalk * 0.25F, false, 1.0F, 0.1F, f, f1);
			this.walk(this.rightThigh, speedWalk, degreeWalk * 4.0F, false, 1.0F, 0.0F, f, f1);
			this.walk(this.rightFoot, speedWalk, degreeWalk * 2.0F, false, 3.5F, -0.1F, f, f1);
			this.walk(this.leftThigh, speedWalk, degreeWalk * 4.0F, true, 1.0F, 0.0F, f, f1);
			this.walk(this.leftFoot, speedWalk, degreeWalk * 2.0F, true, 3.5F, 0.1F, f, f1);
			if (!holdingInHands) {
				this.walk(this.rightArm, speedWalk, degreeWalk * 4.0F, true, 1.0F, 0.0F, f, f1);
				this.walk(this.rightHand, speedWalk, degreeWalk * 2.0F, true, 3.0F, 0.1F, f, f1);
				this.walk(this.leftArm, speedWalk, degreeWalk * 4.0F, false, 1.0F, 0.0F, f, f1);
				this.walk(this.leftHand, speedWalk, degreeWalk * 2.0F, false, 3.0F, -0.1F, f, f1);
			}

			this.walk(this.tail1, speedWalk, degreeWalk, false, -1.0F, -0.15F, f, f1);
			this.walk(this.tail2, speedWalk, degreeWalk * 0.5F, true, 0.0F, -0.15F, f, f1);
			this.walk(this.neck, speedWalk, degreeWalk * 0.25F, false, 2.0F, 0.0F, f, f1);
		}

		if (rat instanceof TamedRat realRat && (RatUpgradeUtils.hasUpgrade(realRat, RatsItemRegistry.RAT_UPGRADE_PLATTER.get()) || RatUpgradeUtils.hasUpgrade(realRat, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get()) || realRat.getMBTransferRate() > 0)) {
			this.leftArm.rotateAngleX = 1.3089969F;
			this.rightArm.rotateAngleX = 1.3089969F;
			this.rightHand.rotateAngleX = 0.9599311F;
			this.rightHand.rotateAngleY = -0.17453292F;
			this.rightHand.rotateAngleZ = 0.08726646F;
			this.leftHand.rotateAngleX = 0.9599311F;
			this.leftHand.rotateAngleY = 0.17453292F;
			this.leftHand.rotateAngleZ = -0.08726646F;
		}

		if (rat instanceof TamedRat realRat && realRat.holdsItemInHandUpgrade() && realRat.crafting) {
			this.walk(this.leftArm, speedRun, degreeRun, true, 2.0F, 0.0F, f2, 1.0F);
			this.walk(this.rightArm, speedRun, degreeRun, false, 2.0F, 0.0F, f2, 1.0F);
			this.walk(this.rightHand, speedRun, degreeRun, true, 5.0F, -0.1F, f2, 1.0F);
			this.walk(this.leftHand, speedRun, degreeRun, false, 5.0F, 0.1F, f2, 1.0F);
		}

		if (rat instanceof TamedRat realRat && realRat.isDancing()) {
			if (realRat.getDanceMoves() == 1) {
				this.progressRotation(this.rightArm, 20.0F, (float) Math.toRadians(-110.0D), 0.0F, (float) Math.toRadians(78.0D), 20.0F);
				this.walk(this.body2, speedDance, degreeDance, false, 1.0F, -0.1F, f2, 1.0F);
				this.swing(this.rightArm, speedDance, degreeDance, false, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.neck, speedDance, degreeDance, false, 2.0F, -0.1F, f2, 1.0F);
			}

			if (realRat.getDanceMoves() == 2) {
				this.walk(this.body2, speedDance, degreeDance * 0.25F, false, 1.0F, -0.1F, f2, 1.0F);
				this.body1.rotateAngleY -= f2 * 0.25F;
				this.walk(this.rightArm, speedDance, degreeDance, false, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.leftArm, speedDance, degreeDance, true, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.rightThigh, speedDance, degreeDance, false, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.leftThigh, speedDance, degreeDance, true, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.neck, speedDance, degreeDance * 0.25F, false, 1.0F, -0.1F, f2, 1.0F);
			}

			if (realRat.getDanceMoves() == 3) {
				this.progressRotation(this.rightArm, 20.0F, (float) Math.toRadians(30.0D), (float) Math.toRadians(-98.0D), (float) Math.toRadians(-130.0D), 20.0F);
				this.progressRotation(this.leftArm, 20.0F, (float) Math.toRadians(30.0D), (float) Math.toRadians(80.0D), (float) Math.toRadians(130.0D), 20.0F);
				this.progressRotation(this.rightThigh, 20.0F, 0.0F, (float) Math.toRadians(15.0D), (float) Math.toRadians(15.0D), 20.0F);
				this.progressRotation(this.leftThigh, 20.0F, 0.0F, (float) Math.toRadians(15.0D), (float) Math.toRadians(-15.0D), 20.0F);
				this.swing(this.rightArm, speedDance, degreeDance, false, 2.0F, -0.1F, f2, 1.0F);
				this.swing(this.leftArm, speedDance, degreeDance, true, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.neck, speedDance, degreeDance * 0.25F, false, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.body1, speedDance, degreeDance * 0.5F, false, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.leftThigh, speedDance, degreeDance * 0.75F, true, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.rightThigh, speedDance, degreeDance * 0.75F, true, 2.0F, -0.1F, f2, 1.0F);
				this.walk(this.tail1, speedDance, degreeDance, true, 2.0F, -0.1F, f2, 1.0F);
				this.bob(this.body1, speedDance, degreeDance * 6.0F, false, f2, 1.0F);
			}
		}
	}
}
