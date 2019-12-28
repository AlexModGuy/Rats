package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelBlackDeath;
import com.github.alexthe666.rats.server.entity.EntityBlackDeath;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerBlackDeathMask extends LayerRenderer<EntityBlackDeath, ModelBlackDeath<EntityBlackDeath>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/black_death_overlay.png");
    private final IEntityRenderer<EntityBlackDeath, ModelBlackDeath<EntityBlackDeath>> ratRenderer;

    public LayerBlackDeathMask(IEntityRenderer<EntityBlackDeath, ModelBlackDeath<EntityBlackDeath>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityBlackDeath rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ratRenderer.bindTexture(TEXTURE);
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

    public boolean shouldCombineTextures() {
        return false;
    }

}
