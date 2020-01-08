package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUpgradeCombiner extends TileEntityRenderer<TileEntityUpgradeCombiner> {
    private static final ModelRatlanteanSpirit MODEL_SPIRIT = new ModelRatlanteanSpirit();
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/upgrade_combiner.png");

    @Override
    public void render(TileEntityUpgradeCombiner te, double x, double y, double z, float aplah, int destroyProgress) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        float f = (float) te.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        GlStateManager.translatef(0.0F, 3F + MathHelper.sin(f * 0.1F) * 0.1F, 0.0F);
        float f1;

        for (f1 = te.ratRotation - te.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
        }

        while (f1 < -(float) Math.PI) {
            f1 += ((float) Math.PI * 2F);
        }
        float f2 = te.ratRotationPrev + f1 * Minecraft.getInstance().getRenderPartialTicks();;
        GlStateManager.rotatef(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(180, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(90, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TEXTURE);
        GlStateManager.disableCull();
        GlStateManager.scalef(1.7F, 1.7F, 1.7F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        MODEL_SPIRIT.render(null, 0, 0.0F, f, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
