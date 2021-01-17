package com.github.alexthe666.rats.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class CommandPressButton extends Button {

    public CommandPressButton(int id, int x, int y, Button.IPressable onPress) {
        super(x, y, 76, 16, new StringTextComponent(""), onPress);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 177;
            if (flag) {
                j += 16;
            }

            this.blit(matrixStack, this.x, this.y, i, j, this.width, this.height);

        }
    }
}
