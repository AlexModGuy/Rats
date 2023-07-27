package com.github.alexthe666.rats.server.entity.ai.goal;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class RatHuntGoal extends TargetGoal {

	private final Pair<Boolean, List<String>> targetsList;
	private final TamedRat rat;
	@Nullable
	protected LivingEntity target;

	public RatHuntGoal(TamedRat rat, boolean whitelist, List<String> targetedMobs) {
		super(rat, true);
		this.rat = rat;
		this.targetsList = Pair.of(whitelist, targetedMobs);
	}

	@Override
	public boolean canUse() {
		if (!this.rat.canMove() || this.rat.isInCage() || !this.rat.shouldHunt()) return false;
		if (reducedTickDelay(10) > 0 && this.mob.getRandom().nextInt(reducedTickDelay(10)) != 0) {
			return false;
		} else {
			this.findTarget();
			return this.target != null;
		}
	}

	protected AABB getTargetSearchArea(double dist) {
		return this.rat.getBoundingBox().inflate(dist, 4.0D, dist);
	}

	protected void findTarget() {
		this.target = this.rat.level().getNearestEntity(this.rat.level().getEntitiesOfClass(LivingEntity.class, this.getTargetSearchArea(this.getFollowDistance()), entity -> (!(entity instanceof OwnableEntity ownable) || ownable.getOwner() != this.rat.getOwner()) && entity != this.rat.getOwner() && this.targetsList.getFirst() == this.targetsList.getSecond().contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString())), TargetingConditions.DEFAULT, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
	}

	@Override
	public void start() {
		this.rat.setTarget(this.target);
		super.start();
	}
}
