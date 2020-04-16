package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class RatAIWander extends WaterAvoidingRandomWalkingGoal {
    private EntityRat rat;

    public RatAIWander(EntityRat creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.rat = creatureIn;
        this.executionChance = 200;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Nullable
    protected Vec3d getPosition() {
        if (this.rat.isInWater()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.rat, 15, 7);
            return vec3d == null ? generateRatPosition() : vec3d;
        } else {
            return this.rat.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.rat, 10, 7) : generateRatPosition();
        }
    }

    private Vec3d generateRatPosition() {
        Vec3d vec3d = null;
        boolean inCage = rat.isInCage() || rat.inTube();
        if (inCage) {
            if (rat.inTube()) {
                vec3d = RatUtils.generateRandomCagePos(this.rat, 30, 10, new Vec3d(-this.rat.getPosX(), -this.rat.getPosY(), -this.rat.getPosZ()), rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.generateRandomCageOrTubePos(this.rat, 20, 20, null, false);
                }
            } else {
                vec3d = RatUtils.generateRandomTubePos(this.rat, 15, 3, new Vec3d(-this.rat.getPosX(), -this.rat.getPosY(), -this.rat.getPosZ()), rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.findRandomCageOrTubeTarget(this.rat, 15, 2);
                }
            }
            if (vec3d != null) {
                vec3d = new Vec3d(RatUtils.findLowestRatCage(new BlockPos(vec3d), this.rat));
            }
        }
        if (!inCage || vec3d == null) {
            if (rat.waterBased) {
                vec3d = RatUtils.generateRandomWaterPos(this.rat, this.rat.isTamed() ? 5 : 10, 7, null, true);
            } else {
                vec3d = RandomPositionGenerator.findRandomTarget(this.rat, this.rat.isTamed() ? 5 : 10, 7);

            }
        }
        return vec3d == null ? super.getPosition() : vec3d;
    }

    public boolean shouldExecute() {
        return shouldRatAIExecute() && super.shouldExecute();
    }

    private boolean shouldRatAIExecute() {
        return rat.canMove() && !rat.hasFlight() && !rat.isDancing() && !rat.isFleeing;
    }

}
