package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.ai.goal.RatWorkGoal;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public interface ChangesAIUpgrade {
	/**
	 * Adds a list of new work tasks for your rat to perform when this upgrade is applied to them. <br>
	 * Make sure your goal implements {@link RatWorkGoal}!<br>
	 * Please note that if you're adding multiple goals that use the same TaskType only the last one will be used!
	 * @param rat the rat that will be using the goal
	 * @return a list of goals for your rat to use when the upgrade is applied
	 */
	List<Goal> addNewWorkGoals(TamedRat rat);
}
