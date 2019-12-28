package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;

public class ModelCube <T extends Entity> extends EntityModel<T> {
    public RendererModel head;

    public ModelCube() {
        this(0.0F);
    }

    public ModelCube(float scale) {
        this.head = new RendererModel(this, 0, 0);
        this.head.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, scale);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.head.render(scale);
    }
}