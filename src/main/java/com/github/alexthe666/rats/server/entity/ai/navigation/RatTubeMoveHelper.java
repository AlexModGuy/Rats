package com.github.alexthe666.rats.server.entity.ai.navigation;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class RatTubeMoveHelper extends MovementController {
    EntityRat rat;

    public RatTubeMoveHelper(EntityRat rat) {
        super(rat);
        this.speed = 1F;
        this.rat = rat;
    }

    public void tick() {
        if (this.action == MovementController.Action.STRAFE) {
            float f = (float) rat.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            float f1 = (float) this.speed * f;
            float f2 = this.moveForward;
            float f3 = this.moveStrafe;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.rat.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.rat.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            PathNavigator pathnavigate = this.rat.getNavigator();

            if (pathnavigate != null) {
                NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();
                if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.rat.world, MathHelper.floor(this.rat.getPosX() + (double) f7), MathHelper.floor(this.rat.getPosY()), MathHelper.floor(this.rat.getPosZ() + (double) f8)) != PathNodeType.WALKABLE) {
                    this.moveForward = 1.0F;
                    this.moveStrafe = 0.0F;
                    f1 = f;
                }
            }

            this.rat.setAIMoveSpeed(f1);
            this.rat.setMoveForward(this.moveForward);
            this.rat.setMoveStrafing(this.moveStrafe);
            this.action = MovementController.Action.WAIT;
        } else if (this.action == MovementController.Action.MOVE_TO) {

            this.action = MovementController.Action.WAIT;
            double d0 = this.posX - this.mob.getPosX();
            double d1 = this.posZ - this.mob.getPosZ();
            double d2 = this.posY - this.mob.getPosY() + 0.25;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < (double)2.5000003E-7F) {
                this.mob.setMoveForward(0.0F);
                return;
            }

            float f9 = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f9, 90.0F);
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
            BlockPos blockpos = new BlockPos(this.mob.getPositionVec());
            if (d2 > 0) {
                this.rat.climbingTube = true;
            }else{
                this.rat.climbingTube = false;
            }
        } else if (this.action == MovementController.Action.JUMPING) {
            this.rat.setAIMoveSpeed((float) (this.speed * this.rat.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
            this.rat.climbingTube = true;
            if (this.rat.isOnGround()) {
                this.action = MovementController.Action.WAIT;
            }
        } else {
            this.rat.setMoveForward(0.0F);
        }
    }
}
