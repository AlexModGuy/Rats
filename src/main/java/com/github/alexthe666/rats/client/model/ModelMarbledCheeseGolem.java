package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMarbledCheeseGolem<T extends Entity> extends AdvancedEntityModel<T> {
    public AdvancedRendererModel body;
    public AdvancedRendererModel headBase;
    public AdvancedRendererModel armLeft1;
    public AdvancedRendererModel armRight1;
    public AdvancedRendererModel thruster;
    public AdvancedRendererModel upperbody;
    public AdvancedRendererModel snout;
    public AdvancedRendererModel ear1;
    public AdvancedRendererModel ear2;
    public AdvancedRendererModel nose;
    public AdvancedRendererModel armLeft2;
    public AdvancedRendererModel drillArm1;
    public AdvancedRendererModel drillArm2;
    public AdvancedRendererModel drilArm3;
    public AdvancedRendererModel blade;
    public AdvancedRendererModel armRight2;
    public AdvancedRendererModel cannon;
    private ModelAnimator animator;

    public ModelMarbledCheeseGolem() {
        this.textureWidth = 128;
        this.textureHeight = 96;
        this.upperbody = new AdvancedRendererModel(this, 0, 34);
        this.upperbody.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.upperbody.addBox(-11.0F, -5.0F, -6.0F, 22, 10, 12, 0.0F);
        this.body = new AdvancedRendererModel(this, 0, 0);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-6.0F, -3.5F, -5.0F, 12, 16, 10, 0.0F);
        this.headBase = new AdvancedRendererModel(this, 44, 0);
        this.headBase.setRotationPoint(0.0F, -15.0F, 0.0F);
        this.headBase.addBox(-4.0F, -6.0F, -7.0F, 8, 7, 11, 0.0F);
        this.setRotateAngle(headBase, 0.22759093446006054F, 0.0F, 0.0F);
        this.ear2 = new AdvancedRendererModel(this, 34, 0);
        this.ear2.mirror = true;
        this.ear2.setRotationPoint(-7.0F, -3.0F, 5.0F);
        this.ear2.addBox(-7.0F, -6.0F, 0.0F, 7, 7, 1, 0.0F);
        this.setRotateAngle(ear2, -0.2617993877991494F, 0.9075712110370513F, 0.0F);
        this.drilArm3 = new AdvancedRendererModel(this, 0, 0);
        this.drilArm3.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.drilArm3.addBox(-0.5F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.setRotateAngle(drilArm3, 0.0F, 0.0F, -0.13962634015954636F);
        this.cannon = new AdvancedRendererModel(this, 68, 47);
        this.cannon.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.cannon.addBox(-3.0F, 0.0F, -4.0F, 6, 14, 7, 0.0F);
        this.setRotateAngle(cannon, -0.3490658503988659F, -0.13321877203096466F, -0.31359068978645194F);
        this.armRight1 = new AdvancedRendererModel(this, 82, 0);
        this.armRight1.mirror = true;
        this.armRight1.setRotationPoint(-17.0F, -4.0F, 0.0F);
        this.armRight1.addBox(-3.0F, -4.0F, -2.5F, 5, 18, 5, 0.0F);
        this.setRotateAngle(armRight1, -0.05235987755982988F, 0.2617993877991494F, 0.22689280275926282F);
        this.thruster = new AdvancedRendererModel(this, 36, 18);
        this.thruster.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.thruster.addBox(-5.0F, -1.0F, -4.0F, 10, 8, 8, 0.0F);
        this.setRotateAngle(thruster, 0.3490658503988659F, 0.0F, 0.0F);
        this.nose = new AdvancedRendererModel(this, 64, 18);
        this.nose.setRotationPoint(0.0F, -3.0F, -3.0F);
        this.nose.addBox(-1.5F, -2.0F, -4.0F, 3, 3, 4, 0.0F);
        this.setRotateAngle(nose, 0.17453292519943295F, 0.0F, 0.0F);
        this.drillArm2 = new AdvancedRendererModel(this, 0, 26);
        this.drillArm2.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.drillArm2.addBox(-1.0F, 0.0F, -1.0F, 1, 12, 2, 0.0F);
        this.setRotateAngle(drillArm2, 0.0F, 0.0F, 0.13962634015954636F);
        this.armLeft2 = new AdvancedRendererModel(this, 107, 26);
        this.armLeft2.setRotationPoint(0.0F, 18.0F, -2.0F);
        this.armLeft2.addBox(-1.5F, -1.0F, -2.5F, 3, 18, 5, 0.0F);
        this.setRotateAngle(armLeft2, -1.0471975511965976F, -0.08726646259971647F, 0.22689280275926282F);
        this.drillArm1 = new AdvancedRendererModel(this, 0, 26);
        this.drillArm1.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.drillArm1.addBox(0.0F, 0.0F, -1.0F, 1, 12, 2, 0.0F);
        this.setRotateAngle(drillArm1, 0.0F, 0.0F, -0.13962634015954636F);
        this.armLeft1 = new AdvancedRendererModel(this, 82, 0);
        this.armLeft1.setRotationPoint(17.0F, -4.0F, 0.0F);
        this.armLeft1.addBox(-2.0F, -4.0F, -2.5F, 5, 18, 5, 0.0F);
        this.setRotateAngle(armLeft1, -0.05235987755982988F, -0.2617993877991494F, -0.22689280275926282F);
        this.blade = new AdvancedRendererModel(this, 0, 42);
        this.blade.setRotationPoint(1.5F, 0.0F, 0.0F);
        this.blade.addBox(0.0F, -7.0F, -7.0F, 0, 14, 14, 0.0F);
        this.setRotateAngle(blade, 0.7853981633974483F, 0.0F, 0.0F);
        this.ear1 = new AdvancedRendererModel(this, 34, 0);
        this.ear1.setRotationPoint(7.0F, -3.0F, 5.0F);
        this.ear1.addBox(0.0F, -6.0F, 0.0F, 7, 7, 1, 0.0F);
        this.setRotateAngle(ear1, -0.2617993877991494F, -0.9075712110370513F, 0.0F);
        this.armRight2 = new AdvancedRendererModel(this, 107, 26);
        this.armRight2.setRotationPoint(0.0F, 18.0F, -2.0F);
        this.armRight2.addBox(-1.5F, -1.0F, -2.5F, 3, 18, 5, 0.0F);
        this.setRotateAngle(armRight2, -1.0471975511965976F, 0.08726646259971647F, -0.22689280275926282F);
        this.snout = new AdvancedRendererModel(this, 72, 23);
        this.snout.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.snout.addBox(-3.0F, -4.0F, -5.0F, 6, 5, 6, 0.0F);
        this.body.addChild(this.upperbody);
        this.headBase.addChild(this.ear2);
        this.drillArm2.addChild(this.drilArm3);
        this.armRight2.addChild(this.cannon);
        this.body.addChild(this.thruster);
        this.snout.addChild(this.nose);
        this.armLeft2.addChild(this.drillArm2);
        this.armLeft1.addChild(this.armLeft2);
        this.armLeft2.addChild(this.drillArm1);
        this.drilArm3.addChild(this.blade);
        this.headBase.addChild(this.ear1);
        this.armRight1.addChild(this.armRight2);
        this.headBase.addChild(this.snout);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, f5);
        this.body.render(f5);
        this.headBase.render(f5);
        this.armRight1.render(f5);
        this.armLeft1.render(f5);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        EntityMarbleCheeseGolem golem = (EntityMarbleCheeseGolem) entity;
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityMarbleCheeseGolem) entity);
        animator.update(entity);
        animator.setAnimation(EntityMarbleCheeseGolem.ANIMATION_MELEE);
        animator.startKeyframe(5);
        rotateFrom(body, 0, -20, 0);
        rotateFrom(armLeft1, -60, -20, 10);
        rotateFrom(armLeft2, -100, -5, 30);
        animator.endKeyframe();
        animator.startKeyframe(5);
        rotateFrom(armLeft1, 0, 40, -10);
        rotateFrom(armLeft2, -30, 40, -30);
        animator.endKeyframe();
        animator.resetKeyframe(5);
        animator.setAnimation(EntityMarbleCheeseGolem.ANIMATION_RANGED);
        animator.startKeyframe(5);
        animator.move(armRight2, -1F, 0, 2.5F);
        rotateFrom(armRight1, -90, -10, 10);
        rotateFrom(armRight2, 10, 0, 0);
        rotateFrom(cannon, 0, 10, 5);
        animator.endKeyframe();
        animator.setStaticKeyframe(5);
        animator.resetKeyframe(5);
        animator.endKeyframe();
    }

    public void renderHead(float f5, float ticksExisted){
        this.resetToDefaultPose();
        ear1.rotationPointZ = 3;
        ear2.rotationPointZ = 3;
        float idleSpeed = 0.1F;
        float idleDegree = 0.7F;
        this.bob(headBase, idleSpeed, idleDegree, 0, 0, false, ticksExisted, 1);
        this.bob(ear2, idleSpeed, idleDegree, -1, 0, false, ticksExisted, 1);
        this.bob(ear1, idleSpeed, idleDegree, -1, 0, false, ticksExisted, 1);
        this.walk(headBase, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, ticksExisted, 1);
        this.swing(ear1, idleSpeed * 0.4F, idleDegree * 0.4F, false, 0, 0, ticksExisted, 1);
        this.swing(ear2, idleSpeed * 0.4F, idleDegree * 0.4F, true, 0, 0, ticksExisted, 1);
        this.headBase.render(f5);
       }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, EntityMarbleCheeseGolem rat) {
        this.blade.rotateAngleX = (float) Math.toRadians(MathHelper.wrapDegrees(f2 * 50));
        float idleSpeed = 0.3F;
        float idleDegree = 0.7F;
        this.bob(body, idleSpeed, idleDegree, -1, 0, false, f2, 1);
        this.bob(headBase, idleSpeed, idleDegree, 0, 0, false, f2, 1);
        this.bob(ear2, idleSpeed, idleDegree, -1, 0, false, f2, 1);
        this.bob(ear1, idleSpeed, idleDegree, -1, 0, false, f2, 1);
        this.bob(armRight1, idleSpeed, idleDegree, 2, 0, false, f2, 1);
        this.bob(armRight2, idleSpeed, idleDegree, 2, 0, false, f2, 1);
        this.bob(armLeft1, idleSpeed, idleDegree, 3, 0, false, f2, 1);
        this.bob(armLeft2, idleSpeed, idleDegree, 3, 0, false, f2, 1);
        this.bob(cannon, idleSpeed, idleDegree, 3, 0, false, f2, 1);
        this.walk(headBase, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
        this.swing(ear1, idleSpeed * 0.4F, idleDegree * 0.4F, false, 0, 0, f2, 1);
        this.swing(ear2, idleSpeed * 0.4F, idleDegree * 0.4F, true, 0, 0, f2, 1);
        this.walk(armRight1, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
        this.walk(armRight2, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, -0.1F, f2, 1);
        this.walk(cannon, idleSpeed * 0.4F, idleDegree * -0.2F, false, 1, 0F, f2, 1);
        this.walk(armLeft1, idleSpeed * 0.4F, idleDegree * 0.1F, false, 0, 0, f2, 1);
        this.walk(armLeft2, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, -0.1F, f2, 1);
        this.walk(body, idleSpeed * 0.4F, idleDegree * 0.1F, false, 1, 0.1F, f2, 1);
        this.walk(thruster, idleSpeed * 0.4F, idleDegree * 0.2F, false, 1, 0.1F, f2, 1);
        this.faceTarget(f3, f4, 1, headBase);
    }

    public void bob(AdvancedRendererModel box, float speed, float degree, float offset, float weight, boolean bounce, float f, float f1) {
        float movementScale = box.getModel().getMovementScale();
        degree *= movementScale;
        speed *= movementScale;

        float bob = (float) (Math.sin(f * speed + offset) * f1 * degree - f1 * degree + f1 * weight);
        if (bounce) {
            bob = (float) -Math.abs((Math.sin(f * speed + offset) * f1 * degree + f1 * weight));
        }
        box.rotationPointY += bob;
    }


    public void setRotateAngle(AdvancedRendererModel advancedRendererModel, float x, float y, float z) {
        advancedRendererModel.rotateAngleX = x;
        advancedRendererModel.rotateAngleY = y;
        advancedRendererModel.rotateAngleZ = z;
    }

    private void rotateFrom(AdvancedRendererModel renderer, float degX, float degY, float degZ) {
        animator.rotate(renderer, (float) Math.toRadians(degX) - renderer.defaultRotationX, (float) Math.toRadians(degY) - renderer.defaultRotationY, (float) Math.toRadians(degZ) - renderer.defaultRotationZ);
    }
}
