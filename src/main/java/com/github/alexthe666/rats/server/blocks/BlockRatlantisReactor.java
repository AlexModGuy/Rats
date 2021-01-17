package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisPortal;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatlantisReactor;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockRatlantisReactor extends ContainerBlock {

    protected BlockRatlantisReactor() {
        super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(100.0F).setLightLevel((p) -> 15));
        this.setRegistryName(RatsMod.MODID, "ratlantis_reactor");
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityRatlantisReactor();
    }

}
