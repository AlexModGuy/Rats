package com.github.alexthe666.rats.client.model;

import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;

public class ModelPlagueDoctor<T extends AbstractVillagerEntity> extends VillagerModel<T> {

    public ModelRenderer plagueMaskNose;
    public ModelRenderer brim;
    public ModelRenderer plagueMaskNose_1;

    public ModelPlagueDoctor(float scale) {
        super(scale, 64, 128);
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.plagueMaskNose_1 = new ModelRenderer(this, 16, 87);
        this.plagueMaskNose_1.setRotationPoint(0.0F, 0.0F, -3.5F);
        this.plagueMaskNose_1.addBox(-1.0F, -1.0F, -3.0F, 2, 2, 4, 0.1F);
        this.setRotateAngle(plagueMaskNose_1, 0.17453292519943295F, 0.0F, 0.0F);
        this.plagueMaskNose = new ModelRenderer(this, 0, 87);
        this.plagueMaskNose.setRotationPoint(0.0F, -2.0F, -5.0F);
        this.plagueMaskNose.addBox(-1.5F, -1.5F, -3.0F, 3, 3, 5, 0.1F);
        this.setRotateAngle(plagueMaskNose, 0.17453292519943295F, 0.0F, 0.0F);
        this.brim = new ModelRenderer(this, -14, 95);
        this.brim.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.brim.addBox(-7.0F, 1.0F, -7.0F, 14, 0, 14, 0.1F);
        this.plagueMaskNose.addChild(this.plagueMaskNose_1);
        this.villagerHead.addChild(this.plagueMaskNose);
        this.villagerHead.addChild(this.brim);
    }


    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}
