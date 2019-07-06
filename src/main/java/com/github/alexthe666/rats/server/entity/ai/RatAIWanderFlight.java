package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class RatAIWanderFlight extends EntityAIBase {
    BlockPos target;
    EntityRat rat;

    public RatAIWanderFlight(EntityRat rat) {
        this.setMutexBits(1);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        if (rat.getUpgrade().getItem() == RatsItemRegistry.RAT_UPGRADE_FLIGHT && rat.canMove()) {
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(16) - 8, rat.posZ + rat.getRNG().nextInt(16) - 8, rat.getRNG());
            return isDirectPathBetweenPoints(new Vec3d(target).add(0.5D, 0.5D, 0.5D)) && !rat.getMoveHelper().isUpdating();
        } else {
            return false;
        }
    }

    public boolean shouldContinueExecuting() {
        return false;
    }

    public void updateTask() {
        if (!isDirectPathBetweenPoints(new Vec3d(target))) {
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(15) - 7, rat.posZ + rat.getRNG().nextInt(15) - 7, rat.getRNG());
        }
        if (rat.world.isAirBlock(target)) {
            rat.getMoveHelper().setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
            if (rat.getAttackTarget() == null) {
                rat.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }

    public boolean isDirectPathBetweenPoints(Vec3d target) {
        RayTraceResult rayTrace = rat.world.rayTraceBlocks(rat.getPositionVector(), target, false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            if (!rat.world.isAirBlock(pos) || !rat.world.isAirBlock(sidePos)) {
                return true;
            } else {
                return rayTrace.typeOfHit != RayTraceResult.Type.MISS;
            }
        }
        return true;
    }

}
