package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatPathPathNavigateGround extends PathNavigateGround {

    public BlockPos targetPosition;

    public RatPathPathNavigateGround(EntityRat entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    public Path getPathToEntityLiving(Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn);

        if (path != null) {
            return this.setPath(path, speedIn);
        } else {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
    }

    public void onUpdateNavigation() {
        super.onUpdateNavigation();
        ((EntityRat)this.entity).setTubeTarget(this.targetPosition);
    }
}
