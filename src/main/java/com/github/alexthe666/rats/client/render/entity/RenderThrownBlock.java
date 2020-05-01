package com.github.alexthe666.rats.client.render.entity;

import com.github.alexthe666.rats.client.model.ModelCube;
import com.github.alexthe666.rats.server.entity.EntityThrownBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RenderThrownBlock extends Render<EntityThrownBlock> {
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/psychic.png");
    private static final ModelCube MODEL_CUBE = new ModelCube(1.1F);

    public RenderThrownBlock() {
        super(Minecraft.getMinecraft().getRenderManager());
        this.shadowSize = 0.5F;
    }

    public void doRender(EntityThrownBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        if (entity.fallTile != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
            this.bindEntityTexture(entity);
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, 0.5F);
            try{
                blockrendererdispatcher.renderBlockBrightness(entity.fallTile, entity.getBrightness());
            }catch (Exception e){

            }
            GlStateManager.translate(0.0F, 0.0F, 1.0F);

            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
                try{
                    blockrendererdispatcher.renderBlockBrightness(entity.fallTile, 1.0F);
                }catch (Exception e){

                }
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.depthMask(false);
        this.bindTexture(LIGHTNING_TEXTURE);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        float f = (float) entity.ticksExisted + partialTicks;
        GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.enableBlend();
        float f1 = 0.5F;
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        MODEL_CUBE.render(entity, 0, 0, 0, 0, 0, 0.0625F);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityThrownBlock entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}