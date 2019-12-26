package com.github.alexthe666.rats.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import org.lwjgl.opengl.GL11;

public class CommandPressButton extends Button {

    public CommandPressButton(int id, int x, int y, Button.IPressable onPress) {
        super(x, y, 76, 16, "", onPress);
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        if (this.active) {
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 177;
            if (flag) {
                j += 16;
            }
            this.blit(this.x, this.y, i, j, width, height);
        }
    }
}
