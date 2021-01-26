package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatlantisBlockRegistry;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class RatsStructureProcessor extends StructureProcessor {

    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private final float integrity;

    public RatsStructureProcessor(float integrity) {
        this.integrity = integrity;
    }

    public RatsStructureProcessor(Dynamic<?> p_i51333_1_) {
        this(p_i51333_1_.get("integrity").asFloat(1.0F));
    }

    public RatsStructureProcessor() {
        integrity = 1.0F;
    }


    public Template.BlockInfo process(IWorldReader worldReader, BlockPos pos, BlockPos pos2, Template.BlockInfo infoIn1, Template.BlockInfo infoIn2, PlacementSettings settings, @Nullable Template template) {
        Random random = settings.getRandom(infoIn2.pos);
        if (random.nextFloat() <= integrity) {
            if (infoIn2.state.getBlock() == RatlantisBlockRegistry.MARBLED_CHEESE_BRICK) {
                return new Template.BlockInfo(infoIn2.pos, RatStructure.getRandomCrackedBlock(infoIn2.state, random), null);
            }
        }
        return infoIn2;
    }

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

}
