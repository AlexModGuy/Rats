package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RatAIHarvestFisherman extends EntityAIBase {
    private static final int RADIUS = 16;
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
        this.setMutexBits(0);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FISHERMAN) || !entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
        this.hasReachedWater = false;
        this.fishingCooldown = 250 + rand.nextInt(750);
    }


    @Override
    public void updateTask() {
        if (this.targetBlock != null) {
            if (entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                if (!hasReachedWater) {
                    this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1D);
                }
                if (isShore(this.targetBlock, entity.world)) {
                    double distance = this.entity.getDistance(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 1.5F) {
                        // this.targetBlock = null;
                        //  this.resetTask();
                        if(throwCooldown == 0){
                            entity.playSound(SoundEvents.ENTITY_BOBBER_THROW, 1, 0.5F);
                            throwCooldown = 20;
                        }
                        hasReachedWater = true;
                    }else{
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
            if(fishingCooldown > 0){
                fishingCooldown--;
            }
            if(fishingCooldown == 0){
                spawnFishingLoot();
                entity.world.setEntityState(entity, (byte) 101);
                entity.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 1, 1);
            }
        } else {
            entity.world.setEntityState(entity, (byte) 86);
            entity.crafting = false;
        }
        if(throwCooldown > 0){
            throwCooldown--;
        }
    }

    private void resetTarget() {
        List<BlockPos> allBlocks = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(this.entity.getPosition().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getPosition().add(RADIUS, RADIUS, RADIUS))) {
            if (isShore(pos, this.entity.world)) {
                allBlocks.add(pos);
            }
        }
        if (!allBlocks.isEmpty()) {
            allBlocks.sort(this.targetSorter);
            this.targetBlock = allBlocks.get(0);
        }
    }

    private boolean isShore(BlockPos pos, World world) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER && world.getBlockState(pos).isOpaqueCube() && world.isAirBlock(pos.up())) {
                return true;
            }
        }
        return false;
    }

    public void spawnFishingLoot() {
        this.fishingCooldown = 250 + rand.nextInt(750);
        double luck = 0.1D;
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) this.entity.world);
        lootcontext$builder.withLuck((float) luck).withLootedEntity(this.entity); // Forge: add player & looted entity to LootContext
        List<ItemStack> result = this.entity.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.entity.getRNG(), lootcontext$builder.build());

        for (ItemStack itemstack : result) {
            EntityItem entityitem = new EntityItem(this.entity.world, this.entity.posX, this.entity.posY, this.entity.posZ, itemstack);
            if(!this.entity.world.isRemote){
                this.entity.world.spawnEntity(entityitem);
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