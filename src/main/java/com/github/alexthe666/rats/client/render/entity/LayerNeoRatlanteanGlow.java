package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityNeoRatlantean;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerNeoRatlanteanGlow implements LayerRenderer<EntityNeoRatlantean> {
    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_glow.png");
    private final RenderNeoRatlantean ratRenderer;

    public LayerNeoRatlanteanGlow(RenderNeoRatlantean ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityNeoRatlantean rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ratRenderer.bindTexture(TEXTURE_EYES);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        GlStateManager.enableLighting();
        GlStateManager.disableCull();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.ratRenderer.setLightmap(rat);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
