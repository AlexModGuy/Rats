package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class RatAIHarvestFarmer extends Goal {
    private final EntityRat entity;
    private final BlockSorter targetSorter;
    private BlockPos targetBlock = null;


    public RatAIHarvestFarmer(EntityRat entity) {
        super();
        this.entity = entity;
        this.targetSorter = new BlockSorter(entity);
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_FARMER) || !holdingSeeds() && !holdingBlock() && !holdingBonemeal()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private boolean holdingSeeds() {
        return !this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && (this.entity.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BlockNamedItem);
    }

    private boolean holdingBonemeal() {
        ItemStack stack = this.entity.getHeldItem(Hand.MAIN_HAND);
        return !stack.isEmpty() && stack.getItem() == Items.BONE_MEAL;
    }

    private boolean holdingBlock() {
        return !this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && (this.entity.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BlockItem);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && (holdingSeeds() || holdingBonemeal() || holdingBlock());
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }


    @Override
    public void tick() {
        if (this.targetBlock != null) {
            if (holdingSeeds()) {
                BlockState block = this.entity.world.getBlockState(this.targetBlock);
                this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
                if (block.getBlock().isFertile(block, entity.world, targetBlock) && entity.world.isAirBlock(targetBlock.up())) {
                    double distance = this.entity.getDistanceSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 4.5F) {
                        if (holdingSeeds()) {
                            ItemStack seedStack = this.entity.getHeldItem(Hand.MAIN_HAND).copy();
                            seedStack.setCount(1);
                            this.entity.getHeldItem(Hand.MAIN_HAND).shrink(1);
                            if (seedStack.getItem() instanceof BlockNamedItem) {
                                entity.world.setBlockState(targetBlock.up(), ((BlockNamedItem) seedStack.getItem()).getBlock().getDefaultState());
                            }
                        }
                        this.targetBlock = null;
                        this.resetTask();
                        return;
                    }
                } else {
                    this.targetBlock = null;
                    this.resetTask();
                    return;
                }
            }
            if (holdingBonemeal()) {
                BlockState block = this.entity.world.getBlockState(this.targetBlock);
                this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
                if (canPlantBeBonemealed(targetBlock, block)) {
                    double distance = this.entity.getDistanceSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 4.5F) {
                        if (holdingBonemeal()) {
                            this.entity.getHeldItem(Hand.MAIN_HAND).shrink(1);
                            if (block.getBlock() instanceof IGrowable) {
                                IGrowable igrowable = (IGrowable) block.getBlock();
                                if (igrowable.canGrow(entity.world, targetBlock, block, entity.world.isRemote)) {
                                    if (!entity.world.isRemote) {
                                        entity.world.playEvent(2005, targetBlock, 0);
                                        igrowable.grow(entity.world, entity.world.rand, targetBlock, block);
                                    }
                                }
                            }
                        }
                        this.targetBlock = null;
                        this.resetTask();
                        return;
                    }
                } else {
                    this.targetBlock = null;
                    this.resetTask();
                    return;
                }
            } else if (holdingBlock()) {
                BlockItem itemBlock = ((BlockItem) entity.getHeldItem(Hand.MAIN_HAND).getItem());
                this.entity.getNavigator().tryMoveToXYZ(this.targetBlock.getX() + 0.5D, this.targetBlock.getY(), this.targetBlock.getZ() + 0.5D, 1.25D);
                if (entity.world.isAirBlock(targetBlock.up())) {
                    double distance = this.entity.getDistanceSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 4.5F) {
                        if (holdingBlock()) {
                            BlockRayTraceResult raytrace = entity.world.rayTraceBlocks(new RayTraceContext(new Vec3d(targetBlock), new Vec3d(targetBlock), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
                            ItemUseContext itemusecontext = new ItemUseContext(null, Hand.MAIN_HAND, raytrace);
                            BlockState BlockState1 = itemBlock.getBlock().getStateForPlacement(new BlockItemUseContext(itemusecontext));
                            this.entity.getHeldItem(Hand.MAIN_HAND).shrink(1);
                            entity.world.setBlockState(targetBlock, BlockState1);
                            if (entity.isEntityInsideOpaqueBlock()) {
                                entity.setPosition(entity.posX, entity.posY + 1, entity.posZ);
                            }
                            SoundType placeSound = BlockState1.getBlock().getSoundType(BlockState1, entity.world, targetBlock, entity);
                            entity.playSound(placeSound.getPlaceSound(), (placeSound.getVolume() + 1.0F) / 2.0F, placeSound.getPitch() * 0.8F);
                        }
                        this.targetBlock = null;
                        this.resetTask();
                        return;
                    }
                } else {
                    this.targetBlock = null;
                    this.resetTask();
                    return;
                }
            }

        }
    }

    private void resetTarget() {
        int RADIUS = entity.getSearchRadius();
        if (holdingBonemeal()) {
            List<BlockPos> allBlocks = new ArrayList<>();
            for (BlockPos pos : BlockPos.getAllInBox(this.entity.getSearchCenter().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getSearchCenter().add(RADIUS, RADIUS, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if (canPlantBeBonemealed(pos, this.entity.world.getBlockState(pos))) {
                    allBlocks.add(pos);
                }
            }
            if (!allBlocks.isEmpty()) {
                allBlocks.sort(this.targetSorter);
                this.targetBlock = allBlocks.get(0);
            }
        } else if (holdingSeeds()) {
            List<BlockPos> allBlocks = new ArrayList<>();
            for (BlockPos pos : BlockPos.getAllInBox(this.entity.getSearchCenter().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getSearchCenter().add(RADIUS, RADIUS, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if (entity.world.getBlockState(pos).getBlock().isFertile(entity.world.getBlockState(pos), entity.world, pos) && entity.world.isAirBlock(pos.up())) {
                    allBlocks.add(pos);
                }
            }
            if (!allBlocks.isEmpty()) {
                allBlocks.sort(this.targetSorter);
                this.targetBlock = allBlocks.get(0);
            }
        } else if (holdingBlock()) {
            List<BlockPos> allBlocks = new ArrayList<>();
            Block block = Blocks.OAK_SAPLING;
            if (this.entity.getHeldItem(Hand.MAIN_HAND).getItem() != null && this.entity.getHeldItem(Hand.MAIN_HAND).getItem() instanceof BlockItem) {
                block = ((BlockItem) this.entity.getHeldItem(Hand.MAIN_HAND).getItem()).getBlock();
            }
            for (BlockPos pos : BlockPos.getAllInBox(this.entity.getSearchCenter().add(-RADIUS, -RADIUS, -RADIUS), this.entity.getSearchCenter().add(RADIUS, RADIUS, RADIUS)).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if (block.getBlock().isValidPosition(block.getDefaultState(), entity.world, targetBlock) && entity.world.isAirBlock(targetBlock.up())) {
                    allBlocks.add(pos);
                }
            }
            if (!allBlocks.isEmpty()) {
                allBlocks.sort(this.targetSorter);
                this.targetBlock = allBlocks.get(0);
            }
        }

    }

    private boolean canPlantBeBonemealed(BlockPos pos, BlockState BlockState) {
        if (BlockState.getBlock() instanceof IGrowable && !(BlockState.getBlock() instanceof TallGrassBlock) && !(BlockState.getBlock() instanceof GrassBlock)) {
            IGrowable igrowable = (IGrowable) BlockState.getBlock();
            if (igrowable.canGrow(entity.world, pos, BlockState, entity.world.isRemote)) {
                if (!entity.world.isRemote) {
                    //  igrowable.grow(worldIn, worldIn.rand, target, BlockState);
                    return igrowable.canUseBonemeal(entity.world, entity.world.rand, pos, BlockState);
                }
            }
        }
        return false;
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
