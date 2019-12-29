package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class RatAIFleePosition extends Goal {
    private EntityRat rat;
    private Path path;

    public RatAIFleePosition(EntityRat rat) {
        this.rat = rat;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (this.rat.fleePos != null && !rat.isInCage()) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.rat, 32, 7, new Vec3d(this.rat.fleePos).add(0.5D, 0.5D, 0.5D));
            if (vec3d == null) {
                return false;
            } else if (this.rat.fleePos.distanceSq(vec3d.x, vec3d.y, vec3d.z, true) < this.rat.getDistanceSq(this.rat.fleePos.getX(), this.rat.fleePos.getY(), this.rat.fleePos.getZ())) {
                return false;
            } else {
                this.path = this.rat.getNavigator().getPathToPos(new BlockPos(vec3d.x, vec3d.y, vec3d.z), 0);
                return this.path != null;
            }
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.rat.getNavigator().noPath() && this.rat.fleePos != null && this.rat.getDistanceSq(this.rat.fleePos.getX(), this.rat.fleePos.getY(), this.rat.fleePos.getZ()) < 32;
    }

    public void startExecuting() {
        this.rat.getNavigator().setPath(this.path, 1.225D);
    }

    public void resetTask() {
        this.path = null;
        this.rat.fleePos = null;
    }
}
