package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.ai.goal.RatDepositGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.harvest.RatMilkCowGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.RatPickupGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesAIUpgrade;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.fluids.FluidType;

import java.util.List;

public class MilkerRatUpgradeItem extends BucketRatUpgradeItem implements ChangesAIUpgrade {
	public MilkerRatUpgradeItem(Properties properties) {
		super(properties, 0, 3, FluidType.BUCKET_VOLUME);
	}

	@Override
	public List<Goal> addNewWorkGoals(TamedRat rat) {
		return List.of(new RatPickupGoal(rat, RatPickupGoal.PickupType.FLUID), new RatDepositGoal(rat, RatDepositGoal.DepositType.FLUID), new RatMilkCowGoal(rat));
	}
}
