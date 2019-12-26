package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

public class ModelRatSeedBowl extends EntityModel {
    public RendererModel block;

    public ModelRatSeedBowl() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.block = new RendererModel(this, 0, 0);
        this.block.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.block.addBox(-2.5F, -2.0F, -2.5F, 5, 2, 5, 0.0F);
    }

    public void render(float f5) {
        this.block.render(f5);
    }
}
