package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Comparator;
import java.util.EnumSet;

public class RatAIPickupFromInventory extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int feedingTicks;

    public RatAIPickupFromInventory(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || !canPickUp() || entity.getAttackTarget() != null) {
            return false;
        }
        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.pickupPos;
    }

    private boolean canPickUp() {
        return this.entity.getCommand() == RatCommand.TRANSPORT || this.entity.getCommand() == RatCommand.HARVEST && (this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) || this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLACER) || this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_BREEDER));
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty();
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
            this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
            double distance = this.entity.getDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
            if (distance < 6.25F * this.entity.getRatDistanceModifier() && distance > 2.72F * this.entity.getRatDistanceModifier() && canSeeChest() && entity instanceof IInventory) {
                toggleChest((IInventory) entity, true);
            }
            if (distance <= 2.89F * this.entity.getRatDistanceModifier() && canSeeChest()) {
                if (entity instanceof IInventory) {
                    toggleChest((IInventory) entity, false);
                }
                LazyOptional<IItemHandler> handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
                if (handler.orElse(null) == null) {
                    return;
                }
                int slot = RatUtils.getItemSlotFromItemHandler(this.entity, handler.orElse(null), this.entity.world.rand);
                int extractSize = this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLATTER) ? 64 : 1;
                ItemStack stack = ItemStack.EMPTY;
                try {
                    if (handler.orElse(null).getSlots() > 0 && handler.orElse(null).extractItem(slot, extractSize, true) != ItemStack.EMPTY) {
                        stack = handler.orElse(null).extractItem(slot, extractSize, false);
                    }
                } catch (Exception e) {
                    //container is empty
                }
                if (slot == -1 || stack == ItemStack.EMPTY) {
                    this.targetBlock = null;
                    this.resetTask();
                } else {
                    ItemStack duplicate = stack.copy();
                    if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                        this.entity.entityDropItem(this.entity.getHeldItem(Hand.MAIN_HAND), 0.0F);
                    }
                    this.entity.setHeldItem(Hand.MAIN_HAND, duplicate);
                    this.targetBlock = null;
                    this.resetTask();
                }
            }

        }
    }

    public void toggleChest(IInventory te, boolean open) {
        if (te instanceof ChestTileEntity) {
            ChestTileEntity chest = (ChestTileEntity) te;
            if (open) {
                this.entity.world.addBlockEvent(this.targetBlock, chest.getBlockState().getBlock(), 1, 1);
            } else {
                this.entity.world.addBlockEvent(this.targetBlock, chest.getBlockState().getBlock(), 1, 0);

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
