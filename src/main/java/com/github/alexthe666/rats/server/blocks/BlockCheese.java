package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCheese extends Block {
    public BlockCheese() {
        super(Material.CLOTH);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.SLIME);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.block_of_cheese");
        this.setRegistryName(RatsMod.MODID, "block_of_cheese");
    }
}
