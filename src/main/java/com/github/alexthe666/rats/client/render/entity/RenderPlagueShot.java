package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderPlagueShot extends EntityRenderer<EntityPlagueShot> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/plague_cloud.png");
    private static final ModelRatlanteanSpirit MODEL_SPIRIT = new ModelRatlanteanSpirit();

    public RenderPlagueShot() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void doRender(EntityPlagueShot entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.translatef((float) x, (float) y, (float) z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        //GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.5F, -1.5F, 1.5F);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 61680.0F, 0.0F);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        GlStateManager.translatef(0F, -1.5F, 0F);
        GlStateManager.rotatef(yaw - 180, 0.0F, 1.0F, 0.0F);
        MODEL_SPIRIT.render(entity, 0, 0, partialTicks, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityPlagueShot entity) {
        return TEXTURE;
    }
}
