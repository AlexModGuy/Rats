package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageUpdateRatFluid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class RatAIDepositFluid extends EntityAIBase {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private BlockPos targetBlock = null;
    private int feedingTicks;
    private int breakingTime;
    private int previousBreakProgress;

    public RatAIDepositFluid(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || entity.getMBTransferRate() == 0 || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.TRANSPORT && this.entity.getCommand() != RatCommand.GATHER && this.entity.getCommand() != RatCommand.HUNT && this.entity.getCommand() != RatCommand.HARVEST || entity.getAttackTarget() != null) {
            return false;
        }
        if (this.entity.transportingFluid == null || this.entity.transportingFluid.amount == 0) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.depositPos;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && (this.entity.transportingFluid != null && this.entity.transportingFluid.amount > 0);
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(targetBlock);
        }

        return true;
    }

    private BlockPos getMovePos() {
        return this.targetBlock.offset(this.entity.depositFacing);
    }

    @Override
    public void updateTask() {
        if (this.targetBlock != null && this.entity.world.getTileEntity(this.targetBlock) != null) {
            TileEntity te = this.entity.world.getTileEntity(this.targetBlock);
            //break block if has miner upgrade
            if (this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !entity.getMoveHelper().isUpdating() && entity.onGround && !this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1D)) {
                BlockPos rayPos = this.entity.rayTraceBlockPos(this.targetBlock.up());
                if (rayPos != null && !rayPos.equals(targetBlock)) {
                    BlockState block = this.entity.world.getBlockState(rayPos);
                    if (RatUtils.canRatBreakBlock(this.entity.world, rayPos, this.entity) && block.getMaterial().blocksMovement() && block.getMaterial() != Material.AIR) {
                        double distance = this.entity.getDistance(rayPos.getX(), rayPos.getY(), rayPos.getZ());
                        SoundType soundType = block.getBlock().getSoundType(block, this.entity.world, rayPos, null);
                        if (distance < 2.5F) {
                            this.entity.world.setEntityState(this.entity, (byte) 85);
                            this.entity.crafting = true;
                            if (distance < 0.6F) {
                                this.entity.motionZ *= 0.0D;
                                this.entity.motionX *= 0.0D;
                                this.entity.getNavigator().clearPath();
                                this.entity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
                            }
                            breakingTime++;
                            int hardness = (int) (block.getBlockHardness(this.entity.world, rayPos) * 100);
                            int i = (int) ((float) this.breakingTime / hardness * 10.0F);
                            if (breakingTime % 10 == 0) {
                                this.entity.playSound(soundType.getHitSound(), soundType.volume, soundType.pitch);
                                this.entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5F);
                            }
                            if (i != this.previousBreakProgress) {
                                this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), rayPos, i);
                                this.previousBreakProgress = i;
                            }
                            if (this.breakingTime == hardness) {
                                this.entity.world.setEntityState(this.entity, (byte) 86);
                                entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1F);
                                this.entity.playSound(soundType.getBreakSound(), soundType.volume, soundType.pitch);
                                this.breakingTime = 0;
                                this.previousBreakProgress = -1;
                                destroyBlock(rayPos, block);
                                this.entity.fleePos = rayPos;
                                targetBlock = null;
                                this.entity.crafting = false;
                                this.resetTask();
                            }
                        }
                    }
                }
            } else {
                this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1D);
                double distance = this.entity.getDistance(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
                if (distance < 1.86 && canSeeChest() && te != null) {
                    FluidStack copiedFluid = this.entity.transportingFluid.copy();
                    IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.entity.depositFacing);
                    if (handler == null) {
                        return;
                    }
                    if (this.entity.transportingFluid != null) {
                        int minusAmount = 0;
                        try {
                            if (handler.getTankProperties().length > 0) {
                                IFluidTankProperties firstTank = handler.getTankProperties()[0];
                                if (handler.getTankProperties().length > 1) {
                                    for (IFluidTankProperties otherTank : handler.getTankProperties()) {
                                        if (copiedFluid != null && copiedFluid.isFluidEqual(otherTank.getContents())) {
                                            firstTank = otherTank;
                                        }
                                    }
                                }
                                if (firstTank.getContents() == null || (copiedFluid == null || copiedFluid.isFluidEqual(firstTank.getContents()))) {
                                    if (handler.fill(copiedFluid, false) != 0) {
                                        minusAmount = handler.fill(copiedFluid, true);
                                    }
                                }

                            }
                        } catch (Exception e) {
                            //container is empty
                        }

                        if (minusAmount <= 0) {
                            this.targetBlock = null;
                            this.resetTask();
                        } else {
                            int total = copiedFluid.amount - minusAmount;
                            if (total <= 0) {
                                this.entity.transportingFluid = null;
                            } else {
                                this.entity.transportingFluid.amount = total;
                            }
                            if (!this.entity.world.isRemote) {
                                RatsMod.NETWORK_WRAPPER.sendToAll(new MessageUpdateRatFluid(this.entity.getEntityId(), this.entity.transportingFluid));
                            }
                            SoundEvent sound = this.entity.transportingFluid == null ? SoundEvents.ITEM_BUCKET_EMPTY : this.entity.transportingFluid.getFluid().getEmptySound();
                            this.entity.playSound(sound, 1, 1);
                            this.targetBlock = null;
                            this.resetTask();
                        }
                    }
                    if (handler == null) {
                        this.targetBlock = null;
                        this.resetTask();
                        return;
                    }
                }
            }

        }
    }

    private void destroyBlock(BlockPos pos, BlockState state) {
        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(drops, this.entity.world, pos, state, 0);
        if (!drops.isEmpty() && entity.canRatPickupItem(drops.get(0))) {
            for (ItemStack drop : drops) {
                this.entity.entityDropItem(drop, 0);
            }
            this.entity.world.destroyBlock(pos, false);
            this.entity.fleePos = pos;
        }
    }
}
