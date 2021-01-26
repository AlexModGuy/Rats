package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.inventory.ContainerUpgradeCombiner;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;

public class GuiUpgradeCombiner extends ContainerScreen<ContainerUpgradeCombiner> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/upgrade_combiner.png");
    private final PlayerInventory playerInventory;
    private final IInventory tileFurnace;

    public GuiUpgradeCombiner(ContainerUpgradeCombiner container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container.tileRatCraftingTable;
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
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
        int l = container.getCookProgressScaled(24);
        this.blit(p_230450_1_, i + 92, j + 35, 176, 14, l + 1, 16);
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityUpgradeCombiner && !((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).canCombine(this.tileFurnace.getStackInSlot(0), this.tileFurnace.getStackInSlot(2)) && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            this.blit(p_230450_1_, i + 42, j + 34, 198, 31, 21, 21);
        }
        if(!RatsMod.RATLANTIS_LOADED){
            this.blit(p_230450_1_, i + 44, j + 57, 0, 166, 16, 16);
        }
        if (container.getBurnLeftScaled(13) > 0) {
            int k = container.getBurnLeftScaled(13);
            this.blit(p_230450_1_, i + 70, j + 58 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack p_230450_1_, int mouseX, int mouseY) {
        String s = this.getTitle().getString();
        this.font.drawString(p_230450_1_, s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 5, 4210752);
        this.font.drawString(p_230450_1_, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 94 + 2, 4210752);
        int screenW = (this.width - this.xSize) / 2;
        int screenH = (this.height - this.ySize) / 2;
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityUpgradeCombiner && !((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).canCombine(this.tileFurnace.getStackInSlot(0), this.tileFurnace.getStackInSlot(2)) && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            if (mouseX > screenW + 42 && mouseX < screenW + 63 && mouseY > screenH + 34 && mouseY < screenH + 55) {
                TranslationTextComponent ratDesc = new TranslationTextComponent("container.upgrade_combiner.cannot_combine");
                List<TranslationTextComponent> list = Arrays.asList(ratDesc);
                renderWrappedToolTip(p_230450_1_, list, mouseX - screenW, mouseY - screenH + 10, font);
            }
        }
    }
}