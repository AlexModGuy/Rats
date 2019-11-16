package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;

public class RatAIFleePosition extends EntityAIBase {
    private EntityRat rat;
    private Path path;

    public RatAIFleePosition(EntityRat rat) {
        this.rat = rat;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (this.rat.fleePos != null && !rat.isInCage()) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.rat, 32, 7, new Vec3d(this.rat.fleePos).add(0.5D, 0.5D, 0.5D));
            if (vec3d == null) {
                return false;
            } else if (this.rat.fleePos.distanceSq(vec3d.x, vec3d.y, vec3d.z) < this.rat.getDistanceSq(this.rat.fleePos)) {
                return false;
            } else {
                this.path = this.rat.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.rat.getNavigator().noPath() && this.rat.fleePos != null && this.rat.getDistanceSq(this.rat.fleePos) < 32;
    }

    public void startExecuting() {
        this.rat.getNavigator().setPath(this.path, 1.33D);
    }

    public void resetTask() {
        this.path = null;
        this.rat.fleePos = null;
    }
}
