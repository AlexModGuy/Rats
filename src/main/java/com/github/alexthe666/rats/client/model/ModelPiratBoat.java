package com.github.alexthe666.rats.client.model;

import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPiratBoat <T extends Entity> extends EntityModel<T> {
    private final int patchList = GLAllocation.generateDisplayLists(1);
    public RendererModel[] boatSides = new RendererModel[5];
    public RendererModel[] paddles = new RendererModel[2];
    public RendererModel noWater;

    public ModelPiratBoat() {
        this.boatSides[0] = (new RendererModel(this, 0, 0)).setTextureSize(128, 64);
        this.boatSides[1] = (new RendererModel(this, 0, 19)).setTextureSize(128, 64);
        this.boatSides[2] = (new RendererModel(this, 0, 27)).setTextureSize(128, 64);
        this.boatSides[3] = (new RendererModel(this, 0, 35)).setTextureSize(128, 64);
        this.boatSides[4] = (new RendererModel(this, 0, 43)).setTextureSize(128, 64);
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
        this.noWater = (new RendererModel(this, 0, 0)).setTextureSize(128, 64);
        this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
        this.noWater.setRotationPoint(0.0F, 13.0F, 1.0F);
        this.noWater.rotateAngleX = ((float) Math.PI / 2F);
    }

    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
        EntityPiratBoat entityboat = (EntityPiratBoat) entityIn;
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }

        this.renderPaddle(entityboat, 0, scale, limbSwing);
        this.renderPaddle(entityboat, 1, scale, limbSwing);
    }

    public void renderMultipass(Entity entityIn, float partialTicks, float p_187054_3_, float p_187054_4_, float p_187054_5_, float p_187054_6_, float scale) {
        GlStateManager.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.colorMask(false, false, false, false);
        this.noWater.render(scale);
        GlStateManager.colorMask(true, true, true, true);
    }

    protected RendererModel makePaddle(boolean p_187056_1_) {
        RendererModel RendererModel = (new RendererModel(this, 62, p_187056_1_ ? 0 : 20)).setTextureSize(128, 64);
        int i = 20;
        int j = 7;
        int k = 6;
        float f = -5.0F;
        RendererModel.addBox(-1.0F, 0.0F, -5.0F, 2, 2, 18);
        RendererModel.addBox(p_187056_1_ ? -1.001F : 0.001F, -3.0F, 8.0F, 1, 6, 7);
        return RendererModel;
    }

    protected void renderPaddle(EntityPiratBoat boat, int paddle, float scale, float limbSwing) {
        float f = limbSwing;
        RendererModel RendererModel = this.paddles[paddle];
        RendererModel.rotateAngleX = (float) MathHelper.clampedLerp(-1.0471975803375244D, -0.2617993950843811D, (double) ((MathHelper.sin(-f) + 1.0F) / 2.0F));
        RendererModel.rotateAngleY = (float) MathHelper.clampedLerp(-(Math.PI / 4D), (Math.PI / 4D), (double) ((MathHelper.sin(-f + 1.0F) + 1.0F) / 2.0F));

        if (paddle == 1) {
            RendererModel.rotateAngleY = (float) Math.PI - RendererModel.rotateAngleY;
        }

        RendererModel.render(scale);
    }
}
