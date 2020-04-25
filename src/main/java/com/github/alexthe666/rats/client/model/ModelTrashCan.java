package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.server.entity.tile.TileEntityTrashCan;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTrashCan<T extends Entity> extends AdvancedEntityModel<T> {
    public AdvancedModelBox can;
    public AdvancedModelBox lid;
    public AdvancedModelBox trash;
    public AdvancedModelBox handle1;
    public AdvancedModelBox handle2;
    public AdvancedModelBox handle3;

    public ModelTrashCan() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.lid = new AdvancedModelBox(this, 0, 32);
        this.lid.setRotationPoint(0.0F, 6.0F, 7.0F);
        this.lid.addBox(-8.0F, -2.0F, -15.0F, 16, 2, 16, 0.0F);
        this.handle1 = new AdvancedModelBox(this, 0, 0);
        this.handle1.setRotationPoint(0.0F, -4.0F, -7.0F);
        this.handle1.addBox(-2.0F, -0.5F, -0.5F, 4, 1, 1, 0.0F);
        this.handle3 = new AdvancedModelBox(this, 0, 4);
        this.handle3.setRotationPoint(2.0F, 0.0F, 0.0F);
        this.handle3.addBox(-1.0F, 0.5F, -0.5F, 1, 2, 1, 0.0F);
        this.can = new AdvancedModelBox(this, 0, 0);
        this.can.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.can.addBox(-7.0F, -18.0F, -7.0F, 14, 18, 14, 0.0F);
        this.trash = new AdvancedModelBox(this, 0, 50);
        this.trash.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.trash.addBox(-7.0F, 0.0F, -7.0F, 14, 0, 14, 0.0F);
        this.handle2 = new AdvancedModelBox(this, 0, 4);
        this.handle2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handle2.addBox(-2.0F, 0.5F, -0.5F, 1, 2, 1, 0.0F);
        this.lid.addChild(this.handle1);
        this.handle1.addChild(this.handle3);
        this.handle1.addChild(this.handle2);
        this.updateDefaultPose();
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(lid, can, trash);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(lid, can, trash, handle1, handle2, handle3);
    }

    public void animate(TileEntityTrashCan trashCan) {
        this.resetToDefaultPose();
        if(trashCan.trashStored <= 0){
            this.trash.showModel = false;
            this.trash.rotationPointY = 24;
        }else{
            this.trash.showModel = true;
            this.trash.rotationPointY = 24 - (2.5F * trashCan.trashStored);
        }
        float openProgress = trashCan.prevLidProgress + (trashCan.lidProgress - trashCan.prevLidProgress) * Minecraft.getInstance().getRenderPartialTicks();
        lid.rotateAngleX += (float)Math.toRadians(-70D * (openProgress / 20F));
    }
}
