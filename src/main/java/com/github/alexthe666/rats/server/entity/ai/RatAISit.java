package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.goal.SitGoal;

public class RatAISit extends SitGoal {

    private EntityRat rat;

    public RatAISit(EntityRat rat) {
        super(rat);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && !rat.inTube();
    }
}
