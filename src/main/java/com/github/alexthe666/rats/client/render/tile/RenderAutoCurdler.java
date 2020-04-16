package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelAutoCurdler;
import com.github.alexthe666.rats.server.blocks.BlockAutoCurdler;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderAutoCurdler extends TileEntityRenderer<TileEntityAutoCurdler> {
    private static final ModelAutoCurdler MODEL_AUTO_CURDLER = new ModelAutoCurdler();
    private static final RenderType TEXTURE = RenderType.getEntityCutout(new ResourceLocation("rats:textures/model/auto_curdler.png"));

    public RenderAutoCurdler(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public static void renderMilk(MatrixStack matrixStackIn, FluidStack fluidStack) {
        float textureYPos = (0.6F * (fluidStack.getAmount() / 5000F));
        matrixStackIn.push();
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.translate(-0.5F, 0.5F, -0.5F);
        matrixStackIn.push();
        AxisAlignedBB boundingBox = new AxisAlignedBB(0.25F, 0.6F - textureYPos, 0.25F, 0.75F, 0.5F, 0.75F);
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(fluidStack.getFluid().getAttributes().getStillTexture()).apply(fluidStack.getFluid().getAttributes().getStillTexture());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
        double avgY = boundingBox.maxY - boundingBox.minY;
        double avgX = Math.abs(boundingBox.maxX - boundingBox.minX);
        double avgZ = Math.abs(boundingBox.maxZ - boundingBox.minZ);
        float f1 = sprite.getMinU();
        float f2 = sprite.getMaxU();
        float f2_alt_x = (float) Math.min(sprite.getMaxU(), f1 + avgX * Math.abs(sprite.getMaxU() - sprite.getMinU()));//sprite.getMaxU();
        float f2_alt_z = (float) Math.min(sprite.getMaxU(), f1 + avgZ * Math.abs(sprite.getMaxU() - sprite.getMinU()));//sprite.getMaxU();
        float f3 = sprite.getMinV();
        float f4 = sprite.getMaxV();//sprite.getMaxU();
        float f4_alt = (float) Math.min(sprite.getMaxV(), f3 + avgY * Math.abs(sprite.getMaxV() - sprite.getMinV()));//sprite.getMaxU();
        float f4_alt_x = (float) Math.min(sprite.getMaxV(), f3 + avgX * Math.abs(sprite.getMaxV() - sprite.getMinV()));//sprite.getMaxU();
        float f4_alt_z = (float) Math.min(sprite.getMaxV(), f3 + avgZ * Math.abs(sprite.getMaxV() - sprite.getMinV()));//sprite.getMaxU();
//back
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f2_alt_x, f3).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f1, f3).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f1, f4_alt).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f2_alt_x, f4_alt).normal(0.0F, 0.0F, -1.0F).endVertex();
        //front
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f1, f4_alt).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f2_alt_x, f4_alt).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f2_alt_x, f3).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f1, f3).normal(0.0F, 0.0F, 1.0F).endVertex();
        //tops
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f1, f4_alt_z).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f2_alt_x, f4_alt_z).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f2_alt_x, f3).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f1, f3).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f1, f4_alt_z).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f2_alt_x, f4_alt_z).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f2_alt_x, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f1, f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        //sides
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(f2_alt_z, f4_alt).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(f2_alt_z, f3).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(f1, f3).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(f1, f4_alt).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(f2_alt_z, f4_alt).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(f2_alt_z, f3).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(f1, f3).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(f1, f4_alt).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
        matrixStackIn.pop();
        matrixStackIn.pop();
        matrixStackIn.pop();
    }

    @Override
    public void render(TileEntityAutoCurdler entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float f = 0;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatHole) {
            f = entity.getWorld().getBlockState(entity.getPos()).get(BlockAutoCurdler.FACING).rotateY().getHorizontalAngle() + 90;
        }
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
        if (!entity.tank.getFluid().isEmpty()) {
            renderMilk(matrixStackIn, entity.tank.getFluid());
        }
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        MODEL_AUTO_CURDLER.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
