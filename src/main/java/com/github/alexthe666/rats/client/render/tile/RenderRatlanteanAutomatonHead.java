package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelMarbledCheeseGolem;
import com.github.alexthe666.rats.server.blocks.BlockMarbledCheeseRatHead;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlanteanAutomatonHead;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class RenderRatlanteanAutomatonHead extends TileEntityRenderer<TileEntityRatlanteanAutomatonHead> {
    private static final ModelMarbledCheeseGolem MODEL_GOLEM = new ModelMarbledCheeseGolem();
    private static final RenderType GOLEM_TEXTURE = RenderType.getEntityCutout(new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem.png"));
    private static final RenderType GLOW_TEXTURE = RenderType.getEyes(new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem_glow.png"));

    public RenderRatlanteanAutomatonHead(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityRatlanteanAutomatonHead te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        float rotation = 0;
        float ticksExisted = Minecraft.getInstance().player.ticksExisted + partialTicks;
        if (te != null && te.getWorld() != null && te.getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockMarbledCheeseRatHead) {
            if (te.getWorld().getBlockState(te.getPos()).get(BlockMarbledCheeseRatHead.FACING) == Direction.NORTH) {
                rotation = 180;
            }
            if (te.getWorld().getBlockState(te.getPos()).get(BlockMarbledCheeseRatHead.FACING) == Direction.EAST) {
                rotation = -90;
            }
            if (te.getWorld().getBlockState(te.getPos()).get(BlockMarbledCheeseRatHead.FACING) == Direction.WEST) {
                rotation = 90;
            }
            ticksExisted = te.ticksExisted + partialTicks;
        }
        matrixStackIn.push();
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, -0.5F, 0.5F);
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180, true));
        matrixStackIn.rotate(new Quaternion(Vector3f.YP, rotation, true));
        matrixStackIn.push();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(GOLEM_TEXTURE);
        MODEL_GOLEM.setTERotationAngles(ticksExisted);
        MODEL_GOLEM.renderHead(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        IVertexBuilder ivertexbuilder2 = bufferIn.getBuffer(GLOW_TEXTURE);
        MODEL_GOLEM.renderHead(matrixStackIn, ivertexbuilder2, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();
        matrixStackIn.pop();

    }
}
