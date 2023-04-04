package com.github.alexthe666.rats.server.entity.ai.goal;

public interface RatWorkGoal {


	TaskType getRatTaskType();

	enum TaskType {
		ATTACK,
		DEPOSIT,
		PICKUP,
		HARVEST
	}
}
