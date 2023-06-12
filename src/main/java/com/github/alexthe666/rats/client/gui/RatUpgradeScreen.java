package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.inventory.RatUpgradeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class RatUpgradeScreen extends AbstractContainerScreen<RatUpgradeMenu> {

	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
	private final int inventoryRows;

	public RatUpgradeScreen(RatUpgradeMenu container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name);
		Container itemInventory = container.inventory;
		this.inventoryRows = itemInventory.getContainerSize() / 9;
		this.imageHeight = 114 + this.inventoryRows * 18;
	}

	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(graphics);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		graphics.blit(CHEST_GUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.inventoryRows * 18 + 17);
		graphics.blit(CHEST_GUI_TEXTURE, i, j + this.inventoryRows * 18 + 17, 0, 126, this.imageWidth, 96);
	}
}
