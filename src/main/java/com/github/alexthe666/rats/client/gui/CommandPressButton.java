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
    public void func_230431_b_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.field_230694_p_) {
            boolean flag = mouseX >= this.field_230690_l_ && mouseY >= this.field_230691_m_ && mouseX < this.field_230690_l_ + this.field_230688_j_ && mouseY < this.field_230691_m_ + this.field_230689_k_;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GuiRat.TEXTURE);
            int i = 0;
            int j = 177;
            if (flag) {
                j += 16;
            }

            this.func_238474_b_(matrixStack, this.field_230690_l_, this.field_230691_m_, i, j, this.field_230688_j_, this.field_230689_k_);

        }
    }
}
