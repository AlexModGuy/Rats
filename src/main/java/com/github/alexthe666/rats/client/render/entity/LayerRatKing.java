package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelEmpty;
import com.github.alexthe666.rats.client.model.ModelRatKing;
import com.github.alexthe666.rats.server.entity.EntityRatKing;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class LayerRatKing extends LayerRenderer<EntityRatKing, ModelEmpty<EntityRatKing>> {
    private static final RenderType TEXTURE_EYES = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png"));
    private static final RenderType TEXTURE_0 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_blue.png"));
    private static final RenderType TEXTURE_1 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_black.png"));
    private static final RenderType TEXTURE_2 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_brown.png"));
    private static final RenderType TEXTURE_3 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_green.png"));
    private final IEntityRenderer<EntityRatKing, ModelEmpty<EntityRatKing>> ratRenderer;
    private final ModelRatKing RAT_MODEL = new ModelRatKing<>(0);
    public LayerRatKing(IEntityRenderer<EntityRatKing, ModelEmpty<EntityRatKing>> ratRendererIn) {
        super(ratRendererIn);
        this.ratRenderer = ratRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityRatKing king, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = MathHelper.interpolateAngle(partialTicks, king.prevRenderYawOffset, king.renderYawOffset);
        float f1 = MathHelper.interpolateAngle(partialTicks, king.prevRotationYawHead, king.rotationYawHead);
        float f2 = f1 - f;
        long roundedTime = king.world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        BlockPos ratPos = king.getLightPosition();
        int brightI = king.world.getLightFor(LightType.SKY, ratPos);
        int brightJ = king.world.getLightFor(LightType.BLOCK, ratPos);
        int brightness;
        if (night) {
            brightness = brightJ;
        } else {
            brightness = Math.max(brightI, brightJ);
        }

        for(int i = 0; i < EntityRatKing.RAT_COUNT; i++){
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(getRatTexture(king.getRatColors(i)));
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(i * EntityRatKing.RAT_ANGLE));
            matrixStackIn.translate(0, 0.6 ,-0.8);
            matrixStackIn.push();
            matrixStackIn.scale(0.6F, 0.6F ,0.6F);
            RAT_MODEL.setRotationAngles(king, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            RAT_MODEL.render(matrixStackIn, ivertexbuilder, packedLightIn, LivingRenderer.getPackedOverlay(king, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            if (brightness < 7){
                IVertexBuilder iGlowBuffer = bufferIn.getBuffer(TEXTURE_EYES);
                RAT_MODEL.render(matrixStackIn, iGlowBuffer, packedLightIn, LivingRenderer.getPackedOverlay(king, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }

            matrixStackIn.pop();
            matrixStackIn.pop();
        }


    }

    private RenderType getRatTexture(int textureIndex) {
        switch (textureIndex){
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case 3:
                return TEXTURE_3;
            default:
                return TEXTURE_0;
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
