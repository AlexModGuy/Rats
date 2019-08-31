package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatIgloo extends ModelBase {
    public ModelRenderer cube1;
    public ModelRenderer crown;
    public ModelRenderer entrance;

    public ModelRatIgloo() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.entrance = new ModelRenderer(this, 30, 0);
        this.entrance.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.entrance.addBox(-2.5F, -5.0F, -3.0F, 5, 5, 3, 0.0F);
        this.cube1 = new ModelRenderer(this, 0, 0);
        this.cube1.setRotationPoint(0.0F, 24.0F, 2.0F);
        this.cube1.addBox(-5.0F, -8.0F, -5.0F, 10, 8, 10, 0.0F);
        this.crown = new ModelRenderer(this, 0, 18);
        this.crown.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.crown.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        this.cube1.addChild(this.entrance);
        this.cube1.addChild(this.crown);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.cube1.render(f5);
    }
}
