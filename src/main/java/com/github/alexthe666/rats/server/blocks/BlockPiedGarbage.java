package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockPiedGarbage extends BlockGarbage {

    public BlockPiedGarbage() {
        super("pied_garbage", 1.0D);
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (random.nextFloat() <= RatConfig.garbageSpawnRate * spawnRateModifier) {
            EntityIllagerPiper rat = new EntityIllagerPiper(RatsEntityRegistry.PIED_PIPER, worldIn);
            rat.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0, 0);
            int i = worldIn.isThundering() ? worldIn.getNeighborAwareLightSubtracted(pos.up(), 10) : worldIn.getLight(pos.up());
            if (i <= random.nextInt(8) && !rat.isEntityInsideOpaqueBlock() && rat.isNotColliding(worldIn)) {
                rat.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);
                if (!worldIn.isRemote) {
                    worldIn.addEntity(rat);
                }
            }
        }
    }

}
