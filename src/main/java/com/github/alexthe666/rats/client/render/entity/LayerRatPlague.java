package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LayerRatPlague extends LayerRenderer<EntityRat, SegmentedModel<EntityRat>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/rat_plague_overlay.png");
    private static final ResourceLocation TEXTURE_LUMBERJACK = new ResourceLocation("rats:textures/entity/rat/rat_lumberjack_upgrade.png");
    private static final ResourceLocation TEXTURE_TOGA = new ResourceLocation("rats:textures/entity/rat/toga.png");
    private static final ResourceLocation TEXTURE_RATINATOR = new ResourceLocation("rats:textures/entity/rat/rat_ratinator_upgrade.png");
    private static final ResourceLocation TEXTURE_PSYCHIC = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private ResourceLocation TEXTURE_GHOST = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat_overlay.png");
    private static final ModelRat RAT_MODEL = new ModelRat(0.5F);
    private final IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> ratRenderer;

    public LayerRatPlague(IEntityRenderer<EntityRat, SegmentedModel<EntityRat>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRat rat, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
     if (!(ratRenderer.getEntityModel() instanceof ModelRat)) {
            return;
        }
        if (rat.hasPlague()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityNoOutline(TEXTURE));
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY) && rat.getHeldRF() > 0) {//rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            float f = (float)rat.ticksExisted + partialTicks;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(TEXTURE_PSYCHIC, f * 0.01F, f * 0.01F));
            ratRenderer.getEntityModel().setRotationAngles(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ratRenderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE_RATINATOR));
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE_LUMBERJACK));
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasToga()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySmoothCutout(TEXTURE_TOGA));
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_ETHEREAL)) {
            float f = (float)rat.ticksExisted + partialTicks;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(TEXTURE_GHOST, f * 0.01F, f * 0.01F));
            ratRenderer.getEntityModel().setRotationAngles(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ratRenderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}