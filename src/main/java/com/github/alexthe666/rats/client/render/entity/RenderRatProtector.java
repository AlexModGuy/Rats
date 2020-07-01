package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderRatProtector extends RenderRat {

    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/rat_protector.png");

    public RenderRatProtector() {
        super();
        this.addLayer(new RenderRatProtector.Overlay(this));
    }


    protected void preRenderCallback(EntityRat rat, MatrixStack stack, float partialTickTime) {
        super.preRenderCallback(rat, stack, partialTickTime);
    }

    public ResourceLocation getEntityTexture(EntityRat entity) {
        return BASE_TEXTURE;
    }

    private class Overlay extends LayerRenderer<EntityRat, SegmentedModel<EntityRat>> {
        private final IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> ratRenderer;

        public Overlay(IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> renderGhostPirat) {
            super(renderGhostPirat);
            this.ratRenderer = renderGhostPirat;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float f = (float)rat.ticksExisted + partialTicks;
            IVertexBuilder vertexBuilder = bufferIn.getBuffer(RatsRenderType.YELLOW_ENTITY_GLINT);
            ratRenderer.getEntityModel().setRotationAngles(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ratRenderer.getEntityModel().render(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

        }
    }
}