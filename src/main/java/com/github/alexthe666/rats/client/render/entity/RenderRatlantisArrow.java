package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.render.type.RatsRenderType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RenderRatlantisArrow extends ArrowRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/ratlantis_arrow.png");

    public RenderRatlantisArrow() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void render(AbstractArrowEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw) - 90.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        packedLightIn = 240;
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
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEyes(TEXTURE)).getVertexBuilder();
        IVertexBuilder vertexBuilder = bufferIn.getBuffer(RatsRenderType.YELLOW_ENTITY_GLINT);
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, 1);
        this.drawVertex(matrix4f, matrix3f, vertexBuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, 1);

        for(int j = 0; j < 4; ++j) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            this.drawVertex(matrix4f, matrix3f, vertexBuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, 1);
            this.drawVertex(matrix4f, matrix3f, vertexBuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, 1);
            this.drawVertex(matrix4f, matrix3f, vertexBuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, 1);
            this.drawVertex(matrix4f, matrix3f, vertexBuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, 1);
        }

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}
