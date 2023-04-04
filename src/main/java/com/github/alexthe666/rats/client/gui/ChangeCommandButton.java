package com.github.alexthe666.rats.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ChangeCommandButton extends Button {
	private final boolean right;

	public ChangeCommandButton(int x, int y, boolean right, Button.OnPress onPress) {
		super(x, y, 7, 11, Component.literal(""), onPress, message -> Component.empty());
		this.right = right;
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			boolean flag = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			RenderSystem.setShaderTexture(0, RatScreen.TEXTURE);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			int i = 0;
			int j = 166;
			if (flag) {
				i += 14;
			}
			if (this.right) {
				i += 7;
			}
			blit(matrixStack, this.getX(), this.getY(), i, j, this.width, this.height);
		}
	}
}
