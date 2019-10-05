package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUpgradeCombiner extends TileEntitySpecialRenderer<TileEntityUpgradeCombiner> {
    private static final ModelRatlanteanSpirit MODEL_SPIRIT = new ModelRatlanteanSpirit();
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/upgrade_combiner.png");

    @Override
    public void render(TileEntityUpgradeCombiner te, double x, double y, double z, float partialTicks, int unused, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        float f = (float) te.ticksExisted + partialTicks;
        GlStateManager.translate(0.0F, 3F + MathHelper.sin(f * 0.1F) * 0.1F, 0.0F);
        float f1;

        for (f1 = te.ratRotation - te.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
        }

        while (f1 < -(float) Math.PI) {
            f1 += ((float) Math.PI * 2F);
        }
        float f2 = te.ratRotationPrev + f1 * partialTicks;
        GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
        this.bindTexture(TEXTURE);
        GlStateManager.disableCull();
        GlStateManager.scale(1.7F, 1.7F, 1.7F);
        MODEL_SPIRIT.render(null, 0, 0.0F, f, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
