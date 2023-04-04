package com.github.alexthe666.rats.server.entity.rat;

public enum RatStatus {
	MOVING(0),
	IDLE(0),
	EATING(1);

	public final int precedence;

	RatStatus(int precedence) {
		this.precedence = precedence;
	}

	public String getTranslateName() {
		return "entity.rats.rat.status." + this.name().toLowerCase();
	}

	public boolean canBeOverriden(AbstractRat rat) {
		if (this == MOVING && rat.isMoving()) {
			return false;
		}
		return this != EATING || !rat.isHoldingFood();
	}
}
