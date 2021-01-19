package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageWheel;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatWheel<T extends Entity> extends AdvancedEntityModel<T> {
    public AdvancedModelBox axel;
    public AdvancedModelBox groundBaseL;
    public AdvancedModelBox groundBaseR;
    public AdvancedModelBox wheel1;
    public AdvancedModelBox wheel2;

    public ModelRatWheel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.wheel2 = new AdvancedModelBox(this, 0, 24);
        this.wheel2.setRotationPoint(0.0F, 5.0F, -5.0F);
        this.wheel2.addBox(-5.5F, 0.0F, -7.0F, 11, 10, 14, 0.0F);
        this.setRotateAngle(wheel2, 1.5707963267948966F, 0.0F, 0.0F);
        this.groundBaseL = new AdvancedModelBox(this, 0, 0);
        this.groundBaseL.mirror = true;
        this.groundBaseL.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.groundBaseL.addBox(6.5F, -5.0F, -1.5F, 1, 8, 3, 0.0F);
        this.wheel1 = new AdvancedModelBox(this, 0, 0);
        this.wheel1.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.wheel1.addBox(-5.5F, 0.0F, -7.0F, 11, 10, 14, 0.0F);
        this.axel = new AdvancedModelBox(this, 0, 50);
        this.axel.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.axel.addBox(-8.0F, -1.0F, -1.0F, 16, 2, 2, 0.0F);
        this.groundBaseR = new AdvancedModelBox(this, 0, 0);
        this.groundBaseR.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.groundBaseR.addBox(-7.5F, -5.0F, -1.5F, 1, 8, 3, 0.0F);
        this.wheel1.addChild(this.wheel2);
        this.axel.addChild(this.wheel1);
        this.updateDefaultPose();
    }

    public void animate(TileEntityRatCageWheel wheel, float partialTicks){
        this.resetToDefaultPose();
        float rot = wheel.prevWheelRot + (wheel.wheelRot - wheel.prevWheelRot) * partialTicks;
        axel.rotateAngleX = (float) Math.toRadians(rot);
        wheel1.setScale(0.9F, 0.9F, 0.9F);
        wheel1.setShouldScaleChildren(true);
    }

    @Override
    public void setRotationAngles(T t, float v, float v1, float v2, float v3, float v4) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(axel, groundBaseL, groundBaseR);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(axel, groundBaseL, groundBaseR, wheel1, wheel2);
    }

    public void setRotateAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}
