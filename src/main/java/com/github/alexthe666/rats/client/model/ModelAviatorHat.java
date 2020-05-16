package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelAviatorHat extends BipedModel<LivingEntity> {
    public ModelRenderer hat1;
    public ModelRenderer sideDingle;
    public ModelRenderer sideDingle2;
    public ModelRenderer goggles1;

    public ModelAviatorHat(float scale) {
        super(scale, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.hat1 = new ModelRenderer(this, 0, 64);
        this.hat1.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.hat1.addBox(-4.5F, -4.0F, -4.5F, 9, 4, 9, 0.0F);
        this.setRotateAngle(hat1, 0.0F, 0F, 0.0F);
        this.sideDingle = new ModelRenderer(this, 0, 78);
        this.sideDingle.setRotationPoint(4.0F, 0.0F, 0.0F);
        this.sideDingle.addBox(0.0F, 0.0F, -2.5F, 1, 7, 5, 0.0F);
        this.setRotateAngle(sideDingle, 0.0F, 0.0F, -0.2617993877991494F);
        this.goggles1 = new ModelRenderer(this, 14, 78);
        this.goggles1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.goggles1.addBox(-5.0F, 0.0F, -5.0F, 10, 3, 10, 0.0F);
        this.sideDingle2 = new ModelRenderer(this, 0, 78);
        this.sideDingle2.mirror = true;
        this.sideDingle2.setRotationPoint(-4.0F, 0.0F, 0.0F);
        this.sideDingle2.addBox(-1.0F, 0.0F, -2.5F, 1, 7, 5, 0.0F);
        this.setRotateAngle(sideDingle2, 0.0F, 0.0F, 0.27314402793711257F);
        this.bipedHead.addChild(this.hat1);
        this.hat1.addChild(this.sideDingle);
        this.hat1.addChild(this.goggles1);
        this.hat1.addChild(this.sideDingle2);
    }

    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entityIn instanceof EntityRat){
            this.goggles1.rotationPointY = 2.0F;
        }else{
            this.goggles1.rotationPointY = 2.0F;
        }
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
