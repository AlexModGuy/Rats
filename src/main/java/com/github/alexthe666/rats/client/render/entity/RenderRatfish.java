package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatFish;
import com.github.alexthe666.rats.server.entity.EntityRatfish;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3f;
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
    public ResourceLocation getEntityTexture(EntityRatfish entity) {
        return TEXTURE;
    }

    protected void applyRotations(EntityRatfish entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float f = 1.0F;
        float f1 = 1.0F;
        if (!entityLiving.isInWater()) {
            f = 1.3F;
            f1 = 1.7F;
        }

        float f2 = f * 4.3F * MathHelper.sin(f1 * 0.6F * ageInTicks);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f2));
        matrixStackIn.translate(0.0D, 0.0D, (double)-0.4F);
        if (!entityLiving.isInWater()) {
            matrixStackIn.translate((double)0.2F, (double)0.1F, 0.0D);
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
        }
    }
}
