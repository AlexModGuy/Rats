package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.AutoCurdlerMenu;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;

public class AutoCurdlerScreen extends AbstractContainerScreen<AutoCurdlerMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(RatsMod.MODID, "textures/gui/container/auto_curdler.png");
	private final AutoCurdlerMenu curdler;

	public AutoCurdlerScreen(AutoCurdlerMenu container, Inventory inv, Component name) {
		super(container, inv, name);
		this.curdler = container;
	}

	public static void renderFluidStack(PoseStack stack, int xPosition, int yPosition, int desiredWidth, int desiredHeight, Fluid fluid) {
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid).getStillTexture());
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		int xTileCount = desiredWidth / 16;
		int xRemainder = desiredWidth - (xTileCount * 16);
		int yTileCount = desiredHeight / 16;
		int yRemainder = desiredHeight - (yTileCount * 16);
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		RenderSystem.enableBlend();
		BufferBuilder vertexBuffer = Tesselator.getInstance().getBuilder();
		vertexBuffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		Matrix4f matrix4f = stack.last().pose();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int width = (xTile == xTileCount) ? xRemainder : 16;
			if (width == 0) {
				break;
			}
			int x = xPosition + (xTile * 16);
			int maskRight = 16 - width;
			int shiftedX = x + 16 - maskRight;
			float uLocalDif = uDif * maskRight / 16;

			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int height = (yTile == yTileCount) ? yRemainder : 16;
				if (height == 0) {
					break;
				}
				int y = yPosition - ((yTile + 1) * 16);
				int maskTop = 16 - height;
				float vLocalDif = vDif * maskTop / 16;

				vertexBuffer.vertex(matrix4f, x, y + 16, 0).uv(uMin + uLocalDif, vMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + 16, 0).uv(uMax, vMax).endVertex();
				vertexBuffer.vertex(matrix4f, shiftedX, y + maskTop, 0).uv(uMax, vMin + vLocalDif).endVertex();
				vertexBuffer.vertex(matrix4f, x, y + maskTop, 0).uv(uMin + uLocalDif, vMin + vLocalDif).endVertex();
			}
		}
		BufferUploader.drawWithShader(vertexBuffer.end());
		RenderSystem.disableBlend();
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(stack);
		super.render(stack, mouseX, mouseY, partialTicks);
		this.renderTooltip(stack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack stack, float partialTicks, int x, int y) {
		this.renderBackground(stack);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;

		blit(stack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int l = this.curdler.getCookProgressionScaled();
		blit(stack, i + 63, j + 35, 176, 0, l + 1, 16);
		int tankWidth = 24;
		int tankHeight = 63;
		int amount = Math.round((this.curdler.getFluidAmount() / (float) this.curdler.getTankCapacity()) * (tankHeight - 4));
		renderFluidStack(stack, i + 29, j + 73, tankWidth, amount, ForgeMod.MILK.get());
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(stack, i + 29, j + 12, 0, 166, tankWidth, tankHeight);
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		int screenW = (this.width - this.imageWidth) / 2;
		int screenH = (this.height - this.imageHeight) / 2;

		if (mouseX > screenW + 29 && mouseX < screenW + 53 && mouseY > screenH + 15 && mouseY < screenH + 73) {
			String fluidName = new FluidStack(ForgeMod.MILK.get(), this.curdler.getTankCapacity()).getDisplayName().getString();
			String fluidSize = this.curdler.getFluidAmount() + " " + Component.translatable("container.auto_curdler.mb").getString();
			List<Component> list = Arrays.asList(Component.literal(fluidName).withStyle(ChatFormatting.BLUE), Component.literal(fluidSize).withStyle(ChatFormatting.GRAY));
			this.renderTooltip(stack, Lists.transform(list, Component::getVisualOrderText), mouseX - screenW, mouseY - screenH);
		}
	}
}