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

    private static final ResourceLocation TEXTURE_RED = new ResourceLocation("rats:textures/entity/ratlantis/laser_beam.png");
    private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation("rats:textures/entity/ratlantis/laser_beam_blue.png");
    private static final RenderType RENDER_TYPE_RED = RenderType.getEyes(TEXTURE_RED);
    private static final RenderType RENDER_TYPE_BLUE = RenderType.getEyes(TEXTURE_BLUE);

    public RenderLaserBeam() {
        super(Minecraft.getInstance().getRenderManager());
    }

    @Override
    public void render(EntityLaserBeam entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
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
        int r = (int)(entityIn.getRGB()[0] * 255F);
        int g = (int)(entityIn.getRGB()[1] * 255F);
        int b = (int)(entityIn.getRGB()[2] * 255F);
        if (f9 > 0.0F) {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f10));
        }

        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(45.0F));
        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(r > 200 ? RENDER_TYPE_RED : RENDER_TYPE_BLUE);
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        packedLightIn = 240;
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn, r, g, b);
        vertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn, r, g, b);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public void vertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, IVertexBuilder p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_, int r, int g, int b) {
        Vector4f vector4f = new Vector4f(p_229039_4_, p_229039_5_, p_229039_6_, 1.0F);
        vector4f.transform(p_229039_1_);
        p_229039_3_.addVertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), r, g, b, 255, p_229039_7_, p_229039_8_, OverlayTexture.NO_OVERLAY, p_229039_12_, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_);

    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(EntityLaserBeam entity) {
        int r = (int)(entity.getRGB()[0] * 255F);
        return r > 200 ? TEXTURE_RED : TEXTURE_BLUE;
    }
}
