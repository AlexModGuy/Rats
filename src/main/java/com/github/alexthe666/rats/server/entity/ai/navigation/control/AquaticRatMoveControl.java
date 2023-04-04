package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class AquaticRatMoveControl extends RatMoveControl {
	private final TamedRat rat;

	public AquaticRatMoveControl(TamedRat rat) {
		super(rat);
		this.rat = rat;
	}

	public void tick() {
		if (this.operation == MoveControl.Operation.MOVE_TO && this.rat.isInWater()) {
			if (this.rat.horizontalCollision && !this.rat.isOnGround()) {
				this.rat.setYRot(this.rat.getYRot() + 180.0F);
				int dist = 3;
				if (!this.rat.isInCage()) {
					this.speedModifier = 0.1F;
					dist = 8;
				}
				BlockPos target = RatUtils.getPositionRelativetoWater(this.rat, this.rat.getLevel(), this.rat.getX() + this.rat.getRandom().nextInt(dist * 2) - dist, this.rat.getZ() + this.rat.getRandom().nextInt(dist * 2) - dist, this.rat.getRandom());
				this.setWantedPosition(target.getX(), target.getY(), target.getZ(), this.speedModifier);
			}
			Vec3 vec3d = new Vec3(this.getWantedX() - this.rat.getX(), this.getWantedY() - this.rat.getY(), this.getWantedZ() - this.rat.getZ());
			double d0 = vec3d.length();
			double edgeLength = this.rat.getBoundingBox().getSize();
			if (d0 < edgeLength) {
				this.operation = MoveControl.Operation.WAIT;
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().scale(0.5D));
			} else {
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.1D / d0)));
				if (this.rat.getTarget() == null) {
					Vec3 vec3d1 = this.rat.getDeltaMovement();
					this.rat.setYRot(-((float) Mth.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI));
				} else {
					double d4 = this.rat.getTarget().getX() - this.rat.getX();
					double d5 = this.rat.getTarget().getZ() - this.rat.getZ();
					this.rat.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
				}
				this.rat.yBodyRot = this.rat.getYRot();

			}
		}
	}
}
