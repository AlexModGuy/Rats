package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

public class BlockGenericStairs extends BlockStairs {

    protected BlockGenericStairs(IBlockState modelState, String name) {
        super(modelState);
        this.setLightOpacity(0);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(name);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
