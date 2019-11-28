package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;


public class BlockGenericSlab extends SlabBlock {

    protected BlockGenericSlab(Block.Properties properties, String name) {
        super(properties);
        this.setRegistryName(RatsMod.MODID, name);
    }

}
