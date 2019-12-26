package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.inventory.ContainerRatUpgrade;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiRatUpgrade extends ContainerScreen<ContainerRatUpgrade> {

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final PlayerInventory playerInventory;
    private final IInventory itemInventory;
    private final int inventoryRows;

    public GuiRatUpgrade(ContainerRatUpgrade container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.playerInventory = playerInventory;
        this.itemInventory = container.inventory;
        int i = 222;
        int j = 114;
        this.inventoryRows = itemInventory.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.getTitle().getFormattedText().replace("[", "").replace("]", ""), 8, 6, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
