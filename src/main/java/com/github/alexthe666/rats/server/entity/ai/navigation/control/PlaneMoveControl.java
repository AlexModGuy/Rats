package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.Plane;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class PlaneMoveControl<T extends Mob & Plane> extends MoveControl {

	private final T plane;

	public PlaneMoveControl(T plane) {
		super(plane);
		this.plane = plane;
	}

	public static float approach(float number, float max, float min) {
		min = Math.abs(min);
		return number < max ? Mth.clamp(number + min, number, max) : Mth.clamp(number - min, max, number);
	}

	public static float approachDegrees(float number, float max, float min) {
		float add = Mth.wrapDegrees(max - number);
		return approach(number, number + add, min);
	}

	public static float degreesDifferenceAbs(float f1, float f2) {
		return Math.abs(Mth.wrapDegrees(f2 - f1));
	}

	@Override
	public void tick() {
		if (this.plane.horizontalCollision) {
			this.plane.setYRot(this.plane.getYRot() + 180.0F);
			this.speedModifier = 0.1F;
			this.plane.setFlightTarget(null);
			return;
		}
		if (this.plane.getControllingPassenger() != null) {
			if (this.plane.getControllingPassenger() instanceof TamedRat rat && !rat.canMove()) {
				return;
			}
		}
		if (this.plane.getFlightTarget() == null && this.hasWanted()) {
			this.plane.setFlightTarget(new Vec3(this.getWantedX(), this.getWantedY(), this.getWantedZ()));
		}
		if (this.plane.getFlightTarget() != null) {
			float distX = (float) (this.plane.getFlightTarget().x() - this.plane.getX());
			float distY = (float) (this.plane.getFlightTarget().y() - this.plane.getY());
			float distZ = (float) (this.plane.getFlightTarget().z() - this.plane.getZ());
			double planeDist = Mth.sqrt(distX * distX + distZ * distZ);
			double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;
			distX = (float) ((double) distX * yDistMod);
			distZ = (float) ((double) distZ * yDistMod);
			planeDist = Mth.sqrt(distX * distX + distZ * distZ);
			double dist = Mth.sqrt(distX * distX + distZ * distZ + distY * distY);
			if (dist > 1.0F) {
				float yawCopy = this.plane.getYRot();
				float atan = (float) Mth.atan2(distZ, distX);
				float yawTurn = Mth.wrapDegrees(this.plane.getYRot() + 90.0F);
				float yawTurnAtan = Mth.wrapDegrees(atan * 57.295776F);
				this.plane.setYRot(approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F);
				this.plane.yBodyRot = this.plane.getYRot();
				if (degreesDifferenceAbs(yawCopy, this.plane.getYRot()) < 3.0F) {
					this.speedModifier = approach((float) this.speedModifier, 1.2F, 0.005F * (1.2F / (float) this.speedModifier));
				} else {
					this.speedModifier = approach((float) this.speedModifier, 0.2F, 0.025F);
					if (dist < 100.0D && this.plane.getTarget() != null) {
						this.speedModifier = this.speedModifier * (dist / 100.0D);
					}
				}
				float finPitch = (float) (-(Mth.atan2(-distY, planeDist) * 57.2957763671875D));
				this.plane.setXRot(finPitch);
				float yawTurnHead = this.plane.getYRot() + 90.0F;
				double xSpeed = this.speedModifier * Mth.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
				double zSpeed = this.speedModifier * Mth.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
				double ySpeed = this.speedModifier * Mth.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
				this.plane.setDeltaMovement(this.plane.getDeltaMovement().add(xSpeed * 0.4D, ySpeed * 0.4D, zSpeed * 0.4D));
			}
		}
	}
}
