package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanRatbot;
import com.github.alexthe666.rats.server.entity.EntityRatlanteanRatbot;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
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

public class LayerRatbotEyes extends LayerRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> {
    private static final RenderType TEXTURE_EYES_0 = RenderType.getEyes(new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_0.png"));
    private static final RenderType TEXTURE_EYES_1 = RenderType.getEyes(new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_1.png"));
    private static final RenderType TEXTURE_EYES_2 = RenderType.getEyes(new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_2.png"));
    private static final RenderType TEXTURE_EYES_3 = RenderType.getEyes(new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot_eyes_3.png"));
    private final IEntityRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> ratRenderer;

    public LayerRatbotEyes(IEntityRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRatlanteanRatbot entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(getTextureForTick(entitylivingbaseIn.ticksExisted * 3));
        this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

    }

    private RenderType getTextureForTick(int ticksExisted) {
        int tickCap = ticksExisted % 40;
        if(tickCap > 19){
            tickCap = tickCap - 20;
            if (tickCap > 15) {
                return TEXTURE_EYES_0;
            } else if (tickCap > 10) {
                return TEXTURE_EYES_1;
            } else if (tickCap > 5) {
                return TEXTURE_EYES_2;
            } else {
                return TEXTURE_EYES_3;
            }
        }else{
            if (tickCap > 15) {
                return TEXTURE_EYES_3;
            } else if (tickCap > 10) {
                return TEXTURE_EYES_2;
            } else if (tickCap > 5) {
                return TEXTURE_EYES_1;
            } else {
                return TEXTURE_EYES_0;
            }
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
