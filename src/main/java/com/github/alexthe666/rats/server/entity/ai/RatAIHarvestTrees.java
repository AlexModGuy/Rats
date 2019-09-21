package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import crafttweaker.api.block.IBlock;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RatAIHarvestTrees extends EntityAIBase {
    private static final int RADIUS = 16;

    private BlockPos targetBlock = null;
    private final EntityRat entity;
    private final RatAIHarvestTrees.BlockSorter targetSorter;
    private int destroyedLeaves;
    private int breakingTime;
    private int previousBreakProgress;

    public RatAIHarvestTrees(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new RatAIHarvestTrees.BlockSorter(entity);
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !this.entity.hasUpgrade( RatsItemRegistry.RAT_UPGRADE_LUMBERJACK)) {
            return false;
        }
        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private void resetTarget() {
        World world = entity.world;
        List<BlockPos> allBlocks = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(this.entity.getPosition().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getPosition().add(RADIUS, RADIUS, RADIUS))) {
            IBlockState block = world.getBlockState(pos);
            if (isBlockLog(block)) {
                BlockPos topOfLog = new BlockPos(pos);
                while(!world.isAirBlock(topOfLog.up()) && topOfLog.getY() < world.getHeight()){
                    topOfLog = topOfLog.up();
                }
                if(isBlockLeaf(world.getBlockState(topOfLog))){
                    //definitely a tree, now find the base of the tree
                    BlockPos logPos = getStump(topOfLog);
                    allBlocks.add(logPos);
                }
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            this.targetBlock = allBlocks.get(0);
        }
    }

    private BlockPos getStump(BlockPos log) {
        if(log.getY() > 0) {
            for (BlockPos pos : BlockPos.getAllInBox(log.add(-4, -4, -4), log.add(4, 0, 4))) {
                IBlockState state = entity.world.getBlockState(pos.down());
                if (isBlockLog(state) || isBlockLeaf(state)) {
                    return getStump(pos.down());
                }
            }
        }
        return log;
    }
    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        destroyedLeaves = 0;
        entity.crafting = false;
        resetTarget();
    }

    @Override
    public void updateTask() {
        if (this.targetBlock != null) {
            IBlockState block = this.entity.world.getBlockState(this.targetBlock);
            if(!this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D)){
                RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(this.targetBlock.getX() + 0.5D, this.targetBlock.getY() + 0.5D, this.targetBlock.getZ() + 0.5D), false);
                if (rayTrace != null && rayTrace.hitVec != null) {
                    BlockPos pos = rayTrace.getBlockPos().offset(rayTrace.sideHit);
                    this.entity.getNavigator().tryMoveToXYZ(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1D);
                }
            }
            if (isBlockLog(block)) {
                double distance = this.entity.getDistance(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                if (distance < 2.5F) {
                    entity.world.setEntityState(entity, (byte) 85);
                    entity.crafting = true;
                    if(distance < 0.6F){
                        entity.motionZ *= 0.0D;
                        entity.motionX *= 0.0D;
                        entity.getNavigator().clearPath();
                        entity.getMoveHelper().action = EntityMoveHelper.Action.WAIT;
                    }
                    breakingTime++;
                    int i = (int) ((float) this.breakingTime / 160.0F * 10.0F);
                    if (breakingTime % 10 == 0) {
                        entity.playSound(SoundEvents.BLOCK_WOOD_HIT, 1, 1);
                        entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1, 0.5F);
                    }
                    if (i != this.previousBreakProgress) {
                        entity.world.sendBlockBreakProgress(entity.getEntityId(), targetBlock, i);
                        this.previousBreakProgress = i;
                    }
                    if (this.breakingTime == 160) {
                        entity.world.setEntityState(entity, (byte) 86);
                        entity.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1, 1);
                        this.breakingTime = 0;
                        this.previousBreakProgress = -1;
                        this.fellTree();
                        this.entity.fleePos = this.targetBlock;
                        this.targetBlock = null;
                        entity.crafting = false;
                        this.resetTask();
                    }
                }
            }else{
                this.entity.fleePos = this.targetBlock;
                this.targetBlock = null;
                this.resetTask();
            }

        }
    }

    private void fellTree() {
        World world = entity.world;
        BlockPos base = new BlockPos(this.targetBlock);
        while(isBlockLog(world.getBlockState(base))){
            destroyedLeaves = 0;
            destroyLeaves(base);
            world.destroyBlock(base, true);
            base = base.up();
        }
    }

    private void destroyLeaves(BlockPos base) {
        if(destroyedLeaves < RatsMod.CONFIG_OPTIONS.maxDestroyedLeaves) {
            for (BlockPos pos : BlockPos.getAllInBox(base.add(-1, -1, -1), base.add(1, 1, 1))) {
                if (!pos.equals(base)) {
                    if (isBlockLog(entity.world.getBlockState(pos)) || isBlockLeaf(entity.world.getBlockState(pos))) {
                        entity.world.destroyBlock(pos, true);
                        destroyedLeaves++;
                        destroyLeaves(pos);
                    }
                }
            }
        }
    }

    public static final boolean isBlockLog(IBlockState block) {
        return block.getBlock() instanceof BlockLog || block.getBlock().getTranslationKey().contains("log") || block.getBlock().getTranslationKey().contains("Log");
    }

    public static final boolean isBlockLeaf(IBlockState block) {
        return block.getBlock() instanceof BlockLeaves
                || block.getBlock().getTranslationKey().contains("leaf") || block.getBlock().getTranslationKey().contains("leaves")
                || block.getBlock().getTranslationKey().contains("Leaf") || block.getBlock().getTranslationKey().contains("Leaves");
    }

    public class BlockSorter implements Comparator<BlockPos> {
        private final Entity entity;

        public BlockSorter(Entity entity) {
            this.entity = entity;
        }

        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            double yDist1 = Math.abs(pos1.getY() + 0.5 - entity.posY);
            double yDist2 = Math.abs(pos2.getY() + 0.5 - entity.posY);
            if(yDist1 == yDist2){
                double distance1 = this.getDistance(pos1);
                double distance2 = this.getDistance(pos2);
                return Double.compare(distance1, distance2);
            }else{
                return Double.compare(yDist1, yDist2);
            }
        }

        private double getDistance(BlockPos pos) {
            double deltaX = this.entity.posX - (pos.getX() + 0.5);
            double deltaY = this.entity.posY + this.entity.getEyeHeight() - (pos.getY() + 0.5);
            double deltaZ = this.entity.posZ - (pos.getZ() + 0.5);
            return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        }
    }
}