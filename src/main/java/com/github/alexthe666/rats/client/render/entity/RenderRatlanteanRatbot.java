package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanRatbot;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityRatlanteanRatbot;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRatlanteanRatbot extends MobRenderer<EntityRatlanteanRatbot, ModelRatlanteanRatbot<EntityRatlanteanRatbot>> {
    private static final ResourceLocation RATBOT_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/ratlantean_ratbot.png");

    public RenderRatlanteanRatbot() {
        super(Minecraft.getInstance().getRenderManager(), new ModelRatlanteanRatbot(0.0F), 0.5F);
        this.addLayer(new LayerRatbotEyes(this));

    }

    public ResourceLocation getEntityTexture(EntityRatlanteanRatbot entity) {
        return RATBOT_TEXTURE;
    }

    protected void preRenderCallback(EntityRatlanteanRatbot rat, MatrixStack matrixStackIn, float partialTickTime) {
        super.preRenderCallback(rat, matrixStackIn, partialTickTime);
        matrixStackIn.scale(1.9F, 1.9F, 1.9F);
        if (!((double)rat.limbSwingAmount < 0.01D)) {
            float f = 13.0F;
            float f1 = rat.limbSwing - rat.limbSwingAmount * (1.0F - partialTickTime) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 6.5F * f2, true));
        }
    }
}