package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAutoCurdler <T extends Entity> extends AdvancedEntityModel<T> {
    public ModelRenderer bottom;
    public ModelRenderer milkTank;

    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of();
    }

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

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.bottom);
    }
}
