package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelAutoCurdler;
import com.github.alexthe666.rats.server.blocks.BlockAutoCurdler;
import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
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
    private static final RenderType TEXTURE_BLOCKS = RenderType.getEntitySmoothCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

    public RenderAutoCurdler(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    public static void renderMilk(IRenderTypeBuffer bufferIn, MatrixStack matrixStackIn, FluidStack fluidStack, int combinedLight, int overlay) {
        float textureYPos = (0.6F * (fluidStack.getAmount() / 5000F));
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.translate(-0.5F, -1.6F, -0.5F);
        matrixStackIn.translate(0F, textureYPos, 0F);
        AxisAlignedBB boundingBox = new AxisAlignedBB(0.25F, 0.6F - textureYPos, 0.25F, 0.75F, 0.5F, 0.75F);
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(fluidStack.getFluid().getAttributes().getStillTexture());
        IVertexBuilder vertexbuffer = bufferIn.getBuffer(TEXTURE_BLOCKS);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
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
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_x,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_x,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, -1.0F).endVertex();
        //front
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_x,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_x,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 0.0F, 1.0F).endVertex();
        //tops
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f4_alt_z).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_x,  f4_alt_z).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_x,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f4_alt_z).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_x,  f4_alt_z).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_x,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
        //sides
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_z,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f2_alt_z,  f3).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f1,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_z,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.minZ).color(255, 255, 255, 255).tex( f2_alt_z,  f3).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f3).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.maxZ).color(255, 255, 255, 255).tex( f1,  f4_alt).overlay(overlay).lightmap(combinedLight).normal(1.0F, 0.0F, 0.0F).endVertex();
        matrixStackIn.pop();
    }

    @Override
    public void render(TileEntityAutoCurdler entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float f = 0;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockAutoCurdler) {
            f = entity.getWorld().getBlockState(entity.getPos()).get(BlockAutoCurdler.FACING).rotateY().getHorizontalAngle() + 90;
        }
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180));
        matrixStackIn.push();
        if (!entity.tank.getFluid().isEmpty()) {
            renderMilk(bufferIn, matrixStackIn, entity.tank.getFluid(), combinedLightIn, combinedOverlayIn);
        }
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        MODEL_AUTO_CURDLER.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();
    }
}
