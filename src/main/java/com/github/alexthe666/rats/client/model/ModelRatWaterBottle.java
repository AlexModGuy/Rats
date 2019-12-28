package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;

public class ModelRatWaterBottle <T extends Entity> extends EntityModel<T> {
    public RendererModel waterBottle;
    public RendererModel bottom;
    public RendererModel sip;

    public ModelRatWaterBottle() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.bottom = new RendererModel(this, 0, 16);
        this.bottom.setRotationPoint(0.0F, 11.0F, -2.5F);
        this.bottom.addBox(-2.0F, 0.0F, -2.0F, 4, 1, 4, 0.0F);
        this.waterBottle = new RendererModel(this, 0, 0);
        this.waterBottle.setRotationPoint(0.0F, 5.0F, -8.0F);
        this.waterBottle.addBox(-2.5F, 0.0F, -5.0F, 5, 11, 5, 0.0F);
        this.setRotateAngle(waterBottle, 0.0F, 3.141592653589793F, 0.0F);
        this.sip = new RendererModel(this, 0, 21);
        this.sip.setRotationPoint(0.0F, 1.0F, 0.75F);
        this.sip.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 1, 0.0F);
        this.setRotateAngle(sip, -0.6108652381980153F, 0.0F, 0.0F);
        this.waterBottle.addChild(this.bottom);
        this.bottom.addChild(this.sip);
    }

    public void render(float f5) {
        this.waterBottle.render(f5);
    }

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
