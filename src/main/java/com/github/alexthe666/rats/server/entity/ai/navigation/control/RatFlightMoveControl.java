package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class RatFlightMoveControl extends RatMoveControl {
	private final TamedRat rat;

	public RatFlightMoveControl(TamedRat rat, float speedModifier) {
		super(rat);
		this.rat = rat;
		this.speedModifier = speedModifier;
	}

	public void tick() {
		if (this.operation == MoveControl.Operation.MOVE_TO) {
			Vec3 vector3d = new Vec3(this.wantedX - this.rat.getX(), this.wantedY - this.rat.getY(), this.wantedZ - this.rat.getZ());
			double d0 = vector3d.length();
			if (d0 < this.rat.getBoundingBox().getSize()) {
				this.operation = MoveControl.Operation.WAIT;
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().scale(0.5D));
			} else {
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
				if (this.rat.getTarget() == null) {
					Vec3 vector3d1 = this.rat.getDeltaMovement();
					this.rat.setYRot(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI));
				} else {
					double d2 = this.rat.getTarget().getX() - this.rat.getX();
					double d1 = this.rat.getTarget().getZ() - this.rat.getZ();
					this.rat.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
				}
				this.rat.yBodyRot = this.rat.getYRot();
			}

		} else if (this.operation == Operation.STRAFE) {
			this.operation = Operation.WAIT;
		}
	}
}
