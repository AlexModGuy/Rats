package com.github.alexthe666.rats.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class CommandPressButton extends Button {

	public CommandPressButton(int x, int y, Button.OnPress onPress) {
		super(x, y, 76, 16, Component.literal(""), onPress, message -> Component.empty());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			boolean flag = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
			int i = 0;
			int j = 177;
			if (flag) {
				j += 16;
			}

			graphics.blit(RatScreen.TEXTURE, this.getX(), this.getY(), i, j, this.width, this.height);

		}
	}
}
