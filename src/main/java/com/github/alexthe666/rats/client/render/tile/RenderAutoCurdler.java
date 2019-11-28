package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelAutoCurdler;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderAutoCurdler extends TileEntitySpecialRenderer<TileEntityAutoCurdler> {
    private static final ModelAutoCurdler MODEL_AUTO_CURDLER = new ModelAutoCurdler();
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/model/auto_curdler.png");

    public static void renderMilk(double x, double y, double z, float rotation, FluidStack fluidStack) {
        float textureYPos = (0.6F * (fluidStack.amount / 5000F));
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(rotation, 0, 1F, 0);
        GL11.glTranslatef(-0.5F, 0.5F, -0.5F);
        GL11.glPushMatrix();
        AxisAlignedBB boundingBox = new AxisAlignedBB(0.25F, 0.6F - textureYPos, 0.25F, 0.75F, 0.5F, 0.75F);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    @Override
    public void render(TileEntityAutoCurdler entity, double x, double y, double z, float f, int f1, float alpha) {
        float rotation = 0;
        if (entity != null && entity.getWorld() != null && entity instanceof TileEntityAutoCurdler) {
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.NORTH) {
                rotation = 180;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.EAST) {
                rotation = -90;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.WEST) {
                rotation = 90;
            }
            if (entity.tank.getFluidAmount() > 0) {
                renderMilk(x, y, z, rotation, entity.tank.getFluid());
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(rotation, 0, 1F, 0);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        MODEL_AUTO_CURDLER.render(0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
