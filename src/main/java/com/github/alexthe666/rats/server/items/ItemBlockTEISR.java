package com.github.alexthe666.rats.server.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.Callable;

public class ItemBlockTEISR extends BlockItem {

    public ItemBlockTEISR(Block block, Properties prop) {
        super(block, prop.setTEISR(ItemBlockTEISR::getTEISR));
    }

    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return com.github.alexthe666.rats.client.render.tile.RatsTEISR::new;
    }


}
