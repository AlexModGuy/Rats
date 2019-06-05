package com.github.alexthe666.rats.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class ChangeCommandButton extends GuiButton {
    private final boolean right;

    public ChangeCommandButton(int id, int x, int y, boolean right) {
        super(id, x, y, 7, 11, "");
        this.right = right;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        if (this.enabled) {
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 166;
            if (flag) {
                i += 14;
            }
            if (this.right) {
                i += 7;
            }
            this.drawTexturedModalRect(this.x, this.y, i, j, width, height);
        }
    }
}
