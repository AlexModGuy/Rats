package com.github.alexthe666.rats.server.entity.ai.navigation;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;

public class FlyingRatPathNavigate extends FlyingPathNavigator {

    public EntityRat rat;

    public FlyingRatPathNavigate(EntityRat entityIn, World worldIn) {
        super(entityIn, worldIn);
        rat = entityIn;
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        if (rat.hasFlightUpgrade()) {
            rat.getMoveHelper().setMoveTo(entityIn.getPosX(), entityIn.getPosY() + entityIn.getHeight(), entityIn.getPosZ(), 0.25D);
        }
        Path path = this.getPathToEntity(entityIn, 0);
        return path != null && this.setPath(path, speedIn);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        if (rat.hasFlightUpgrade()) {
            rat.getMoveHelper().setMoveTo(x, y, z, speedIn * 0.25D);
        }
        return true;
    }
}
