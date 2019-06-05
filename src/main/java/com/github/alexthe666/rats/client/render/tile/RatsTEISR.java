package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RatsTEISR extends TileEntityItemStackRenderer {

    private RenderRatHole renderRatHole = new RenderRatHole();
    private RenderRatTrap renderRatTrap = new RenderRatTrap();

    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE)) {
            renderRatHole.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP)) {
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            renderRatTrap.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
    }
}