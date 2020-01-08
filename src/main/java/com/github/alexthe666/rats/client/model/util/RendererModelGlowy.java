package com.github.alexthe666.rats.client.model.util;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class RendererModelGlowy extends RendererModel {

    public RendererModelGlowy(Model model, String boxNameIn) {
        super(model, boxNameIn);
    }

    public RendererModelGlowy(Model model) {
        super(model);
    }

    public RendererModelGlowy(Model model, int texOffX, int texOffY) {
        super(model, texOffX, texOffY);
    }

    public void render(float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 61680.0F, 0.0F);
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        super.render(scale);
        gamerenderer.setupFogColor(false);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
