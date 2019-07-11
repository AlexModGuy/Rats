package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Comparator;

public class RatDepositInInventory extends EntityAIBase {
    private static final int RADIUS = 16;

    private BlockPos targetBlock = null;
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private int feedingTicks;

    public RatDepositInInventory(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.TRANSPORT && this.entity.getCommand() != RatCommand.GATHER && this.entity.getCommand() != RatCommand.HUNT && this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || entity.getAttackTarget() != null) {
            return false;
        }
        if (this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
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
        return targetBlock != null && !this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
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

    private BlockPos getMovePos(){
        return this.targetBlock.offset(this.entity.depositFacing);
    }

    @Override
    public void updateTask() {
        if (this.targetBlock != null && this.entity.world.getTileEntity(this.targetBlock) != null) {
            TileEntity entity = this.entity.world.getTileEntity(this.targetBlock);
            this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1D);
            double distance = this.entity.getDistance(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
            if (distance < 2 && distance >= 1.65 && canSeeChest() && entity instanceof IInventory) {
                toggleChest((IInventory) entity, true);
            }
            if (distance < 1.65 && canSeeChest()) {
                if (entity instanceof IInventory) {
                    toggleChest((IInventory) entity, false);
                }
                IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.entity.depositFacing);
                ItemStack duplicate = this.entity.getHeldItem(EnumHand.MAIN_HAND).copy();
                if(ItemHandlerHelper.insertItem(handler, duplicate, true).isEmpty()){
                    ItemHandlerHelper.insertItem(handler, duplicate, false);
                    this.entity.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                    this.targetBlock = null;
                    this.resetTask();
                }

            }

        }
    }

    public void toggleChest(IInventory te, boolean open) {
        if (te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            if (open) {
                chest.numPlayersUsing++;
                this.entity.world.addBlockEvent(this.targetBlock, chest.getBlockType(), 1, chest.numPlayersUsing);
            } else {
                if (chest.numPlayersUsing > 0) {
                    chest.numPlayersUsing = 0;
                    this.entity.world.addBlockEvent(this.targetBlock, chest.getBlockType(), 1, chest.numPlayersUsing);
                }
            }
        }
    }

    public class BlockSorter implements Comparator<BlockPos> {
        private final Entity entity;

        public BlockSorter(Entity entity) {
            this.entity = entity;
        }

        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            double distance1 = this.getDistance(pos1);
            double distance2 = this.getDistance(pos2);
            return Double.compare(distance1, distance2);
        }

        private double getDistance(BlockPos pos) {
            double deltaX = this.entity.posX - (pos.getX() + 0.5);
            double deltaY = this.entity.posY + this.entity.getEyeHeight() - (pos.getY() + 0.5);
            double deltaZ = this.entity.posZ - (pos.getZ() + 0.5);
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }
}
