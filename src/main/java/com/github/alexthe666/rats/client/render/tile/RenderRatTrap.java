package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatTrap;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRatTrap extends TileEntityRenderer<TileEntityRatTrap> {
    private static final ModelRatTrap MODEL_RAT_TRAP = new ModelRatTrap();
    private static final RenderType TEXTURE = RenderType.getEntityCutout(new ResourceLocation("rats:textures/model/rat_trap.png"));

    public RenderRatTrap(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityRatTrap entity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float rotation = 0;
        float shutProgress = 0;
        ItemStack bait = ItemStack.EMPTY;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatTrap) {
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockRatTrap.FACING) == Direction.NORTH) {
                rotation = 180;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockRatTrap.FACING) == Direction.EAST) {
                rotation = -90;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).get(BlockRatTrap.FACING) == Direction.WEST) {
                rotation = 90;
            }
            shutProgress = entity.shutProgress;
            bait = entity.getBait();
        }
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 1.5D, 0.5D);
        matrixStackIn.push();

        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rotation, true));
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(TEXTURE);
        MODEL_RAT_TRAP.animateHinge(shutProgress);
        MODEL_RAT_TRAP.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!bait.isEmpty()) {
            matrixStackIn.scale(0.4F, 0.4F, 0.4F);
            matrixStackIn.translate(0, 3.4F, -0.5F);
            matrixStackIn.rotate(new Quaternion(Vector3f.XP, 90, true));
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 180, true));
            Minecraft.getInstance().getItemRenderer().renderItem(bait, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        }
        matrixStackIn.pop();
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
