package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelMilitaryHat extends BipedModel<LivingEntity> {
    public ModelRenderer hat1;
    public ModelRenderer hat2;
    public ModelRenderer hat3;
    public ModelRenderer hat4;
    public ModelRenderer gold;

    public ModelMilitaryHat(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.hat4 = new ModelRenderer(this, 0, 87);
        this.hat4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat4.addBox(-4.0F, -1.0F, -4.0F, 8, 1, 8, 0.0F);
        this.gold = new ModelRenderer(this, 0, 65);
        this.gold.setRotationPoint(0.0F, -2.5F, 3.5F);
        this.gold.addBox(-1.0F, -1.0F, -0.5F, 2, 2, 1, 0.0F);
        this.hat1 = new ModelRenderer(this, 0, 64);
        this.hat1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.hat1.addBox(-3.5F, -4.0F, -3.5F, 7, 4, 7, 0.0F);
        this.setRotateAngle(hat1, 0.0F, 3.141592653589793F, 0.0F);
        this.hat3 = new ModelRenderer(this, 0, 97);
        this.hat3.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.hat3.addBox(-2.5F, -1.0F, 1.0F, 5, 1, 2, 0.0F);
        this.hat2 = new ModelRenderer(this, 0, 75);
        this.hat2.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.hat2.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        this.setRotateAngle(hat2, 0.2617993877991494F, 0.0F, 0.0F);
        this.hat1.addChild(this.hat4);
        this.hat1.addChild(this.gold);
        this.hat1.addChild(this.hat3);
        this.hat1.addChild(this.hat2);
        this.bipedHead.addChild(hat1);
    }

    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
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
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
