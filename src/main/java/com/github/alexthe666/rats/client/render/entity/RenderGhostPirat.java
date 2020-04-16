package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

    public ResourceLocation getEntityTexture(EntityRat entity) {
        return BASE_TEXTURE;
    }

    private class LayerGhostPirat extends LayerRenderer<EntityRat, ModelRat<EntityRat>> {
        private final IEntityRenderer<EntityRat, ModelRat<EntityRat>> ratRenderer;
        private ResourceLocation GHOST_OVERLAY = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat_overlay.png");

        public LayerGhostPirat(IEntityRenderer<EntityRat, ModelRat<EntityRat>> renderGhostPirat) {
            super(renderGhostPirat);
            this.ratRenderer = renderGhostPirat;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float f = (float)rat.ticksExisted + partialTicks;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(GHOST_OVERLAY, f * 0.01F, f * 0.01F));
            ratRenderer.getEntityModel().setRotationAngles(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ratRenderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

        }
    }
}