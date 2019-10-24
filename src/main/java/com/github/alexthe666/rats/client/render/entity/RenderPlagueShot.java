package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelNeoRatlantean;
import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.EntityLaserPortal;
import com.github.alexthe666.rats.server.entity.EntityPlagueCloud;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderPlagueShot extends Render<EntityPlagueShot> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/plague_cloud.png");
    private static final ModelRatlanteanSpirit MODEL_SPIRIT = new ModelRatlanteanSpirit();

    public RenderPlagueShot() {
        super(Minecraft.getMinecraft().getRenderManager());
    }

    public void doRender(EntityPlagueShot entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.translate((float) x, (float) y, (float) z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5F, -1.5F, 1.5F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 0.0F);
        float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        GlStateManager.translate(0F, -1.5F, 0F);
        GlStateManager.rotate(yaw - 180, 0.0F, 1.0F, 0.0F);
        MODEL_SPIRIT.render(entity, 0, 0, partialTicks, 0, 0, 0.0625F);
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
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
