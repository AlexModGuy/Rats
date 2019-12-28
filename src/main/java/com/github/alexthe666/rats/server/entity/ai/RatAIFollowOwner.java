package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class RatAIFollowOwner extends Goal {
    private final EntityRat rat;
    private final double followSpeed;
    World world;
    float maxDist;
    float minDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public RatAIFollowOwner(EntityRat tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.rat = tameableIn;
        this.world = tameableIn.world;
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    public boolean shouldExecute() {
        if (rat.isTamed() && rat.isFollowing()) {
            LivingEntity LivingEntity = this.rat.getOwner();
            if (LivingEntity == null) {
                return false;
            } else if (LivingEntity instanceof PlayerEntity && ((PlayerEntity) LivingEntity).isSpectator()) {
                return false;
            } else if (this.rat.isSitting()) {
                return false;
            } else if (this.rat.getDistanceSq(LivingEntity) < (double) (this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = LivingEntity;
                return true;
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return !this.rat.getNavigator().noPath() && rat.isFollowing() && this.rat.getDistanceSq(this.owner) > (double) (this.maxDist * this.maxDist);
    }

    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.rat.getPathPriority(PathNodeType.WATER);
        this.rat.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask() {
        this.owner = null;
        this.rat.getNavigator().clearPath();
        this.rat.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    public void tick() {
        this.rat.getLookController().setLookPositionWithEntity(this.owner, 10.0F, (float) this.rat.getVerticalFaceSpeed());
        if (rat.isFollowing()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                boolean teleport = false;
                if (!this.rat.getLeashed() && !this.rat.isPassenger() && this.rat.getOwner() instanceof PlayerEntity) {
                    if (this.rat.getDistanceSq(this.owner) >= 144.0D) {
                        teleport = true;
                        int i = MathHelper.floor(this.owner.posX) - 2;
                        int j = MathHelper.floor(this.owner.posZ) - 2;
                        int k = MathHelper.floor(this.owner.getBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i + l, k - 1, j + i1))) {
                                    this.rat.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.rat.rotationYaw, this.rat.rotationPitch);
                                    this.rat.getNavigator().clearPath();
                                    return;
                                }
                            }
                        }
                    }
                }
                if (!teleport) {
                    this.rat.getNavigator().tryMoveToEntityLiving(this.owner, this.followSpeed);
                }
            }

        }
    }

    protected boolean canTeleportToBlock(BlockPos pos) {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.canEntitySpawn(this.world, pos, this.rat.getType()) && this.world.isAirBlock(pos.up()) && this.world.isAirBlock(pos.up(2));
    }
}