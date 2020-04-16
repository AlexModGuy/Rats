package com.github.alexthe666.rats.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.EntityModel;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatIgloo <T extends Entity> extends SegmentedModel<T> {
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
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(cube1);
    }
}
