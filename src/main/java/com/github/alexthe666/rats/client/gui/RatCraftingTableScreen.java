package com.github.alexthe666.rats.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.RatCraftingTableMenu;
import com.github.alexthe666.rats.server.message.CycleRatRecipePacket;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import com.mojang.blaze3d.systems.RenderSystem;
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

// 1.21: RecipeBookComponent / RecipeUpdateListener require the menu to be a RecipeBookMenu<I,R>,
// which RatCraftingTableMenu intentionally is not (see comment there). The recipe-book sidebar is
// therefore not present on this screen; cycle-result buttons and the cooking-progress display
// remain so the rat-driven crafting flow still has full UI affordance.
public class RatCraftingTableScreen extends AbstractContainerScreen<RatCraftingTableMenu> {
	private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(RatsMod.MODID, "textures/gui/container/rat_crafting_table.png");
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
		this.addRenderableWidget(new CycleResultButton(this.leftPos + 100, this.topPos + 58, false, button -> {
			this.table.incrementRecipeIndex(false);
			PacketDistributor.sendToServer(new CycleRatRecipePacket(this.table.getCraftingTable().getBlockPos().asLong(), false));
		}));
		this.addRenderableWidget(new CycleResultButton(this.leftPos + 100, this.topPos + 28, true, button -> {
			this.table.incrementRecipeIndex(true);
			PacketDistributor.sendToServer(new CycleRatRecipePacket(this.table.getCraftingTable().getBlockPos().asLong(), true));
		}));
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics, mouseX, mouseY, partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(graphics, mouseX, mouseY);

		RenderSystem.disableDepthTest();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				int xPos = 36 + i * 18;
				int yPos = 22 + j * 18;
				graphics.pose().pushPose();
				graphics.pose().translate(this.leftPos, this.topPos, 300.0D);
				graphics.fill(xPos, yPos, xPos + 16, yPos + 16, 0x9f8b8b8b);
				graphics.pose().popPose();
			}
		}
		RenderSystem.enableDepthTest();

		Optional<CraftingRecipe> recipe = this.table.getCraftingTable().getGuideRecipe();
		if (recipe.isPresent() && !this.table.getSlot(0).hasItem()) {
			graphics.renderItem(recipe.get().getResultItem(Minecraft.getInstance().level.registryAccess()), this.leftPos + 130, this.topPos + 40);
			RenderSystem.disableDepthTest();
			graphics.pose().pushPose();
			graphics.pose().translate(this.leftPos, this.topPos, 300.0D);
			graphics.fill(130, 40, 146, 56, 0x9f8b8b8b);
			graphics.pose().popPose();
			RenderSystem.enableDepthTest();
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		int l = this.table.getCookProgressionScaled();
		graphics.blit(TEXTURE, this.leftPos + 96, this.topPos + 39, 0, 211, l, 16);
		if (this.table.getCraftingTable().hasRat()) {
			graphics.blit(TEXTURE, this.leftPos + 8, this.topPos + 20, 176, 0, 21, 21);
		} else {
			graphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 40, 198, 0, 21, 21);
		}
		if (this.table.getCraftingTable().getRecipeUsed() == null) {
			graphics.blit(TEXTURE, this.leftPos + 95, this.topPos + 38, 220, 0, 21, 21);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		Font font = this.font;
		String s = this.getTitle().getString();
		graphics.drawString(this.font, s, this.imageWidth / 2.0F - font.width(s) / 2.0F, 5, 4210752, false);
		graphics.drawString(this.font, this.playerInventory.getDisplayName().getString(), 8, this.imageHeight - 93, 4210752, false);
		graphics.drawString(this.font, Component.translatable(RatsLangConstants.CRAFTING_INPUT), 8, this.imageHeight - 125, 4210752, false);
		int screenW = (this.width - 248) / 2;
		int screenH = (this.height - 166) / 2;

		if (!this.table.getCraftingTable().hasRat()) {
			if (this.isHovering(6, 34, 25, 29, mouseX, mouseY)) {
				Component ratDesc = Component.translatable(RatsLangConstants.CRAFTING_NEEDS_RAT);
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
