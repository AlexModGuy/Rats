package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityPirat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class PiratAIWander extends Goal {
    private EntityPirat rat;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public PiratAIWander(EntityPirat creatureIn, double speedIn) {
        this(creatureIn, speedIn, 20);
    }

    public PiratAIWander(EntityPirat creatureIn, double speedIn, int chance) {
        this.rat = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (this.rat.getRNG().nextInt(executionChance) != 0) {
                return false;
            }
        }
        Vec3d vec3d = RatUtils.generateRandomWaterPos(this.rat, 10, 5, null, true);
        if (vec3d == null) {
            return false;
        } else {
            BlockPos water = RatUtils.findLowestWater(new BlockPos(vec3d), rat);
            this.xPosition = water.getX();
            this.yPosition = water.getY();
            this.zPosition = water.getZ();
            this.mustUpdate = false;
            return true;
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.rat.getNavigator().noPath();
    }

    @Override
    public void startExecuting() {
        this.rat.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}
