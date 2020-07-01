package com.github.alexthe666.rats.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class ChangeCommandButton extends Button {
    private final boolean right;

    public ChangeCommandButton(int id, int x, int y, boolean right, Button.IPressable onPress) {
        super(x, y, 7, 11, new StringTextComponent(""), onPress);
        this.right = right;
    }

    @Override
    public void func_230431_b_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (Minecraft.getInstance().currentScreen instanceof GuiRatCraftingTable) {
            if (!((GuiRatCraftingTable) Minecraft.getInstance().currentScreen).shouldRenderButtons()) {
                return;
            }
        }
        if (this.field_230694_p_) {
            boolean flag = mouseX >= this.field_230690_l_ && mouseY >= this.field_230691_m_ && mouseX < this.field_230690_l_ + this.field_230688_j_ && mouseY < this.field_230691_m_ + this.field_230689_k_;
            Minecraft.getInstance().textureManager.bindTexture(GuiRat.TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 0;
            int j = 166;
            if (flag) {
                i += 14;
            }
            if (this.right) {
                i += 7;
            }
            this.func_238474_b_(matrixStack, this.field_230690_l_, this.field_230691_m_, i, j, this.field_230688_j_, this.field_230689_k_);
        }
    }
}
