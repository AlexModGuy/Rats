package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelCube;
import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class RenderThrownBlock extends EntityRenderer<EntityThrownBlock> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private static final ModelCube MODEL_CUBE = new ModelCube(1.1F);

    public RenderThrownBlock() {
        super(Minecraft.getInstance().getRenderManager());
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityThrownBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        if (entity.fallTile != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
            this.bindEntityTexture(entity);
            GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
            blockrendererdispatcher.renderBlockBrightness(entity.fallTile, entity.getBrightness());
            GlStateManager.translatef(0.0F, 0.0F, 1.0F);
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
                blockrendererdispatcher.renderBlockBrightness(entity.fallTile, 1.0F);
                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.depthMask(false);
        this.bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = (float) entity.ticksExisted + partialTicks;
        GlStateManager.translatef(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        float f1 = 0.5F;
        GlStateManager.color4f(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        MODEL_CUBE.render(entity, 0, 0, 0, 0, 0, 0.0625F);
        gamerenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getEntityTexture(EntityThrownBlock entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}