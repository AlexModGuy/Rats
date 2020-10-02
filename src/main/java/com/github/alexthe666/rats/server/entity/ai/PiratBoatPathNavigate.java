package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.ratlantis.EntityPiratBoat;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.World;

public class PiratBoatPathNavigate extends SwimmerPathNavigator {
    private EntityPiratBoat boat;

    public PiratBoatPathNavigate(EntityPiratBoat LivingEntityIn, World worldIn) {
        super(LivingEntityIn, worldIn);
        this.boat = LivingEntityIn;
    }

    protected boolean isInLiquid() {
        return this.entity.isInWater() || this.boat.getBoatStatus() != BoatEntity.Status.ON_LAND && this.boat.getBoatStatus() != BoatEntity.Status.IN_AIR;
    }
}
