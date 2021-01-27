package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeSeparator;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderUpgradeSeparator extends TileEntityRenderer<TileEntityUpgradeSeparator> {
    private static ItemStack RENDER_STACK = new ItemStack(RatlantisItemRegistry.ANCIENT_SAWBLADE);

    public RenderUpgradeSeparator(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityUpgradeSeparator entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(RatsMod.RATLANTIS_LOADED){
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, 0.15D, 0.5D);
            float f = (float) entity.ratRotationPrev + Minecraft.getInstance().getRenderPartialTicks();
            matrixStackIn.translate(0.0F, 1F + MathHelper.sin(f * 0.1F) * 0.1F, 0.0F);
            float f1;

            for (f1 = entity.ratRotation - entity.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
            }

            while (f1 < -(float) Math.PI) {
                f1 += ((float) Math.PI * 2F);
            }
            float f2 = entity.ratRotationPrev + f1 * Minecraft.getInstance().getRenderPartialTicks();
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, -f2 * 0.1F * (180F / (float) Math.PI), true));
            matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 180, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90, true));
            Minecraft.getInstance().getItemRenderer().renderItem(RENDER_STACK, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
            matrixStackIn.pop();
        }

    }
}
