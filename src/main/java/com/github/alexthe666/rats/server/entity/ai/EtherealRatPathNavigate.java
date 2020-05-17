package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EtherealRatPathNavigate extends FlyingPathNavigator {

    public EntityRat rat;

    public EtherealRatPathNavigate(EntityRat entityIn, World worldIn) {
        super(entityIn, worldIn);
        rat = entityIn;
    }

    protected PathFinder getPathFinder(int p_179679_1_) {
        this.nodeProcessor = new RatWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        this.nodeProcessor.setCanSwim(true);
        return new PathFinder(this.nodeProcessor, p_179679_1_);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        rat.getMoveHelper().setMoveTo(entityIn.getPosX(), entityIn.getPosY() + entityIn.getHeight(), entityIn.getPosZ(), speedIn);
        Path path = this.getPathToEntity(entityIn, 0);
        return path != null && this.setPath(path, speedIn);
    }

    public boolean tryMoveToXYZ(double x, double y, double z, double speedIn) {
        rat.getMoveHelper().setMoveTo(x, y, z, speedIn);

        return this.setPath(this.getPathToPos(new BlockPos(x, y, z), 0), speedIn);
    }
}
