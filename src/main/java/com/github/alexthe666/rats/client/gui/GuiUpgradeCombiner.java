package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.inventory.ContainerUpgradeCombiner;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class GuiUpgradeCombiner extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/upgrade_combiner.png");
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;
    public ChangeCommandButton previousCommand;
    public ChangeCommandButton nextCommand;

    public GuiUpgradeCombiner(IInventory furnaceInv, InventoryPlayer playerInv) {
        super(new ContainerUpgradeCombiner(furnaceInv, playerInv.player));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
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
        if (!((TileEntityUpgradeCombiner) tileFurnace).canSmelt() && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            if (mouseX > screenW + 42 && mouseX < screenW + 63 && mouseY > screenH + 34 && mouseY < screenH + 55) {
                String ratDesc = I18n.format("container.upgrade_combiner.cannot_combine");
                net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(ratDesc), mouseX - screenW, mouseY - screenH + 10, width, height, 120, fontRenderer);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i + 92, j + 35, 176, 14, l + 1, 16);
        if (!((TileEntityUpgradeCombiner) tileFurnace).canSmelt() && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            this.drawTexturedModalRect(i + 42, j + 34, 198, 31, 21, 21);
        }
        if (this.tileFurnace.getField(0) > 0) {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 70, j + 58 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

    private int getCookProgressScaled(int pixels) {
        int i = this.tileFurnace.getField(2);
        int j = this.tileFurnace.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels) {
        int i = this.tileFurnace.getField(1);

        if (i == 0) {
            i = 200;
        }

        return this.tileFurnace.getField(0) * pixels / i;
    }
}