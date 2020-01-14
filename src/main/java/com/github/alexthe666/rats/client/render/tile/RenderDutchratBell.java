package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.entity.tile.TileEntityDutchratBell;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.BellModel;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderDutchratBell extends TileEntityRenderer<TileEntityDutchratBell> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/dutchrat_bell.png");
    private final BellModel field_217654_d = new BellModel();

    public void render(TileEntityDutchratBell tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        this.bindTexture(TEXTURE);
        GlStateManager.translatef((float)x, (float)y, (float)z);
        float f = (float)tileEntityIn.field_213943_a + partialTicks;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (tileEntityIn.field_213944_b) {
            float f3 = MathHelper.sin(f / (float)Math.PI) / (0.1F + f / 3.0F);
            if (tileEntityIn.field_213945_c == Direction.NORTH) {
                f1 = -f3;
            } else if (tileEntityIn.field_213945_c == Direction.SOUTH) {
                f1 = f3;
            } else if (tileEntityIn.field_213945_c == Direction.EAST) {
                f2 = -f3;
            } else if (tileEntityIn.field_213945_c == Direction.WEST) {
                f2 = f3;
            }
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
        gamerenderer.setupFogColor(true);
        this.field_217654_d.func_217099_a(f1, f2, 0.0625F);
        gamerenderer.setupFogColor(false);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}