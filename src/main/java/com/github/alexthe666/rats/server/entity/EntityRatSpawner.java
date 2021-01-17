package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EntityRatSpawner extends CreatureEntity {

    boolean hasSpawnedRat = false;

    protected EntityRatSpawner(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void tick() {
        if(hasSpawnedRat){
           this.remove();
        }else{
            hasSpawnedRat = true;
            EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, world);
            rat.copyLocationAndAnglesFrom(this);
            if(!world.isRemote){
                rat.onInitialSpawn((ServerWorld)world, world.getDifficultyForLocation(this.getOnPosition()), SpawnReason.NATURAL, null, null);
            }
            if (!world.isRemote) {
                world.addEntity(rat);
            }
        }
    }

    public static boolean func_223325_c(EntityType<? extends CreatureEntity> p_223325_0_, IWorld p_223325_1_, SpawnReason p_223325_2_, BlockPos p_223325_3_, Random p_223325_4_) {
        boolean peaceful = p_223325_1_.getDifficulty() == Difficulty.PEACEFUL;
        if(peaceful){
            return false;
        }
        if(RatConfig.ratOverworldOnly && ((ServerWorld)p_223325_1_).getDimensionKey() != World.OVERWORLD){
            return false;
        }
        return func_223323_a(p_223325_1_, p_223325_3_, p_223325_4_) && canSpawnOn(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_) && p_223325_4_.nextInt(peaceful ? 32 : 3) == 0;
    }

    public static boolean func_223323_a(IWorld p_223323_0_, BlockPos p_223323_1_, Random p_223323_2_) {
        if (p_223323_0_.getLightFor(LightType.SKY, p_223323_1_) > p_223323_2_.nextInt(32)) {
            return false;
        } else {
            int i = ((ServerWorld)p_223323_0_).isThundering() ? p_223323_0_.getNeighborAwareLightSubtracted(p_223323_1_, 10) : p_223323_0_.getLight(p_223323_1_);
            return i <= p_223323_2_.nextInt(8);
        }
    }

}