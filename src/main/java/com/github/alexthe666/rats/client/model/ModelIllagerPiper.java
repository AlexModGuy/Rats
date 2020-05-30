package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.IllagerModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class ModelIllagerPiper<T extends EntityIllagerPiper> extends SegmentedModel<T> implements IHasArm, IHasHead  {
    public ModelRenderer hat;
    public ModelRenderer hat_feather;
    public ModelRenderer hatTop;
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer arms;
    private ModelRenderer lvt_5_1_;
    private final ModelRenderer field_217143_g;
    private final ModelRenderer field_217144_h;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;

    public ModelIllagerPiper() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.head = (new ModelRenderer(this)).setTextureSize(64, 128);
        this.head.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0);
        this.hat = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 128);
        this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0 + 0.45F);
        this.head.addChild(this.hat);
        this.hat.showModel = false;
        lvt_5_1_ = (new ModelRenderer(this)).setTextureSize(64, 128);
        lvt_5_1_.setRotationPoint(0.0F, 0 - 2.0F, 0.0F);
        lvt_5_1_.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0);
        this.head.addChild(lvt_5_1_);
        this.body = (new ModelRenderer(this)).setTextureSize(64, 128);
        this.body.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
        this.body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0);
        this.body.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0 + 0.5F);
        this.arms = (new ModelRenderer(this)).setTextureSize(64, 128);
        this.arms.setRotationPoint(0.0F, 0.0F + 0 + 2.0F, 0.0F);
        this.arms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0);
        ModelRenderer lvt_6_1_ = (new ModelRenderer(this, 44, 22)).setTextureSize(64, 128);
        lvt_6_1_.mirror = true;
        lvt_6_1_.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0);
        this.arms.addChild(lvt_6_1_);
        this.arms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0);
        this.field_217143_g = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 128);
        this.field_217143_g.setRotationPoint(-2.0F, 12.0F + 0, 0.0F);
        this.field_217143_g.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
        this.field_217144_h = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 128);
        this.field_217144_h.mirror = true;
        this.field_217144_h.setRotationPoint(2.0F, 12.0F + 0, 0.0F);
        this.field_217144_h.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
        this.rightArm = (new ModelRenderer(this, 40, 46)).setTextureSize(64, 128);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
        this.rightArm.setRotationPoint(-5.0F, 2.0F + 0, 0.0F);
        this.leftArm = (new ModelRenderer(this, 40, 46)).setTextureSize(64, 128);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0);
        this.leftArm.setRotationPoint(5.0F, 2.0F + 0, 0.0F);
        this.hat_feather = new ModelRenderer(this, 0, 72);
        this.hat_feather.setRotationPoint(-2.5F, -5.0F, 0.0F);
        this.hat_feather.addBox(0.0F, -9.0F, 0.0F, 0, 9, 9, 0.0F);
        this.setRotateAngle(hat_feather, 0.0F, 0.0F, -0.3490658503988659F);
        this.hat = new ModelRenderer(this, 0, 64);
        this.hat.setRotationPoint(0.0F, -6.0F, 0.0F);
        this.hat.addBox(-4.5F, -5.0F, -6.0F, 9, 4, 12, 0.0F);
        this.setRotateAngle(hat, 0.17453292519943295F, 0.0F, 0.0F);
        this.hatTop = new ModelRenderer(this, 0, 118);
        this.hatTop.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.hatTop.addBox(-2.5F, -2.0F, -4.0F, 5, 2, 8, 0.0F);
        this.head.addChild(this.hat);
        this.hat.addChild(this.hat_feather);
        this.hat.addChild(this.hatTop);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head, body, field_217143_g, field_217144_h, rightArm, leftArm);
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelRenderer func_205062_a() {
        return this.hat;
    }

    public ModelRenderer getModelHead() {
        return this.head;
    }

    public void translateHand(HandSide p_225599_1_, MatrixStack p_225599_2_) {
        this.getArm(p_225599_1_).translateRotate(p_225599_2_);
        p_225599_2_.translate(0.15F, 0.2F, 0F);
        p_225599_2_.rotate(Vector3f.ZP.rotationDegrees(15));
    }

    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.arms.rotationPointY = 3.0F;
        this.arms.rotationPointZ = -1.0F;
        this.arms.rotateAngleX = -0.75F;
        if (this.isSitting) {
            this.rightArm.rotateAngleX = -0.62831855F;
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = -0.62831855F;
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.field_217143_g.rotateAngleX = -1.4137167F;
            this.field_217143_g.rotateAngleY = 0.31415927F;
            this.field_217143_g.rotateAngleZ = 0.07853982F;
            this.field_217144_h.rotateAngleX = -1.4137167F;
            this.field_217144_h.rotateAngleY = -0.31415927F;
            this.field_217144_h.rotateAngleZ = -0.07853982F;
        } else {
            this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.rotateAngleY = 0.0F;
            this.rightArm.rotateAngleZ = 0.0F;
            this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.rotateAngleY = 0.0F;
            this.leftArm.rotateAngleZ = 0.0F;
            this.field_217143_g.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.field_217143_g.rotateAngleY = 0.0F;
            this.field_217143_g.rotateAngleZ = 0.0F;
            this.field_217144_h.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount * 0.5F;
            this.field_217144_h.rotateAngleY = 0.0F;
            this.field_217144_h.rotateAngleZ = 0.0F;
        }
        if (((EntityIllagerPiper) entityIn).getHeldItem(Hand.MAIN_HAND).getItem() == RatsItemRegistry.RAT_FLUTE) {
            float f = 0.01F * (float) (entityIn.getEntityId() % 10);
            lvt_5_1_.rotateAngleY = 0.0F;
            lvt_5_1_.rotationPointZ = -2.7F;
            lvt_5_1_.rotationPointY = 1.5F;
            lvt_5_1_.rotateAngleZ = MathHelper.cos((float) entityIn.ticksExisted * f) * 2.5F * 0.017453292F;
            lvt_5_1_.rotateAngleX = -1.2F;
            this.rightArm.rotateAngleY = -0.3F + this.head.rotateAngleY;
            this.leftArm.rotateAngleY = 0.6F + this.head.rotateAngleY;
            this.rightArm.rotateAngleX = -1.5707964F + this.head.rotateAngleX + 0.1F;
            this.leftArm.rotateAngleX = -1.5F + this.head.rotateAngleX;
        }
    }

}
