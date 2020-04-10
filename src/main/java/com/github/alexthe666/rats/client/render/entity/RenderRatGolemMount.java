package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatGolemMount;
import com.github.alexthe666.rats.server.entity.EntityRatGolemMount;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderRatGolemMount extends MobRenderer<EntityRatGolemMount, ModelRatGolemMount<EntityRatGolemMount>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/mount/golem_mount.png");

    public RenderRatGolemMount() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRatGolemMount(), 0.75F);

    }

    protected ResourceLocation getEntityTexture(EntityRatGolemMount entity) {
        return TEXTURE;
    }

    protected void preRenderCallback(EntityRatGolemMount rat, float partialTickTime) {
        super.preRenderCallback(rat, partialTickTime);

    }
}