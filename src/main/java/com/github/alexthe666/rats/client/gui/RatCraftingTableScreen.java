package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.message.CycleRatRecipePacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.CraftingRecipe;

import java.util.Optional;

public class RatCraftingTableScreen extends AbstractContainerScreen<RatCraftingTableMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/rat_crafting_table.png");
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
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
		this.renderBackground(stack);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int l = this.table.getCookProgressionScaled();
		blit(stack, i + 96, j + 40, 0, 211, l, 16);
		if (this.table.getCraftingTable().hasRat()) {
			blit(stack, i + 8, j + 20, 176, 0, 21, 21);
		} else {
			blit(stack, i + 7, j + 40, 198, 0, 21, 21);
		}
		if (this.table.getCraftingTable().getRecipeUsed() == null) {
			blit(stack, i + 95, j + 38, 198, 0, 21, 21);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		Font font = this.font;
		String s = this.getTitle().getString();
		font.draw(stack, s, this.imageWidth / 2.0F - font.width(s) / 2.0F, 5, 4210752);
		font.draw(stack, this.playerInventory.getDisplayName().getString(), 8, this.imageHeight - 93, 4210752);
		font.draw(stack, Component.translatable("container.rat_crafting_table.input"), 8, this.imageHeight - 125, 4210752);
		int screenW = (this.width - 248) / 2;
		int screenH = (this.height - 166) / 2;
		Optional<CraftingRecipe> recipe = this.table.getCraftingTable().getGuideRecipe();
		if (recipe.isPresent() && !this.table.getSlot(0).hasItem()) {
			this.itemRenderer.renderGuiItem(stack, recipe.get().getResultItem(Minecraft.getInstance().level.registryAccess()), 130, 40);
			RenderSystem.disableDepthTest();
			GuiComponent.fill(stack, 130, 40, 146, 56, 0x9f8b8b8b);
			RenderSystem.enableDepthTest();
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				RenderSystem.disableDepthTest();
				int xPos = 36 + i * 18;
				int yPos = 22 + j * 18;
				GuiComponent.fill(stack, xPos, yPos, xPos + 16, yPos + 16, 0x9f8b8b8b);
				RenderSystem.enableDepthTest();
			}
		}

		if (!this.table.getCraftingTable().hasRat()) {
			if (mouseX > screenW + 40 && mouseX < screenW + 65 && mouseY > screenH + 14 && mouseY < screenH + 40) {
				Component ratDesc = Component.translatable("container.rat_crafting_table.rat_desc");
				this.renderTooltip(stack, Minecraft.getInstance().font.split(ratDesc, 200), mouseX - screenW - 40, mouseY - screenH + 10);
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
		public void renderWidget(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
			if (Minecraft.getInstance().screen instanceof RatCraftingTableScreen table && !table.shouldRenderButtons())
				return;
			if (this.visible) {
				RenderSystem._setShaderTexture(0, RatCraftingTableScreen.TEXTURE);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;

				int textureX = 22;
				int textureY = 211;

				if (this.isHovered) textureX += this.width;

				if (!this.up) textureY += this.height;

				blit(ms, this.getX(), this.getY(), textureX, textureY, this.width, this.height);
			}
		}
	}
}