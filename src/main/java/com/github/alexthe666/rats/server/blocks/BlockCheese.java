package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCheese extends Block {
    public BlockCheese() {
        super(Block.Properties.create(Material.WOOL).sound(SoundType.SLIME).hardnessAndResistance(2.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "block_of_cheese");
    }


}
