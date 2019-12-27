package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelNeoRatlantean;
import com.github.alexthe666.rats.server.entity.EntityLaserPortal;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderLaserPortal extends EntityRenderer<EntityLaserPortal> {

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("rats:textures/entity/ratlantis/neo_ratlantean_glow.png");
    private static final ModelNeoRatlantean MODEL_NEO_RATLANTEAN = new ModelNeoRatlantean();

    public RenderLaserPortal() {
        super(Minecraft.getInstance().getRenderManager());
        MODEL_NEO_RATLANTEAN.floatyPivot.setRotationPoint(0, 0, 0);
        MODEL_NEO_RATLANTEAN.floatyPivot.rotateAngleY = 0.7853981633974483F;
    }

    public void doRender(EntityLaserPortal entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.translatef((float) x, (float) y, (float) z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlphaTest();
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        float d1 = this.interpolateValue(entity.scaleOfPortalPrev, entity.scaleOfPortal, (partialTicks));
        GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(1.5F * d1, 1.5F * d1, 1.5F * d1);
        GlStateManager.translatef(0, 0.5F, 0);
        GlStateManager.translatef(0, 1 - d1, 0);
        GlStateManager.rotatef(90, 1, 0, 0);
        GlStateManager.rotatef(90 + entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0F, 0, 1F);
        GlStateManager.rotatef(entity.ticksExisted * 10, 0, 1, 0);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 61680.0F, 0.0F);
        MODEL_NEO_RATLANTEAN.floatyPivot.render(0.0625F);
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
    protected ResourceLocation getEntityTexture(EntityLaserPortal entity) {
        return TEXTURE_EYES;
    }
}
