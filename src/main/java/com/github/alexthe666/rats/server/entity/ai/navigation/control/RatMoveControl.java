package com.github.alexthe666.rats.server.entity.ai.navigation.control;

import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import net.minecraft.world.entity.ai.control.MoveControl;

public class RatMoveControl extends MoveControl {

	protected final AbstractRat rat;

	public RatMoveControl(AbstractRat rat) {
		super(rat);
		this.rat = rat;
	}

	@Override
	public boolean hasWanted() {
		return super.hasWanted() && this.rat.canMove();
	}
}
