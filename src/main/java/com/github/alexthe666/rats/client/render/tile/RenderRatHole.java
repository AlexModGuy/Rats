package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatCageDecorated;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
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

    public RenderRatHole(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void renderAABB(AxisAlignedBB boundingBox, double x, double y, double z, TextureAtlasSprite sprite) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
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
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex( f2_alt_x,  f3).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex( f1,  f3).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex( f1,  f4_alt).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex( f2_alt_x,  f4_alt).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        //front
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex( f1,  f4_alt).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex( f2_alt_x,  f4_alt).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex( f2_alt_x,  f3).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex( f1,  f3).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        //tops
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex( f1,  f4_alt_z).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex( f2_alt_x,  f4_alt_z).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex( f2_alt_x,  f3).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex( f1,  f3).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex( f1,  f4_alt_z).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex( f2_alt_x,  f4_alt_z).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex( f2_alt_x,  f3).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex( f1,  f3).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        //sides
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex( f2_alt_z,  f4_alt).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex( f2_alt_z,  f3).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex( f1,  f3).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex( f1,  f4_alt).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex( f2_alt_z,  f4_alt).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex( f2_alt_z,  f3).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex( f1,  f3).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex( f1,  f4_alt).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();

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
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.push();
        IBakedModel ibakedmodel = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        renderAABB(TOP_AABB, x, y, z, ibakedmodel.getParticleTexture());
        renderAABB(EAST_CORNER_AABB, x, y, z, ibakedmodel.getParticleTexture());
        renderAABB(WEST_CORNER_AABB, x, y, z, ibakedmodel.getParticleTexture());
        renderAABB(NORTH_CORNER_AABB, x, y, z, ibakedmodel.getParticleTexture());
        renderAABB(SOUTH_CORNER_AABB, x, y, z, ibakedmodel.getParticleTexture());
        if (connectedEast) {
            renderAABB(NS_RIGHT_AABB, x, y, z, ibakedmodel.getParticleTexture());
        }
        if (connectedWest) {
            renderAABB(NS_LEFT_AABB, x, y, z, ibakedmodel.getParticleTexture());
        }
        if (connectedNorth) {
            renderAABB(EW_LEFT_AABB, x, y, z, ibakedmodel.getParticleTexture());
        }
        if (connectedSouth) {
            renderAABB(EW_RIGHT_AABB, x, y, z, ibakedmodel.getParticleTexture());
        }
        matrixStackIn.pop();
        matrixStackIn.pop();
    }
}
