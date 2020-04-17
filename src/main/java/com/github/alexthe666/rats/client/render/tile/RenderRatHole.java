package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderRatHole extends TileEntityRenderer<TileEntityRatHole> {

    private static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0F, 0.5F, 0F, 1F, 1F, 1F);
    private static final AxisAlignedBB NS_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 0.25F, 0.5F, 1F);
    private static final AxisAlignedBB NS_RIGHT_AABB = new AxisAlignedBB(0.75F, 0F, 0F, 1F, 0.5F, 1F);
    private static final AxisAlignedBB EW_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 0.25F);
    private static final AxisAlignedBB EW_RIGHT_AABB = new AxisAlignedBB(0, 0F, 0.75F, 1F, 0.5F, 1F);

    private static final AxisAlignedBB NORTH_CORNER_AABB = new AxisAlignedBB(-0.001F, -0.001F, -0.001F, 0.251F, 0.5F, 0.251F);
    private static final AxisAlignedBB EAST_CORNER_AABB = new AxisAlignedBB(0.74999F, -0.001F, -0.001F, 1F, 0.5F, 0.251F);
    private static final AxisAlignedBB SOUTH_CORNER_AABB = new AxisAlignedBB(-0.001F, -0.001F, 0.74999F, 0.251, 0.5F, 1.001F);
    private static final AxisAlignedBB WEST_CORNER_AABB = new AxisAlignedBB(0.74999F, -0.001F, 0.74999F, 1.001F, 0.5F, 1.001F);

    private static final RenderType TEXTURE = RenderType.getEntitySmoothCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

    public RenderRatHole(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void renderAABB(IRenderTypeBuffer bufferIn, AxisAlignedBB boundingBox, MatrixStack matrixStack, TextureAtlasSprite sprite, int combinedLight, int overlay) {
        matrixStack.push();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(true);
        IVertexBuilder vertexbuffer = bufferIn.getBuffer(TEXTURE);
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
        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
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
        matrixStack.pop();
    }

    @Override
    public void render(TileEntityRatHole entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        BlockState state = Blocks.OAK_PLANKS.getDefaultState();
        boolean connectedNorth = false;
        boolean connectedEast = false;
        boolean connectedSouth = false;
        boolean connectedWest = false;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatHole) {
            BlockState actualState = entity.getBlockState();
            connectedNorth = actualState.get(BlockRatHole.NORTH);
            connectedEast = actualState.get(BlockRatHole.EAST);
            connectedSouth = actualState.get(BlockRatHole.SOUTH);
            connectedWest = actualState.get(BlockRatHole.WEST);
            state = entity.getImmitatedBlockState();
        }
        int x = 0;
        int y = 0;
        int z = 0;
        matrixStackIn.push();
        IBakedModel ibakedmodel = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
        renderAABB(bufferIn, TOP_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        renderAABB(bufferIn, EAST_CORNER_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        renderAABB(bufferIn, WEST_CORNER_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        renderAABB(bufferIn, NORTH_CORNER_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        renderAABB(bufferIn, SOUTH_CORNER_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        if (connectedEast) {
            renderAABB(bufferIn, NS_RIGHT_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        }
        if (connectedWest) {
            renderAABB(bufferIn, NS_LEFT_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        }
        if (connectedNorth) {
            renderAABB(bufferIn, EW_LEFT_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        }
        if (connectedSouth) {
            renderAABB(bufferIn, EW_RIGHT_AABB, matrixStackIn, ibakedmodel.getParticleTexture(), combinedLightIn, combinedOverlayIn);
        }
        matrixStackIn.pop();
    }
}
