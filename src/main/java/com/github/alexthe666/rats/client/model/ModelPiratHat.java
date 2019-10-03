package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

public class ModelPiratHat extends ModelBiped {
    public ModelRenderer piratHat1;
    public ModelRenderer brim1;
    public ModelRenderer brim2;
    public ModelRenderer brim3;
    public ModelRenderer brim4;
    public ModelRenderer middle;
    public ModelRenderer brim6;
    public ModelRenderer brim5;
    public ModelRenderer brim6_1;

    public ModelPiratHat(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.middle = new ModelRenderer(this, 0, 86);
        this.middle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.middle.addBox(-4.0F, -5.0F, -3.0F, 8, 5, 6, 0.0F);
        this.brim3 = new ModelRenderer(this, 23, 78);
        this.brim3.setRotationPoint(6.0F, 0.0F, 0.0F);
        this.brim3.addBox(0.01F, -3.0F, -4.0F, 1, 3, 8, 0.0F);
        this.brim2 = new ModelRenderer(this, 0, 80);
        this.brim2.setRotationPoint(0.0F, 0.0F, 4.0F);
        this.brim2.addBox(-7.0F, -4.0F, 0.0F, 14, 4, 1, 0.0F);
        this.setRotateAngle(brim2, 0.17453292519943295F, 0.0F, 0.0F);
        this.brim6_1 = new ModelRenderer(this, 1, 101);
        this.brim6_1.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.brim6_1.addBox(-3.0F, -1.0F, 0.0F, 6, 1, 1, 0.0F);
        this.piratHat1 = new ModelRenderer(this, -10, 64);
        this.piratHat1.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.piratHat1.addBox(-7.0F, -0.01F, -5.0F, 14, 0, 9, 0.0F);
        this.setRotateAngle(piratHat1, 0.0F, 3.141592653589793F, 0.0F);
        this.brim1 = new ModelRenderer(this, 0, 75);
        this.brim1.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.brim1.addBox(-7.0F, -4.0F, 0.0F, 14, 4, 1, 0.0F);
        this.setRotateAngle(brim1, -0.17453292519943295F, 0.0F, 0.0F);
        this.brim6 = new ModelRenderer(this, 30, 90);
        this.brim6.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.brim6.addBox(-5.0F, -2.0F, 0.0F, 10, 2, 1, 0.0F);
        this.brim5 = new ModelRenderer(this, 0, 97);
        this.brim5.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.brim5.addBox(-5.0F, -3.0F, 0.0F, 10, 3, 1, 0.0F);
        this.brim4 = new ModelRenderer(this, 23, 78);
        this.brim4.setRotationPoint(-6.0F, 0.0F, 0.0F);
        this.brim4.addBox(-1.01F, -3.0F, -4.0F, 1, 3, 8, 0.0F);
        this.piratHat1.addChild(this.middle);
        this.piratHat1.addChild(this.brim3);
        this.piratHat1.addChild(this.brim2);
        this.brim5.addChild(this.brim6_1);
        this.piratHat1.addChild(this.brim1);
        this.brim1.addChild(this.brim6);
        this.brim2.addChild(this.brim5);
        this.piratHat1.addChild(this.brim4);
        this.bipedHead.addChild(this.piratHat1);
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

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
