package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Comparator;

public class RatPickupFromInventory extends EntityAIBase {
    private static final int RADIUS = 16;

    private BlockPos targetBlock = null;
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private int feedingTicks;

    public RatPickupFromInventory(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.TRANSPORT || this.entity.isInCage() || entity.getAttackTarget() != null) {
            return false;
        }
        if(!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()){
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.pickupPos;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    public void resetTask(){
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    public boolean canSeeChest(){
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(targetBlock.getX() + 0.5, targetBlock.getY() + 0.5, targetBlock.getZ() + 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(targetBlock);
        }
        return true;
    }
    @Override
    public void updateTask() {
        if (this.targetBlock != null && this.entity.world.getTileEntity(this.targetBlock) != null) {
            TileEntity entity = this.entity.world.getTileEntity(this.targetBlock);
            this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
            double distance = this.entity.getDistance(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 1, this.targetBlock.getZ() + 0.5D);
            if(distance < 2 && distance >= 1.65 && canSeeChest() && entity instanceof IInventory){
                toggleChest((IInventory)entity, true);
            }
            if (distance < 1.65 && canSeeChest()) {
                if(entity instanceof IInventory){
                    toggleChest((IInventory)entity, false);
                }
                IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                int slot = RatUtils.getItemSlotFromItemHandler(this.entity, handler, this.entity.world.rand);
                int extractSize = this.entity.getUpgrade().getItem() == RatsItemRegistry.RAT_UPGRADE_PLATTER ? 64 : 1;
                ItemStack stack = ItemStack.EMPTY;
                try{
                    stack = handler.extractItem(slot, extractSize, false);
                }catch(Exception e){
                    System.err.println("Rat tried to extract item that didnt exist");
                    e.printStackTrace();
                }
                if(slot == -1 || stack == ItemStack.EMPTY){
                    this.targetBlock = null;
                    this.resetTask();
                }else{
                    ItemStack duplicate = stack.copy();
                    if(!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote){
                        this.entity.entityDropItem(this.entity.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
                    }
                    this.entity.setHeldItem(EnumHand.MAIN_HAND, duplicate);
                    this.targetBlock = null;
                    this.resetTask();
                }
                this.entity.fleePos = this.targetBlock;
            }

        }
    }

    public void toggleChest(IInventory te, boolean open){
        if(te instanceof TileEntityChest){
            TileEntityChest chest = (TileEntityChest) te;
            if(open){
                chest.numPlayersUsing++;
                this.entity.world.addBlockEvent(this.targetBlock, chest.getBlockType(), 1, chest.numPlayersUsing);
            }else{
                if(chest.numPlayersUsing > 0){
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
