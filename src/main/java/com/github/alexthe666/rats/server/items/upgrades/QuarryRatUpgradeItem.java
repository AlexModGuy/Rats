package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.RatQuarryGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.DamageImmunityUpgrade;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class QuarryRatUpgradeItem extends OreMinerRatUpgradeItem implements DamageImmunityUpgrade {
	public QuarryRatUpgradeItem(Properties properties) {
		super(properties);
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatQuarryGoal(rat));
	}

	@Override
	public boolean isImmuneToDamageSource(TamedRat rat, DamageSource source) {
		return source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypeTags.IS_FALL);
	}
}
