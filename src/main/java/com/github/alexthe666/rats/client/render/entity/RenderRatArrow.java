package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityRatArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderRatArrow extends RenderArrow<EntityRatArrow> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat_arrow.png");

    public RenderRatArrow() {
        super(Minecraft.getMinecraft().getRenderManager());
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityRatArrow entity) {
        return TEXTURE;
    }
}
