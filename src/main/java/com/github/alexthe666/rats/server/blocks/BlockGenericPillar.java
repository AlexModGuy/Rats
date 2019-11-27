package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGenericPillar extends RotatedPillarBlock {

    public BlockGenericPillar(String name, Material mat, float hardness, float resistance, SoundType sound) {
        super(Block.Properties.create(mat).sound(sound).hardnessAndResistance(hardness, resistance));
        this.setRegistryName(RatsMod.MODID, name);
    }
}
