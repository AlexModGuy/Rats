package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatConfig;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

public abstract class RatAIMoveToBlock extends Goal {
    private final CreatureEntity creature;
    private final double movementSpeed;
    private final int searchLength;
    /**
     * Controls task execution delay
     */
    protected int runDelay;
    /**
     * Block to move to
     */
    protected BlockPos destinationBlock = BlockPos.ZERO;
    protected double distanceCheck = 1.0D;
    private int timeoutCounter;
    private int maxStayTicks;
    private boolean isAboveDestination;

    public RatAIMoveToBlock(CreatureEntity creature, double speedIn, int length) {
        this.creature = creature;
        this.movementSpeed = speedIn;
        this.searchLength = length;
        this.distanceCheck = 1.0D;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    public boolean shouldExecute() {
        if (this.runDelay > 0) {
            --this.runDelay;
            return false;
        } else {
            this.runDelay = RatConfig.ratUpdateDelay + this.creature.getRNG().nextInt(RatConfig.ratUpdateDelay);
            return this.searchForDestination();
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        this.timeoutCounter = 0;
        this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
    }

    public double getTargetDistanceSq() {
        return 1.0D;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (!this.destinationBlock.up().withinDistance(this.creature.getPositionVec(), this.getTargetDistanceSq())) {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0) {
                this.creature.getNavigator().tryMoveToXYZ((double) ((float) this.destinationBlock.getX()) + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) ((float) this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
            }
        } else {
            this.isAboveDestination = true;
            --this.timeoutCounter;
        }
    }

    protected boolean getIsAboveDestination() {
        return this.isAboveDestination;
    }

    private boolean searchForDestination() {
        int i = this.searchLength;
        int j = 1;
        BlockPos blockpos = new BlockPos(this.creature);

        for (int k = 0; k <= 1; k = k > 0 ? -k : 1 - k) {
            for (int l = 0; l < i; ++l) {
                for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        BlockPos blockpos1 = blockpos.add(i1, k - 1, j1);

                        if (this.creature.isWithinHomeDistanceFromPosition(blockpos1) && this.shouldMoveTo(this.creature.world, blockpos1)) {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Return true to set given position as destination
     */
    protected abstract boolean shouldMoveTo(World worldIn, BlockPos pos);
}