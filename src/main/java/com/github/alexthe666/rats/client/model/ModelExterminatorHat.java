package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelExterminatorHat extends BipedModel {
    public ModelRenderer hat1;
    public ModelRenderer cigarette;
    public ModelRenderer hat2;

    public ModelExterminatorHat(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.cigarette = new ModelRenderer(this, 0, 77);
        this.cigarette.setRotationPoint(-1.5F, -1.0F, -3.5F);
        this.cigarette.addBox(-0.5F, -0.5F, -4.0F, 1, 1, 4, 0.0F);
        this.setRotateAngle(cigarette, 0.17453292519943295F, 0.2617993877991494F, 0.0F);
        this.hat1 = new ModelRenderer(this, 0, 64);
        this.hat1.setRotationPoint(0.0F, -7.4F, 1.6F);
        this.hat1.addBox(-3.5F, -4.0F, -5.5F, 7, 3, 8, 0.0F);
        this.setRotateAngle(hat1, -0.08726646259971647F, 0.0F, 0.0F);
        this.hat2 = new ModelRenderer(this, 0, 75);
        this.hat2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hat2.addBox(-4.5F, -1.0F, -11.0F, 9, 1, 14, 0.0F);
        this.bipedHead.addChild(this.cigarette);
        this.bipedHead.addChild(this.hat1);
        this.hat1.addChild(this.hat2);
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

    public BipedModel withEntity(LivingEntity entity) {
        if(entity instanceof EntityRat){

            this.cigarette.setRotationPoint(-1.5F, -1F, -7F);
            this.setRotateAngle(cigarette, 0.2F, 0.7F, 0.0F);
            this.hat1.setRotationPoint(0.0F, -3F, 1.6F);
            this.setRotateAngle(hat1, -0.2F, 0.0F, 0.0F);

        }else{
            this.cigarette.setRotationPoint(-1.5F, -1.0F, -3.5F);
            this.setRotateAngle(cigarette, 0.17453292519943295F, 0.2617993877991494F, 0.0F);
            this.hat1.setRotationPoint(0.0F, -7.4F, 1.6F);
            this.setRotateAngle(hat1, -0.08726646259971647F, 0.0F, 0.0F);

        }
        return this;
    }
}
