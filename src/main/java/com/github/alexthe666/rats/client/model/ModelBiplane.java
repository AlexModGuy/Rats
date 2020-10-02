package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatBaronPlane;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatBiplaneMount;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;

public class ModelBiplane<T extends MobEntity> extends AdvancedEntityModel<T> {
    public AdvancedModelBox body1;
    public AdvancedModelBox body2;
    public AdvancedModelBox cabin;
    public AdvancedModelBox wings2;
    public AdvancedModelBox wings3;
    public AdvancedModelBox propellerBase;
    public AdvancedModelBox legLeft;
    public AdvancedModelBox legRight;
    public AdvancedModelBox wheelsCenterThing;
    public AdvancedModelBox body3;
    public AdvancedModelBox fin1;
    public AdvancedModelBox fin2;
    public AdvancedModelBox backThing;
    public AdvancedModelBox wings1;
    public AdvancedModelBox wing1StrutsLeft;
    public AdvancedModelBox wing1StrutsRight;
    public AdvancedModelBox gun1;
    public AdvancedModelBox gun2;
    public AdvancedModelBox wing2StrutsLeft;
    public AdvancedModelBox wing2StrutsRight;
    public AdvancedModelBox propellerNode;
    public AdvancedModelBox propeller;
    public AdvancedModelBox wheel1;
    public AdvancedModelBox wheel2;

    public ModelBiplane() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.wheelsCenterThing = new AdvancedModelBox(this, 133, 0);
        this.wheelsCenterThing.setRotationPoint(0.0F, 14.0F, 0.0F);
        this.wheelsCenterThing.addBox(-15.0F, 0.0F, -2.0F, 30, 2, 7, 0.0F);
        this.propellerNode = new AdvancedModelBox(this, 0, 0);
        this.propellerNode.setRotationPoint(0.0F, 0.0F, -5.0F);
        this.propellerNode.addBox(-1.0F, -1.0F, -2.0F, 2, 2, 2, 0.0F);
        this.cabin = new AdvancedModelBox(this, 0, 4);
        this.cabin.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.cabin.addBox(-2.5F, -2.5F, -2.0F, 5, 5, 5, 0.0F);
        this.backThing = new AdvancedModelBox(this, 221, 0);
        this.backThing.setRotationPoint(0.0F, 4.0F, 14.0F);
        this.backThing.addBox(0.0F, 0.0F, -5.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(backThing, 2.321986036853256F, 0.0F, 0.0F);
        this.wings1 = new AdvancedModelBox(this, 0, 78);
        this.wings1.setRotationPoint(0.0F, 6.0F, -3.0F);
        this.wings1.addBox(-45.0F, 0.0F, 0.0F, 90, 1, 20, 0.0F);
        this.gun2 = new AdvancedModelBox(this, 200, 23);
        this.gun2.setRotationPoint(-9.0F, -1.5F, 8.0F);
        this.gun2.addBox(-1.5F, -1.5F, -15.0F, 3, 3, 15, 0.0F);
        this.fin1 = new AdvancedModelBox(this, 134, 9);
        this.fin1.setRotationPoint(0.0F, 2.0F, 12.0F);
        this.fin1.addBox(-11.0F, 0.0F, 0.0F, 22, 1, 14, 0.0F);
        this.wheel1 = new AdvancedModelBox(this, 0, 36);
        this.wheel1.setRotationPoint(15.0F, 1.0F, 0.0F);
        this.wheel1.addBox(0.0F, -4.0F, -4.0F, 2, 8, 8, 0.0F);
        this.legLeft = new AdvancedModelBox(this, 113, 0);
        this.legLeft.setRotationPoint(5.0F, 5.0F, 0.0F);
        this.legLeft.addBox(-1.0F, 0.0F, -1.5F, 2, 12, 3, 0.0F);
        this.setRotateAngle(legLeft, 0.0F, 0.0F, -0.6108652381980153F);
        this.propeller = new AdvancedModelBox(this, 0, 232);
        this.propeller.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.propeller.addBox(-10.0F, -10.0F, -1.0F, 20, 20, 0, 0.0F);
        this.wheel2 = new AdvancedModelBox(this, 0, 36);
        this.wheel2.setRotationPoint(-15.0F, 1.0F, 0.0F);
        this.wheel2.addBox(-2.0F, -4.0F, -4.0F, 2, 8, 8, 0.0F);
        this.fin2 = new AdvancedModelBox(this, 224, 11);
        this.fin2.setRotationPoint(-0.5F, 1.0F, 15.0F);
        this.fin2.addBox(0.0F, -11.0F, 0.0F, 1, 12, 12, 0.0F);
        this.wing1StrutsLeft = new AdvancedModelBox(this, 0, 200);
        this.wing1StrutsLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing1StrutsLeft.addBox(11.0F, -17.0F, 2.5F, 25, 17, 15, 0.0F);
        this.wing2StrutsLeft = new AdvancedModelBox(this, 0, 100);
        this.wing2StrutsLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing2StrutsLeft.addBox(11.0F, -17.0F, 2.5F, 25, 17, 15, 0.0F);
        this.wing2StrutsRight = new AdvancedModelBox(this, 0, 100);
        this.wing2StrutsRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing2StrutsRight.addBox(-36.0F, -17.0F, 2.5F, 25, 17, 15, 0.0F);
        this.body2 = new AdvancedModelBox(this, 72, 0);
        this.body2.setRotationPoint(0.0F, 1.0F, 19.0F);
        this.body2.addBox(-5.0F, -4.5F, 0.0F, 10, 9, 21, 0.0F);
        this.gun1 = new AdvancedModelBox(this, 200, 23);
        this.gun1.setRotationPoint(9.0F, -1.5F, 8.0F);
        this.gun1.addBox(-1.5F, -1.5F, -15.0F, 3, 3, 15, 0.0F);
        this.body1 = new AdvancedModelBox(this, 0, 0);
        this.body1.setRotationPoint(0.0F, 4.0F, -6.0F);
        this.body1.addBox(-5.5F, -5.5F, -6.0F, 11, 11, 25, 0.0F);
        this.legRight = new AdvancedModelBox(this, 123, 0);
        this.legRight.setRotationPoint(-5.0F, 5.0F, 0.0F);
        this.legRight.addBox(-1.0F, 0.0F, -1.5F, 2, 12, 3, 0.0F);
        this.setRotateAngle(legRight, 0.0F, 0.0F, 0.6108652381980153F);
        this.wings2 = new AdvancedModelBox(this, 0, 36);
        this.wings2.setRotationPoint(0.0F, -15.0F, -3.0F);
        this.wings2.addBox(-45.0F, 0.0F, 0.0F, 90, 1, 20, 0.0F);
        this.wings3 = new AdvancedModelBox(this, 0, 57);
        this.wings3.setRotationPoint(0.0F, -33.0F, -3.0F);
        this.wings3.addBox(-45.0F, 0.0F, 0.0F, 90, 1, 20, 0.0F);
        this.propellerBase = new AdvancedModelBox(this, 47, 0);
        this.propellerBase.setRotationPoint(0.0F, 0.0F, -7.0F);
        this.propellerBase.addBox(-6.5F, -6.5F, -5.0F, 13, 13, 8, 0.0F);
        this.body3 = new AdvancedModelBox(this, 192, 0);
        this.body3.setRotationPoint(0.0F, 0.5F, 21.0F);
        this.body3.addBox(-3.5F, -4.0F, 0.0F, 7, 8, 15, 0.0F);
        this.wing1StrutsRight = new AdvancedModelBox(this, 0, 200);
        this.wing1StrutsRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing1StrutsRight.addBox(-36.0F, -17.0F, 2.5F, 25, 17, 15, 0.0F);
        this.body1.addChild(this.wheelsCenterThing);
        this.propellerBase.addChild(this.propellerNode);
        this.body1.addChild(this.cabin);
        this.body3.addChild(this.backThing);
        this.cabin.addChild(this.wings1);
        this.wings1.addChild(this.gun2);
        this.body3.addChild(this.fin1);
        this.wheelsCenterThing.addChild(this.wheel1);
        this.body1.addChild(this.legLeft);
        this.propellerNode.addChild(this.propeller);
        this.wheelsCenterThing.addChild(this.wheel2);
        this.body3.addChild(this.fin2);
        this.wings1.addChild(this.wing1StrutsLeft);
        this.wings2.addChild(this.wing2StrutsLeft);
        this.wings2.addChild(this.wing2StrutsRight);
        this.body1.addChild(this.body2);
        this.wings1.addChild(this.gun1);
        this.body1.addChild(this.legRight);
        this.body1.addChild(this.wings2);
        this.body1.addChild(this.wings3);
        this.body1.addChild(this.propellerBase);
        this.body2.addChild(this.body3);
        this.wings1.addChild(this.wing1StrutsRight);
        this.updateDefaultPose();
    }

