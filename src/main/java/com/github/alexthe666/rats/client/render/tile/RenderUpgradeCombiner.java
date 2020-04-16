package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatlanteanSpirit;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderUpgradeCombiner extends TileEntityRenderer<TileEntityUpgradeCombiner> {
    private static final ModelRatlanteanSpirit MODEL_SPIRIT = new ModelRatlanteanSpirit();
    private static final RenderType TEXTURE = RenderType.getEntityCutout(new ResourceLocation("rats:textures/entity/ratlantis/upgrade_combiner.png"));

    public RenderUpgradeCombiner(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityUpgradeCombiner entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        float f = (float) entity.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        matrixStackIn.translate(0.0F, 3F + MathHelper.sin(f * 0.1F) * 0.1F, 0.0F);
        float f1;

        for (f1 = entity.ratRotation - entity.ratRotationPrev; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
        }

        while (f1 < -(float) Math.PI) {
            f1 += ((float) Math.PI * 2F);
        }
        float f2 = entity.ratRotationPrev + f1 * Minecraft.getInstance().getRenderPartialTicks();;
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, -f2 * (180F / (float) Math.PI), true));
        matrixStackIn.rotate(new Quaternion(Vector3f.ZP, 180, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90, true));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        MODEL_SPIRIT.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.pop();
    }
}
