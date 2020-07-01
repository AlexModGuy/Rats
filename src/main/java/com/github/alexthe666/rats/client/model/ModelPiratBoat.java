package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ModelPiratBoat <T extends EntityPiratBoat> extends SegmentedModel<T> {

    public ModelRenderer[] boatSides = new ModelRenderer[5];
    public ModelRenderer[] paddles = new ModelRenderer[2];
    public ModelRenderer noWater;

    public ModelPiratBoat() {
        this.boatSides[0] = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.boatSides[1] = (new ModelRenderer(this, 0, 19)).setTextureSize(128, 64);
        this.boatSides[2] = (new ModelRenderer(this, 0, 27)).setTextureSize(128, 64);
        this.boatSides[3] = (new ModelRenderer(this, 0, 35)).setTextureSize(128, 64);
        this.boatSides[4] = (new ModelRenderer(this, 0, 43)).setTextureSize(128, 64);
        int i = 32;
        int j = 6;
        int k = 20;
        int l = 4;
        int i1 = 28;
        this.boatSides[0].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
        this.boatSides[0].setRotationPoint(0.0F, 20.0F, 1.0F);
        this.boatSides[1].addBox(-13.0F, -7.0F, -1.0F, 18, 6, 2, 0.0F);
        this.boatSides[1].setRotationPoint(-15.0F, 21.0F, 4.0F);
        this.boatSides[2].addBox(-8.0F, -7.0F, -1.0F, 16, 6, 2, 0.0F);
        this.boatSides[2].setRotationPoint(15.0F, 21.0F, 0.0F);
        this.boatSides[3].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
        this.boatSides[3].setRotationPoint(0.0F, 21.0F, -9.0F);
        this.boatSides[4].addBox(-14.0F, -7.0F, -1.0F, 28, 6, 2, 0.0F);
        this.boatSides[4].setRotationPoint(0.0F, 21.0F, 9.0F);
        this.boatSides[0].rotateAngleX = ((float) Math.PI / 2F);
        this.boatSides[1].rotateAngleY = ((float) Math.PI * 3F / 2F);
        this.boatSides[2].rotateAngleY = ((float) Math.PI / 2F);
        this.boatSides[3].rotateAngleY = (float) Math.PI;
        this.paddles[0] = this.makePaddle(true);
        this.paddles[0].setRotationPoint(3.0F, 13.0F, 9.0F);
        this.paddles[1] = this.makePaddle(false);
        this.paddles[1].setRotationPoint(3.0F, 13.0F, -9.0F);
        this.paddles[1].rotateAngleY = (float) Math.PI;
        this.paddles[0].rotateAngleZ = 0.19634955F;
        this.paddles[1].rotateAngleZ = 0.19634955F;
        this.noWater = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 64);
        this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
        this.noWater.setRotationPoint(0.0F, 13.0F, 1.0F);
        this.noWater.rotateAngleX = ((float) Math.PI / 2F);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        renderPaddle(entityIn, 0, 1, limbSwing);
        renderPaddle(entityIn, 1, 1, limbSwing);
    }

    protected ModelRenderer makePaddle(boolean p_187056_1_) {
        ModelRenderer ModelRenderer = (new ModelRenderer(this, 62, p_187056_1_ ? 0 : 20)).setTextureSize(128, 64);
        int i = 20;
        int j = 7;
        int k = 6;
        float f = -5.0F;
        ModelRenderer.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 18);
        ModelRenderer.addBox(p_187056_1_ ? -1.001F : 0.001F, -3.0F, 8.0F, 1, 6, 7);
        return ModelRenderer;
    }

    protected void renderPaddle(EntityPiratBoat boat, int paddle, float scale, float limbSwing) {
        float f = limbSwing;
        ModelRenderer ModelRenderer = this.paddles[paddle];
        ModelRenderer.rotateAngleX = (float) MathHelper.clampedLerp(-1.0471975803375244D, -0.2617993950843811D, (double) ((MathHelper.sin(-f) + 1.0F) / 2.0F));
        ModelRenderer.rotateAngleY = (float) MathHelper.clampedLerp(-(Math.PI / 4D), (Math.PI / 4D), (double) ((MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F));

        if (paddle == 1) {
            ModelRenderer.rotateAngleY = (float) Math.PI - ModelRenderer.rotateAngleY;
        }
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.paddles[0], this.paddles[1], this.boatSides[0], this.boatSides[1], this.boatSides[2], this.boatSides[3], this.boatSides[4]);
    }
}
