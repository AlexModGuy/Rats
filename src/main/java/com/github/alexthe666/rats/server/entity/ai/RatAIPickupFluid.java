package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageUpdateRatFluid;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.EnumSet;

public class RatAIPickupFluid extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private BlockPos targetBlock = null;
    private int feedingTicks;

    public RatAIPickupFluid(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || !canPickUp() || entity.getAttackTarget() != null || entity.getMBTransferRate() == 0) {
            return false;
        }
        if (this.entity.transportingFluid != null && this.entity.transportingFluid.amount >= this.entity.getMBTransferRate()) {
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
        return targetBlock != null && (this.entity.transportingFluid == null || this.entity.transportingFluid.amount < this.entity.getMBTransferRate());
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5), false, entity);
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
            this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            double distance = this.entity.getDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
            if (distance <= 2.89F && canSeeChest()) {
                LazyOptional<IFluidHandler> handler = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN);
                if (handler.orElse(null) == null) {
                    return;
                }
                int currentAmount = 0;
                if (this.entity.transportingFluid != null) {
                    currentAmount = this.entity.transportingFluid.amount;
                }
                int howMuchWeWant = this.entity.getMBTransferRate() - currentAmount;

                FluidStack drainedStack = null;
                try {
                    int totalTankHeld = 0;
                    if (handler.orElse(null).getTankProperties().length > 0) {
                        IFluidTankProperties firstTank = handler.orElse(null).getTankProperties()[0];
                        if (handler.orElse(null).getTankProperties().length > 1) {
                            for (IFluidTankProperties otherTank : handler.orElse(null).getTankProperties()) {
                                if (this.entity.transportingFluid != null && this.entity.transportingFluid.isFluidEqual(otherTank.getContents())) {
                                    firstTank = otherTank;
                                }
                            }
                        }
                        if (firstTank.getContents() != null && (this.entity.transportingFluid == null || this.entity.transportingFluid.isFluidEqual(firstTank.getContents()))) {
                            howMuchWeWant = Math.min(firstTank.getContents().amount, howMuchWeWant);

                            if (handler.orElse(null).drain(howMuchWeWant, false) != null) {
                                drainedStack = handler.orElse(null).drain(howMuchWeWant, true);
                            }
                        }

                    }
                } catch (Exception e) {
                    //container is empty
                }
                if (drainedStack == null) {
                    this.targetBlock = null;
                    this.resetTask();
                } else {
                    if (this.entity.transportingFluid == null) {
                        this.entity.transportingFluid = drainedStack.copy();
                    } else {
                        this.entity.transportingFluid.amount += Math.max(drainedStack.amount, 0);
                    }
                    if (!this.entity.world.isRemote) {
                        RatsMod.sendMSGToAll(new MessageUpdateRatFluid(this.entity.getEntityId(), this.entity.transportingFluid));
                    }
                    SoundEvent sound = this.entity.transportingFluid == null ? SoundEvents.ITEM_BUCKET_FILL : this.entity.transportingFluid.getFluid().getFillSound();
                    this.entity.playSound(sound, 1, 1);
                    this.targetBlock = null;
                    this.resetTask();
                }
            }

        }
    }
}
