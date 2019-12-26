package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;

public class ModelRatTrap extends AdvancedEntityModel {
    public AdvancedRendererModel bottom;
    public AdvancedRendererModel hingeMain;
    public AdvancedRendererModel spring;
    public AdvancedRendererModel hingeTop;
    public AdvancedRendererModel hinge2;

    public ModelRatTrap() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.hingeTop = new AdvancedRendererModel(this, 0, 27);
        this.hingeTop.setRotationPoint(-0.5F, 0.0F, 7.0F);
        this.hingeTop.addBox(-9.0F, -0.5F, 0.0F, 10, 1, 1, 0.0F);
        this.hinge2 = new AdvancedRendererModel(this, 0, 19);
        this.hinge2.setRotationPoint(-9.0F, 0.0F, 0.0F);
        this.hinge2.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 7, 0.0F);
        this.hingeMain = new AdvancedRendererModel(this, 0, 19);
        this.hingeMain.setRotationPoint(4.5F, 22.0F, 0.0F);
        this.hingeMain.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 7, 0.0F);
        this.spring = new AdvancedRendererModel(this, 0, 16);
        this.spring.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.spring.addBox(-4.0F, -1.0F, 0.0F, 8, 1, 2, 0.0F);
        this.bottom = new AdvancedRendererModel(this, 0, 0);
        this.bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.bottom.addBox(-4.0F, -2.0F, -7.0F, 8, 2, 14, 0.0F);
        this.hingeMain.addChild(this.hingeTop);
        this.hingeMain.addChild(this.hinge2);
        this.bottom.addChild(this.spring);
        this.updateDefaultPose();
    }

    public void render(float f5, float shutProgress) {
        this.resetToDefaultPose();
        this.hingeMain.rotateAngleX += (float) (shutProgress * Math.PI / 6.0F);
        this.hingeMain.render(f5);
        this.bottom.render(f5);

    }
}
