package com.github.alexthe666.rats.server.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.world.World;

public class PiratBoatPathNavigate extends PathNavigateSwimmer {

    public PiratBoatPathNavigate(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected boolean isInLiquid() {
        return this.entity.isOverWater();
    }
}
