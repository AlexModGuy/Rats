package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGhostPirat extends RenderRat {

    private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat.png");

    public RenderGhostPirat() {
        super();
        this.addLayer(new LayerGhostPirat(this));
    }

    protected void renderModel(EntityRat rat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        GlStateManager.pushMatrix();
        super.renderModel(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GlStateManager.popMatrix();
    }

    protected void preRenderCallback(EntityRat rat, float partialTickTime) {
        super.preRenderCallback(rat, partialTickTime);
        this.shadowSize = 0.35F;
        GL11.glScaled(2.0F, 2.0F, 2.0F);
    }

    protected ResourceLocation getEntityTexture(EntityRat entity) {
        return BASE_TEXTURE;
    }

    private class LayerGhostPirat extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
        private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRenderer;
        private ResourceLocation GHOST_OVERLAY = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat_overlay.png");

        public LayerGhostPirat(IEntityRenderer<EntityRat, ModelRat<EntityRat>> renderGhostPirat) {
            super(renderGhostPirat);
            this.ratRenderer = renderGhostPirat;
        }

        public void render(EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            boolean flag = rat.isInvisible();
            GlStateManager.depthMask(!flag);
            this.bindTexture(GHOST_OVERLAY);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f = (float)rat.ticksExisted + partialTicks;
            GlStateManager.translatef(f * 0.01F, f * 0.01F, 0.0F);
            GlStateManager.matrixMode(5888);
            GlStateManager.enableBlend();
            float f1 = 0.5F;
            GlStateManager.color4f(0.5F, 0.5F, 0.5F, 0.8F);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            this.getEntityModel().setModelAttributes(this.ratRenderer.getEntityModel());
            GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
            gamerenderer.setupFogColor(true);
            this.ratRenderer.getEntityModel().renderNoWiskers(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            gamerenderer.setupFogColor(false);
            this.ratRenderer.func_217758_e(rat);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }
}