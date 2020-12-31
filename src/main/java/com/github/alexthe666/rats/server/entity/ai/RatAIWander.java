package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class RatAIWander extends RandomWalkingGoal {
    private EntityRat rat;
    protected final float probability = 0.01F;

    public RatAIWander(EntityRat creatureIn, double speedIn) {
        super(creatureIn, speedIn, 200, false);
        this.rat = creatureIn;
        this.executionChance = 200;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Nullable
    protected Vector3d getPosition() {
        if (this.rat.isInWater()) {
            Vector3d vec3d = RandomPositionGenerator.getLandPos(this.rat, 15, 7);
            return vec3d == null ? generateRatPosition() : vec3d;
        } else {
            return this.rat.getRNG().nextFloat() >= this.probability && !rat.isInCage() ? RandomPositionGenerator.getLandPos(this.rat, 10, 7) : generateRatPosition();
        }
    }

    private Vector3d generateRatPosition() {
        Vector3d vec3d = null;
        boolean inCage = rat.isInCage() || rat.inTube();
        if (inCage) {
            if (rat.inTube()) {
                vec3d = RatUtils.generateRandomCagePos(this.rat, 30, 10, null, rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.generateRandomCageOrTubePos(this.rat, 5, 5, null, false);
                }
            } else {
                vec3d = RatUtils.generateRandomTubePos(this.rat, 3, 2, null, rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.findRandomCageOrTubeTarget(this.rat, 5, 2);
                }
            }
            if (vec3d != null) {
                BlockPos posToFind = RatUtils.findLowestRatCage(new BlockPos(vec3d), this.rat);
                vec3d = new Vector3d(posToFind.getX(), posToFind.getY(), posToFind.getZ());
            }
        }
        if (!inCage || vec3d == null) {
            if (rat.waterBased) {
                vec3d = RatUtils.generateRandomWaterPos(this.rat, this.rat.isTamed() ? 5 : 10, 7, null, true);
            } else {
                if (this.creature.isInWaterOrBubbleColumn()) {
                    Vector3d lvt_1_1_ = RandomPositionGenerator.getLandPos(this.creature, 15, 7);
                    vec3d = lvt_1_1_ == null ? super.getPosition() : lvt_1_1_;
                } else {
                    vec3d = this.creature.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.creature, 10, 7) : super.getPosition();
                }

            }
        }
        return vec3d;
    }

    public boolean shouldExecute() {
        executionChance = rat.isInCage() ? 15 : 200;

        return shouldRatAIExecute() &&
                super.shouldExecute();
    }

    private boolean shouldRatAIExecute() {
        return rat.canMove() && !rat.hasFlightUpgrade() && !rat.isDancing() && !rat.isFleeing && rat.shouldWander();
    }

}
