package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RatAIHarvestMine extends EntityAIBase {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int breakingTime;
    private int previousBreakProgress;
    private IBlockState prevMiningState = null;

    public RatAIHarvestMine(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER)) {
            return false;
        }
        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        List<BlockPos> allBlocks = new ArrayList<>();
        NonNullList<ItemStack> mining = getMiningList();
        for (BlockPos pos : BlockPos.getAllInBox(this.entity.getPosition().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getPosition().add(RADIUS, RADIUS, RADIUS))) {
            if (doesListContainBlock(entity.world, mining, pos)) {
                allBlocks.add(pos);
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            this.targetBlock = allBlocks.get(0);
        }
    }

    private NonNullList<ItemStack> getMiningList() {
        NBTTagCompound nbttagcompound1 = entity.getUpgrade(RatsItemRegistry.RAT_UPGRADE_MINER).getTagCompound();
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
        if (nbttagcompound1 != null && nbttagcompound1.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
        }
        return nonnulllist;
    }

    private boolean doesListContainBlock(World world, NonNullList<ItemStack> list, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        ItemStack getStack = state.getBlock().getItem(world, pos, state);
        for (ItemStack stack : list) {
            if (stack.isItemEqual(getStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    @Override
    public void updateTask() {
        if (this.targetBlock != null) {
            BlockPos rayPos = entity.rayTraceBlockPos(this.targetBlock);
            if (rayPos == null) {
                rayPos = this.targetBlock;
            }
            if (this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D)) {
                rayPos = this.targetBlock;
            } else {
                this.entity.getNavigator().tryMoveToXYZ(rayPos.getX() + 0.5D, rayPos.getY(), rayPos.getZ() + 0.5D, 1D);
            }
            if (!entity.getMoveHelper().isUpdating() && entity.onGround) {
                IBlockState block = this.entity.world.getBlockState(rayPos);
                SoundType soundType = block.getBlock().getSoundType(block, entity.world, rayPos, null);
                if (RatUtils.canRatBreakBlock(entity.world, rayPos, entity) && block.getMaterial().blocksMovement() && block.getMaterial() != Material.AIR) {
                    double distance = this.entity.getDistance(rayPos.getX(), rayPos.getY(), rayPos.getZ());
                    if (distance < 2.5F) {
                        entity.world.setEntityState(entity, (byte) 85);
                        entity.crafting = true;
                        if (block == prevMiningState) {
                            entity.world.setEntityState(entity, (byte) 85);
                            entity.crafting = true;
                        } else {
                            entity.world.setEntityState(entity, (byte) 86);
                            entity.crafting = false;
                        }
                        if (distance < 0.6F) {
                            entity.motionZ *= 0.0D;
                            entity.motionX *= 0.0D;
                            entity.getNavigator().clearPath();
                            entity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
                        }
                        breakingTime++;
                        int hardness = (int) (block.getBlockHardness(entity.world, rayPos) * 100);
                        int i = (int) ((float) this.breakingTime / hardness * 10.0F);
                        if (breakingTime % 5 == 0) {
                            entity.playSound(soundType.getHitSound(), soundType.volume + 1, soundType.pitch);
                        }
                        if (i != this.previousBreakProgress) {
                            entity.world.sendBlockBreakProgress(entity.getEntityId(), rayPos, i);
                            this.previousBreakProgress = i;
                        }
                        if (this.breakingTime == hardness) {
                            entity.world.setEntityState(entity, (byte) 86);
                            entity.playSound(soundType.getBreakSound(), soundType.volume, soundType.pitch);
                            entity.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1F);
                            this.breakingTime = 0;
                            this.previousBreakProgress = -1;
                            destroyBlock(rayPos, block);
                            this.entity.fleePos = rayPos;
                            targetBlock = null;
                            entity.crafting = false;
                            this.resetTask();
                        }
                        prevMiningState = block;
                    }
                }
            }

        }
    }

    private void destroyBlock(BlockPos pos, IBlockState state) {
        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(drops, this.entity.world, pos, state, 0);
        if (!drops.isEmpty() && entity.canRatPickupItem(drops.get(0))) {
            ItemStack duplicate = drops.get(0).copy();
            drops.remove(0);
            if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                this.entity.entityDropItem(this.entity.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
            }
            this.entity.setHeldItem(EnumHand.MAIN_HAND, duplicate);
            for (ItemStack drop : drops) {
                this.entity.entityDropItem(drop, 0);
            }
            this.entity.world.destroyBlock(pos, false);
            if (state.getBlock() instanceof BlockCrops) {
                this.entity.world.setBlockState(pos, state.getBlock().getDefaultState());
            }
            this.entity.fleePos = pos;
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
