package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.client.model.ModelRatTrap;
import com.github.alexthe666.rats.server.blocks.BlockRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTrap;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatTube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderRatTubeDebug extends TileEntitySpecialRenderer<TileEntityRatTube> {
    @Override
    public void render(TileEntityRatTube entity, double x, double y, double z, float f, int f1, float alpha) {
        /*GlStateManager.pushMatrix();
        double dist = 50;
        List<AxisAlignedBB> boundingBoxList = new ArrayList<>();
        entity.getBlockType().addCollisionBoxToList(entity.getWorld().getBlockState(entity.getPos()),
                entity.getWorld(), entity.getPos(), new AxisAlignedBB(x - dist, y - dist, z - dist, x + dist, y + dist, z + dist),
                boundingBoxList, Minecraft.getMinecraft().player, false);

        for(AxisAlignedBB aabb : boundingBoxList){
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            Render.renderOffsetAABB(aabb, 0, 0, 0);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();*/
    }
}
