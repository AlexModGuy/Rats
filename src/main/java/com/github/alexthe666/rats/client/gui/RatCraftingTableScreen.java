package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.message.CycleRatRecipePacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.Optional;

public class RatCraftingTableScreen extends AbstractContainerScreen<RatCraftingTableMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/rat_crafting_table.png");
	private final Inventory playerInventory;
	private final RatCraftingTableMenu table;

	public RatCraftingTableScreen(RatCraftingTableMenu container, Inventory inv, Component name) {
		super(container, inv, name);
		this.playerInventory = inv;
		this.table = container;
		this.imageHeight = 211;
	}

	@Override
	protected void init() {
		super.init();
		this.renderables.clear();
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		this.addRenderableWidget(new CycleResultButton(i + 135, j + 38, false, button -> {
			this.table.incrementRecipeIndex(false);
			RatsNetworkHandler.CHANNEL.sendToServer(new CycleRatRecipePacket(this.table.getCraftingTable().getBlockPos().asLong(), false));
		}));
		this.addRenderableWidget(new CycleResultButton(i + 135, j + 5, true, button -> {
			this.table.incrementRecipeIndex(true);
			RatsNetworkHandler.CHANNEL.sendToServer(new CycleRatRecipePacket(this.table.getCraftingTable().getBlockPos().asLong(), true));
		}));
	}

	@Override
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
		graphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int l = this.table.getCookProgressionScaled();
		graphics.blit(TEXTURE, i + 96, j + 40, 0, 211, l, 16);
		if (this.table.getCraftingTable().hasRat()) {
			graphics.blit(TEXTURE, i + 8, j + 20, 176, 0, 21, 21);
		} else {
			graphics.blit(TEXTURE, i + 7, j + 40, 198, 0, 21, 21);
		}
		if (this.table.getCraftingTable().getRecipeUsed() == null) {
			graphics.blit(TEXTURE, i + 95, j + 38, 220, 0, 21, 21);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		Font font = this.font;
		String s = this.getTitle().getString();
		graphics.drawString(this.font, s, this.imageWidth / 2.0F - font.width(s) / 2.0F, 5, 4210752, false);
		graphics.drawString(this.font, this.playerInventory.getDisplayName().getString(), 8, this.imageHeight - 93, 4210752, false);
		graphics.drawString(this.font, Component.translatable("container.rat_crafting_table.input"), 8, this.imageHeight - 125, 4210752, false);
		int screenW = (this.width - 248) / 2;
		int screenH = (this.height - 166) / 2;
		Optional<CraftingRecipe> recipe = this.table.getCraftingTable().getGuideRecipe();
		if (recipe.isPresent() && !this.table.getSlot(0).hasItem()) {
			graphics.renderItem(recipe.get().getResultItem(Minecraft.getInstance().level.registryAccess()), 130, 40);
			RenderSystem.disableDepthTest();
			graphics.fill(130, 40, 146, 56, 0x9f8b8b8b);
			RenderSystem.enableDepthTest();
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				RenderSystem.disableDepthTest();
				int xPos = 36 + i * 18;
				int yPos = 22 + j * 18;
				graphics.fill(xPos, yPos, xPos + 16, yPos + 16, 0x9f8b8b8b);
				RenderSystem.enableDepthTest();
			}
		}

		if (!this.table.getCraftingTable().hasRat()) {
			if (this.isHovering(6, 34, 25, 29, mouseX, mouseY)) {
				Component ratDesc = Component.translatable("container.rat_crafting_table.rat_desc");
				graphics.renderTooltip(this.font, this.font.split(ratDesc, 200), mouseX - screenW - 40, mouseY - screenH + 10);
			}
		}
	}

	public boolean shouldRenderButtons() {
		return this.table.getCraftingTable().getPossibleRecipes().size() > 1;
	}

	private static class CycleResultButton extends Button {
		private final boolean up;

		public CycleResultButton(int x, int y, boolean up, OnPress onClick) {
			super(x, y, 14, 9, Component.empty(), onClick, message -> Component.empty());
			this.up = up;
		}

		@Override
		public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
			if (Minecraft.getInstance().screen instanceof RatCraftingTableScreen table && !table.shouldRenderButtons())
				return;
			if (this.visible) {
				this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

				int textureX = 22;
				int textureY = 211;

				if (this.isHovered) textureX += this.width;

				if (!this.up) textureY += this.height;

				graphics.blit(TEXTURE, this.getX(), this.getY(), textureX, textureY, this.width, this.height);
			}
		}
	}
}