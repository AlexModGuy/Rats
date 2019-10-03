package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityMarbleCheeseGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerCheeseGolemGlow implements LayerRenderer<EntityMarbleCheeseGolem> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem_glow.png");
    private final RenderMarbledCheeseGolem ratRenderer;

    public LayerCheeseGolemGlow(RenderMarbledCheeseGolem ratRendererIn) {
        this.ratRenderer = ratRendererIn;
    }

    public void doRenderLayer(EntityMarbleCheeseGolem rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.ratRenderer.bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthFunc(514);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        GlStateManager.enableLighting();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        this.ratRenderer.getMainModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        this.ratRenderer.setLightmap(rat);
        GlStateManager.disableBlend();
        GlStateManager.depthFunc(515);
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}