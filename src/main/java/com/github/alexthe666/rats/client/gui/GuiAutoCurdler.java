package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import com.github.alexthe666.rats.server.inventory.ContainerAutoCurdler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

public class GuiAutoCurdler extends ContainerScreen<ContainerAutoCurdler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/auto_curdler.png");
    private final PlayerInventory playerInventory;
    private final IInventory tileFurnace;
    public ChangeCommandButton previousCommand;
    public ChangeCommandButton nextCommand;
    public ITextComponent name;
    private int cookTime;
    private int totalCookTime;

    public GuiAutoCurdler(ContainerAutoCurdler container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container.tileRatCraftingTable;
        this.name = name;
    }

    public static void renderFluidStack(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        ResourceLocation res = fluidStack.getFluid().getAttributes().getStillTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        float u1 = 0;
        float v1 = 0;
        do {
            int currentHeight = Math.min(16, height);
            height -= currentHeight;
            float v2 = sprite.getInterpolatedV((16 * currentHeight) / (float) sprite.getHeight());
            int x2 = x;
            int width2 = width;
            do {
                int currentWidth = Math.min(sprite.getWidth(), width2);
                width2 -= currentWidth;
                bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(x2, y, depth).tex(u1, v1).endVertex();
                bufferbuilder.pos(x2, y + currentHeight, depth).tex(u1, v2).endVertex();
                bufferbuilder.pos(x2 + currentWidth, y + currentHeight, depth).tex(16, v2).endVertex();
                bufferbuilder.pos(x2 + currentWidth, y, depth).tex(16, v1).endVertex();
                tessellator.draw();
                x2 += currentWidth;
            } while (width2 > 0);

            y += currentHeight;
        } while (height > 0);
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.getTitle().getFormattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 5, 4210752);
        this.font.drawString(this.getTitle().getFormattedText(), 8, this.ySize - 94 + 2, 4210752);
        int screenW = (this.width - this.xSize) / 2;
        int screenH = (this.height - this.ySize) / 2;
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityAutoCurdler && ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).tank.getFluid() != null) {
            if (mouseX > screenW + 29 && mouseX < screenW + 53 && mouseY > screenH + 15 && mouseY < screenH + 73) {
                String fluidName = TextFormatting.BLUE.toString() + ((TileEntityAutoCurdler)  RatsMod.PROXY.getRefrencedTE()).tank.getFluid().getDisplayName().getFormattedText();
                String fluidSize = TextFormatting.GRAY.toString() + ((TileEntityAutoCurdler)  RatsMod.PROXY.getRefrencedTE()).tank.getFluidAmount() + " " + I18n.format("container.auto_curdler.mb");
                net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(fluidName, fluidSize), mouseX - screenW, mouseY - screenH + 10, width, height, 120, font);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int l = ((ContainerAutoCurdler)container).getCookProgressionScaled();
        this.blit(i + 63, j + 35, 176, 0, l + 1, 16);
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityAutoCurdler && ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).tank.getFluid() != null) {
            FluidTank tank = ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).tank;
            int textureYPos = (int) (57 * (tank.getFluidAmount() / (float) tank.getCapacity()));
            renderFluidStack(i + 29, j + 15 - textureYPos + 57, 24, textureYPos, 0, tank.getFluid());
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        }
        this.blit(i + 29, j + 15, 0, 166, 24, 58);
    }

}