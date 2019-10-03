package com.github.alexthe666.rats.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCube extends ModelBase {
    public ModelRenderer head;

    public ModelCube() {
        this(0.0F);
    }

    public ModelCube(float scale) {
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, scale);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        this.head.render(scale);
    }
}