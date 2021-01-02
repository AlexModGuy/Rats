package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class RatAIDepositInInventory extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int feedingTicks;
    private int breakingTime;
    private int previousBreakProgress;

    public RatAIDepositInInventory(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.TRANSPORT && this.entity.getCommand() != RatCommand.GATHER && this.entity.getCommand() != RatCommand.HUNT_ANIMALS && this.entity.getCommand() != RatCommand.HARVEST || entity.getAttackTarget() != null) {
            return false;
        }
        if (!this.entity.shouldDepositItem(entity.getHeldItemMainhand())) {
            return false;
        }
        if (this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        this.targetBlock = entity.getDepositPos();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && !this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && this.entity.shouldDepositItem(entity.getHeldItemMainhand());
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
            if ((this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER_ORE) || this.entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) && !entity.getMoveHelper().isUpdating() && entity.func_233570_aj_()&& !this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY(), getMovePos().getZ() + 0.5D, 1.25D)) {
                RatUtils.doRatMinerLogic(entity, targetBlock, this);
            } else {
                this.entity.getNavigator().tryMoveToXYZ(getMovePos().getX() + 0.5D, getMovePos().getY() + 0.5D, getMovePos().getZ() + 0.5D, 1.25D);

                double distance = Math.sqrt(this.entity.getRatDistanceSq(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D));
                if (distance < 3.5D * entity.getRatDistanceModifier() && distance > 2.5D * entity.getRatDistanceModifier() && canSeeChest() && te instanceof IInventory) {
                    toggleChest((IInventory) te, true);
                }
                if (distance <= 2.5D * entity.getRatDistanceModifier() && canSeeChest()) {
                    if (te instanceof IInventory) {
                        toggleChest((IInventory) te, false);
                    }
                    LazyOptional<IItemHandler> handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.entity.depositFacing);
                    if(handler.orElse(null) != null) {
                        ItemStack duplicate = this.entity.getHeldItem(Hand.MAIN_HAND).copy();
                        if (!ItemHandlerHelper.insertItem(handler.orElse(null), duplicate, true).equals(duplicate)) {
                            ItemStack shrunkenStack = ItemHandlerHelper.insertItem(handler.orElse(null), duplicate, false);
                            if(shrunkenStack.isEmpty()){
                                this.entity.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                            }else{
                                this.entity.setHeldItem(Hand.MAIN_HAND, shrunkenStack);
                            }
                            this.targetBlock = null;
                            this.resetTask();
                        }
                    }
                }
            }

        }
    }

    private void destroyBlock(BlockPos pos, BlockState state) {
        if(entity.world instanceof ServerWorld){
            LootContext.Builder loot = new LootContext.Builder((ServerWorld)entity.world).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck((float)1.0F);
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
            double deltaX = this.entity.getPosX() - (pos.getX() + 0.5);
            double deltaY = this.entity.getPosY() + this.entity.getEyeHeight() - (pos.getY() + 0.5);
            double deltaZ = this.entity.getPosZ() - (pos.getZ() + 0.5);
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }
}
