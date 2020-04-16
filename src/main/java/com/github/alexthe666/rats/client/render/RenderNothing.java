package com.github.alexthe666.rats.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderNothing extends EntityRenderer<Entity> {

    public RenderNothing() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void render(Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
