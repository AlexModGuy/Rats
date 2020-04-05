package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanRatbot;
import com.github.alexthe666.rats.server.entity.EntityRatlanteanRatbot;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerRatbotEyes extends LayerRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> {
    private static final ResourceLocation TEXTURE_EYES_0 = new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_0.png");
    private static final ResourceLocation TEXTURE_EYES_1 = new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_1.png");
    private static final ResourceLocation TEXTURE_EYES_2 = new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_2.png");
    private static final ResourceLocation TEXTURE_EYES_3 = new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_3.png");
    private final IEntityRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> ratRenderer;

    public LayerRatbotEyes(IEntityRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityRatlanteanRatbot rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ratRenderer.bindTexture(getTextureForTick(rat.ticksExisted * 3));
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthFunc(514);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        GlStateManager.enableLighting();
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        gamerenderer.setupFogColor(false);
        this.ratRenderer.func_217758_e(rat);
        GlStateManager.disableBlend();
        GlStateManager.depthFunc(515);
    }

    private ResourceLocation getTextureForTick(int ticksExisted) {
        int tickCap = ticksExisted % 40;
        if(tickCap > 19){
            tickCap = tickCap - 20;
            if (tickCap > 15) {
                return TEXTURE_EYES_0;
            } else if (tickCap > 10) {
                return TEXTURE_EYES_1;
            } else if (tickCap > 5) {
                return TEXTURE_EYES_2;
            } else {
                return TEXTURE_EYES_3;
            }
        }else{
            if (tickCap > 15) {
                return TEXTURE_EYES_3;
            } else if (tickCap > 10) {
                return TEXTURE_EYES_2;
            } else if (tickCap > 5) {
                return TEXTURE_EYES_1;
            } else {
                return TEXTURE_EYES_0;
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
