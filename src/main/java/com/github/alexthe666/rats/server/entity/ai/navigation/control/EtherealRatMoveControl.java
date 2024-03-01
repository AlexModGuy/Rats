package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class EtherealRatMoveControl extends RatMoveControl {

	public EtherealRatMoveControl(AbstractRat rat) {
		super(rat);
	}

	public void tick() {
		if (this.operation == Operation.MOVE_TO) {
			Vec3 vector3d = new Vec3(this.wantedX - this.rat.getX(), this.wantedY - this.rat.getY(), this.wantedZ - this.rat.getZ());
			double d0 = vector3d.length();

			this.rat.setDeltaMovement(this.rat.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.025D / d0)));

			double yAdd = this.wantedY - this.rat.getY();
			if(d0 > this.rat.getBbWidth()){
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().add(0.0D, (double) this.rat.getSpeed() * Mth.clamp(yAdd, -1, 1) * 0.6F, 0.0D));
				Vec3 vector3d1 = this.rat.getDeltaMovement();
				this.rat.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * Mth.RAD_TO_DEG);
				this.rat.yBodyRot = this.rat.getYRot();
			}
		} else if (this.operation == Operation.STRAFE || this.operation == Operation.JUMPING) {
			this.operation = Operation.WAIT;
		}
	}
}
