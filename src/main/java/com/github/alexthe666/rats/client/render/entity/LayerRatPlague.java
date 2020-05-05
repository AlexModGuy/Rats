package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRat;
import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatColorUtil;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.VertexBuilderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerRatPlague extends LayerRenderer<EntityRat, SegmentedModel<EntityRat>> {
    private static final RenderType TEXTURE = RenderType.getEntityNoOutline(new ResourceLocation("rats:textures/entity/rat/rat_plague_overlay.png"));
    private static final RenderType TEXTURE_DYED_NOT = RenderType.getEntitySmoothCutout(new ResourceLocation("rats:textures/entity/rat/rat_dyed_not.png"));
    private static final RenderType TEXTURE_DYED = RenderType.getEntityNoOutline(new ResourceLocation("rats:textures/entity/rat/rat_dyed.png"));
    private static final RenderType TEXTURE_LUMBERJACK = RenderType.getEntitySmoothCutout(new ResourceLocation("rats:textures/entity/rat/rat_lumberjack_upgrade.png"));
    private static final RenderType TEXTURE_TOGA = RenderType.getEntitySmoothCutout(new ResourceLocation("rats:textures/entity/rat/toga.png"));
    private static final RenderType TEXTURE_RATINATOR = RenderType.getEntitySmoothCutout(new ResourceLocation("rats:textures/entity/rat/rat_ratinator_upgrade.png"));
    private static final ResourceLocation TEXTURE_PSYCHIC = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private ResourceLocation TEXTURE_GHOST = new ResourceLocation("rats:textures/entity/ratlantis/ghost_pirat_overlay.png");
    private ResourceLocation TEXTURE_DYED_LOC = new ResourceLocation("rats:textures/entity/rat/rat_dyed.png");
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
        if (rat.isDyed()) {
            IVertexBuilder ivertexbuilder;
            float r;
            float g;
            float b;
            if (rat.getDyeColor() == 100) {
                ivertexbuilder = bufferIn.getBuffer(RatsRenderType.RAINBOW_GLINT);
                r = 1.0F;
                g = 1.0F;
                b = 1.0F;
                this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(rat, 0), r, g, b, 1.0F);
                IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(TEXTURE_DYED_NOT);
                 this.getEntityModel().render(matrixStackIn, ivertexbuilder2, packedLightIn, LivingRenderer.getPackedOverlay(rat, 0), r, g, b, 1.0F);
            } else {
                ivertexbuilder = bufferIn.getBuffer(TEXTURE_DYED);
                float[] lvt_14_2_ = RatColorUtil.getDyeRgb(DyeColor.byId(rat.getDyeColor()));
                r = lvt_14_2_[0];
                g = lvt_14_2_[1];
                b = lvt_14_2_[2];
                this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(rat, 0), r, g, b, 1.0F);
            }
        }
        if (rat.hasPlague()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(rat, 0), 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY) && rat.getHeldRF() > 0) {//rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PSYCHIC)) {
            float f = (float)rat.ticksExisted + partialTicks;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEnergySwirl(TEXTURE_PSYCHIC, f * 0.01F, f * 0.01F));
            ratRenderer.getEntityModel().setRotationAngles(rat, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            ratRenderer.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_RATINATOR)) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_RATINATOR);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_LUMBERJACK);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (rat.hasToga()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE_TOGA);
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