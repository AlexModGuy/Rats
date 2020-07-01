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

    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        this.func_230446_a_(p_230450_1_);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(p_230450_1_,i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.func_238474_b_(p_230450_1_,i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
        this.field_230712_o_.func_238405_a_(p_230450_1_, this.func_231171_q_().getString().replace("[", "").replace("]", ""), 8, 6, 4210752);
        this.field_230712_o_.func_238405_a_(p_230450_1_, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96, 4210752);

    }
}
