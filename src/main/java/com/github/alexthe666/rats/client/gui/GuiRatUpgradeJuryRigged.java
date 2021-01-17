package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.inventory.ContainerRatUpgradeJuryRigged;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GuiRatUpgradeJuryRigged extends ContainerScreen<ContainerRatUpgradeJuryRigged> {

    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("rats:textures/gui/rat_upgrade_jury_rigged.png");
    private final PlayerInventory playerInventory;
    private final IInventory itemInventory;
    private final int inventoryRows;

    public GuiRatUpgradeJuryRigged(ContainerRatUpgradeJuryRigged container, PlayerInventory playerInventory, ITextComponent name) {
        super(container, playerInventory, name);
        this.playerInventory = playerInventory;
        this.itemInventory = container.inventory;
        int i = 222;
        int j = 114;
        this.inventoryRows = 1;
        this.ySize = 114 + this.inventoryRows * 18;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.renderBackground(p_230450_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(p_230450_1_,i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(p_230450_1_,i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
