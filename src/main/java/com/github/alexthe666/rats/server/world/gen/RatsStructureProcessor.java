package com.github.alexthe666.rats.server.world.gen;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template;

public class RatsStructureProcessor implements ITemplateProcessor {

    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private float integrity = 1F;

    public RatsStructureProcessor(float integrity) {
        this.integrity = integrity;
    }

    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (worldIn.rand.nextFloat() <= integrity) {
            if (worldIn.getBlockState(pos).getBlock() == Blocks.VINE) {
                return null;
            }
            if (blockInfoIn.blockState.getBlock() == RatsBlockRegistry.MARBLED_CHEESE_BRICK) {
                BlockState state = RatStructure.getRandomCrackedBlock(null, worldIn.rand);
                return new Template.BlockInfo(pos, state, null);
            }
            return blockInfoIn;
        }
        return null;
    }

}
