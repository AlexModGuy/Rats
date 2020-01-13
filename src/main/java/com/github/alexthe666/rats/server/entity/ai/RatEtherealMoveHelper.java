package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RatEtherealMoveHelper extends MovementController {
    EntityRat rat;

    public RatEtherealMoveHelper(EntityRat rat) {
        super(rat);
        this.speed = 1F;
        this.rat = rat;
    }

    public void tick() {
        if (this.action == Action.MOVE_TO) {
            Vec3d vec3d = new Vec3d(this.posX - rat.posX, this.posY - rat.posY, this.posZ - rat.posZ);
            double d0 = vec3d.length();
            double edgeLength = rat.getBoundingBox().getAverageEdgeLength();
            if (d0 < edgeLength) {
                this.action = Action.WAIT;
                rat.setMotion(rat.getMotion().scale(0.5D));
            } else {
                rat.setMotion(rat.getMotion().add(vec3d.scale(this.speed * 0.05D / d0)));
                if (rat.getAttackTarget() == null) {
                    Vec3d vec3d1 = rat.getMotion();
                    rat.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
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
