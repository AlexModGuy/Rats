package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumSet;

public class RatAIPickupEnergy extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private BlockPos targetBlock = null;
    private int feedingTicks;

    public RatAIPickupEnergy(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || !canPickUp() || entity.getAttackTarget() != null || entity.getRFTransferRate() == 0) {
            return false;
        }
        if (this.entity.getHeldRF() >= this.entity.getRFTransferRate()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.pickupPos;
    }

    private boolean canPickUp() {
        return this.entity.getCommand() == RatCommand.TRANSPORT || this.entity.getCommand() == RatCommand.HARVEST && this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldRF() < this.entity.getRFTransferRate();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vector3d(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5), false, entity);
        if (rayTrace instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTrace;
            BlockPos pos = blockRayTraceResult.getPos();
            BlockPos sidePos = blockRayTraceResult.getPos().offset(blockRayTraceResult.getFace());
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(targetBlock);
        }

        return true;
    }

    @Override
    public void tick() {
        if (this.targetBlock != null && this.entity.world.getTileEntity(this.targetBlock) != null) {
            TileEntity entity = this.entity.world.getTileEntity(this.targetBlock);
            this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
            double distance = this.entity.getDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
            if (distance <= 2.89F * this.entity.getRatDistanceModifier() && canSeeChest()) {
                LazyOptional<IEnergyStorage> handler = entity.getCapability(CapabilityEnergy.ENERGY, Direction.DOWN);
                if (handler.orElse(null) == null) {
                    return;
                }
                int howMuchWeWant = this.entity.getRFTransferRate() - this.entity.getHeldRF();
                int recievedEnergy = 0;
                try {
                    howMuchWeWant = Math.min(handler.orElse(null).getEnergyStored(), howMuchWeWant);
                    if (handler.orElse(null).extractEnergy(howMuchWeWant, true) > 0) {
                        recievedEnergy = handler.orElse(null).extractEnergy(howMuchWeWant, false);
                    }
                } catch (Exception e) {
                    //container is empty
                }
                if (recievedEnergy <= 0) {
                    this.targetBlock = null;
                    this.resetTask();
                } else {
                    this.entity.setHeldRF(this.entity.getHeldRF() + recievedEnergy);
                    this.targetBlock = null;
                    this.resetTask();
                }
            }

        }
    }
}
