package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.util.math.MathHelper;

public class RatAIFollowOwner extends EntityAIFollowOwner {
    EntityRat rat;
    private int timeToRecalcPath;

    public RatAIFollowOwner(EntityRat rat, double speed, float minDist, float maxDist) {
        super(rat, speed, minDist, maxDist);
        this.rat = rat;
        this.setMutexBits(1);
    }

    public void updateTask() {
        this.rat.getLookHelper().setLookPositionWithEntity(this.rat, 10.0F, (float) this.rat.getVerticalFaceSpeed());
        EntityLivingBase owner = rat.getOwner();
        if (!this.rat.isSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                boolean shouldTeleport;
                if(rat.getUpgrade().getItem() == RatsItemRegistry.RAT_UPGRADE_FLIGHT){
                    shouldTeleport = rat.getDistance(owner) > 20;
                    rat.getMoveHelper().setMoveTo((double) owner.posX, (double) owner.posY + 2, (double) owner.posZ, 0.25D);
                }else{
                    shouldTeleport = !rat.getNavigator().tryMoveToEntityLiving(owner, 1.33D) || rat.getDistance(owner) > 20;
                }
                if (shouldTeleport) {
                    if (!rat.getLeashed() && !rat.isRiding()) {
                        if (rat.getDistanceSq(owner) >= 144.0D) {
                            int i = MathHelper.floor(owner.posX) - 2;
                            int j = MathHelper.floor(owner.posZ) - 2;
                            int k = MathHelper.floor(owner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                                        rat.setLocationAndAngles((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), rat.rotationYaw, rat.rotationPitch);
                                        rat.getNavigator().clearPath();
                                        timeToRecalcPath = 0;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && rat.getCommand() == RatCommand.FOLLOW && !rat.isInCage();
    }
}
