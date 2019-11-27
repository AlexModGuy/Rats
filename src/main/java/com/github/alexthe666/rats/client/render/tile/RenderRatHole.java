package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.BlockRatHole;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatHole;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderRatHole extends TileEntitySpecialRenderer<TileEntityRatHole> {

    private static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0F, 0.5F, 0F, 1F, 1F, 1F);
    private static final AxisAlignedBB NS_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 0.25F, 0.5F, 1F);
    private static final AxisAlignedBB NS_RIGHT_AABB = new AxisAlignedBB(0.75F, 0F, 0F, 1F, 0.5F, 1F);
    private static final AxisAlignedBB EW_LEFT_AABB = new AxisAlignedBB(0F, 0F, 0F, 1F, 0.5F, 0.25F);
    private static final AxisAlignedBB EW_RIGHT_AABB = new AxisAlignedBB(0, 0F, 0.75F, 1F, 0.5F, 1F);

    private static final AxisAlignedBB NORTH_CORNER_AABB = new AxisAlignedBB(-0.001F, -0.001F, -0.001F, 0.251F, 0.5F, 0.251F);
    private static final AxisAlignedBB EAST_CORNER_AABB = new AxisAlignedBB(0.74999F, -0.001F, -0.001F, 1F, 0.5F, 0.251F);
    private static final AxisAlignedBB SOUTH_CORNER_AABB = new AxisAlignedBB(-0.001F, -0.001F, 0.74999F, 0.251, 0.5F, 1.001F);
    private static final AxisAlignedBB WEST_CORNER_AABB = new AxisAlignedBB(0.74999F, -0.001F, 0.74999F, 1.001F, 0.5F, 1.001F);

    public static void renderAABB(AxisAlignedBB boundingBox, double x, double y, double z, TextureAtlasSprite sprite) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
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
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex((double) f2_alt_x, (double) f3).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex((double) f1, (double) f3).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex((double) f1, (double) f4_alt).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex((double) f2_alt_x, (double) f4_alt).normal(0.0F, 0.0F, -1.0F).endVertex();
        //front
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex((double) f1, (double) f4_alt).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex((double) f2_alt_x, (double) f4_alt).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex((double) f2_alt_x, (double) f3).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex((double) f1, (double) f3).normal(0.0F, 0.0F, 1.0F).endVertex();
        //tops
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex((double) f1, (double) f4_alt_z).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex((double) f2_alt_x, (double) f4_alt_z).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex((double) f2_alt_x, (double) f3).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex((double) f1, (double) f3).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex((double) f1, (double) f4_alt_z).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex((double) f2_alt_x, (double) f4_alt_z).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex((double) f2_alt_x, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex((double) f1, (double) f3).normal(0.0F, 1.0F, 0.0F).endVertex();
        //sides
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex((double) f2_alt_z, (double) f4_alt).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex((double) f2_alt_z, (double) f3).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex((double) f1, (double) f3).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex((double) f1, (double) f4_alt).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex((double) f2_alt_z, (double) f4_alt).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex((double) f2_alt_z, (double) f3).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex((double) f1, (double) f3).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex((double) f1, (double) f4_alt).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();

    }

    @Override
    public void render(TileEntityRatHole entity, double x, double y, double z, float f, int f1, float alpha) {
        BlockState state = Blocks.PLANKS.getDefaultState();
        boolean connectedNorth = false;
        boolean connectedEast = false;
        boolean connectedSouth = false;
        boolean connectedWest = false;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatHole) {
            BlockState actualState = RatsBlockRegistry.RAT_HOLE.getActualState(entity.getWorld().getBlockState(entity.getPos()), entity.getWorld(), entity.getPos());
            connectedNorth = actualState.getValue(BlockRatHole.NORTH);
            connectedEast = actualState.getValue(BlockRatHole.EAST);
            connectedSouth = actualState.getValue(BlockRatHole.SOUTH);
            connectedWest = actualState.getValue(BlockRatHole.WEST);
            state = entity.getImmitatedBlockState();
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();
        IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(state);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
