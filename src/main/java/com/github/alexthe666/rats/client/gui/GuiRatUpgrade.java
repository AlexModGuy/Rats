package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.inventory.ContainerRat;
import com.github.alexthe666.rats.server.inventory.ContainerRatUpgrade;
import com.github.alexthe666.rats.server.inventory.InventoryRatUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiRatUpgrade extends ContainerScreen<ContainerRatUpgrade> {

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory playerInventory;
    private final IInventory itemInventory;
    private final int inventoryRows;

    public GuiRatUpgrade(InventoryPlayer playerInventory, InventoryRatUpgrade itemInventory) {
        super(new ContainerRatUpgrade(Minecraft.getMinecraft().player, playerInventory, itemInventory));
        this.playerInventory = playerInventory;
        this.itemInventory = itemInventory;
        this.allowUserInput = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = itemInventory.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.itemInventory.getDisplayName().getUnformattedText().replace("[", "").replace("]", ""), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
