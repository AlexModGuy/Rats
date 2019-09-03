package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockRatgloveFlower extends BlockBush {

    public BlockRatgloveFlower() {
        super(Material.CLOTH);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.ratglove_flower");
        this.setRegistryName(RatsMod.MODID, "ratglove_flower");
    }

    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT;
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }
}
