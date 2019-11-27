package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.BlockState;

public class BlockGenericStairs extends BlockStairs {

    protected BlockGenericStairs(BlockState modelState, String name) {
        super(modelState);
        this.setLightOpacity(0);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }
}
