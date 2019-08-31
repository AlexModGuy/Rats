package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatHammock extends ModelBase {
    public ModelRenderer midSection;
    public ModelRenderer string;
    public ModelRenderer left;
    public ModelRenderer right;

    public ModelRatHammock() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.left = new ModelRenderer(this, 24, 16);
        this.left.mirror = true;
        this.left.setRotationPoint(2.0F, 1.0F, 0.0F);
        this.left.addBox(0.0F, -1.0F, -4.0F, 5, 1, 8, 0.0F);
        this.setRotateAngle(left, 0.0F, 0.0F, -0.3490658503988659F);
        this.string = new ModelRenderer(this, 0, 0);
        this.string.setRotationPoint(-6.4F, 15.4F, -4.0F);
        this.string.addBox(-0.1F, -7.3F, 0.0F, 13, 8, 8, 0.0F);
        this.midSection = new ModelRenderer(this, 0, 16);
        this.midSection.setRotationPoint(0.0F, 17.0F, 0.0F);
        this.midSection.addBox(-2.0F, 0.0F, -4.0F, 4, 1, 8, 0.0F);
        this.right = new ModelRenderer(this, 24, 16);
        this.right.setRotationPoint(-2.0F, 1.0F, 0.0F);
        this.right.addBox(-5.0F, -1.0F, -4.0F, 5, 1, 8, 0.0F);
        this.setRotateAngle(right, 0.0F, 0.0F, 0.3490658503988659F);
        this.midSection.addChild(this.left);
        this.midSection.addChild(this.right);
    }

    public void renderString(float f5) {
        this.string.render(f5);
    }

    public void renderHammock(float f5) {
        this.midSection.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
