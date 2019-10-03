package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RatAIFollowOwner extends EntityAIBase {
    private final EntityRat rat;
    private final double followSpeed;
    World world;
    float maxDist;
    float minDist;
    private EntityLivingBase owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public RatAIFollowOwner(EntityRat tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.rat = tameableIn;
        this.world = tameableIn.world;
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (rat.isTamed() && rat.isFollowing()) {
            EntityLivingBase entitylivingbase = this.rat.getOwner();
            if (entitylivingbase == null) {
                return false;
            } else if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer) entitylivingbase).isSpectator()) {
                return false;
            } else if (this.rat.isSitting()) {
                return false;
            } else if (this.rat.getDistanceSq(entitylivingbase) < (double) (this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = entitylivingbase;
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

    public void updateTask() {
        this.rat.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.rat.getVerticalFaceSpeed());
        if (rat.isFollowing()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                boolean teleport = false;
                if (!this.rat.getLeashed() && !this.rat.isRiding()) {
                    if (this.rat.getDistanceSq(this.owner) >= 144.0D) {
                        teleport = true;
                        int i = MathHelper.floor(this.owner.posX) - 2;
                        int j = MathHelper.floor(this.owner.posZ) - 2;
                        int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);
                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
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

    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset) {
        BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.rat) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }
}