package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

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
            rat.onInitialSpawn(world, world.getDifficultyForLocation(this.getPosition()), SpawnReason.NATURAL, null, null);
            if (!world.isRemote) {
                world.addEntity(rat);
            }
        }
    }

    public static boolean func_223325_c(EntityType<? extends CreatureEntity> p_223325_0_, IWorld p_223325_1_, SpawnReason p_223325_2_, BlockPos p_223325_3_, Random p_223325_4_) {
        return func_223323_a(p_223325_1_, p_223325_3_, p_223325_4_) && canSpawnOn(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
    }

    public static boolean func_223323_a(IWorld p_223323_0_, BlockPos p_223323_1_, Random p_223323_2_) {
        if (p_223323_0_.getLightFor(LightType.SKY, p_223323_1_) > p_223323_2_.nextInt(32)) {
            return false;
        } else {
            int i = p_223323_0_.getWorld().isThundering() ? p_223323_0_.getNeighborAwareLightSubtracted(p_223323_1_, 10) : p_223323_0_.getLight(p_223323_1_);
            return i <= p_223323_2_.nextInt(8);
        }
    }

}