package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelPartyHat extends BipedModel {
    public RendererModel hat1;
    public RendererModel hat2;
    public RendererModel hat3;
    public RendererModel hat4;
    public RendererModel hat5;
    public RendererModel puff1;
    public RendererModel puff2;

    public ModelPartyHat(float scale) {
        super(scale, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.hat3 = new RendererModel(this, 0, 55);
        this.hat3.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.hat3.addBox(-2.0F, -4.0F, -2.0F, 4, 2, 4, 0.0F);
        this.hat4 = new RendererModel(this, 18, 40);
        this.hat4.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.hat4.addBox(-1.5F, -4.0F, -1.5F, 3, 2, 3, 0.0F);
        this.hat5 = new RendererModel(this, 18, 48);
        this.hat5.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.hat5.addBox(-1.0F, -4.0F, -1.0F, 2, 1, 2, 0.0F);
        this.hat2 = new RendererModel(this, 0, 48);
        this.hat2.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.hat2.addBox(-2.5F, -4.0F, -2.5F, 5, 2, 5, 0.0F);
        this.puff2 = new RendererModel(this, 40, 40);
        this.puff2.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.puff2.addBox(-2.5F, -5.0F, 0.0F, 5, 5, 0, 0.0F);
        this.setRotateAngle(puff2, 0.0F, -0.7853981633974483F, 0.0F);
        this.hat1 = new RendererModel(this, 0, 40);
        this.hat1.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.hat1.addBox(-3.0F, -4.0F, -3.0F, 6, 2, 6, 0.0F);
        this.puff1 = new RendererModel(this, 40, 40);
        this.puff1.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.puff1.addBox(-2.5F, -5.0F, 0.0F, 5, 5, 0, 0.0F);
        this.setRotateAngle(puff1, 0.0F, 0.7853981633974483F, 0.0F);
        this.hat2.addChild(this.hat3);
        this.hat3.addChild(this.hat4);
        this.hat4.addChild(this.hat5);
        this.hat1.addChild(this.hat2);
        this.hat5.addChild(this.puff2);
        this.hat5.addChild(this.puff1);
        this.bipedHead.addChild(this.hat1);
    }

    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entityIn instanceof ArmorStandEntity) {
            ArmorStandEntity entityarmorstand = (ArmorStandEntity) entityIn;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            this.bipedHeadwear.copyModelAngles(this.bipedHead);
        } else {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        }
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}