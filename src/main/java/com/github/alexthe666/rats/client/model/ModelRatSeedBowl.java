package com.github.alexthe666.rats.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatSeedBowl <T extends Entity> extends SegmentedModel<T> {
    public ModelRenderer block;

    public ModelRatSeedBowl() {
        this.textureWidth = 32;
        this.textureHeight = 16;
        this.block = new ModelRenderer(this, 0, 0);
        this.block.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.block.addBox(-2.5F, -2.0F, -2.5F, 5, 2, 5, 0.0F);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.block);
    }
}
