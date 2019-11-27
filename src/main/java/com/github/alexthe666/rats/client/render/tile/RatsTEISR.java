package com.github.alexthe666.rats.client.render.tile;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeCombiner;
import com.github.alexthe666.rats.server.entity.tile.TileEntityUpgradeSeparator;
import net.minecraft.client.renderer.tileentity.TileItemEntityStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RatsTEISR extends TileItemEntityStackRenderer {

    private RenderRatHole renderRatHole = new RenderRatHole();
    private RenderRatTrap renderRatTrap = new RenderRatTrap();
    private RenderAutoCurdler renderAutoCurdler = new RenderAutoCurdler();
    private RenderRatlantisPortal renderRatlantisPortal = new RenderRatlantisPortal();
    private RenderUpgradeCombiner renderUpgradeCombiner = new RenderUpgradeCombiner();
    private RenderUpgradeSeparator renderUpgradeSeparator = new RenderUpgradeSeparator();
    private TileEntityUpgradeCombiner dummyCombiner = new TileEntityUpgradeCombiner();
    private TileEntityUpgradeSeparator dummySeparator = new TileEntityUpgradeSeparator();

    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.UPGRADE_COMBINER)) {
            renderUpgradeCombiner.render(dummyCombiner, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        //if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.UPGRADE_SEPARATOR)) {
        //    renderUpgradeSeparator.render(dummySeparator, 0, 0, 0, 0.0F, 0, 0.0F);
        //}
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_HOLE)) {
            renderRatHole.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RAT_TRAP)) {
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            renderRatTrap.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.AUTO_CURDLER)) {
            renderAutoCurdler.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(RatsBlockRegistry.RATLANTIS_PORTAL)) {
            renderRatlantisPortal.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }

    }
}