package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootTables;

import java.util.*;
import java.util.stream.Collectors;

public class RatAIHarvestFisherman extends Goal {
    private final EntityRat entity;
    private final RatAIHarvestFisherman.BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private boolean hasReachedWater = false;
    private int fishingCooldown = 1000;
    private int throwCooldown = 0;
    private Random rand = new Random();

    public RatAIHarvestFisherman(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new RatAIHarvestFisherman.BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) || !entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && entity.getHeldItem(Hand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
        this.hasReachedWater = false;
        this.fishingCooldown = 250 + rand.nextInt(750);
    }


    @Override
    public void tick() {
        if (this.targetBlock != null) {
            if (entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
                if(hasReachedWater){
                    this.entity.getNavigator().clearPath();
                }
                if (!hasReachedWater) {
                    this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
                }
                if (isShore(this.targetBlock, entity.world)) {
                    double distance = this.entity.getDistanceSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 4.5F * this.entity.getRatDistanceModifier()) {
                        // this.targetBlock = null;
                        //  this.resetTask();
                        if (throwCooldown == 0) {
                            entity.playSound(SoundEvents.ENTITY_FISHING_BOBBER_THROW, 1, 0.5F);
                            throwCooldown = 20;
                        }
                        hasReachedWater = true;
                    } else {
                        hasReachedWater = false;
                    }
                } else {
                    this.targetBlock = null;
                    this.resetTask();
                }
            }
        }
        if (hasReachedWater) {
            //spawnFishingLoot();
            entity.world.setEntityState(entity, (byte) 85);
            entity.crafting = true;
            if (fishingCooldown > 0) {
                fishingCooldown--;
            }
            if (fishingCooldown == 0) {
                spawnFishingLoot();
                entity.world.setEntityState(entity, (byte) 101);
                entity.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 1, 1);
            }
        } else {
            entity.world.setEntityState(entity, (byte) 86);
            entity.crafting = false;
        }
        if (throwCooldown > 0) {
            throwCooldown--;
        }
    }

    private void resetTarget() {
        List<BlockPos> allBlocks = new ArrayList<>();
        int RADIUS = entity.getSearchRadius();
        for (BlockPos pos : BlockPos.getAllInBox(this.entity.getSearchCenter().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getSearchCenter().add(RADIUS, RADIUS, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
            if (isShore(pos, this.entity.world)) {
                allBlocks.add(pos);
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            this.targetBlock = allBlocks.get(0);
        }
    }

    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private boolean isShore(BlockPos pos, World world) {
        for (Direction facing : HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER && world.getBlockState(pos).isOpaqueCube(this.entity.world, pos) && world.isAirBlock(pos.up())) {
                return true;
            }
        }
        return false;
    }

    public void spawnFishingLoot() {
        this.fishingCooldown = 250 + rand.nextInt(750);
        double luck = 0.1D;
        LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld) this.entity.world);
        lootcontext$builder.withLuck((float) luck); // Forge: add player & looted entity to LootContext
        LootParameterSet.Builder lootparameterset$builder = new LootParameterSet.Builder();
        List<ItemStack> result = entity.world.getServer().getLootTableManager().getLootTableFromLocation(LootTables.GAMEPLAY_FISHING).generate(lootcontext$builder.build(lootparameterset$builder.build()));

        for (ItemStack itemstack : result) {
            ItemEntity ItemEntity = new ItemEntity(this.entity.world, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), itemstack);
            if (!this.entity.world.isRemote) {
                this.entity.world.addEntity(ItemEntity);
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