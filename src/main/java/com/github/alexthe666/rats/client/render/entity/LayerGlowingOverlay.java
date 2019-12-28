package com.github.alexthe666.rats.client.render.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

public class LayerGlowingOverlay<T extends LivingEntity> extends LayerRenderer<T, EntityModel<T>> {
    private final IEntityRenderer<T, EntityModel<T>> ratRenderer;
    private final ResourceLocation texture;

    public LayerGlowingOverlay(IEntityRenderer<T, EntityModel<T>> ratRendererIn, ResourceLocation texture) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
        this.texture = texture;
    }

    public void render(T rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ratRenderer.bindTexture(texture);
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
        return true;
    }
}
