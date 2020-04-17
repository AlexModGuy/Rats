package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPiratCannon<T extends Entity> extends AdvancedEntityModel<T>{
    public AdvancedModelBox base;
    public AdvancedModelBox middle;
    public AdvancedModelBox backCannon;
    public AdvancedModelBox midCannon;
    public AdvancedModelBox whick;
    public AdvancedModelBox frontCannon;

    public ModelPiratCannon() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.backCannon = new AdvancedModelBox(this, 0, 16);
        this.backCannon.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.backCannon.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 12, 0.0F);
        this.midCannon = new AdvancedModelBox(this, 0, 36);
        this.midCannon.setRotationPoint(0.0F, 0.0F, -9.0F);
        this.midCannon.addBox(-3.0F, -3.0F, -6.0F, 6, 6, 10, 0.0F);
        this.middle = new AdvancedModelBox(this, 40, 0);
        this.middle.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.middle.addBox(-2.0F, -10.0F, -2.5F, 4, 10, 5, 0.0F);
        this.frontCannon = new AdvancedModelBox(this, 0, 52);
        this.frontCannon.setRotationPoint(0.0F, 0.0F, -6.0F);
        this.frontCannon.addBox(-4.0F, -4.0F, -2.0F, 8, 8, 2, 0.0F);
        this.base = new AdvancedModelBox(this, 0, 0);
        this.base.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.base.addBox(-5.0F, -2.0F, -5.0F, 10, 2, 10, 0.0F);
        this.whick = new AdvancedModelBox(this, 0, 0);
        this.whick.setRotationPoint(0.0F, -4.0F, 4.0F);
        this.whick.addBox(0.0F, -4.0F, -2.0F, 0, 4, 4, 0.0F);
        this.middle.addChild(this.backCannon);
        this.backCannon.addChild(this.midCannon);
        this.base.addChild(this.middle);
        this.midCannon.addChild(this.frontCannon);
        this.backCannon.addChild(this.whick);
        this.updateDefaultPose();
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(base);
    }

    public void setRotateAngle(AdvancedModelBox ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.base, this.middle, this.backCannon, this.frontCannon, this.midCannon, this.whick);
    }
}
