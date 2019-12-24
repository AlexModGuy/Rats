package com.github.alexthe666.rats.server.entity.ai;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class PiratPathNavigate extends SwimmerPathNavigator {

    public PiratPathNavigate(MobEntity entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    protected boolean isInLiquid() {
        return this.entity.isInWaterOrBubbleColumn() || this.entity.isInLava() || this.entity.isPassenger();
    }
}