package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.github.alexthe666.rats.server.entity.EntityLaserBeam;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderLaserBeam extends ArrowRenderer<EntityLaserBeam> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/laser_beam.png");

    public RenderLaserBeam() {
        super(Minecraft.getInstance().getRenderManager());
    }

    @Override
    public void render(EntityLaserBeam entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        int r = (int)(entityIn.getRGB()[0] * 255F);
        int g = (int)(entityIn.getRGB()[1] * 255F);
        int b = (int)(entityIn.getRGB()[2] * 255F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 0.15625F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = 0.15625F;
        float f7 = 0.3125F;
        float f8 = 0.05625F;
        float f9 = (float)entityIn.arrowShake - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f10));
        }

        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(45.0F));
        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RatsRenderType.getGlowingTranslucent(TEXTURE));
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn, r, g, b);
        this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn, r, g, b);

        for(int j = 0; j < 4; ++j) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn, r, g, b);
            this.func_229039_a_(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn, r, g, b);
        }

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public void func_229039_a_(Matrix4f p_229039_1_, Matrix3f p_229039_2_, IVertexBuilder p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_, int r, int g, int b) {
        p_229039_3_.pos(p_229039_1_, (float)p_229039_4_, (float)p_229039_5_, (float)p_229039_6_).color(r, g, b, 255).tex(p_229039_7_, p_229039_8_).overlay(OverlayTexture.NO_OVERLAY).lightmap(p_229039_12_).normal(p_229039_2_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityLaserBeam entity) {
        return TEXTURE;
    }
}
