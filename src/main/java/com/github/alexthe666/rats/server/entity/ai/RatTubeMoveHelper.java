package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.MathHelper;

public class RatTubemoveController extends MovementController {
    EntityRat rat;

    public RatTubemoveController(EntityRat rat) {
        super(rat);
        this.speed = 1F;
        this.rat = rat;
    }

    public void tick() {
        if (this.action == MovementController.Action.STRAFE) {
            float f = (float) rat.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
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
                if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.rat.world, MathHelper.floor(this.rat.posX + (double) f7), MathHelper.floor(this.rat.posY), MathHelper.floor(this.rat.posZ + (double) f8)) != PathNodeType.WALKABLE) {
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
            double d0 = this.posX - this.rat.posX;
            double d1 = this.posZ - this.rat.posZ;
            double d2 = this.posY - this.rat.posY;
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < 2.500000277905201E-7D) {
                this.rat.setMoveForward(0.0F);
                return;
            }
            float f9 = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
            this.rat.rotationYaw = this.limitAngle(this.rat.rotationYaw, f9, 90.0F);
            this.rat.setAIMoveSpeed((float) (this.speed * this.rat.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
            this.rat.climbingTube = false;
            //this.rat.motionY += 0.1D;
            if (d2 > 0) {
                this.rat.climbingTube = true;
            }
        } else if (this.action == MovementController.Action.JUMPING) {
            this.rat.setAIMoveSpeed((float) (this.speed * this.rat.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
            this.rat.climbingTube = true;
            if (this.rat.onGround) {
                this.action = MovementController.Action.WAIT;
            }
        } else {
            this.rat.setMoveForward(0.0F);
        }
    }
}
