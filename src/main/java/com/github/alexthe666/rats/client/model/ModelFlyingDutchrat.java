package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class ModelFlyingDutchrat<T extends EntityDutchrat> extends AdvancedEntityModel<T> implements IHasArm {
    public AdvancedModelBox body1;
    public AdvancedModelBox body2;
    public AdvancedModelBox neck;
    public AdvancedModelBox leftArm1;
    public AdvancedModelBox rightArm1;
    public AdvancedModelBox coat;
    public AdvancedModelBox body3;
    public AdvancedModelBox body4;
    public AdvancedModelBox tail1;
    public AdvancedModelBox head;
    public AdvancedModelBox upperJaw;
    public AdvancedModelBox lowerJaw;
    public AdvancedModelBox eyebrowLeft;
    public AdvancedModelBox eyebrowRight;
    public AdvancedModelBox earLeft;
    public AdvancedModelBox earRight;
    public AdvancedModelBox nose;
    public AdvancedModelBox wiskers1;
    public AdvancedModelBox wiskers2;
    public AdvancedModelBox beard;
    public AdvancedModelBox leftArm2;
    public AdvancedModelBox hookBase;
    public AdvancedModelBox hook1;
    public AdvancedModelBox hook2;
    public AdvancedModelBox rightArm2;
    public AdvancedModelBox paw;
    public ModelAnimator animator;

    public ModelFlyingDutchrat() {
        this.textureWidth = 128;
        this.textureHeight = 128;
        this.tail1 = new AdvancedModelBox(this, 113, 13);
        this.tail1.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.tail1.addBox(-1.5F, -1.0F, -1.5F, 3, 9, 3, 0.0F);
        this.setRotateAngle(tail1, 0.22759093446006054F, 0.0F, 0.0F);
        this.upperJaw = new AdvancedModelBox(this, 105, 25);
        this.upperJaw.setRotationPoint(0.0F, -3.0F, -6.5F);
        this.upperJaw.addBox(-2.0F, -1.5F, -4.0F, 4, 4, 6, 0.0F);
        this.setRotateAngle(upperJaw, 0.18203784098300857F, 0.0F, 0.0F);
        this.eyebrowLeft = new AdvancedModelBox(this, 76, 30);
        this.eyebrowLeft.setRotationPoint(3.0F, -4.0F, -2.0F);
        this.eyebrowLeft.addBox(-1.0F, -1.0F, -2.0F, 2, 1, 7, 0.0F);
        this.setRotateAngle(eyebrowLeft, 0.12217304763960307F, 0.17453292519943295F, 0.0F);
        this.nose = new AdvancedModelBox(this, 116, 0);
        this.nose.setRotationPoint(0.0F, -1.5F, -4.0F);
        this.nose.addBox(-1.0F, -0.5F, -1.0F, 2, 2, 3, 0.0F);
        this.setRotateAngle(nose, 0.14207500488242633F, 0.0F, 0.0F);
        this.body4 = new AdvancedModelBox(this, 0, 18);
        this.body4.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.body4.addBox(-2.5F, -2.0F, -2.0F, 5, 9, 4, 0.0F);
        this.setRotateAngle(body4, 0.22759093446006054F, 0.0F, 0.0F);
        this.earLeft = new AdvancedModelBox(this, 0, 51);
        this.earLeft.setRotationPoint(3.0F, -3.0F, 2.0F);
        this.earLeft.addBox(-1.0F, -3.0F, 0.0F, 4, 4, 0, 0.0F);
        this.setRotateAngle(earLeft, -0.6981317007977318F, -0.17453292519943295F, 0.7853981633974483F);
        this.neck = new AdvancedModelBox(this, 37, 0);
        this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.neck.addBox(-2.0F, -2.0F, -2.0F, 4, 2, 4, 0.0F);
        this.beard = new AdvancedModelBox(this, 0, 62);
        this.beard.setRotationPoint(0.0F, 0.5F, -4.0F);
        this.beard.addBox(-3.5F, 0.5F, 0.0F, 7, 7, 0, 0.0F);
        this.lowerJaw = new AdvancedModelBox(this, 18, 18);
        this.lowerJaw.setRotationPoint(0.0F, 0.0F, -4.0F);
        this.lowerJaw.addBox(-2.0F, -1.5F, -5.0F, 4, 2, 5, 0.0F);
        this.setRotateAngle(lowerJaw, -0.091106186954104F, 0.0F, 0.0F);
        this.wiskers1 = new AdvancedModelBox(this, 0, 55);
        this.wiskers1.mirror = true;
        this.wiskers1.setRotationPoint(2.0F, 0.0F, -2.0F);
        this.wiskers1.addBox(0.0F, -2.5F, 0.0F, 7, 7, 0, 0.0F);
        this.setRotateAngle(wiskers1, 0.0F, -0.3490658503988659F, 0.0F);
        this.leftArm2 = new AdvancedModelBox(this, 38, 37);
        this.leftArm2.setRotationPoint(8.0F, 0.0F, 0.0F);
        this.leftArm2.addBox(-0.5F, -1.5F, -1.5F, 15, 3, 3, 0.0F);
        this.setRotateAngle(leftArm2, -0.27314402793711257F, 0.7285004297824331F, 0.0F);
        this.coat = new AdvancedModelBox(this, 37, 16);
        this.coat.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.coat.addBox(-7.0F, 0.0F, -4.5F, 14, 12, 9, 0.0F);
        this.rightArm2 = new AdvancedModelBox(this, 38, 37);
        this.rightArm2.mirror = true;
        this.rightArm2.setRotationPoint(-8.0F, 0.0F, 0.0F);
        this.rightArm2.addBox(-14.5F, -1.5F, -1.5F, 15, 3, 3, 0.0F);
        this.setRotateAngle(rightArm2, -0.27314402793711257F, -0.7285004297824331F, 0.0F);
        this.head = new AdvancedModelBox(this, 10, 29);
        this.head.setRotationPoint(0.0F, -1.7F, 0.0F);
        this.head.addBox(-3.0F, -5.0F, -5.0F, 6, 6, 8, 0.0F);
        this.setRotateAngle(head, 0.07505881616829756F, 0.0F, 0.0F);
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
        this.setRotateAngle(rightArm1, 0.0F, -0.3490658503988659F, -0.8726646259971648F);
        this.body3 = new AdvancedModelBox(this, 83, 16);
        this.body3.setRotationPoint(0.0F, 7.0F, 0.0F);
        this.body3.addBox(-4.5F, -2.0F, -2.5F, 9, 9, 5, 0.0F);
        this.setRotateAngle(body3, 0.18203784098300857F, 0.0F, 0.0F);
        this.body2 = new AdvancedModelBox(this, 46, 0);
        this.body2.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.body2.addBox(-6.0F, -2.0F, -3.5F, 12, 9, 7, 0.0F);
        this.setRotateAngle(body2, 0.091106186954104F, 0.0F, 0.0F);
        this.earRight = new AdvancedModelBox(this, 0, 51);
        this.earRight.mirror = true;
        this.earRight.setRotationPoint(-3.0F, -3.0F, 2.0F);
        this.earRight.addBox(-3.0F, -3.0F, 0.0F, 4, 4, 0, 0.0F);
        this.setRotateAngle(earRight, -0.6981317007977318F, 0.17453292519943295F, -0.7853981633974483F);
        this.hookBase = new AdvancedModelBox(this, 107, 35);
        this.hookBase.setRotationPoint(14.0F, 0.0F, 0.0F);
        this.hookBase.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, 0.0F);
        this.setRotateAngle(hookBase, 0.6981317007977318F, 0.0F, 0.136659280431156F);
        this.leftArm1 = new AdvancedModelBox(this, 84, 0);
        this.leftArm1.setRotationPoint(7.0F, 4.0F, 0.0F);
        this.leftArm1.addBox(-3.0F, -2.0F, -2.0F, 12, 4, 4, 0.0F);
        this.setRotateAngle(leftArm1, 0.0F, 0.3490658503988659F, 0.8726646259971648F);
        this.hook1 = new AdvancedModelBox(this, 112, 8);
        this.hook1.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.hook1.addBox(0.0F, -1.0F, -1.0F, 5, 2, 2, 0.0F);
        this.eyebrowRight = new AdvancedModelBox(this, 94, 30);
        this.eyebrowRight.setRotationPoint(-3.0F, -4.0F, -2.0F);
        this.eyebrowRight.addBox(-1.0F, -1.0F, -2.0F, 2, 1, 7, 0.0F);
        this.setRotateAngle(eyebrowRight, 0.12217304763960307F, -0.17453292519943295F, 0.0F);
        this.wiskers2 = new AdvancedModelBox(this, 0, 55);
        this.wiskers2.setRotationPoint(-2.0F, 0.0F, -2.0F);
        this.wiskers2.addBox(-7.0F, -2.5F, 0.0F, 7, 7, 0, 0.0F);
        this.setRotateAngle(wiskers2, 0.0F, 0.3490658503988659F, 0.0F);
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
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body1);
    }

    public void animate(EntityDutchrat entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        animator.update(entity);
        animator.setAnimation(EntityDutchrat.ANIMATION_SLASH);
        animator.startKeyframe(5);
        rotate(body1, 0, 20, 0);
        rotate(rightArm1, -80, 80, 30);
        rotate(rightArm2, -10, 0, 0);
        rotate(leftArm1, 0, -20, 0);
        rotate(lowerJaw, 50, 0, 0);
        rotate(beard, -50, 0, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotate(lowerJaw, 50, 0, 0);
        rotate(beard, -50, 0, 0);
        rotate(body1, 0, -60, 0);
        rotate(rightArm1, 40, 0, 30);
        rotate(leftArm1, 0, 20, 0);
        rotate(paw, 0, 30, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotate(lowerJaw, 50, 0, 0);
        rotate(beard, -50, 0, 0);
        rotate(body1, 0, -20, 0);
        rotate(rightArm2, -10, 0, 0);
        rotate(leftArm1, -40, -20, 30);
        rotate(leftArm2, 0, 60, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotate(lowerJaw, 50, 0, 0);
        rotate(beard, -50, 0, 0);
        rotate(body1, 0, 60, 0);
        rotate(rightArm1, 40, 0, -30);
        rotate(leftArm1, 0, -20, 0);
        rotate(leftArm2, 0, 20, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityDutchrat.ANIMATION_STAB);
        animator.startKeyframe(5);
        rotate(body1, 0, 20, 0);
        rotate(rightArm1, 30, 100, 30);
        rotate(leftArm1, 0, -20, 0);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotate(body1, 0, -30, 0);
        rotate(rightArm1, 20, -30, 0);
        rotate(rightArm2, 20, 30, 0);
        rotate(paw, 0, 20, 0);
        animator.endKeyframe();
        animator.setStaticKeyframe(2);
        animator.resetKeyframe(5);
        animator.setAnimation(EntityDutchrat.ANIMATION_SPEAK);
        animator.startKeyframe(5);
        rotate(beard, -50, 0, 0);
        rotate(lowerJaw, 50, 0, 0);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityDutchrat.ANIMATION_THROW);
        animator.startKeyframe(5);
        rotate(beard, -50, 0, 0);
        rotate(lowerJaw, 50, 0, 0);
        rotate(body1, 0, 60, 0);
        rotate(rightArm1, 0, -180, -70);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotate(beard, -50, 0, 0);
        rotate(lowerJaw, 50, 0, 0);
        rotate(body1, 0, -20, 0);
        rotate(rightArm1, 0, 20, 10);
        animator.endKeyframe();
        animator.resetKeyframe(5);
    }

    private void rotate(AdvancedModelBox renderer, float degX, float degY, float degZ) {
        animator.rotate(renderer, (float) Math.toRadians(degX), (float) Math.toRadians(degY), (float) Math.toRadians(degZ));
    }

    private void rotateFrom(AdvancedModelBox renderer, float degX, float degY, float degZ) {
        animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
    }

    @Override
    public void setRotationAngles(EntityDutchrat rat, float f, float f1, float f2, float f3, float f4) {
        animate(rat, f, f1, f2, f3, f4);
        float idleSpeed = 0.1F;
        float idleDegree = 0.1F;
        float walkSpeed = 0.6F;
        float walkDegree = 0.3F;
        this.bob(body1, idleSpeed, idleDegree * 7F, false, f2, 1);
        this.bob(rightArm1, idleSpeed, idleDegree * 3.5F, false, f2, 1);
        this.bob(leftArm1, idleSpeed, idleDegree * 3.5F, false, f2, 1);
        this.walk(head, idleSpeed * 0.5F, idleDegree * 1F, false, 0, 0, f2, 1);
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

    public void setRotateAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body1,
                body2,
                neck,
                leftArm1,
                rightArm1,
                coat,
                body3,
                body4,
                tail1,
                head,
                upperJaw,
                lowerJaw,
                eyebrowLeft,
                eyebrowRight,
                earLeft,
                earRight,
                nose,
                wiskers1,
                wiskers2,
                beard,
                leftArm2,
                hookBase,
                hook1,
                hook2,
                rightArm2,
                paw);
    }

    public ModelRenderer getArm(HandSide p_191361_1_) {
        return paw;
    }

    @Override
    public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
        body1.translateRotate(matrixStackIn);
        rightArm1.translateRotate(matrixStackIn);
        rightArm2.translateRotate(matrixStackIn);
        paw.translateRotate(matrixStackIn);
        matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 90.0F, true));
        matrixStackIn.translate(0.1F, -0.45F, 0);
    }
}
