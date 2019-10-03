package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.entity.ai.EntityAISit;

public class RatAISit extends EntityAISit {

    private EntityRat rat;

    public RatAISit(EntityRat rat) {
        super(rat);
        this.rat = rat;
    }

    public boolean shouldExecute() {
        return super.shouldExecute() && !rat.inTube();
    }
}
