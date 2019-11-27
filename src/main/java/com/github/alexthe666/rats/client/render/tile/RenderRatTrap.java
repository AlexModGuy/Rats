package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatTrap;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderRatTrap extends TileEntitySpecialRenderer<TileEntityRatTrap> {
    private static final ModelRatTrap MODEL_RAT_TRAP = new ModelRatTrap();
    private static final ResourceLocation TEXTURE = new ResourceLocation("rats:textures/model/rat_trap.png");

    @Override
    public void render(TileEntityRatTrap entity, double x, double y, double z, float f, int f1, float alpha) {
        float rotation = 0;
        float shutProgress = 0;
        ItemStack bait = ItemStack.EMPTY;
        if (entity != null && entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos()).getBlock() instanceof BlockRatTrap && entity instanceof TileEntityRatTrap) {
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.NORTH) {
                rotation = 180;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.EAST) {
                rotation = -90;
            }
            if (entity.getWorld().getBlockState(entity.getPos()).getValue(BlockRatTrap.FACING) == Direction.WEST) {
                rotation = 90;
            }
            shutProgress = entity.shutProgress;
            bait = entity.getBait();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(rotation, 0, 1F, 0);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        MODEL_RAT_TRAP.render(0.0625F, shutProgress);
        if (!bait.isEmpty()) {
            GL11.glScalef(0.4F, 0.4F, 0.4F);
            GL11.glTranslated(0, 3.4F, -0.5F);
            GL11.glRotatef(90, 1F, 0, 0);
            GL11.glRotatef(180, 0, 1F, 0);
            Minecraft.getMinecraft().getRenderItem().renderItem(bait, ItemCameraTransforms.TransformType.FIXED);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }
}
