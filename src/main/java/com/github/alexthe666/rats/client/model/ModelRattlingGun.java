package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedRendererModel;
import net.minecraft.entity.Entity;

public class ModelRattlingGun<T extends Entity> extends AdvancedEntityModel<T> {
    public AdvancedRendererModel base1;
    public AdvancedRendererModel pivot;
    public AdvancedRendererModel backleg;
    public AdvancedRendererModel leftleg;
    public AdvancedRendererModel rightleg;
    public AdvancedRendererModel base2;
    public AdvancedRendererModel centerThingy;
    public AdvancedRendererModel behind;
    public AdvancedRendererModel gun1;
    public AdvancedRendererModel handle1;
    public AdvancedRendererModel seatConnector1;
    public AdvancedRendererModel handle2;
    public AdvancedRendererModel handle3;
    public AdvancedRendererModel seatConnector2;
    public AdvancedRendererModel seat;
    public AdvancedRendererModel seatBack;
    public AdvancedRendererModel barrel1;
    public AdvancedRendererModel barrel2;
    public AdvancedRendererModel barrel3;
    public AdvancedRendererModel barrel4;
    public AdvancedRendererModel barrel5;
    public AdvancedRendererModel gun2;
    public AdvancedRendererModel gun3;

    public ModelRattlingGun() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.barrel2 = new AdvancedRendererModel(this, 0, 31);
        this.barrel2.setRotationPoint(2.3F, 0.0F, -2.0F);
        this.barrel2.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
        this.barrel3 = new AdvancedRendererModel(this, 0, 31);
        this.barrel3.setRotationPoint(-2.3F, 0.0F, -2.0F);
        this.barrel3.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
        this.backleg = new AdvancedRendererModel(this, 56, 13);
        this.backleg.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.backleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
        this.setRotateAngle(backleg, 0.5235987755982988F, 0.0F, 0.0F);
        this.seatConnector2 = new AdvancedRendererModel(this, 26, 48);
        this.seatConnector2.setRotationPoint(0.0F, 0.5F, 8.0F);
        this.seatConnector2.addBox(-8.0F, -0.5F, 0.0F, 10, 1, 3, 0.0F);
        this.leftleg = new AdvancedRendererModel(this, 56, 13);
        this.leftleg.setRotationPoint(2.0F, 0.0F, -2.0F);
        this.leftleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
        this.setRotateAngle(leftleg, 0.5235987755982988F, 2.2689280275926285F, 0.0F);
        this.barrel5 = new AdvancedRendererModel(this, 0, 31);
        this.barrel5.setRotationPoint(0.0F, -2.3F, -2.0F);
        this.barrel5.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
        this.base1 = new AdvancedRendererModel(this, 0, 48);
        this.base1.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.base1.addBox(-2.0F, 0.0F, -2.0F, 4, 3, 4, 0.0F);
        this.seatBack = new AdvancedRendererModel(this, 0, 0);
        this.seatBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.seatBack.addBox(-3.0F, -8.0F, -0.5F, 6, 8, 1, 0.0F);
        this.handle2 = new AdvancedRendererModel(this, 42, 13);
        this.handle2.setRotationPoint(-5.0F, 0.0F, 0.0F);
        this.handle2.addBox(-1.0F, -1.0F, -1.0F, 1, 2, 6, 0.0F);
        this.gun3 = new AdvancedRendererModel(this, 0, 31);
        this.gun3.setRotationPoint(0.0F, 0.0F, -11.0F);
        this.gun3.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 1, 0.0F);
        this.base2 = new AdvancedRendererModel(this, 2, 24);
        this.base2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base2.addBox(-3.0F, -1.0F, -3.0F, 6, 1, 6, 0.0F);
        this.barrel4 = new AdvancedRendererModel(this, 0, 31);
        this.barrel4.setRotationPoint(0.0F, 2.3F, -2.0F);
        this.barrel4.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 16, 0.0F);
        this.seatConnector1 = new AdvancedRendererModel(this, 6, 48);
        this.seatConnector1.setRotationPoint(0.0F, 3.0F, 5.0F);
        this.seatConnector1.addBox(-2.5F, 0.0F, -2.0F, 5, 1, 10, 0.0F);
        this.centerThingy = new AdvancedRendererModel(this, 34, 31);
        this.centerThingy.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.centerThingy.addBox(-4.0F, -4.0F, -1.0F, 8, 8, 2, 0.0F);
        this.barrel1 = new AdvancedRendererModel(this, 0, 0);
        this.barrel1.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.barrel1.addBox(-1.5F, -1.5F, -18.0F, 3, 3, 18, 0.0F);
        this.handle3 = new AdvancedRendererModel(this, 51, 2);
        this.handle3.setRotationPoint(-1.0F, 0.0F, 4.0F);
        this.handle3.addBox(-3.0F, -0.5F, -0.5F, 3, 1, 1, 0.0F);
        this.gun1 = new AdvancedRendererModel(this, 18, 31);
        this.gun1.setRotationPoint(0.0F, 0.0F, -1.0F);
        this.gun1.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 2, 0.0F);
        this.seat = new AdvancedRendererModel(this, 40, 52);
        this.seat.setRotationPoint(-7.0F, -0.5F, 3.0F);
        this.seat.addBox(-2.5F, -1.0F, -6.0F, 5, 1, 6, 0.0F);
        this.behind = new AdvancedRendererModel(this, 32, 0);
        this.behind.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.behind.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 7, 0.0F);
        this.gun2 = new AdvancedRendererModel(this, 0, 31);
        this.gun2.setRotationPoint(0.0F, 0.0F, -15.0F);
        this.gun2.addBox(-3.0F, -3.0F, -1.0F, 6, 6, 1, 0.0F);
        this.pivot = new AdvancedRendererModel(this, 26, 21);
        this.pivot.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.pivot.addBox(-2.0F, -4.0F, -2.0F, 4, 6, 4, 0.0F);
        this.rightleg = new AdvancedRendererModel(this, 56, 13);
        this.rightleg.setRotationPoint(-2.0F, 0.0F, -2.0F);
        this.rightleg.addBox(-1.0F, 0.0F, -2.0F, 2, 15, 2, 0.0F);
        this.setRotateAngle(rightleg, 0.5235987755982988F, -2.2689280275926285F, 0.0F);
        this.handle1 = new AdvancedRendererModel(this, 51, 0);
        this.handle1.setRotationPoint(-3.0F, 0.0F, 2.0F);
        this.handle1.addBox(-5.0F, -0.5F, -0.5F, 5, 1, 1, 0.0F);
        this.gun1.addChild(this.barrel2);
        this.gun1.addChild(this.barrel3);
        this.base1.addChild(this.backleg);
        this.seatConnector1.addChild(this.seatConnector2);
        this.base1.addChild(this.leftleg);
        this.gun1.addChild(this.barrel5);
        this.seat.addChild(this.seatBack);
        this.handle1.addChild(this.handle2);
        this.gun1.addChild(this.gun3);
        this.base1.addChild(this.base2);
        this.gun1.addChild(this.barrel4);
        this.behind.addChild(this.seatConnector1);
        this.pivot.addChild(this.centerThingy);
        this.gun1.addChild(this.barrel1);
        this.handle2.addChild(this.handle3);
        this.centerThingy.addChild(this.gun1);
        this.seatConnector2.addChild(this.seat);
        this.centerThingy.addChild(this.behind);
        this.gun1.addChild(this.gun2);
        this.base1.addChild(this.rightleg);
        this.behind.addChild(this.handle1);
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.base1.render(f5);
        this.pivot.render(f5);
    }

    public void setRotateAngle(AdvancedRendererModel AdvancedRendererModel, float x, float y, float z) {
        AdvancedRendererModel.rotateAngleX = x;
        AdvancedRendererModel.rotateAngleY = y;
        AdvancedRendererModel.rotateAngleZ = z;
    }
}
