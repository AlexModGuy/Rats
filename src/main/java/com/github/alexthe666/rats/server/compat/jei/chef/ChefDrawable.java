package com.github.alexthe666.rats.server.compat.jei.chef;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class ChefDrawable implements IDrawable {
    protected static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/chef_rat_jei.png");

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 120;
    }

    @Override
    public void draw(MatrixStack sta, int xOffset, int yOffset) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(sta, xOffset, yOffset, 3, 4, 170, 79);
        int scaledProgress = (minecraft.player.ticksExisted % 100) * 24 / 100;

        this.drawTexturedModalRect(sta, 71, 50, 176, 0, scaledProgress + 1, 16);
    }

    public void drawTexturedModalRect(MatrixStack stackIn, int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        Matrix4f matrix4f = stackIn.getLast().getMatrix();
        bufferbuilder.pos(matrix4f, (x + 0),  (y + height),  0).tex( ((float) (textureX + 0) * 0.00390625F),  ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos(matrix4f, (x + width),  (y + height),  0).tex( ((float) (textureX + width) * 0.00390625F),  ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos(matrix4f,  (x + width),  (y + 0),  0).tex( ((float) (textureX + width) * 0.00390625F),  ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos(matrix4f, (x + 0),  (y + 0),  0).tex( ((float) (textureX + 0) * 0.00390625F),  ((float) (textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}