package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAutoCurdler extends ModelBase {
    public ModelRenderer bottom;
    public ModelRenderer milkTank;

    public ModelAutoCurdler() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.bottom = new ModelRenderer(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-7.0F, -8.0F, -7.0F, 14, 8, 14, 0.0F);
        this.milkTank = new ModelRenderer(this, 0, 22);
        this.milkTank.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.milkTank.addBox(-5.0F, -16.0F, -5.0F, 10, 10, 10, 0.0F);
        this.bottom.addChild(this.milkTank);
    }

    public void render(float f5) {
        this.bottom.render(f5);
    }
}
