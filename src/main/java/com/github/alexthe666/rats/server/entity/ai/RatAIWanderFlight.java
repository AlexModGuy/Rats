package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.BlockRatCage;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
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
            int dist = 8;
            if(rat.isInCage()){
                dist = 3;
            }
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(dist * 2) - dist, rat.posZ + rat.getRNG().nextInt(dist * 2) - dist, rat.getRNG());
            if(!rat.getMoveHelper().isUpdating()){
                return rat.isDirectPathBetweenPoints(new Vec3d(target));
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return false;
    }

    public void updateTask() {
        if (!rat.isDirectPathBetweenPoints(new Vec3d(target))) {
            int dist = 8;
            if (rat.isInCage()) {
                dist = 3;
            }
            target = EntityRat.getPositionRelativetoGround(rat, rat.world, rat.posX + rat.getRNG().nextInt(dist * 2) - dist, rat.posZ + rat.getRNG().nextInt(dist * 2) - dist, rat.getRNG());
        }
        if (rat.world.isAirBlock(target) || rat.world.getBlockState(target).getBlock() instanceof BlockRatCage) {
            rat.getMoveHelper().setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
            if (rat.getAttackTarget() == null) {
                rat.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);
            }
        }
    }
}
