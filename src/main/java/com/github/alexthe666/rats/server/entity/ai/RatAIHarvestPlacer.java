package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class RatAIHarvestPlacer extends Goal {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private BlockPos targetBlock = null;


    public RatAIHarvestPlacer(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_PLACER) || !holdingBlock()) {
            return false;
        }
        resetTarget();
        return targetBlock != null;
    }

    private boolean holdingBlock() {
        ItemStack stack = this.entity.getHeldItem(Hand.MAIN_HAND);
        return !stack.isEmpty() && stack.getItem() instanceof BlockItem;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetBlock != null && holdingBlock() && entity.world.getBlockState(targetBlock).getMaterial().isReplaceable();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }

    @Override
    public void tick() {
        if (this.targetBlock != null) {
            if (holdingBlock()) {
                ItemStack stack = this.entity.getHeldItem(Hand.MAIN_HAND);
                BlockItem blockItem = (BlockItem) stack.getItem();
                BlockState block = this.entity.world.getBlockState(this.targetBlock);
                BlockPos moveToPos = this.targetBlock;
                this.entity.getNavigator().tryMoveToXYZ(moveToPos.getX() + 0.5D, moveToPos.getY(), moveToPos.getZ() + 0.5D, 1D);
                if (block.getBlock().isValidPosition(block, entity.world, targetBlock) && entity.world.isAirBlock(targetBlock.up())) {
                    double distance = this.entity.getDistanceSq(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 2.5F) {
                        ItemStack seedStack = this.entity.getHeldItem(Hand.MAIN_HAND).copy();
                        seedStack.setCount(1);
                        this.entity.getHeldItem(Hand.MAIN_HAND).shrink(1);
                        BlockRayTraceResult raytrace = entity.world.rayTraceBlocks(new RayTraceContext(new Vec3d(targetBlock), new Vec3d(targetBlock), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
                        ItemUseContext itemusecontext = new ItemUseContext(null, Hand.MAIN_HAND, raytrace);
                        BlockState BlockState1 = blockItem.getBlock().getStateForPlacement(new BlockItemUseContext(itemusecontext));
                        entity.world.setBlockState(targetBlock, BlockState1);
                        if (entity.isEntityInsideOpaqueBlock()) {
                            entity.setPosition(entity.posX, entity.posY + 1, entity.posZ);
                        }
                        SoundType placeSound = BlockState1.getBlock().getSoundType(BlockState1, entity.world, targetBlock, entity);
                        entity.playSound(placeSound.getPlaceSound(), (placeSound.getVolume() + 1.0F) / 2.0F, placeSound.getPitch() * 0.8F);
                        this.targetBlock = null;
                        this.resetTask();
                    }
                } else {
                    this.targetBlock = null;
                    this.resetTask();
                }
            }
        }
    }

    private void resetTarget() {
        BlockPos newTarget = null;
        if (entity.detachHome()) {
            newTarget = entity.getHomePosition();
            if (!entity.world.getBlockState(newTarget).getMaterial().isReplaceable()) {
                newTarget = newTarget.up();
            }
        }
        if (newTarget != null) {
            this.targetBlock = newTarget;
        }
    }
}
