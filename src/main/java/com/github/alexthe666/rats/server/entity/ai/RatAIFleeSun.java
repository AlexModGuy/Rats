package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.goal.FleeSunGoal;

public class RatAIFleeSun extends FleeSunGoal {
    private EntityRat rat;

    public RatAIFleeSun(EntityRat rat, double movementSpeedIn) {
        super(rat, movementSpeedIn);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        return !rat.isTamed() && !rat.isInCage() && super.shouldExecute();
    }
}
