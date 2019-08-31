package com.github.alexthe666.rats.client.model;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRatBreedingLantern extends AdvancedModelBase {
    public AdvancedModelRenderer top;
    public AdvancedModelRenderer connector1;
    public AdvancedModelRenderer connector2;
    public AdvancedModelRenderer connector3;
    public AdvancedModelRenderer heart1;
    public AdvancedModelRenderer heart2;
    public AdvancedModelRenderer heart3;
    public AdvancedModelRenderer heart6;
    public AdvancedModelRenderer heart4;
    public AdvancedModelRenderer heart5;

    public ModelRatBreedingLantern() {
        this.textureWidth = 16;
        this.textureHeight = 32;
        this.top = new AdvancedModelRenderer(this, 0, 0);
        this.top.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.top.addBox(-2.0F, 1.0F, -2.0F, 4, 1, 4, 0.0F);
        this.connector2 = new AdvancedModelRenderer(this, 0, 0);
        this.connector2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.connector2.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.heart1 = new AdvancedModelRenderer(this, 0, 5);
        this.heart1.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.heart1.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.heart4 = new AdvancedModelRenderer(this, 0, 20);
        this.heart4.setRotationPoint(0.0F, 1.0F, 0.5F);
        this.heart4.addBox(0.0F, 0.0F, -1.0F, 2, 3, 1, 0.0F);
        this.connector3 = new AdvancedModelRenderer(this, 0, 0);
        this.connector3.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.connector3.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.connector1 = new AdvancedModelRenderer(this, 0, 0);
        this.connector1.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.connector1.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1, 0.0F);
        this.heart3 = new AdvancedModelRenderer(this, 0, 15);
        this.heart3.setRotationPoint(-1.5F, 1.0F, 0.0F);
        this.heart3.addBox(-3.0F, -2.0F, -1.0F, 3, 3, 2, 0.0F);
        this.heart6 = new AdvancedModelRenderer(this, 0, 24);
        this.heart6.setRotationPoint(0.0F, 6.0F, 0.0F);
        this.heart6.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2, 0.0F);
        this.heart5 = new AdvancedModelRenderer(this, 0, 20);
        this.heart5.mirror = true;
        this.heart5.setRotationPoint(0.0F, 1.0F, 0.5F);
        this.heart5.addBox(-2.0F, 0.0F, -1.0F, 2, 3, 1, 0.0F);
        this.heart2 = new AdvancedModelRenderer(this, 0, 15);
        this.heart2.mirror = true;
        this.heart2.setRotationPoint(1.5F, 1.0F, 0.0F);
        this.heart2.addBox(0.0F, -2.0F, -1.0F, 3, 3, 2, 0.0F);
        this.connector1.addChild(this.connector2);
        this.connector3.addChild(this.heart1);
        this.heart2.addChild(this.heart4);
        this.connector2.addChild(this.connector3);
        this.top.addChild(this.connector1);
        this.heart1.addChild(this.heart3);
        this.heart1.addChild(this.heart6);
        this.heart3.addChild(this.heart5);
        this.heart1.addChild(this.heart2);
        this.updateDefaultPose();
    }
    public void render(float f5) {
        this.resetToDefaultPose();
        this.top.render(f5);
    }
}
