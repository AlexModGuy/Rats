package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.block.entity.UpgradeCombinerBlockEntity;
import com.github.alexthe666.rats.server.inventory.UpgradeCombinerMenu;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class UpgradeCombinerScreen extends AbstractContainerScreen<UpgradeCombinerMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/upgrade_combiner.png");
	private final Inventory playerInventory;
	private final UpgradeCombinerMenu combiner;

	public UpgradeCombinerScreen(UpgradeCombinerMenu container, Inventory inv, Component name) {
		super(container, inv, name);
		this.playerInventory = inv;
		this.combiner = container;
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
		int l = this.combiner.getCookProgressScaled();
		blit(stack, i + 92, j + 35, 176, 14, l + 1, 16);
		if (!UpgradeCombinerBlockEntity.canCombine(this.combiner.container.getItem(0), this.combiner.container.getItem(2)) && !this.combiner.container.getItem(0).isEmpty() && !this.combiner.container.getItem(2).isEmpty()) {
			blit(stack, i + 42, j + 34, 198, 31, 21, 21);
		}
		if (!RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			blit(stack, i + 44, j + 57, 0, 166, 16, 16);
		}
		if (this.combiner.getBurnLeftScaled() > 0) {
			int k = this.combiner.getBurnLeftScaled();
			blit(stack, i + 70, j + 58 + 12 - k, 176, 12 - k, 14, k + 1);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		String s = this.getTitle().getString();
		this.font.draw(stack, s, this.imageWidth / 2.0F - this.font.width(s) / 2.0F, 5, 4210752);
		this.font.draw(stack, this.playerInventory.getDisplayName().getString(), 8, this.imageHeight - 94 + 2, 4210752);
		int screenW = (this.width - this.imageWidth) / 2;
		int screenH = (this.height - this.imageHeight) / 2;
		if (UpgradeCombinerBlockEntity.canCombine(this.combiner.container.getItem(0), this.combiner.container.getItem(2)) && !this.combiner.container.getItem(0).isEmpty() && !this.combiner.container.getItem(2).isEmpty()) {
			if (mouseX > screenW + 42 && mouseX < screenW + 63 && mouseY > screenH + 34 && mouseY < screenH + 55) {
				Component ratDesc = Component.translatable("container.upgrade_combiner.cannot_combine");
				List<Component> list = List.of(ratDesc);
				this.renderTooltip(stack, Lists.transform(list, Component::getVisualOrderText), mouseX - screenW, mouseY - screenH + 10);
			}
		}
	}
}