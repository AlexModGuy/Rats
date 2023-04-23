package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.JuryRiggedRatUpgradeMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class JuryRiggedRatUpgradeScreen extends AbstractContainerScreen<JuryRiggedRatUpgradeMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/rat_upgrade_jury_rigged.png");

	public JuryRiggedRatUpgradeScreen(JuryRiggedRatUpgradeMenu container, Inventory playerInventory, Component name) {
		super(container, playerInventory, name);
		this.imageHeight = 132;
		this.inventoryLabelY = 38;
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
		blit(stack, i, j, 0, 0, this.imageWidth, 35);
		blit(stack, i, j + 35, 0, 126, this.imageWidth, 96);
	}
}
