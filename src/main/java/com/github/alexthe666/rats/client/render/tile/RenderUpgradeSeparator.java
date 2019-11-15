package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeSeparator;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUpgradeSeparator extends TileEntitySpecialRenderer<TileEntityUpgradeSeparator> {
    private static ItemStack RENDER_STACK = new ItemStack(RatsItemRegistry.ANCIENT_SAWBLADE);

    @Override
    public void render(TileEntityUpgradeSeparator te, double x, double y, double z, float partialTicks, int unused, float alpha) {
        GlStateManager.pushMatrix();
        float f = (float) te.ratRotationPrev + (te.ratRotation - te.ratRotationPrev) * partialTicks;
        GlStateManager.translate((float) x + 0.5F, (float) y + 1 + (Math.sin(f * 0.3F) * 0.1F), (float) z + 0.5F);
        GlStateManager.rotate(f * 1.5F, 0.0F, 1.0F, 0.0F);
        GlStateManager.disableCull();
        GlStateManager.translate(0, 0, -0.2F);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(1.7F, 1.7F, 1.7F);
        Minecraft.getMinecraft().getRenderItem().renderItem(RENDER_STACK, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
    }
}
