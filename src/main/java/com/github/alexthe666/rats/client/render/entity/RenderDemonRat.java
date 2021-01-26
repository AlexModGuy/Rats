package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.ResourceLocation;

public class RenderDemonRat extends RenderRat {

    public static final ResourceLocation BASE_TEXTURE = new ResourceLocation("rats:textures/entity/rat/demon_rat.png");

    public RenderDemonRat() {
        super();
    }


    protected void preRenderCallback(EntityRat rat, MatrixStack stack, float partialTickTime) {
        stack.scale(1.5F, 1.5F, 1.5F);
        super.preRenderCallback(rat, stack, partialTickTime);

    }

    public ResourceLocation getEntityTexture(EntityRat entity) {
        return BASE_TEXTURE;
    }
}