package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RatAIWander extends EntityAIBase {
    private EntityRat rat;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;

    public RatAIWander(EntityRat creatureIn, double speedIn) {
        this(creatureIn, speedIn, 100);
    }

    public RatAIWander(EntityRat creatureIn, double speedIn, int chance) {
        this.rat = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!rat.canMove() || rat.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FLIGHT) || rat.isDancing() || rat.isFleeing) {
            return false;
        }
        if (!this.mustUpdate) {
            if (this.rat.getRNG().nextInt(rat.isInCage() || rat.inTube() ? 3 : executionChance) != 0) {
                return false;
            }
        }
        Vec3d vec3d = null;
        boolean inCage = rat.isInCage() || rat.inTube();
        if(inCage){
            if(rat.inTube()){
                vec3d = RatUtils.generateRandomCagePos(this.rat, 30, 10, new Vec3d(-this.rat.posX, -this.rat.posY, -this.rat.posZ), rat.waterBased);
                if(vec3d == null){
                    vec3d = RatUtils.generateRandomCageOrTubePos(this.rat, 20, 20, null, false);
                }
            }else{
                vec3d = RatUtils.generateRandomTubePos(this.rat, 15, 3, new Vec3d(-this.rat.posX, -this.rat.posY, -this.rat.posZ), rat.waterBased);
                if(vec3d == null){
                    vec3d = RatUtils.findRandomCageOrTubeTarget(this.rat, 15, 2);
                }
            }
        }
        if(!inCage || vec3d == null){
            if(rat.waterBased){
                vec3d = RatUtils.generateRandomWaterPos(this.rat, this.rat.isTamed() ? 5 : 10, 7, null ,true);
            }else{
                vec3d = RandomPositionGenerator.findRandomTarget(this.rat, this.rat.isTamed() ? 5 : 10, 7);

            }
        }
        if (vec3d == null) {
            return false;
        } else {
            if(inCage){
                vec3d = new Vec3d(RatUtils.findLowestRatCage(new BlockPos(vec3d), this.rat));
            }
            this.xPosition = vec3d.x;
            this.yPosition = vec3d.y;
            this.zPosition = vec3d.z;
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
