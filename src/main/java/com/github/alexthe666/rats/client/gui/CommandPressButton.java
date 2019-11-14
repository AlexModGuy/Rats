package com.github.alexthe666.rats.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class CommandPressButton extends GuiButton {

    public CommandPressButton(int id, int x, int y) {
        super(id, x, y, 76, 16, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (this.enabled) {
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 177;
            if (flag) {
                j += 16;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, width, height);
        }
    }
}
