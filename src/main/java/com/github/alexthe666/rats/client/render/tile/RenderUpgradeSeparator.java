package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeSeparator;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;

public class RenderUpgradeSeparator extends TileEntityRenderer<TileEntityUpgradeSeparator> {
    private static ItemStack RENDER_STACK = new ItemStack(RatsItemRegistry.ANCIENT_SAWBLADE);

    @Override
    public void render(TileEntityUpgradeSeparator te, double x, double y, double z, float alpha, int destroyProg) {
        GlStateManager.pushMatrix();
        float f = te.ratRotationPrev + (te.ratRotation - te.ratRotationPrev) * Minecraft.getInstance().getRenderPartialTicks();
        GlStateManager.translatef((float) x + 0.5F, (float) (y + 1 + (Math.sin(f * 0.3F) * 0.1F)), (float) z + 0.5F);
        GlStateManager.rotatef(f * 1.5F, 0.0F, 1.0F, 0.0F);
        GlStateManager.disableCull();
        GlStateManager.translatef(0, 0, -0.2F);
        GlStateManager.rotatef(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.scalef(1.7F, 1.7F, 1.7F);
        Minecraft.getInstance().getItemRenderer().renderItem(RENDER_STACK, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
    }
}
