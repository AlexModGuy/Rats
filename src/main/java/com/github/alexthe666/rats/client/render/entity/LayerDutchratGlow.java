package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelFlyingDutchrat;
import com.github.alexthe666.rats.server.entity.EntityDutchrat;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerDutchratGlow extends LayerRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> {
    private static final ResourceLocation GLOW_1 = new ResourceLocation("rats:textures/entity/ratlantis/dutchrat_glow_1.png");
    private static final ResourceLocation GLOW_2 = new ResourceLocation("rats:textures/entity/ratlantis/dutchrat_glow_2.png");
    private final IEntityRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> ratRenderer;

    public LayerDutchratGlow(IEntityRenderer<EntityDutchrat, ModelFlyingDutchrat<EntityDutchrat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    public void render(EntityDutchrat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        GL11.glPushMatrix();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        this.ratRenderer.bindTexture(GLOW_1);
        this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
        this.ratRenderer.func_217758_e(rat);

        GL11.glPushMatrix();
        this.ratRenderer.bindTexture(GLOW_2);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        GlStateManager.enableLighting();
        gamerenderer.setupFogColor(true);
        this.ratRenderer.getEntityModel().render(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        gamerenderer.setupFogColor(false);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.ratRenderer.func_217758_e(rat);
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GL11.glPopMatrix();
    }

    public boolean shouldCombineTextures() {
        return true;
    }

}
