package com.github.alexthe666.rats.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import org.lwjgl.opengl.GL11;

public class ChangeCommandButton extends Button {
    private final boolean right;

    public ChangeCommandButton(int id, int x, int y, boolean right, Button.IPressable onPress) {
        super(x, y, 7, 11, "", onPress);
        this.right = right;
    }

    @Override
    public void render(int mouseX, int mouseY, float partial) {
        if (Minecraft.getInstance().currentScreen instanceof GuiRatCraftingTable) {
            if (!((GuiRatCraftingTable) Minecraft.getInstance().currentScreen).shouldRenderButtons()) {
                return;
            }
        }
        if (this.active) {
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().textureManager.bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 166;
            if (flag) {
                i += 14;
            }
            if (this.right) {
                i += 7;
            }
            this.blit(this.x, this.y, i, j, width, height);
        }
    }
}
