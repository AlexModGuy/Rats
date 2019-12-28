package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AquaticRatPathNavigate extends FlyingPathNavigator {

    public EntityRat rat;

    public AquaticRatPathNavigate(EntityRat entityIn, World worldIn) {
        super(entityIn, worldIn);
        rat = entityIn;
    }

    public boolean tryMoveToLivingEntity(Entity entityIn, double speedIn) {
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC)) {
            rat.getMoveHelper().setMoveTo(entityIn.posX, entityIn.posY + entityIn.getHeight(), entityIn.posZ, 0.25D);
        }
        Path path = this.getPathToEntityLiving(entityIn, 0);
        return path != null && this.setPath(path, speedIn);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        if (rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_AQUATIC)) {
            rat.getMoveHelper().setMoveTo(x, y, z, 0.25D);
        }
        return this.setPath(this.getPathToPos(new BlockPos(x, y, z), 0), speedIn);
    }
}
