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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

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
        if (!this.entity.transportingFluid.isEmpty() && this.entity.transportingFluid.getAmount() >= this.entity.getMBTransferRate()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.getPickupPos();
    }

    private boolean canPickUp() {
        return this.entity.getCommand() == RatCommand.TRANSPORT || this.entity.getCommand() == RatCommand.HARVEST && this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && (this.entity.transportingFluid.isEmpty() || this.entity.transportingFluid.getAmount() < this.entity.getMBTransferRate());
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVec(), new Vector3d(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5), false, entity);
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
            double distance = this.entity.getRatDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D);
            if (distance <= 2.89F * this.entity.getRatDistanceModifier() && canSeeChest()) {
                LazyOptional<IFluidHandler> handler = entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN);
                if (!handler.isPresent()) {
                    return;
                }
                int currentAmount = 0;
                if (!this.entity.transportingFluid.isEmpty()) {
                    currentAmount = this.entity.transportingFluid.getAmount();
                }
                int howMuchWeWant = this.entity.getMBTransferRate() - currentAmount;

                FluidStack drainedStack = null;
                try {
                    if (handler.orElse(null).getTanks() > 0) {
                        FluidStack firstTank = handler.orElse(null).getFluidInTank(0);
                        if (handler.orElse(null).getTanks() > 1) {
                            for(int i = 0; i < handler.orElse(null).getTanks(); i++){
                                FluidStack otherTank = handler.orElse(null).getFluidInTank(i);
                                if (!this.entity.transportingFluid.isEmpty() && this.entity.transportingFluid.isFluidEqual(otherTank)) {
                                    firstTank = otherTank;
                                }
                            }
                        }
                        if (firstTank.isEmpty() && (this.entity.transportingFluid.isEmpty() || this.entity.transportingFluid.isFluidEqual(firstTank))) {
                            howMuchWeWant = Math.min(firstTank.getAmount(), howMuchWeWant);

                            if (handler.orElse(null).drain(howMuchWeWant, IFluidHandler.FluidAction.SIMULATE) != null) {
                                drainedStack = handler.orElse(null).drain(howMuchWeWant, IFluidHandler.FluidAction.EXECUTE);
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
                    if (this.entity.transportingFluid.isEmpty()) {
                        this.entity.transportingFluid = drainedStack.copy();
                    } else {
                        this.entity.transportingFluid.setAmount(this.entity.transportingFluid.getAmount() + Math.max(drainedStack.getAmount(), 0));
                    }
                    if (!this.entity.world.isRemote) {
                        RatsMod.sendMSGToAll(new MessageUpdateRatFluid(this.entity.getEntityId(), this.entity.transportingFluid));
                    }
                    SoundEvent sound = this.entity.transportingFluid.isEmpty() ? SoundEvents.ITEM_BUCKET_FILL : SoundEvents.ITEM_BUCKET_FILL;//this.entity.transportingFluid.getFluid().getFillSound();
                    this.entity.playSound(sound, 1, 1);
                    this.targetBlock = null;
                    this.resetTask();
                }
            }

        }
    }
}
