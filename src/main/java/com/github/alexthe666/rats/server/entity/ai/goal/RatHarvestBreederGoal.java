package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RatHarvestBreederGoal extends BaseRatHarvestGoal {
	private final TamedRat rat;

	public RatHarvestBreederGoal(TamedRat rat) {
		super(rat);
		this.rat = rat;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (!this.checkTheBasics(false, false)) {
			return false;
		}
		if (this.rat.getMainHandItem().isEmpty()) {
			return false;
		}
		this.resetTarget();
		return this.getTargetEntity() != null;
	}

	@Override
	public void tick() {
		if (this.getTargetEntity() != null && this.getTargetEntity().isAlive() && !this.rat.getMainHandItem().isEmpty()) {
			this.rat.getNavigation().moveTo(this.getTargetEntity(), 1.25D);
			if (this.rat.distanceToSqr(this.getTargetEntity()) < this.rat.getRatHarvestDistance(1.0D)) {
				if (this.getTargetEntity() instanceof Animal animal && !animal.isInLove()) {
					animal.setInLove(null);
					this.rat.getMainHandItem().shrink(1);
					this.rat.gameEvent(GameEvent.ENTITY_INTERACT);
				}
				this.stop();
			}
		} else {
			this.stop();
		}
	}

	private void resetTarget() {
		int radius = this.rat.getRadius();
		AABB bb = new AABB(-radius, -radius, -radius, radius, radius, radius).move(this.rat.getSearchCenter());
		Predicate<Animal> perRatPredicate = entity -> !(entity instanceof AbstractRat) && !entity.isBaby() && !entity.isInLove() && entity.getAge() == 0 && entity.isFood(this.rat.getMainHandItem());
		List<Animal> list = this.rat.level().getEntitiesOfClass(Animal.class, bb, perRatPredicate);
		list.sort(Comparator.comparingDouble(this.rat::distanceToSqr));
		if (!list.isEmpty()) {
			this.setTargetEntity(list.get(0));
		}
	}
}