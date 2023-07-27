package com.github.alexthe666.rats.server.entity.rat;

public enum RatCommand {
	WANDER(true, true, true, true, false),
	SIT(false, true, false, false, false),
	FOLLOW(true, true, false, true, false),
	HUNT(true, false, true, true, true),
	GATHER(true, false, false, false, true),
	HARVEST(true, false, false, false, true),
	TRANSPORT(true, false, false, false, true),
	PATROL(true, true, false, true, false);

	public final boolean freeMove;
	public final boolean allowsEating;
	public final boolean allowsWandering;
	public final boolean allowsAttacking;
	public final boolean workCommand;

	RatCommand(boolean freeMove, boolean allowsEating, boolean allowsWandering, boolean allowsAttacking, boolean workCommand) {
		this.freeMove = freeMove;
		this.allowsEating = allowsEating;
		this.allowsWandering = allowsWandering;
		this.allowsAttacking = allowsAttacking;
		this.workCommand = workCommand;
	}

	public String getTranslateName() {
		return "entity.rats.rat.command." + this.name().toLowerCase();
	}

	public String getTranslateDescription() {
		return "entity.rats.rat.command." + this.name().toLowerCase() + ".desc";
	}

}
