package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatFish;
import com.github.alexthe666.rats.server.entity.EntityRatfish;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class RenderRatfish extends MobRenderer<EntityRatfish, ModelRatFish<EntityRatfish>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/ratfish.png");

    public RenderRatfish() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRatFish<>(0), 0.3F);
    }

    @Nullable
    protected ResourceLocation getEntityTexture(EntityRatfish entity) {
        return TEXTURE;
    }

    protected void applyRotations(EntityRatfish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
        GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
        if (!entityLiving.isInWater()) {
            GlStateManager.translatef(0.1F, 0.1F, -0.1F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
