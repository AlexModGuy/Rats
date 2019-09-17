package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.world.World;

public class FlyingRatPathNavigate extends PathNavigateFlying {

    public EntityRat rat;

    public FlyingRatPathNavigate(EntityRat entityIn, World worldIn) {
        super(entityIn, worldIn);
        rat = entityIn;
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
            rat.getMoveHelper().setMoveTo((double) entityIn.posX, (double) entityIn.posY + entityIn.height, (double) entityIn.posZ, 0.25D);
        }
        Path path = this.getPathToEntityLiving(entityIn);
        return path != null && this.setPath(path, speedIn);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT)) {
            rat.getMoveHelper().setMoveTo(x, y, z, 0.25D);
        }
        return this.setPath(this.getPathToXYZ(x, y, z), speedIn);
    }
}
