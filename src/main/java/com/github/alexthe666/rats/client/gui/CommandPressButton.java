package com.github.alexthe666.rats.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CommandPressButton extends Button {

	public CommandPressButton(int x, int y, Button.OnPress onPress) {
		super(x, y, 76, 16, Component.literal(""), onPress, message -> Component.empty());
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			boolean flag = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, RatScreen.TEXTURE);
			int i = 0;
			int j = 177;
			if (flag) {
				j += 16;
			}

			blit(matrixStack, this.getX(), this.getY(), i, j, this.width, this.height);

		}
	}
}
