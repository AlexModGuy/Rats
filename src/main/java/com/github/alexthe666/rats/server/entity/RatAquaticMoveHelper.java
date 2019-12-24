package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RatAquaticMoveHelper extends EntityMoveHelper {
    EntityRat rat;

    public RatAquaticMoveHelper(EntityRat rat) {
        super(rat);
        this.rat = rat;
    }

    public void onUpdateMoveHelper() {
        if (this.action == EntityMoveHelper.Action.MOVE_TO && this.rat.isInWater()) {
            if (rat.collidedHorizontally && !rat.onGround) {
                rat.rotationYaw += 180.0F;
                int dist = 3;
                if (!rat.isInCage()) {
                    this.speed = 0.1F;
                    dist = 8;
                }
                BlockPos target = EntityRat.getPositionRelativetoWater(rat, rat.world, rat.posX + rat.getRNG().nextInt(dist * 2) - dist, rat.posZ + rat.getRNG().nextInt(dist * 2) - dist, rat.getRNG());
                this.posX = target.getX();
                this.posY = target.getY();
                this.posZ = target.getZ();
            }
            double d0 = this.posX - rat.posX;
            double d1 = this.posY - rat.posY;
            double d2 = this.posZ - rat.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            d3 = (double) MathHelper.sqrt(d3);
            double edgeLength = rat.getBoundingBox().getAverageEdgeLength();
            if (d3 < edgeLength) {
                this.action = EntityMoveHelper.Action.WAIT;
                rat.motionX *= 0.5D;
                rat.motionY *= 0.5D;
                rat.motionZ *= 0.5D;
            } else {
                rat.motionX += d0 / d3 * 0.1D * this.speed;
                rat.motionY += d1 / d3 * 0.1D * this.speed;
                rat.motionZ += d2 / d3 * 0.1D * this.speed;
                if (rat.getAttackTarget() == null) {
                    rat.rotationYaw = -((float) MathHelper.atan2(rat.motionX, rat.motionZ)) * (180F / (float) Math.PI);
                    rat.renderYawOffset = rat.rotationYaw;
                } else {
                    double d4 = rat.getAttackTarget().posX - rat.posX;
                    double d5 = rat.getAttackTarget().posZ - rat.posZ;
                    rat.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                    rat.renderYawOffset = rat.rotationYaw;
                }

            }
        }
    }
}
