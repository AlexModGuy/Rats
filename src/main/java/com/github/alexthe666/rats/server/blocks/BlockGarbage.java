package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockGarbage extends Block {
    public BlockGarbage() {
        super(Material.GROUND);
        this.setHardness(0.7F);
        this.setSoundType(SoundType.GROUND);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.garbage_pile");
        this.setRegistryName(RatsMod.MODID, "garbage_pile");
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return rand.nextInt(3) == 0 ? RatsItemRegistry.PLASTIC_WASTE : super.getItemDropped(state, rand, fortune);
    }

}
