package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class RatAIWanderFlight extends Goal {
    BlockPos target;
    EntityRat rat;

    public RatAIWanderFlight(EntityRat rat) {
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        this.rat = rat;
    }

    public boolean shouldExecute() {
        if (rat.hasFlight() && rat.canMove() && !rat.isInWater()) {
            int dist = 8;
            if (rat.isInCage()) {
                dist = 3;
            }
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(dist * 2) - dist, rat.posZ + rat.getRNG().nextInt(dist * 2) - dist, rat.getRNG());
            if (!rat.moveController.isUpdating()) {
                return rat.isDirectPathBetweenPoints(new Vec3d(target));
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return false;
    }

    public void tick() {
        if (!rat.isDirectPathBetweenPoints(new Vec3d(target))) {
            int dist = 8;
            if (rat.isInCage()) {
                dist = 3;
            }
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(dist * 2) - dist, rat.posZ + rat.getRNG().nextInt(dist * 2) - dist, rat.getRNG());
        }
        if (rat.world.isAirBlock(target) || rat.world.getBlockState(target).getBlock() instanceof BlockRatCage) {
            rat.moveController.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
            if (rat.getAttackTarget() == null) {
                rat.getLookController().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
