package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.github.alexthe666.rats.server.inventory.ContainerAutoCurdler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

public class GuiAutoCurdler extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/auto_curdler.png");
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;
    public ChangeCommandButton previousCommand;
    public ChangeCommandButton nextCommand;

    public GuiAutoCurdler(IInventory furnaceInv, InventoryPlayer playerInv) {
        super(new ContainerAutoCurdler(furnaceInv, playerInv.player));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }

    public static void renderFluidStack(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        do {
            int currentHeight = Math.min(sprite.getIconHeight(), height);
            height -= currentHeight;
            float v2 = sprite.getInterpolatedV((16 * currentHeight) / (float) sprite.getIconHeight());
            int x2 = x;
            int width2 = width;
            do {
                int currentWidth = Math.min(sprite.getIconWidth(), width2);
                width2 -= currentWidth;
                float u2 = sprite.getInterpolatedU((16 * currentWidth) / (float) sprite.getIconWidth());
                bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(x2, y, depth).tex(u1, v1).endVertex();
                bufferbuilder.pos(x2, y + currentHeight, depth).tex(u1, v2).endVertex();
                bufferbuilder.pos(x2 + currentWidth, y + currentHeight, depth).tex(u2, v2).endVertex();
                bufferbuilder.pos(x2 + currentWidth, y, depth).tex(u2, v1).endVertex();
                tessellator.draw();
                x2 += currentWidth;
            } while (width2 > 0);

            y += currentHeight;
        } while (height > 0);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 94 + 2, 4210752);
        int screenW = (this.width - this.xSize) / 2;
        int screenH = (this.height - this.ySize) / 2;
        if (((TileEntityAutoCurdler) tileFurnace).tank.getFluid() != null) {
            if (mouseX > screenW + 29 && mouseX < screenW + 53 && mouseY > screenH + 15 && mouseY < screenH + 73) {
                String fluidName = TextFormatting.BLUE.toString() + ((TileEntityAutoCurdler) tileFurnace).tank.getFluid().getLocalizedName();
                String fluidSize = TextFormatting.GRAY.toString() + ((TileEntityAutoCurdler) tileFurnace).tank.getFluidAmount() + " " + I18n.format("container.auto_curdler.mb");
                net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(fluidName, fluidSize), mouseX - screenW, mouseY - screenH + 10, width, height, 120, fontRenderer);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int l = this.getCookProgressScaled(50);
        this.drawTexturedModalRect(i + 63, j + 35, 176, 0, l + 1, 16);
        if (((TileEntityAutoCurdler) tileFurnace).tank.getFluid() != null) {
            FluidTank tank = ((TileEntityAutoCurdler) tileFurnace).tank;
            int textureYPos = (int) (57 * (tank.getFluidAmount() / (float) tank.getCapacity()));
            renderFluidStack(i + 29, j + 15 - textureYPos + 57, 24, textureYPos, 0, tank.getFluid());
            this.mc.getTextureManager().bindTexture(TEXTURE);
        }
        this.drawTexturedModalRect(i + 29, j + 15, 0, 166, 24, 58);
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tileFurnace.getField(1);
        int j = this.tileFurnace.getField(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}