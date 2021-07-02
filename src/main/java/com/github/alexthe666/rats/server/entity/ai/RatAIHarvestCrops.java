package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class RatAIHarvestCrops extends Goal {
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int feedingTicks;

    public RatAIHarvestCrops(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage()) {
            return false;
        }
        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        List<BlockPos> allBlocks = new ArrayList<>();
        int RADIUS = entity.getSearchRadius();
        for (BlockPos pos : BlockPos.getAllInBox(this.entity.getSearchCenter().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getSearchCenter().add(RADIUS, RADIUS, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
            BlockState block = this.entity.world.getBlockState(pos);
            if ((block.getBlock() instanceof CropsBlock && ((CropsBlock) block.getBlock()).isMaxAge(block) || !(block.getBlock() instanceof CropsBlock) && block.getBlock() instanceof BushBlock || block.getMaterial() == Material.GOURD)) {
                if(!(block.getBlock() instanceof StemBlock) && !(block.getBlock() instanceof AttachedStemBlock)){
                    LootContext.Builder loot = new LootContext.Builder((ServerWorld)entity.world).withParameter(LootParameters.field_237457_g_, entity.getPositionVec()).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck((float)1.0F);
                    List<ItemStack> items = block.getBlock().getDrops(block, loot);
                    for(ItemStack stack : items){
                        if (entity.canRatPickupItem(stack)) {
                            allBlocks.add(pos);
                        }
                    }
                }
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            this.targetBlock = allBlocks.get(0);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && (this.entity.world.getBlockState(targetBlock).getBlock() instanceof BushBlock || this.entity.world.getBlockState(targetBlock).getMaterial() == Material.GOURD);
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {
            BlockState block = this.entity.world.getBlockState(this.targetBlock);
            this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
            if (block.getBlock() instanceof BushBlock || block.getMaterial() == Material.GOURD) {
                if (block.getBlock() instanceof CropsBlock && !((CropsBlock) block.getBlock()).isMaxAge(block)) {
                    this.targetBlock = null;
                    this.resetTask();
                    return;
                }
                double distance = this.entity.getRatDistanceCenterSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                if (distance < 4.25F * this.entity.getRatDistanceModifier()) {
                    LootContext.Builder loot = new LootContext.Builder((ServerWorld)entity.world).withParameter(LootParameters.field_237457_g_, entity.getPositionVec()).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck((float)1.0F);
                    List<ItemStack> drops = block.getBlock().getDrops(block, loot);
                    if (!drops.isEmpty() && entity.canRatPickupItem(drops.get(0))) {
                        ItemStack duplicate = drops.get(0).copy();
                        try{
                            drops.remove(0);
                        }catch (Exception e){}
                        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                            this.entity.entityDropItem(this.entity.getHeldItem(Hand.MAIN_HAND), 0.0F);
                        }
                        this.entity.setHeldItem(Hand.MAIN_HAND, duplicate);
                        for (ItemStack drop : drops) {
                            this.entity.entityDropItem(drop, 0);
                        }

                    }
                    this.entity.fleePos = this.targetBlock;

                    if ((!RatConfig.ratsBreakBlockOnHarvest || entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_REPLANTER))) {
                        if(block.getMaterial() == Material.GOURD || block.getBlock() instanceof FlowerBlock || block.getBlock() instanceof LilyPadBlock){
                            this.entity.world.destroyBlock(targetBlock, false);
                        }else{
                            this.entity.world.setBlockState(targetBlock, block.getBlock().getDefaultState());
                        }
                    }else{
                        this.entity.world.destroyBlock(targetBlock, false);
                    }
                    this.targetBlock = null;
                    this.resetTask();
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
            double deltaX = this.entity.getPosX() - (pos.getX() + 0.5);
            double deltaY = this.entity.getPosY() + this.entity.getEyeHeight() - (pos.getY() + 0.5);
            double deltaZ = this.entity.getPosZ() - (pos.getZ() + 0.5);
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }
}
