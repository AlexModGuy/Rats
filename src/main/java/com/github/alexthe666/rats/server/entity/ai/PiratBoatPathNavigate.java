package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPiratBoat;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.world.World;

public class PiratBoatPathNavigate extends PathNavigateSwimmer {

    public PiratBoatPathNavigate(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected boolean isInLiquid()
    {
        return this.entity.isOverWater();
    }
}
