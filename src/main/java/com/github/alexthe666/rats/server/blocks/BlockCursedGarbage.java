package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockCursedGarbage extends BlockGarbage {

    public BlockCursedGarbage() {
        super("cursed_garbage", 1.0D);
    }

    public void onSpawnRat(EntityRat rat){
        rat.setPlague(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(20) == 0) {
            BlockPos blockpos = pos.up();
            if (worldIn.isAirBlock(blockpos)) {
                double d0 = (double)pos.getX() + (double)rand.nextFloat();
                double d1 = (double)pos.getY() + 1.05D;
                double d2 = (double)pos.getZ() + (double)rand.nextFloat();
                worldIn.addParticle(ParticleTypes.ENTITY_EFFECT, d0, d1, d2, 0, rand.nextGaussian() * 0.05D + 0.75D, 0);
            }
        }

    }
}
