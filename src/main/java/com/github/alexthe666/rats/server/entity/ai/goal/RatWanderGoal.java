package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RatWanderGoal extends WaterAvoidingRandomStrollGoal {

	private final TamedRat rat;

	public RatWanderGoal(TamedRat rat, double speedModifier) {
		super(rat, speedModifier);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.rat.canMove() && this.rat.shouldWander() && super.canUse();
	}

	@Override
	public void tick() {
		if (this.rat.hasFlightUpgrade()) {
			this.rat.setFlying(true);
		}
		super.tick();
	}

	@Nullable
	@Override
	protected Vec3 getPosition() {
		Vec3 viewVec = this.mob.getViewVector(0.0F);
		Vec3 vec31 = null;
//		if (this.rat.blockPosition().getY() + 20 > this.rat.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.rat.blockPosition()).getY()) {
//			return Vec3.atCenterOf(this.rat.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, this.rat.blockPosition()).above(3 + this.rat.getRandom().nextInt(5)));
//		}
		if (this.mob.isInWaterOrBubble()) {
			vec31 = LandRandomPos.getPos(this.mob, 15, 15);
		}
		if (this.rat.hasFlightUpgrade()) {
			vec31 = HoverRandomPos.getPos(this.mob, 8, 3, viewVec.x, viewVec.z, ((float) Math.PI / 2F), 3, 1);
			return vec31 != null ? vec31 : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, viewVec.x, viewVec.z, (float) Math.PI / 2F);
		} else {
			return vec31 != null ? vec31 : LandRandomPos.getPos(this.mob, 10, 7);
		}
	}
}
