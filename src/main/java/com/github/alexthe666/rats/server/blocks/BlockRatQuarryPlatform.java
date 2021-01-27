package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockRatQuarryPlatform extends Block implements INoTab{

    public BlockRatQuarryPlatform() {
        super(Block.Properties.create(Material.WOOL).sound(SoundType.SLIME).notSolid().variableOpacity().hardnessAndResistance(1.0F, 0.0F));
        this.setRegistryName(RatsMod.MODID, "rat_quarry_platform");
    }

    public boolean isOpaqueCube(IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Deprecated
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        switch(type) {
            case LAND:
                return false;
            case WATER:
                return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
            case AIR:
                return !state.hasOpaqueCollisionShape(worldIn, pos);
            default:
                return false;
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }
}
