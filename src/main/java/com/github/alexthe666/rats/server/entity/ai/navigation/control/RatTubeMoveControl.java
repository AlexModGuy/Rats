package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;

public class RatTubeMoveControl extends RatMoveControl {
	private final TamedRat rat;

	public RatTubeMoveControl(TamedRat rat) {
		super(rat);
		this.speedModifier = 1F;
		this.rat = rat;
	}

	public void tick() {
		if (this.operation == MoveControl.Operation.STRAFE) {
			float f = (float) rat.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
			float f1 = (float) this.speedModifier * f;
			float f2 = this.strafeForwards;
			float f3 = this.strafeRight;
			float f4 = Mth.sqrt(f2 * f2 + f3 * f3);

			if (f4 < 1.0F) {
				f4 = 1.0F;
			}

			f4 = f1 / f4;
			f2 = f2 * f4;
			f3 = f3 * f4;
			float f5 = Mth.sin(this.rat.getYRot() * Mth.DEG_TO_RAD);
			float f6 = Mth.cos(this.rat.getYRot() * Mth.DEG_TO_RAD);
			float f7 = f2 * f6 - f3 * f5;
			float f8 = f3 * f6 + f2 * f5;
			PathNavigation pathnavigate = this.rat.getNavigation();

			NodeEvaluator nodeprocessor = pathnavigate.getNodeEvaluator();
			if (nodeprocessor.getBlockPathType(this.rat.getLevel(), Mth.floor(this.rat.getX() + (double) f7), Mth.floor(this.rat.getY()), Mth.floor(this.rat.getZ() + (double) f8)) != BlockPathTypes.WALKABLE) {
				this.strafeForwards = 1.0F;
				this.strafeRight = 0.0F;
				f1 = f;
			}

			this.rat.setSpeed(f1);
			this.rat.setZza(this.strafeForwards);
			this.rat.setXxa(this.strafeRight);
			this.operation = MoveControl.Operation.WAIT;
		} else if (this.operation == MoveControl.Operation.MOVE_TO) {

			this.operation = MoveControl.Operation.WAIT;
			double d0 = this.getWantedX() - this.mob.getX();
			double d1 = this.getWantedZ() - this.mob.getZ();
			double d2 = this.getWantedY() - this.mob.getY() + 0.25;
			double d3 = d0 * d0 + d2 * d2 + d1 * d1;
			if (d3 < (double) 2.5000003E-7F) {
				this.mob.setZza(0.0F);
				return;
			}

			float f9 = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
			this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
			this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
			this.rat.climbingTube = d2 > 0;
		} else if (this.operation == MoveControl.Operation.JUMPING) {
			this.rat.setSpeed((float) (this.speedModifier * this.rat.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
			this.rat.climbingTube = true;
			if (this.rat.isOnGround()) {
				this.operation = MoveControl.Operation.WAIT;
			}
		} else {
			this.rat.setZza(0.0F);
		}
	}
}
