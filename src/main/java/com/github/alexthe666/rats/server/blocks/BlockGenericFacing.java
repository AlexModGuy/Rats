package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGenericFacing extends Block {

    public static final DirectionProperty FACING = DirectionProperty.create("facing");

    public BlockGenericFacing(String name, Material mat, float hardness, float resistance, SoundType sound) {
        super(Block.Properties.create(mat).sound(sound).hardnessAndResistance(hardness, resistance));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        this.setRegistryName(RatsMod.MODID, name);
    }


    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
