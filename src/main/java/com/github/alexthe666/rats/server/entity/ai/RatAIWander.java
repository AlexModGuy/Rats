package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class RatAIWander extends EntityAIWanderAvoidWater {
    private EntityRat rat;

    public RatAIWander(EntityRat creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.rat = creatureIn;
        this.executionChance = 200;
        this.setMutexBits(1);
    }

    @Nullable
    protected Vec3d getPosition() {
        if (this.entity.isInWater()) {
            Vec3d vec3d = RandomPositionGenerator.getLandPos(this.entity, 15, 7);
            return vec3d == null ? generateRatPosition() : vec3d;
        } else {
            return this.entity.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.getLandPos(this.entity, 10, 7) : generateRatPosition();
        }
    }

    private Vec3d generateRatPosition() {
        Vec3d vec3d = null;
        boolean inCage = rat.isInCage() || rat.inTube();
        if (inCage) {
            if (rat.inTube()) {
                vec3d = RatUtils.generateRandomCagePos(this.rat, 30, 10, new Vec3d(-this.rat.posX, -this.rat.posY, -this.rat.posZ), rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.generateRandomCageOrTubePos(this.rat, 20, 20, null, false);
                }
            } else {
                vec3d = RatUtils.generateRandomTubePos(this.rat, 15, 3, new Vec3d(-this.rat.posX, -this.rat.posY, -this.rat.posZ), rat.waterBased);
                if (vec3d == null) {
                    vec3d = RatUtils.findRandomCageOrTubeTarget(this.rat, 15, 2);
                }
            }
            if(vec3d != null){
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
        return rat.canMove() && !rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) && !rat.isDancing() && !rat.isFleeing;
    }

}
