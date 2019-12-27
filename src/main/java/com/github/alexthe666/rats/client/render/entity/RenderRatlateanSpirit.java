package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.EntityPlagueCloud;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class RenderRatlateanSpirit<T extends MobEntity> extends MobRenderer<T, ModelRatlanteanSpirit<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rat/ratlantean_spirit.png");
    private static final ResourceLocation TEXTURE_CLOUD = new ResourceLocation("rats:textures/entity/plague_cloud.png");

    public RenderRatlateanSpirit() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRatlanteanSpirit(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(MobEntity entity) {
        return entity instanceof EntityPlagueCloud ? TEXTURE_CLOUD : TEXTURE;
    }

    protected void preRenderCallback(MobEntity LivingEntityIn, float partialTickTime) {
        doPortalEffect(LivingEntityIn, partialTickTime);
        float scale = LivingEntityIn instanceof EntityPlagueCloud ? 2 : 1.5F;
        GlStateManager.scalef(scale, scale, scale);
    }

    public void doPortalEffect(Entity entity, float partialTicks) {
    }
}