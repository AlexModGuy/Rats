package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

public class BlockJackORatern extends HorizontalBlock {

    public BlockJackORatern() {
        super(Block.Properties.create(Material.GOURD).sound(SoundType.WOOD).hardnessAndResistance(1.0F, 0).func_235838_a_((p) -> 15));
        this.setRegistryName(RatsMod.MODID, "jack_o_ratern");
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }
}