    public void setRotateAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(
                body1,
                body2,
                cabin,
                wings2,
                wings3,
                propellerBase,
                legLeft,
                legRight,
                wheelsCenterThing,
                body3,
                fin1,
                fin2,
                backThing,
                wings1,
                wing1StrutsLeft,
                wing1StrutsRight,
                gun1,
                gun2,
                wing2StrutsLeft,
                wing2StrutsRight,
                propellerNode,
                propeller,
                wheel1,
                wheel2
        );
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body1);
    }

    @Override
    public void setRotationAngles(T t, float v, float v1, float v2, float v3, float v4) {
        this.resetToDefaultPose();
        if(!t.func_233570_aj_() && t instanceof EntityRatBaronPlane){
            EntityRatBaronPlane plane = (EntityRatBaronPlane)t;
            plane.roll_buffer.applyChainFlapBuffer(body1);
            plane.pitch_buffer.applyChainWaveBuffer(body1);
            float f7 = plane.prevPlanePitch + (plane.getPlanePitch() - plane.prevPlanePitch) * v3;
            this.body1.rotateAngleX = (float)Math.toRadians(f7);
        }
        if(!t.func_233570_aj_() && t instanceof EntityRatBiplaneMount){
            EntityRatBiplaneMount plane = (EntityRatBiplaneMount)t;
            plane.roll_buffer.applyChainFlapBuffer(body1);
            plane.pitch_buffer.applyChainWaveBuffer(body1);
            float f7 = plane.prevPlanePitch + (plane.getPlanePitch() - plane.prevPlanePitch) * v3;
            this.body1.rotateAngleX = (float)Math.toRadians(f7);
        }
        this.propeller.rotateAngleZ = v2 * 1.5F;

    }
}
