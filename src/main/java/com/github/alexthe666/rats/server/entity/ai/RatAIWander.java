package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
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
        this(creatureIn, speedIn, 20);
    }

    public RatAIWander(EntityRat creatureIn, double speedIn, int chance) {
        this.rat = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!rat.canMove()) {
            return false;
        }
        if (!this.mustUpdate) {
            if (this.rat.getRNG().nextInt(rat.isInCage() ? 3 : executionChance) != 0) {
                return false;
            }
        }
        Vec3d vec3d;
        boolean inCage = rat.isInCage();
        if(inCage){
            vec3d = RatUtils.findRandomCageTarget(this.rat, 5, 2);
        }else{
            vec3d = RandomPositionGenerator.findRandomTarget(this.rat, this.rat.isTamed() ? 5 : 10, 7);
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
