package com.github.alexthe666.rats.server.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class PiratBoatPathNavigate extends SwimmerPathNavigator {

    public PiratBoatPathNavigate(MobEntity LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
    }

    protected boolean isInLiquid() {
        //TODO this.entity.isOverWater()
        return this.entity.isInWater();
    }
}
