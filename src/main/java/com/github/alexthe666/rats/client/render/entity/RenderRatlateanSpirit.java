package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.EntityPlagueCloud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.ResourceLocation;

public class RenderRatlateanSpirit extends RenderLiving<EntityMob> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/ratlantean_spirit.png");
    private static final ResourceLocation TEXTURE_CLOUD = new ResourceLocation("rats:textures/entity/plague_cloud.png");

    public RenderRatlateanSpirit() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelRatlanteanSpirit(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityMob entity) {
        return entity instanceof EntityPlagueCloud ? TEXTURE_CLOUD : TEXTURE;
    }

    protected void preRenderCallback(EntityMob entitylivingbaseIn, float partialTickTime) {
        doPortalEffect(entitylivingbaseIn, partialTickTime);
        float scale = entitylivingbaseIn instanceof EntityPlagueCloud ? 2 : 1.5F;
        GlStateManager.scale(scale, scale, scale);
    }

    public void doPortalEffect(Entity entity, float partialTicks) {
    }
}