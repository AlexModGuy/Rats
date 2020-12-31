package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.tile.TileEntityRatQuarry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RatAIHarvestQuarry extends Goal {
    private final EntityRat entity;
    private final RatAIHarvestQuarry.BlockSorter targetSorter;
    private BlockPos targetBlock = null;
    private int breakingTime;
    private int previousBreakProgress;
    private BlockState prevMiningState = null;
    private boolean buildStairs = false;
    private Direction stairDirection = Direction.NORTH;

    public RatAIHarvestQuarry(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new RatAIHarvestQuarry.BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_QUARRY) && entity.depositPos != null) {
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
        BlockPos quarryPos = entity.depositPos;
        if (quarryPos != null && entity.world.getTileEntity(quarryPos) instanceof TileEntityRatQuarry) {
            TileEntityRatQuarry quarry = ((TileEntityRatQuarry) entity.world.getTileEntity(quarryPos));
            int RADIUS = quarry.getRadius();
            for (BlockPos pos : BlockPos.getAllInBox(quarryPos.add(-RADIUS, -1, -RADIUS), quarryPos.add(RADIUS, -quarryPos.getY() - 1, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if ((!entity.world.isAirBlock(pos) && doesListContainBlock(entity.world, pos)) || entity.world.getFluidState(pos).isSource()) {
                    BlockState state = entity.world.getBlockState(pos);
                    if (state.getBlock() != RatsBlockRegistry.RAT_QUARRY && state.getBlock() != RatsBlockRegistry.RAT_QUARRY_PLATFORM  && state.getBlockHardness(entity.world, pos) > 0F) {
                        allBlocks.add(pos);
                    }
                }
            }
            if (!allBlocks.isEmpty()) {
                allBlocks.sort(this.targetSorter);
                this.targetBlock = allBlocks.get(allBlocks.size() - 1);
                BlockPos stairs = quarry.getNextPosForStairs();
                if (stairs.getY() >= targetBlock.getY()) {
                    this.buildStairs = true;
                    stairDirection = quarry.stairDirection;
                    targetBlock = stairs;
                }
            }
        }

    }

    private boolean doesListContainBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        ItemStack getStack = state.getBlock().getItem(world, pos, state);
        return entity.canRatPickupItem(getStack);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty();
    }

    public void resetTask() {
        buildStairs = false;
        resetTarget();
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {
            BlockPos rayPos = entity.rayTraceBlockPos(this.targetBlock);
            if(this.entity.world.getFluidState(this.targetBlock).isEmpty() && this.entity.world.isAirBlock(this.targetBlock)){
                this.resetTask();
            }
            if(this.entity.isInLava() && entity.world.getBlockState(this.entity.func_233580_cy_().up()).isAir()){
                entity.world.setBlockState(this.entity.func_233580_cy_().up(), Blocks.WATER.getDefaultState());
                this.entity.heal(15F);
                this.entity.getJumpController().setJumping();
            }
            if (rayPos == null || !this.canMineBlock(rayPos)) {
                rayPos = this.targetBlock;
            }
            if (this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D)) {
                rayPos = this.targetBlock;
            } else {
                this.entity.getNavigator().tryMoveToXYZ(rayPos.getX() + 0.5D, rayPos.getY(), rayPos.getZ() + 0.5D, 1.25D);
            }

            double distance = this.entity.getRatDistanceCenterSq(rayPos.getX() + 0.5D, rayPos.getY() + 0.5D, rayPos.getZ() + 0.5D);
            if (!entity.getMoveHelper().isUpdating() && (entity.func_233570_aj_() || entity.isInWater() || entity.isInLava() || entity.isRidingSpecialMount())) {
                BlockState block = this.entity.world.getBlockState(rayPos);
                SoundType soundType = block.getBlock().getSoundType(block, entity.world, rayPos, null);
                if (buildStairs) {
                    if (distance < 6F * this.entity.getRatDistanceModifier()) {
                        entity.world.setBlockState(targetBlock, RatsBlockRegistry.RAT_QUARRY_PLATFORM.getDefaultState());
                        entity.world.setEntityState(entity, (byte) 86);
                        targetBlock = null;
                        prevMiningState = block;
                        entity.crafting = false;
                        this.resetTask();
                    }
                } else {
                    if (block.getMaterial() != Material.AIR) {
                        if (distance < 6F * this.entity.getRatDistanceModifier()) {
                            if(block.getFluidState().isSource()){
                                BlockState replace = Blocks.COBBLESTONE.getDefaultState();
                                if(block.getFluidState().isTagged(FluidTags.LAVA)){
                                    replace = Blocks.OBSIDIAN.getDefaultState();
                                }
                                this.entity.world.setBlockState(rayPos, replace);
                                this.entity.setPosition(entity.getPosX(), entity.getPosY() + 1F, entity.getPosZ());
                                this.entity.heal(2);
                                this.resetTask();
                            }
                            entity.world.setEntityState(entity, (byte) 85);
                            entity.crafting = true;
                            if (block == prevMiningState) {
                                entity.world.setEntityState(entity, (byte) 85);
                                entity.crafting = true;
                            } else {
                                entity.world.setEntityState(entity, (byte) 86);
                                entity.crafting = false;
                            }
                            if (distance < 1.5F * this.entity.getRatDistanceModifier()) {
                                this.entity.setMotion(0, 0, 0);
                                entity.getNavigator().clearPath();
                                //entity.moveController.action = MovementController.Action.WAIT;
                            }
                            breakingTime++;
                            int hardness = (int) (block.getBlockHardness(entity.world, rayPos) * 10);
                            int i = (int) ((float) this.breakingTime / hardness * 10.0F);
                            if (breakingTime % 5 == 0) {
                                entity.playSound(soundType.getHitSound(), soundType.volume + 1, soundType.pitch);
                            }
                            if (i != this.previousBreakProgress) {
                                entity.world.sendBlockBreakProgress(entity.getEntityId(), rayPos, i);
                                this.previousBreakProgress = i;
                            }
                            if (this.breakingTime >= hardness) {
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
    }

    private boolean canMineBlock(BlockPos rayPos) {
        BlockState state = entity.world.getBlockState(rayPos);
        return state.getBlock() != RatsBlockRegistry.RAT_QUARRY && state.getBlock() != RatsBlockRegistry.RAT_QUARRY_PLATFORM && doesListContainBlock(entity.world, rayPos);
    }

    private void destroyBlock(BlockPos pos, BlockState state) {
        if (entity.world instanceof ServerWorld) {
            LootContext.Builder loot = new LootContext.Builder((ServerWorld) entity.world).withParameter(LootParameters.field_237457_g_, entity.getPositionVec()).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck(1.0F);
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

    public class BlockSorter implements Comparator<BlockPos> {
        private final EntityRat entity;

        public BlockSorter(EntityRat entity) {
            this.entity = entity;
        }

        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            FluidState state1 = entity.world.getFluidState(pos2);
            FluidState state2 = entity.world.getFluidState(pos1);
            double distance1 = pos2.getY();
            double distance2 = pos1.getY();
            if(state1.isSource() && !state2.isSource()){
                return 1;
            }
            if(state2.isSource() && !state1.isSource()){
                return -1;
            }
            int d = Double.compare(distance2, distance1);
            return d;
        }

        private double getDistance(BlockPos pos) {
            BlockPos depositPos = entity.depositPos;
            if (depositPos != null && entity.world.getTileEntity(depositPos) instanceof TileEntityRatQuarry) {
                TileEntityRatQuarry quarry = (TileEntityRatQuarry) entity.world.getTileEntity(depositPos);
                BlockPos perspective = depositPos.add(-quarry.getRadius(), -1, -quarry.getRadius());
                double deltaX = perspective.getX() - (pos.getX() + 0.5);
                double deltaY = perspective.getY() - (pos.getY() + 0.5);
                double deltaZ = perspective.getZ() - (pos.getZ() + 0.5);
                return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
            } else {
                double deltaX = this.entity.getPosX() - (pos.getX() + 0.5);
                double deltaY = this.entity.getPosY() + this.entity.getEyeHeight() - (pos.getY() + 0.5);
                double deltaZ = this.entity.getPosZ() - (pos.getZ() + 0.5);
                return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
            }

        }
    }
}
