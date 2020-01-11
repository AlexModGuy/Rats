package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.server.entity.EntityDutchratSword;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderDutchratSword extends EntityRenderer<EntityDutchratSword> {

    private static final ItemStack PIRAT_SWORD = new ItemStack(RatsItemRegistry.GHOST_PIRAT_CUTLASS);

    public RenderDutchratSword() {
        super(Minecraft.getInstance().getRenderManager());
    }

    public void doRender(EntityDutchratSword entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y, (float) z);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(entityYaw, 0F, 1, 0);
        GlStateManager.translatef(0, 0.5F, 0);
        GlStateManager.pushMatrix();
        GlStateManager.scalef(3F, 3F, 3F);
        GlStateManager.rotatef(90, 0, 1, 0);
        GlStateManager.rotatef((entity.ticksExisted + partialTicks) * 10, 0, 0, 1);
        GlStateManager.translatef(0, -0.15F, 0);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        minecraft.getItemRenderer().renderItem(PIRAT_SWORD, entity.getCreator() == null ? minecraft.player : entity.getCreator(), ItemCameraTransforms.TransformType.GROUND, false);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }
        this.func_217758_e(entity);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDutchratSword entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
