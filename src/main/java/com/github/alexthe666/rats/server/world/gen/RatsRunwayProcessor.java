package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class RatsRunwayProcessor extends StructureProcessor {

    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, BlockPos pos2, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings, @Nullable Template template) {
        if (blockInfoIn2.state.getBlock() == Blocks.GRASS) {
            Random rand = settings.getRandom(blockInfoIn2.pos);
            if(rand.nextBoolean()){
                Template.BlockInfo newInfo = new Template.BlockInfo(blockInfoIn2.pos, RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.getDefaultState(), null);
                return newInfo;
            }
        }
        if (blockInfoIn2.state.getBlock() == Blocks.DIRT) {
            Random rand = settings.getRandom(blockInfoIn2.pos);
            if(rand.nextBoolean()){
                Template.BlockInfo newInfo = new Template.BlockInfo(blockInfoIn2.pos, RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.getDefaultState(), null);
                return newInfo;
            }
        }
        return blockInfoIn2;
    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

}
