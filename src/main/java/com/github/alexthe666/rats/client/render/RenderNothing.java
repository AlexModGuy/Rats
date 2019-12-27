package com.github.alexthe666.rats.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderNothing extends EntityRenderer<Entity> {

    public RenderNothing() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
