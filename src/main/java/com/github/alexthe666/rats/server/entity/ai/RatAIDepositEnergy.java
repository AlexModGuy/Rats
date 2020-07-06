package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumSet;
import java.util.List;

public class RatAIDepositEnergy extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private BlockPos targetBlock = null;
    private int feedingTicks;
    private int breakingTime;
    private int previousBreakProgress;

    public RatAIDepositEnergy(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || entity.getRFTransferRate() == 0 || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.TRANSPORT && this.entity.getCommand() != RatCommand.GATHER && this.entity.getCommand() != RatCommand.HUNT_ANIMALS && this.entity.getCommand() != RatCommand.HARVEST || entity.getAttackTarget() != null) {
            return false;
        }
        if (this.entity.getHeldRF() <= 0) {
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
        return targetBlock != null && this.entity.getHeldRF() > 0;
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

    private Vector3d getMovePos() {
        BlockPos minusVec = this.targetBlock.offset(this.entity.depositFacing).subtract(this.targetBlock);
        return new Vector3d(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()).add(minusVec.getX() * 0.25D, minusVec.getY() * 0.25D, minusVec.getZ() * 0.25D);
    }

    @Override
    public void tick() {
        if (this.targetBlock != null && this.entity.world.getTileEntity(this.targetBlock) != null) {
            TileEntity te = this.entity.world.getTileEntity(this.targetBlock);
            //break block if has miner upgrade
            if (this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER) && !entity.getMoveHelper().isUpdating() && entity.func_233570_aj_()&& !this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1.25D)) {
                RatUtils.doRatMinerLogic(entity, targetBlock, this);
            } else {
                this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1.25D);
                double distance = this.entity.getDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
                if (distance < 3.4 && canSeeChest() && te != null) {
                    LazyOptional<IEnergyStorage> handler = te.getCapability(CapabilityEnergy.ENERGY, this.entity.depositFacing);
                    if (handler.orElse(null) == null) {
                        this.targetBlock = null;
                        this.resetTask();
                        return;
                    }
                    int howMuchWeHave = this.entity.getHeldRF();
                    int inputtedEnergy = 0;
                    try {
                        if (handler.orElse(null).canReceive() && handler.orElse(null).receiveEnergy(howMuchWeHave, true) > 0) {
                            inputtedEnergy = handler.orElse(null).receiveEnergy(howMuchWeHave, false);
                        }
                    } catch (Exception e) {
                        //container is empty
                    }
                    if (inputtedEnergy <= 0) {
                        this.targetBlock = null;
                        this.resetTask();
                    } else {
                        this.entity.setHeldRF(Math.max(0, this.entity.getHeldRF() - inputtedEnergy));
                        this.targetBlock = null;
                        this.resetTask();
                    }
                }
            }

        }
    }

    private void destroyBlock(BlockPos pos, BlockState state) {
        if(entity.world instanceof ServerWorld){
            LootContext.Builder loot = new LootContext.Builder((ServerWorld)entity.world).withParameter(LootParameters.POSITION, new BlockPos(pos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck((float)1.0F);
            List<ItemStack> drops = state.getBlock().getDrops(state, loot);
            if (!drops.isEmpty() && entity.canRatPickupItem(drops.get(0))) {
                for (ItemStack drop : drops) {
                    this.entity.entityDropItem(drop, 0);
                }
                this.entity.world.destroyBlock(pos, false);
                this.entity.fleePos = pos;
            }
        }

    }
}
