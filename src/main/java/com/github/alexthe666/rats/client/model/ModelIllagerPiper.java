package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class ModelIllagerPiper extends IllagerModel {
    public RendererModel hat;
    public RendererModel hat_feather;
    public RendererModel hatTop;

    public ModelIllagerPiper() {
        super(0, 0, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.hat_feather = new RendererModel(this, 0, 72);
        this.hat_feather.setRotationPoint(-2.5F, -5.0F, 0.0F);
        this.hat_feather.addBox(0.0F, -9.0F, 0.0F, 0, 9, 9, 0.0F);
        this.setRotateAngle(hat_feather, 0.0F, 0.0F, -0.3490658503988659F);
        this.hat = new RendererModel(this, 0, 64);
        this.hat.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.hat.addBox(-4.5F, -5.0F, -6.0F, 9, 4, 12, 0.0F);
        this.setRotateAngle(hat, 0.17453292519943295F, 0.0F, 0.0F);
        this.hatTop = new RendererModel(this, 0, 118);
        this.hatTop.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.hatTop.addBox(-2.5F, -2.0F, -4.0F, 5, 2, 8, 0.0F);
        this.head.addChild(this.hat);
        this.hat.addChild(this.hat_feather);
        this.hat.addChild(this.hatTop);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }

    public void setRotationAngles(AbstractIllagerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (((AbstractIllagerEntity) entityIn).getHeldItem(Hand.MAIN_HAND).getItem() == RatsItemRegistry.RAT_FLUTE) {
            float f = 0.01F * (float) (entityIn.getEntityId() % 10);
            RendererModel nose = this.head.childModels.get(0);
            nose.rotateAngleY = 0.0F;
            nose.rotateAngleZ = MathHelper.cos((float) entityIn.ticksExisted * f) * 2.5F * 0.017453292F;
            nose.rotateAngleX = -0.9F;
            nose.offsetZ = -0.09375F;
            nose.offsetY = 0.1875F;
        }
        AbstractIllagerEntity.ArmPose abstractillager$illagerarmpose = ((AbstractIllagerEntity) entityIn).getArmPose();
        if (abstractillager$illagerarmpose == AbstractIllagerEntity.ArmPose.BOW_AND_ARROW) {
            this.rightArm.rotateAngleY = -0.1F + this.head.rotateAngleY;
            this.rightArm.rotateAngleX = -((float) Math.PI / 2F) + this.head.rotateAngleX;
            this.leftArm.rotateAngleX = -0.9F + 0;
            this.leftArm.rotateAngleY = 0;
            this.leftArm.rotateAngleZ = ((float) Math.PI / 2F) + this.head.rotateAngleX;
        }
    }
}
