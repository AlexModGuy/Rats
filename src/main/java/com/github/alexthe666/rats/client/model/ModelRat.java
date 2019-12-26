package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPirat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

public class ModelRat<T extends Entity> extends AdvancedEntityModel<T>{
    public AdvancedRendererModel body1;
    public AdvancedRendererModel body2;
    public AdvancedRendererModel tail1;
    public AdvancedRendererModel leftThigh;
    public AdvancedRendererModel rightThigh;
    public AdvancedRendererModel neck;
    public AdvancedRendererModel leftArm;
    public AdvancedRendererModel rightArm;
    public AdvancedRendererModel head;
    public AdvancedRendererModel snout;
    public AdvancedRendererModel leftEar;
    public AdvancedRendererModel rightEar;
    public AdvancedRendererModel leftEye;
    public AdvancedRendererModel rightEye;
    public AdvancedRendererModel nose;
    public AdvancedRendererModel wisker1;
    public AdvancedRendererModel wisker2;
    public AdvancedRendererModel leftHand;
    public AdvancedRendererModel rightHand;
    public AdvancedRendererModel tail2;
    public AdvancedRendererModel leftFoot;
    public AdvancedRendererModel rightFoot;
    private ModelAnimator animator;

    public ModelRat(float scale) {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.neck = new AdvancedRendererModel(this, 32, 17);
        this.neck.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.neck.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 1, scale);
        this.nose = new AdvancedRendererModel(this, 32, 26);
        this.nose.setRotationPoint(0.0F, -0.6F, -2.1F);
        this.nose.addBox(-0.5F, -0.3F, -1.0F, 1, 1, 2, 0.0F);
        this.rightEye = new AdvancedRendererModel(this, 37, 20);
        this.rightEye.setRotationPoint(-2.0F, -0.5F, -1.6F);
        this.rightEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, scale);
        this.leftFoot = new AdvancedRendererModel(this, 22, 15);
        this.leftFoot.mirror = true;
        this.leftFoot.setRotationPoint(0.0F, 3.0F, 0.5F);
        this.leftFoot.addBox(-0.5F, 0.0F, -3.5F, 1, 1, 4, scale);
        this.wisker2 = new AdvancedRendererModel(this, 35, 0);
        this.wisker2.setRotationPoint(-1.0F, 0.0F, -1.0F);
        this.wisker2.addBox(-2.5F, -1.5F, 0.0F, 2, 3, 0, scale);
        this.wisker1 = new AdvancedRendererModel(this, 40, 0);
        this.wisker1.setRotationPoint(1.0F, 0.0F, -1.0F);
        this.wisker1.addBox(0.5F, -1.5F, 0.0F, 2, 3, 0, scale);
        this.tail2 = new AdvancedRendererModel(this, 0, 11);
        this.tail2.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.tail2.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8, scale);
        this.leftEye = new AdvancedRendererModel(this, 37, 20);
        this.leftEye.setRotationPoint(2.0F, -0.5F, -1.6F);
        this.leftEye.addBox(-0.5F, -0.7F, -0.5F, 1, 1, 1, scale);
        this.leftArm = new AdvancedRendererModel(this, 32, 9);
        this.leftArm.mirror = true;
        this.leftArm.setRotationPoint(2.0F, 1.5F, -3.0F);
        this.leftArm.addBox(0.0F, 0.0F, -1.0F, 1, 2, 2, scale);
        this.body2 = new AdvancedRendererModel(this, 22, 0);
        this.body2.setRotationPoint(0.0F, -0.4F, 0.5F);
        this.body2.addBox(-2.0F, -2.0F, -5.0F, 4, 4, 5, scale);
        this.rightThigh = new AdvancedRendererModel(this, 22, 9);
        this.rightThigh.setRotationPoint(-2.5F, 0.0F, 5.0F);
        this.rightThigh.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, scale);
        this.head = new AdvancedRendererModel(this, 22, 20);
        this.head.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.head.addBox(-2.0F, -1.5F, -2.5F, 4, 3, 3, scale);
        this.setRotateAngle(head, 0.18656417346763232F, 0.0F, 0.0F);
        this.body1 = new AdvancedRendererModel(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body1.addBox(-2.5F, -3.0F, 0.0F, 5, 5, 6, scale);
        this.rightFoot = new AdvancedRendererModel(this, 22, 15);
        this.rightFoot.setRotationPoint(0.0F, 3.0F, 0.5F);
        this.rightFoot.addBox(-0.5F, 0.0F, -3.5F, 1, 1, 4, scale);
        this.tail1 = new AdvancedRendererModel(this, 0, 11);
        this.tail1.setRotationPoint(0.0F, 0.0F, 6.0F);
        this.tail1.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 8, scale);
        this.rightHand = new AdvancedRendererModel(this, 32, 13);
        this.rightHand.setRotationPoint(-0.4F, 2.0F, -0.3F);
        this.rightHand.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 2, scale);
        this.leftEar = new AdvancedRendererModel(this, 33, 20);
        this.leftEar.mirror = true;
        this.leftEar.setRotationPoint(1.5F, -0.5F, 0.0F);
        this.leftEar.addBox(0.0F, -2.0F, 0.0F, 2, 2, 0, scale);
        this.setRotateAngle(leftEar, 0.0F, -0.7853981633974483F, 0.0F);
        this.rightEar = new AdvancedRendererModel(this, 33, 20);
        this.rightEar.setRotationPoint(-1.5F, -0.5F, 0.0F);
        this.rightEar.addBox(-2.0F, -2.0F, 0.0F, 2, 2, 0, scale);
        this.setRotateAngle(rightEar, 0.0F, 0.7853981633974483F, 0.0F);
        this.rightArm = new AdvancedRendererModel(this, 32, 9);
        this.rightArm.setRotationPoint(-2.0F, 1.5F, -3.0F);
        this.rightArm.addBox(-1.0F, 0.0F, -1.0F, 1, 2, 2, scale);
        this.leftThigh = new AdvancedRendererModel(this, 22, 9);
        this.leftThigh.mirror = true;
        this.leftThigh.setRotationPoint(2.5F, 0.0F, 5.0F);
        this.leftThigh.addBox(-1.0F, 0.0F, -1.5F, 2, 3, 3, scale);
        this.snout = new AdvancedRendererModel(this, 22, 26);
        this.snout.setRotationPoint(0.0F, 0.5F, -2.5F);
        this.snout.addBox(-1.5F, -1.0F, -2.0F, 3, 2, 2, scale);
        this.leftHand = new AdvancedRendererModel(this, 32, 13);
        this.leftHand.mirror = true;
        this.leftHand.setRotationPoint(0.4F, 2.0F, -0.3F);
        this.leftHand.addBox(-0.5F, 0.0F, -1.5F, 1, 1, 2, scale);
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
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body1.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        EntityRat rat = (EntityRat) entity;
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityRat) entity);
        animator.update(entity);//ANIMATION_IDLE_SCRATCH
        animator.setAnimation(EntityRat.ANIMATION_IDLE_SCRATCH);
        animator.startKeyframe(5);
        scratchPosition(rat);
        rotateFrom(rightArm, -90, 0, 0);
        rotateFrom(leftArm, -90, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        scratchPosition(rat);
        rotateFrom(rightArm, -25, 30, 0);
        rotateFrom(leftArm, -25, -30, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        scratchPosition(rat);
        rotateFrom(rightArm, -90, 0, 0);
        rotateFrom(leftArm, -90, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        scratchPosition(rat);
        rotateFrom(rightArm, -25, 30, 0);
        rotateFrom(leftArm, -25, -30, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityRat.ANIMATION_IDLE_SNIFF);
        animator.startKeyframe(3);
        rotateFrom(neck, -30, 0, 0);
        rotateFrom(nose, 15, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        rotateFrom(neck, -60, 0, 0);
        rotateFrom(nose, -15, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        rotateFrom(neck, -30, 0, 0);
        rotateFrom(nose, 15, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        rotateFrom(neck, -60, 0, 0);
        rotateFrom(nose, -15, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(3);
        rotateFrom(neck, -30, 0, 0);
        rotateFrom(nose, 15, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityRat.ANIMATION_DANCE_0);
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, 25, 0);
        rotateFrom(leftArm, -100, 0, -65);
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, -25, 0);
        rotateFrom(rightArm, -100, 0, 65);
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, 25, 0);
        rotateFrom(leftArm, -100, 0, -65);
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, -25, 0);
        rotateFrom(rightArm, -100, 0, 65);
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, 25, 0);
        rotateFrom(leftArm, -100, 0, -65);
        animator.endKeyframe();
        animator.startKeyframe(5);
        animator.move(body2, 0, 0, 0.5F);
        rotateFrom(body2, 10, -25, 0);
        rotateFrom(rightArm, -100, 0, 65);
        animator.endKeyframe();
        animator.resetKeyframe(5);

    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityRat rat) {
        float speedWalk = 1F;
        float degreeWalk = 0.3F;
        float speedRun = 1F;
        float degreeRun = 0.4F;
        float speedIdle = 0.75F;
        float degreeIdle = 0.15F;
        float speedDance = 0.75F;
        float degreeDance = 0.4F;
        boolean running = rat.isSprinting() || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT);
        boolean holdingInHands = !rat.getHeldItem(Hand.MAIN_HAND).isEmpty() && (!rat.holdInMouth || rat.cookingProgress > 0) || rat.getAnimation() == EntityRat.ANIMATION_EAT ||
                rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS) || rat.getMBTransferRate() > 0;
        float maxTailRotation = (float) Math.toRadians(15);

        float f12 = (float) Math.toRadians(-15) + f1;
        if (rat.getRidingEntity() != null && rat.getRidingEntity() instanceof LivingEntity) {
            maxTailRotation = (float) Math.toRadians(30);
            LivingEntity rider = (LivingEntity) rat.getRidingEntity();
            f12 = (float) Math.toRadians(-15) + rider.limbSwingAmount;
            this.walk(this.tail1, speedIdle, degreeIdle, false, -1, 0, rider.limbSwing, rider.limbSwingAmount);
            this.walk(this.tail2, speedIdle, degreeIdle * 0.5F, false, -2, 0, rider.limbSwing, rider.limbSwingAmount);

        }
        if (f12 > maxTailRotation) {
            f12 = maxTailRotation;
        }
        if (f12 < (float) Math.toRadians(-15)) {
            f12 = (float) Math.toRadians(-15);
        }

        progressRotation(body1, rat.holdProgress, (float) Math.toRadians(-70F), 0, 0, 5.0F);
        progressRotation(body2, rat.holdProgress, (float) Math.toRadians(10F), 0, 0, 5.0F);
        progressRotation(head, rat.holdProgress, (float) Math.toRadians(70F), 0, 0, 5.0F);
        progressRotation(tail1, rat.holdProgress, (float) Math.toRadians(75F), 0, 0, 5.0F);
        progressRotation(tail2, rat.holdProgress, (float) Math.toRadians(5F), 0, 0, 5.0F);
        progressRotation(leftThigh, rat.holdProgress, (float) Math.toRadians(70F), 0, 0, 5.0F);
        progressRotation(rightThigh, rat.holdProgress, (float) Math.toRadians(70F), 0, 0, 5.0F);
        progressRotation(leftArm, rat.holdProgress, (float) Math.toRadians(60F), 0, 0, 5.0F);
        progressRotation(rightArm, rat.holdProgress, (float) Math.toRadians(60F), 0, 0, 5.0F);
        if (!rat.holdsItemInHandUpgrade()) {
            progressRotation(leftHand, rat.holdProgress, (float) Math.toRadians(10F), (float) Math.toRadians(40F), 0, 5.0F);
            progressRotation(rightHand, rat.holdProgress, (float) Math.toRadians(10F), (float) Math.toRadians(-40F), 0, 5.0F);
        }
        progressPosition(body1, rat.holdProgress, 0, 16F, 0, 5.0F);
        progressPosition(leftThigh, rat.holdProgress, 2.5F, 0, 4.3F, 5.0F);
        progressPosition(rightThigh, rat.holdProgress, -2.5F, 0, 4.3F, 5.0F);
        progressRotation(leftThigh, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, (float) Math.toRadians(-60F), 5.0F);
        progressRotation(rightThigh, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, (float) Math.toRadians(60F), 5.0F);
        progressRotation(leftFoot, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, 0, 5.0F);
        progressRotation(rightFoot, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, 0, 5.0F);
        progressRotation(leftArm, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, (float) Math.toRadians(-60F), 5.0F);
        progressRotation(rightArm, rat.deadInTrapProgress, (float) Math.toRadians(20F), 0, (float) Math.toRadians(60F), 5.0F);
        progressRotation(head, rat.deadInTrapProgress, (float) Math.toRadians(-20F), 0, 0, 5.0F);
        progressRotation(tail1, rat.deadInTrapProgress, 0, (float) Math.toRadians(10F), 0, 5.0F);
        progressRotation(tail2, rat.deadInTrapProgress, 0, (float) Math.toRadians(30F), 0, 5.0F);

        progressRotation(rightFoot, rat.sitProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(rightEar, rat.sitProgress, -0.17453292519943295F, 0.7853981633974483F, -0.7853981633974483F, 20F);
        progressRotation(leftFoot, rat.sitProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(leftThigh, rat.sitProgress, 1.1344640137963142F, 0.0F, 0.0F, 20F);
        progressRotation(body1, rat.sitProgress, -1.1344640137963142F, 0.0F, 0.0F, 20F);
        progressRotation(head, rat.sitProgress, 0.7285004297824331F, 0.0F, 0.0F, 20F);
        progressRotation(rightThigh, rat.sitProgress, 1.1344640137963142F, 0.0F, 0.0F, 20F);
        progressRotation(neck, rat.sitProgress, 0.091106186954104F, 0.0F, 0.0F, 20F);
        progressRotation(leftEar, rat.sitProgress, -0.17453292519943295F, -0.7853981633974483F, 0.7853981633974483F, 20F);
        progressRotation(leftArm, rat.sitProgress, 1.3089969389957472F, 0.0F, 0.0F, 20F);
        progressRotation(rightArm, rat.sitProgress, 1.3089969389957472F, 0.0F, 0.0F, 20F);
        progressRotation(rightHand, rat.sitProgress, 0.9599310885968813F, -0.17453292519943295F, 0.08726646259971647F, 20F);
        progressRotation(leftHand, rat.sitProgress, 0.9599310885968813F, 0.17453292519943295F, -0.08726646259971647F, 20F);
        progressRotation(body2, rat.sitProgress, 0.3490658503988659F, 0.0F, 0.0F, 20F);
        if (!rat.isPassenger()) {
            progressRotation(tail2, rat.sitProgress, 0.20943951023931953F, 0.6108652381980153F, 0.0F, 20F);
            progressRotation(tail1, rat.sitProgress, 1.2F, 0.17453292519943295F, 0.6981317007977318F, 20F);
        } else if (rat instanceof EntityPirat || rat.isDancing()) {
            progressRotation(tail1, rat.sitProgress, 1.1F, 0, 0, 20F);
        }
        progressPosition(body1, rat.sitProgress, 0, 16F, 0, 20F);
        progressPosition(leftThigh, rat.sitProgress, 2.5F, 0, 4.5F, 20F);
        progressPosition(rightThigh, rat.sitProgress, -2.5F, 0, 4.5F, 20F);

        progressRotation(rightFoot, rat.sleepProgress, 1.5707963267948966F, 0.0F, 0.0F, 20F);
        progressRotation(leftThigh, rat.sleepProgress, -1.5707963267948966F, -0.3701843343479973F, 0.0F, 20F);
        progressRotation(leftArm, rat.sleepProgress, -1.5707963267948966F, -0.36425021489121656F, 0.0F, 20F);
        progressRotation(head, rat.sleepProgress, 0.18656417346763232F, 0.0F, 0.0F, 20F);
        progressRotation(body1, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(tail1, rat.sleepProgress, -0.11927082124560143F, 1.3203415791337103F, 0.0F, 20F);
        progressRotation(leftFoot, rat.sleepProgress, 1.5707963267948966F, 0.0F, 0.0F, 20F);
        progressRotation(wisker2, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(rightEar, rat.sleepProgress, 0.0F, 0.7853981633974483F, 0.0F, 20F);
        progressRotation(tail2, rat.sleepProgress, 0.0F, 1.5635243700937593F, 0.0F, 20F);
        progressRotation(rightEye, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(wisker1, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(nose, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(rightArm, rat.sleepProgress, -1.5707963267948966F, 0.0F, 0.0F, 20F);
        progressRotation(snout, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(leftEar, rat.sleepProgress, 0.0F, -0.6269985541154942F, 0.0F, 20F);
        progressRotation(rightHand, rat.sleepProgress, 1.5707963267948966F, 0.0F, 0.0F, 20F);
        progressRotation(neck, rat.sleepProgress, 0.0F, -0.7054040181353239F, 0.0F, 20F);
        progressRotation(body2, rat.sleepProgress, 0.0F, -0.4290601759792177F, 0.0F, 20F);
        progressRotation(rightThigh, rat.sleepProgress, -1.5707963267948966F, 0.0F, 0.0F, 20F);
        progressRotation(leftEye, rat.sleepProgress, 0.0F, 0.0F, 0.0F, 20F);
        progressRotation(leftHand, rat.sleepProgress, 1.6470628053380598F, 0.0F, 0.0F, 20F);


        if (rat.getAnimation() == EntityRat.ANIMATION_EAT) {
            this.walk(this.neck, speedIdle * 1.5F, degreeIdle * 1.5F, true, 2, -0.4F, f2, 1);
            this.walk(this.head, speedIdle * 1.5F, degreeIdle * 1.5F, true, 2, -0.2F, f2, 1);
            if (!rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) && !rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS)) {
                this.walk(this.leftArm, speedIdle, degreeIdle * 0.5F, true, 1, 0, f2, 1);
                this.walk(this.rightArm, speedIdle, degreeIdle * 0.5F, true, 1, 0, f2, 1);
                this.walk(this.rightHand, speedIdle, degreeIdle * 0.5F, true, 0, -0.1F, f2, 1);
                this.walk(this.leftHand, speedIdle, degreeIdle * 0.5F, true, 0, -0.1F, f2, 1);
            }
        }
        this.tail1.rotateAngleX += f12;
        this.tail2.rotateAngleX -= f12 / 2F;
        float ulatingScale = 0.9F + (float) Math.sin(f2 * 0.75F) * 0.1F;
        if (!rat.isDeadInTrap) {
            if (RatsMod.PROXY.shouldRenderNameplates() && rat.getAnimation() != EntityRat.ANIMATION_IDLE_SCRATCH) {
                this.faceTarget(f3, f4, 2, neck, head);
            }
            this.swing(this.wisker2, speedIdle, degreeIdle, false, 0, 0, f2, 1);
            this.swing(this.wisker1, speedIdle, degreeIdle, true, 0, 0, f2, 1);
            this.flap(this.wisker2, speedIdle, degreeIdle, false, 1, 0, f2, 1);
            this.flap(this.wisker1, speedIdle, degreeIdle, false, 1, 0, f2, 1);
            this.walk(this.wisker2, speedIdle, degreeIdle, false, 2, 0, f2, 1);
            this.walk(this.wisker1, speedIdle, degreeIdle, false, 2, 0, f2, 1);
            this.nose.setScale(ulatingScale, ulatingScale, ulatingScale);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC) && rat.isInWater()) {
            AdvancedRendererModel[] body = new AdvancedRendererModel[]{this.body1, this.tail1, this.tail2};
            this.chainSwing(body, speedRun * 0.3F, degreeRun, -2, f, f1);
            this.bob(this.body1, speedRun * 0.6F, degreeRun * 6, false, f, f1);
        }
        if (running) {
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
            if (!holdingInHands) {
                this.walk(this.leftArm, speedRun, degreeRun * 2F, true, 2, 0, f, f1);
                this.walk(this.rightArm, speedRun, degreeRun * 2F, true, 2, 0, f, f1);
                this.walk(this.rightHand, speedRun, degreeRun * 2F, true, 5, -0.1F, f, f1);
                this.walk(this.leftHand, speedRun, degreeRun * 2F, true, 5, -0.1F, f, f1);
            }

        } else {
            this.walk(this.body1, speedWalk, degreeWalk * 0.25F, false, 0, 0, f, f1);
            this.walk(this.body2, speedWalk, degreeWalk * 0.25F, false, 1, 0.1F, f, f1);
            this.walk(this.rightThigh, speedWalk, degreeWalk * 4F, false, 1, 0, f, f1);
            this.walk(this.rightFoot, speedWalk, degreeWalk * 2F, false, 3.5F, -0.1F, f, f1);
            this.walk(this.leftThigh, speedWalk, degreeWalk * 4F, true, 1, 0, f, f1);
            this.walk(this.leftFoot, speedWalk, degreeWalk * 2F, true, 3.5F, 0.1F, f, f1);
            if (!holdingInHands) {
                this.walk(this.rightArm, speedWalk, degreeWalk * 4F, true, 1, 0, f, f1);
                this.walk(this.rightHand, speedWalk, degreeWalk * 2F, true, 3F, 0.1F, f, f1);
                this.walk(this.leftArm, speedWalk, degreeWalk * 4F, false, 1, 0, f, f1);
                this.walk(this.leftHand, speedWalk, degreeWalk * 2F, false, 3F, -0.1F, f, f1);
            }
            this.walk(this.tail1, speedWalk, degreeWalk, false, -1, -0.15F, f, f1);
            this.walk(this.tail2, speedWalk, degreeWalk * 0.5F, true, 0, -0.15F, f, f1);
            this.walk(this.neck, speedWalk, degreeWalk * 0.25F, false, 2, 0, f, f1);

        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS) || rat.getMBTransferRate() > 0) {
            leftArm.rotateAngleX = 1.3089969389957472F;
            rightArm.rotateAngleX = 1.3089969389957472F;
            rightHand.rotateAngleX = 0.9599310885968813F;
            rightHand.rotateAngleY = -0.17453292519943295F;
            rightHand.rotateAngleZ = 0.08726646259971647F;
            leftHand.rotateAngleX = 0.9599310885968813F;
            leftHand.rotateAngleY = 0.17453292519943295F;
            leftHand.rotateAngleZ = -0.08726646259971647F;
        }
        if ((rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_CRAFTING) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN)) && rat.crafting) {
            this.walk(this.leftArm, speedRun, degreeRun * 1F, true, 2, 0, f2, 1);
            this.walk(this.rightArm, speedRun, degreeRun * 1F, false, 2, 0, f2, 1);
            this.walk(this.rightHand, speedRun, degreeRun * 1F, true, 5, -0.1F, f2, 1);
            this.walk(this.leftHand, speedRun, degreeRun * 1F, false, 5, 0.1F, f2, 1);
        }
        if (rat.isDancing()) {
            if (rat.getDanceMoves() == 1) {
                progressRotation(rightArm, 20, (float) Math.toRadians(-110), 0, (float) Math.toRadians(78), 20F);
                this.walk(this.body2, speedDance, degreeDance, false, 1, -0.1F, f2, 1);
                this.swing(this.rightArm, speedDance, degreeDance, false, 2, -0.1F, f2, 1);
                this.walk(this.neck, speedDance, degreeDance, false, 2, -0.1F, f2, 1);
            }
            if (rat.getDanceMoves() == 2) {
                this.walk(this.body2, speedDance, degreeDance * 0.25F, false, 1, -0.1F, f2, 1);
                this.body1.rotateAngleY -= f2 * 0.25F;
                this.walk(this.rightArm, speedDance, degreeDance, false, 2, -0.1F, f2, 1);
                this.walk(this.leftArm, speedDance, degreeDance, true, 2, -0.1F, f2, 1);
                this.walk(this.rightThigh, speedDance, degreeDance, false, 2, -0.1F, f2, 1);
                this.walk(this.leftThigh, speedDance, degreeDance, true, 2, -0.1F, f2, 1);
                this.walk(this.neck, speedDance, degreeDance * 0.25F, false, 1, -0.1F, f2, 1);
            }
            if (rat.getDanceMoves() == 3) {
                progressRotation(rightArm, 20, (float) Math.toRadians(30), (float) Math.toRadians(-98), (float) Math.toRadians(-130), 20F);
                progressRotation(leftArm, 20, (float) Math.toRadians(30), (float) Math.toRadians(80), (float) Math.toRadians(130), 20F);
                progressRotation(rightThigh, 20, 0, (float) Math.toRadians(15), (float) Math.toRadians(15), 20F);
                progressRotation(leftThigh, 20, 0, (float) Math.toRadians(15), (float) Math.toRadians(-15), 20F);
                this.swing(this.rightArm, speedDance, degreeDance, false, 2, -0.1F, f2, 1);
                this.swing(this.leftArm, speedDance, degreeDance, true, 2, -0.1F, f2, 1);
                this.walk(this.neck, speedDance, degreeDance * 0.25F, false, 2, -0.1F, f2, 1);
                this.walk(this.body1, speedDance, degreeDance * 0.5F, false, 2, -0.1F, f2, 1);
                this.walk(this.leftThigh, speedDance, degreeDance * 0.75F, true, 2, -0.1F, f2, 1);
                this.walk(this.rightThigh, speedDance, degreeDance * 0.75F, true, 2, -0.1F, f2, 1);
                this.walk(this.tail1, speedDance, degreeDance * 1, true, 2, -0.1F, f2, 1);
                this.bob(this.body1, speedDance, degreeDance * 6, false, f2, 1);
            }
        }
    }

    public void setRotateAngle(AdvancedRendererModel AdvancedRendererModel, float x, float y, float z) {
        AdvancedRendererModel.rotateAngleX = x;
        AdvancedRendererModel.rotateAngleY = y;
        AdvancedRendererModel.rotateAngleZ = z;
    }

    public void progressRotation(AdvancedRendererModel model, float progress, float rotX, float rotY, float rotZ, float divisor) {
        model.rotateAngleX += progress * (rotX - model.defaultRotationX) / divisor;
        model.rotateAngleY += progress * (rotY - model.defaultRotationY) / divisor;
        model.rotateAngleZ += progress * (rotZ - model.defaultRotationZ) / divisor;
    }

    public void progressPosition(AdvancedRendererModel model, float progress, float x, float y, float z, float divisor) {
        model.rotationPointX += progress * (x - model.defaultPositionX) / divisor;
        model.rotationPointY += progress * (y - model.defaultPositionY) / divisor;
        model.rotationPointZ += progress * (z - model.defaultPositionZ) / divisor;
    }

    private void scratchPosition(EntityRat rat) {
        if (rat.holdProgress == 0) {
            rotateFrom(neck, 38, 0, 0);
            rotateFrom(head, 58, 0, 0);
            rotateFrom(rightHand, 25, 10, -30);
            rotateFrom(leftHand, 25, -10, 30);
        }
    }

    private void rotateFrom(AdvancedRendererModel renderer, float degX, float degY, float degZ) {
        animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
    }

}
