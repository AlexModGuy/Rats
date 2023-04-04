package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class EtherealRatMoveControl extends RatMoveControl {
	private final AbstractRat rat;

	public EtherealRatMoveControl(AbstractRat rat) {
		super(rat);
		this.speedModifier = 1F;
		this.rat = rat;
	}

	public void tick() {
		if (this.operation == Operation.MOVE_TO) {
			Vec3 vec3d = new Vec3(this.getWantedX() - this.rat.getX(), this.getWantedY() - this.rat.getY(), this.getWantedZ() - this.rat.getZ());
			double d0 = vec3d.length();
			double edgeLength = this.rat.getBoundingBox().getSize();
			if (d0 < edgeLength) {
				this.operation = Operation.WAIT;
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().scale(0.5D));
			} else {
				this.rat.setDeltaMovement(this.rat.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.05D / d0)));
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
