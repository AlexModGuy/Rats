package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class RatsStructureProcessor implements ITemplateProcessor {

    private float integrity = 1F;
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    public RatsStructureProcessor(float integrity) {
        this.integrity = integrity;
    }

    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (worldIn.rand.nextFloat() <= integrity) {
            if (worldIn.getBlockState(pos).getBlock() == Blocks.VINE) {
                return null;
            }
            if (blockInfoIn.blockState.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK) {
                IBlockState state = RatStructure.getRandomCrackedBlock(null, worldIn.rand);
                return new Template.BlockInfo(pos, state, null);
            }
            return blockInfoIn;
        }
        return null;
    }

}
