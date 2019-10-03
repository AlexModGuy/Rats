package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGeneric extends Block {
    public BlockGeneric(String name, Material mat, float hardness, float resistance, SoundType sound) {
        super(mat);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
    }

    public BlockGeneric(String name, Material mat, float hardness, float resistance, SoundType sound, float light) {
        super(mat);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
        this.setLightLevel(light);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        if (this == RatsBlockRegistry.MARBLED_CHEESE_DIRT) {
            return BlockRenderLayer.CUTOUT;
        } else {
            return super.getRenderLayer();
        }
    }

}
