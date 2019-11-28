package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelSantaHat extends ModelBiped {
    public ModelRenderer santaHat;
    public ModelRenderer red1;
    public ModelRenderer red2;
    public ModelRenderer red3;
    public ModelRenderer red4;
    public ModelRenderer ball;

    public ModelSantaHat(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.red1 = new ModelRenderer(this, 0, 76);
        this.red1.setRotationPoint(-4.0F, 0.0F, -3.5F);
        this.red1.addBox(0.0F, -5.0F, 0.0F, 8, 5, 7, 0.0F);
        this.setRotateAngle(red1, -0.2617993877991494F, 0.0F, 0.091106186954104F);
        this.santaHat = new ModelRenderer(this, 0, 64);
        this.santaHat.setRotationPoint(0.0F, -8.5F, 0.0F);
        this.santaHat.addBox(-5.0F, -0.01F, -5.0F, 10, 2, 10, 0.0F);
        this.red3 = new ModelRenderer(this, 0, 100);
        this.red3.setRotationPoint(1.0F, -6.0F, 0.5F);
        this.red3.addBox(0.0F, -4.0F, 0.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(red3, -0.20943951023931953F, 0.0F, 0.27314402793711257F);
        this.red4 = new ModelRenderer(this, 16, 100);
        this.red4.setRotationPoint(1.0F, -4.0F, 1.0F);
        this.red4.addBox(0.0F, -2.0F, 0.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(red4, -0.20943951023931953F, 0.0F, 0.27314402793711257F);
        this.red2 = new ModelRenderer(this, 0, 88);
        this.red2.setRotationPoint(1.0F, -5.0F, 0.5F);
        this.red2.addBox(0.0F, -6.0F, 0.0F, 6, 6, 6, 0.0F);
        this.setRotateAngle(red2, -0.20943951023931953F, 0.0F, 0.27314402793711257F);
        this.ball = new ModelRenderer(this, 30, 64);
        this.ball.setRotationPoint(1.0F, -1.6F, 1.0F);
        this.ball.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
        this.setRotateAngle(ball, 0.0F, -0.7853981633974483F, 0.17453292519943295F);
        this.bipedHead.addChild(this.santaHat);
        this.santaHat.addChild(this.red1);
        this.red2.addChild(this.red3);
        this.red3.addChild(this.red4);
        this.red1.addChild(this.red2);
        this.red4.addChild(this.ball);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        if (entityIn instanceof EntityArmorStand) {
            EntityArmorStand entityarmorstand = (EntityArmorStand) entityIn;
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
            copyModelAngles(this.bipedHead, this.bipedHeadwear);
        } else {
            super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        }
    }
}
