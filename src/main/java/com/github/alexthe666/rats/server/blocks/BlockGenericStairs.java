package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

public class BlockGenericStairs extends StairsBlock {

    protected BlockGenericStairs(BlockState modelState, Block.Properties properties, String name) {
        super(modelState, properties);
        this.setRegistryName(RatsMod.MODID, name);
    }

}
