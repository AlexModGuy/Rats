package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelPiratBoat;
import com.github.alexthe666.rats.client.model.ModelPiratCannon;
import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

public class LayerPiratBoatSail extends LayerRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> {
    protected static final ModelPiratCannon MODEL_PIRAT_CANNON = new ModelPiratCannon();
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon.png");
    protected static final ResourceLocation TEXTURE_PIRATE_CANNON_FIRE = new ResourceLocation("rats:textures/entity/ratlantis/pirat_cannon_fire.png");
    private final IEntityRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> ratRenderer;

    public LayerPiratBoatSail(IEntityRenderer<EntityPiratBoat, ModelPiratBoat<EntityPiratBoat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityPiratBoat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.rotatef(180F, 1F, 0F, 0F);
        GlStateManager.rotatef(90F, 0F, 1F, 0F);
        GlStateManager.translatef(0F, -0.8F, -0.9F);
        GlStateManager.scalef(4F, 4F, 4F);
        Minecraft.getInstance().getItemRenderer().renderItem(EntityPiratBoat.BANNER, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.rotatef(-90F, 0F, 1F, 0F);
        GlStateManager.translatef(0, 0.1F, -0.6F);
        GlStateManager.scalef(0.75F, 0.75F, 0.75F);
        ratRenderer.bindTexture(TEXTURE_PIRATE_CANNON);
        MODEL_PIRAT_CANNON.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.popMatrix();

        if (rat.isFiring()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotatef(-90F, 0F, 1F, 0F);
            GlStateManager.translatef(0, 0.1F, -0.6F);
            GlStateManager.scalef(0.75F, 0.75F, 0.75F);
            GlStateManager.disableLighting();
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 61680.0F, 0.0F);
            ratRenderer.bindTexture(TEXTURE_PIRATE_CANNON_FIRE);
            MODEL_PIRAT_CANNON.render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
