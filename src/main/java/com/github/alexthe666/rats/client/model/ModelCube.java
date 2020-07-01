package com.github.alexthe666.rats.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCube <T extends Entity> extends SegmentedModel<T> {
    public ModelRenderer head;

    public ModelCube() {
        this(0.0F);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head);
    }

    public ModelCube(float scale) {
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, scale);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
    }
}