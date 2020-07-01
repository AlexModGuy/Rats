package com.github.alexthe666.rats.server.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;

public class RatsStructureVineProcessor extends StructureProcessor {

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH};
    private static final BlockState VINE = Blocks.VINE.getDefaultState();
    private float integrity = 1F;

    public RatsStructureVineProcessor(float integrity) {
        this.integrity = integrity;
    }

    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        Random random = settings.getRandom(pos);
        if(blockInfoIn.state.getMaterial().isReplaceable()){
            for (Direction offsetDir : HORIZONTALS) {
                BlockPos offsetPos = pos.offset(offsetDir);
                if (worldIn.getBlockState(offsetPos).isOpaqueCube(worldIn, offsetPos)) {
                    for (net.minecraft.util.Direction secondOffset : HORIZONTALS) {
                        if (!worldIn.getBlockState(offsetPos.offset(secondOffset)).isOpaqueCube(worldIn, offsetPos) && random.nextInt(8) == 0) {
                            Direction opposFacing = secondOffset.getOpposite();
                            BlockState vineState = Blocks.VINE.getDefaultState().with(VineBlock.NORTH, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.NORTH)).with(VineBlock.EAST, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.EAST)).with(VineBlock.SOUTH, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.SOUTH)).with(VineBlock.WEST, Boolean.valueOf(opposFacing == net.minecraft.util.Direction.WEST));
                            return new Template.BlockInfo(pos, vineState, null);
                        }
                    }
                }
            }
        }

        return blockInfoIn;
    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

}
