package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.world.structure.RatlantisStructureRegistry;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

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


    public Template.BlockInfo process(IWorldReader worldReader, BlockPos pos, Template.BlockInfo infoIn1, Template.BlockInfo infoIn2, PlacementSettings settings) {
        Random random = settings.getRandom(infoIn2.pos);
        if (random.nextFloat() <= integrity) {
            if (infoIn2.state.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK) {
                return new Template.BlockInfo(infoIn2.pos, RatStructure.getRandomCrackedBlock(infoIn2.state, random), null);
            }
        }
        return infoIn2;
    }

    @Override
    protected IStructureProcessorType getType() {
        return RatlantisStructureRegistry.RAT_RUINS_PROCESSOR;
    }

    @Override
    protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("integrity"), ops.createFloat(this.integrity))));
    }

}
