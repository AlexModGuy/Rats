package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelRattlingGun;
import com.github.alexthe666.rats.server.entity.EntityRattlingGun;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderRattlingGun extends EntityRenderer<EntityRattlingGun> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/rattling_gun.png");
    private static final ResourceLocation TEXTURE_FIRING = new ResourceLocation("rats:textures/entity/rattling_gun_firing.png");
    public static final ModelRattlingGun GUN_MODEL = new ModelRattlingGun<>();

    public RenderRattlingGun() {
        super(Minecraft.getInstance().getRenderManager());
    }

    protected void preRenderCallback(EntityRattlingGun rat, float partialTickTime) {
    }

    public void doRender(EntityRattlingGun entity, double x, double y, double z, float entityYaw, float partialTicks) {
        renderGun(entity, x, y, z, entityYaw, partialTicks, TEXTURE, false);
        if(entity.isFiring()){
            renderGun(entity, x, y, z, entityYaw, partialTicks, TEXTURE_FIRING, true);
        }
    }

    public void renderGun(EntityRattlingGun entity, double x, double y, double z, float entityYaw, float partialTicks, ResourceLocation texture, boolean glow) {
        GlStateManager.pushMatrix();
        if(glow){
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlphaTest();
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        }
        GlStateManager.translatef((float) x, (float) y, (float) z);
        float f = 0.0625F;
          this.bindTexture(texture);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }
        GUN_MODEL.setRotationAngles(entity, 0, 0, entity.ticksExisted + partialTicks, 0, 0, 0.0625F);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 1.5F, 0);
        GlStateManager.rotatef(180, 1.0F, 0.0F, 0.0F);
        GUN_MODEL.base1.render(0.0625F);
        GL11.glPushMatrix();
        GlStateManager.rotatef(90 + entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0F, 1F, 0F);
        GUN_MODEL.pivot.render(0.0625F);
        GL11.glPopMatrix();
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }
        if(glow){
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    public ResourceLocation getEntityTexture(EntityRattlingGun entity) {
        return TEXTURE;
    }
}
