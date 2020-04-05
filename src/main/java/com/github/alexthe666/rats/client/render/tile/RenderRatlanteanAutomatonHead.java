package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelMarbledCheeseGolem;
import com.github.alexthe666.rats.server.blocks.BlockMarbledCheeseRatHead;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlanteanAutomatonHead;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class RenderRatlanteanAutomatonHead extends TileEntityRenderer<TileEntityRatlanteanAutomatonHead> {
    private static final ModelMarbledCheeseGolem MODEL_GOLEM = new ModelMarbledCheeseGolem();
    private static final ResourceLocation GOLEM_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation("rats:textures/entity/ratlantis/marble_cheese_golem_glow.png");

    @Override
    public void render(TileEntityRatlanteanAutomatonHead te, double x, double y, double z, float aplah, int destroyProgress) {
        float rotation = 0;
        float ticksExisted = Minecraft.getInstance().player.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        GlStateManager.pushMatrix();
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
            ticksExisted = (float) te.ticksExisted + Minecraft.getInstance().getRenderPartialTicks();
        }
        GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GlStateManager.translatef(0.0F, -1.0F + MathHelper.sin(ticksExisted * 0.1F) * 0.1F, 0.0F);
        GlStateManager.pushMatrix();
        GL11.glRotatef(rotation + 90, 0, -1F, 0);
        GlStateManager.rotatef(180, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(90, 0.0F, 1.0F, 0.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(GOLEM_TEXTURE);
        MODEL_GOLEM.renderHead(0.0625F, ticksExisted);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GL11.glRotatef(rotation + 90, 0, -1F, 0);
        GlStateManager.rotatef(180, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(90, 0.0F, 1.0F, 0.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(GLOW_TEXTURE);
        GlStateManager.disableCull();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 0.0F);
        MODEL_GOLEM.renderHead(0.0625F, ticksExisted);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
