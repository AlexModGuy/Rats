package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelRatlantisArmor extends BipedModel {
    public ModelRenderer tinyRatHead1;
    public ModelRenderer ear1;
    public ModelRenderer ear2;
    public ModelRenderer ratHead2;
    public ModelRenderer jaw;
    public ModelRenderer nose;
    public ModelRenderer tinyRatHead2;

    public ModelRatlantisArmor(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.ear1 = new ModelRenderer(this, 24, 90);
        this.ear1.setRotationPoint(-9.0F, -10.0F, 7.0F);
        this.ear1.addBox(-3.5F, -3.5F, 0.0F, 7, 7, 1, 0);
        this.setRotateAngle(ear1, 0.0F, 0.6981317007977318F, 0.0F);
        this.ear2 = new ModelRenderer(this, 24, 90);
        this.ear2.setRotationPoint(9.0F, -10.0F, 7.0F);
        this.ear2.addBox(-3.5F, -3.5F, 0.0F, 7, 7, 1, 0);
        this.setRotateAngle(ear2, 0.0F, -0.6981317007977318F, 0.0F);
        this.jaw = new ModelRenderer(this, 11, 80);
        this.jaw.setRotationPoint(0.0F, -1.0F, -4.0F);
        this.jaw.addBox(-2.0F, 0.0F, -5.0F, 4, 2, 6, 0);
        this.setRotateAngle(jaw, 0.36425021489121656F, 0.0F, 0.0F);
        this.nose = new ModelRenderer(this, 0, 75);
        this.nose.setRotationPoint(0.0F, -2.5F, -5.5F);
        this.nose.addBox(-1.0F, -1.0F, -1.5F, 2, 2, 3, 0);
        this.setRotateAngle(nose, 0.3490658503988659F, 0.0F, 0.0F);
        this.ratHead2 = new ModelRenderer(this, 0, 90);
        this.ratHead2.setRotationPoint(0.0F, -1.0F, -4.0F);
        this.ratHead2.addBox(-2.5F, -2.5F, -6.0F, 5, 4, 6, 0);
        this.tinyRatHead2 = new ModelRenderer(this, 0, 80);
        this.tinyRatHead2.setRotationPoint(2.0F, -2.0F, 0.0F);
        this.tinyRatHead2.addBox(-2.5F, -7.0F, 0.0F, 5, 9, 0, 0);
        this.setRotateAngle(tinyRatHead2, 0.0F, 0.0F, 0.5235987755982988F);
        this.tinyRatHead1 = new ModelRenderer(this, 0, 80);
        this.tinyRatHead1.setRotationPoint(-2.0F, -2.0F, 0.0F);
        this.tinyRatHead1.addBox(-2.5F, -7.0F, 0.0F, 5, 9, 0, 0);
        this.setRotateAngle(tinyRatHead1, 0.0F, 0.0F, -0.5235987755982988F);
        this.bipedHead.addChild(this.ear1);
        this.bipedHead.addChild(this.ear2);
        this.bipedHead.addChild(this.jaw);
        this.ratHead2.addChild(this.nose);
        this.bipedHead.addChild(this.ratHead2);
        this.bipedLeftArm.addChild(this.tinyRatHead2);
        this.bipedRightArm.addChild(this.tinyRatHead1);
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
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
}
