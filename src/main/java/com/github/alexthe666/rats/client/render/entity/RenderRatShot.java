package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.client.model.ModelStaticRat;
import com.github.alexthe666.rats.server.entity.EntityRatShot;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import javax.annotation.Nullable;

public class RenderRatShot extends EntityRenderer<EntityRatShot> {

    private static final RenderType TEXTURE_EYES = RenderType.getEyes(new ResourceLocation("rats:textures/entity/rat/rat_eye_glow.png"));
    private static final RenderType TEXTURE_0 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_blue.png"));
    private static final RenderType TEXTURE_1 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_black.png"));
    private static final RenderType TEXTURE_2 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_brown.png"));
    private static final RenderType TEXTURE_3 = RenderType.getEntityCutoutNoCull(new ResourceLocation("rats:textures/entity/rat/rat_green.png"));
    private static final ModelStaticRat MODEL_STATIC_RAT = new ModelStaticRat(0);

    public RenderRatShot() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void render(EntityRatShot entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        long roundedTime = entity.world.getDayTime() % 24000;
        boolean night = roundedTime >= 13000 && roundedTime <= 22000;
        BlockPos ratPos = entity.getLightPosition();
        int brightI = entity.world.getLightFor(LightType.SKY, ratPos);
        int brightJ = entity.world.getLightFor(LightType.BLOCK, ratPos);
        int brightness;
        if (night) {
            brightness = brightJ;
        } else {
            brightness = Math.max(brightI, brightJ);
        }
        matrixStackIn.push();
        matrixStackIn.scale(0.6F, -0.6F, 0.6F);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        matrixStackIn.translate(0F, -1.5F, 0F);
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, yaw - 180, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.XN, pitch , true));

        RenderType resLoc;
        switch (entity.getColorVariant()){
            case 1:
                resLoc = TEXTURE_1;
                break;
            case 2:
                resLoc = TEXTURE_2;
                break;
            case 3:
                resLoc = TEXTURE_3;
                break;
            default:
                resLoc = TEXTURE_0;
                break;
        }
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(resLoc);
        float f = (entity.ticksExisted + partialTicks) * 0.5F;
        float f1 = 1;
        MODEL_STATIC_RAT.setRotationAngles(entity, f, f1, entity.ticksExisted + partialTicks, 0, 0);
        MODEL_STATIC_RAT.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(false)), 1.0F, 1.0F, 1.0F, 1.0F);

        if(brightness < 7){
            IVertexBuilder iGlowBuffer = bufferIn.getBuffer(TEXTURE_EYES);
            MODEL_STATIC_RAT.render(matrixStackIn, iGlowBuffer, packedLightIn, OverlayTexture.getPackedUV(OverlayTexture.getU(0), OverlayTexture.getV(false)), 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrixStackIn.pop();

        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityRatShot entity) {
        return null;
    }
}
