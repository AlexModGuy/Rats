package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelTrashCan;
import com.github.alexthe666.rats.server.blocks.BlockTrashCan;
import com.github.alexthe666.rats.server.entity.tile.TileEntityTrashCan;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderTrashCan extends TileEntityRenderer<TileEntityTrashCan> {
    private static final ModelTrashCan MODEL_TRASH_CAN = new ModelTrashCan();
    private static final RenderType TEXTURE = RenderType.func_230167_a_(new ResourceLocation("rats:textures/model/trash_can.png"), true);

    public RenderTrashCan(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityTrashCan entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float rotation = 0;
        float shutProgress = 0;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockTrashCan) {
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockTrashCan.FACING) == Direction.NORTH) {
                rotation = 180;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockTrashCan.FACING) == Direction.EAST) {
                rotation = -90;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockTrashCan.FACING) == Direction.WEST) {
                rotation = 90;
            }
            shutProgress = entity.lidProgress;
        }
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.501D, 0.5D);
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rotation, true));
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        MODEL_TRASH_CAN.animate(entity);
        MODEL_TRASH_CAN.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
