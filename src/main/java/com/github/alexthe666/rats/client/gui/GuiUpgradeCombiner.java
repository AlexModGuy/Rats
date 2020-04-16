package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.inventory.ContainerUpgradeCombiner;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.Arrays;

public class GuiUpgradeCombiner extends ContainerScreen<ContainerUpgradeCombiner> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/gui/upgrade_combiner.png");
    private final PlayerInventory playerInventory;
    private final IInventory tileFurnace;

    public GuiUpgradeCombiner(ContainerUpgradeCombiner container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container.tileRatCraftingTable;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.getTitle().getFormattedText();
        this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 5, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 94 + 2, 4210752);
        int screenW = (this.width - this.xSize) / 2;
        int screenH = (this.height - this.ySize) / 2;
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityUpgradeCombiner && !((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).canCombine(this.tileFurnace.getStackInSlot(0), this.tileFurnace.getStackInSlot(2)) && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            if (mouseX > screenW + 42 && mouseX < screenW + 63 && mouseY > screenH + 34 && mouseY < screenH + 55) {
                String ratDesc = I18n.format("container.upgrade_combiner.cannot_combine");
                renderTooltip(Arrays.asList(ratDesc), mouseX - screenW, mouseY - screenH + 10, font);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        int l = container.getCookProgressScaled(24);
        this.blit(i + 92, j + 35, 176, 14, l + 1, 16);
        if (RatsMod.PROXY.getRefrencedTE() instanceof TileEntityUpgradeCombiner && !((TileEntityUpgradeCombiner) RatsMod.PROXY.getRefrencedTE()).canCombine(this.tileFurnace.getStackInSlot(0), this.tileFurnace.getStackInSlot(2)) && !this.tileFurnace.getStackInSlot(0).isEmpty() && !this.tileFurnace.getStackInSlot(2).isEmpty()) {
            this.blit(i + 42, j + 34, 198, 31, 21, 21);
        }
        if (container.getBurnLeftScaled(13) > 0) {
            int k = container.getBurnLeftScaled(13);
            this.blit(i + 70, j + 58 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

}