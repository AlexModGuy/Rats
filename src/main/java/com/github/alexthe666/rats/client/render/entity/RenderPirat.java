package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderPirat extends RenderRat {

    public RenderPirat() {
        super();
    }

    protected void preRenderCallback(EntityRat rat, MatrixStack stack, float partialTickTime) {
        super.preRenderCallback(rat, stack, partialTickTime);
        stack.scale(1.6F, 1.6F, 1.6F);
    }
}
