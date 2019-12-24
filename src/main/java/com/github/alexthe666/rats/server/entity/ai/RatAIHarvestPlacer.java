package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

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
        ItemStack stack = this.entity.getHeldItem(EnumHand.MAIN_HAND);
        return !stack.isEmpty() && stack.getItem() instanceof ItemBlock;
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
                ItemStack stack = this.entity.getHeldItem(EnumHand.MAIN_HAND);
                ItemBlock blockItem = (ItemBlock) stack.getItem();
                BlockState block = this.entity.world.getBlockState(this.targetBlock);
                BlockPos moveToPos = this.targetBlock;
                this.entity.getNavigator().tryMoveToXYZ(moveToPos.getX() + 0.5D, moveToPos.getY(), moveToPos.getZ() + 0.5D, 1D);
                if (blockItem.getBlock().canPlaceBlockAt(entity.world, targetBlock) && entity.world.mayPlace(blockItem.getBlock(), this.targetBlock, false, Direction.UP, (Entity) null)) {
                    double distance = this.entity.getDistance(this.targetBlock.getX(), this.targetBlock.getY(), this.targetBlock.getZ());
                    if (distance < 1.5F) {
                        ItemStack seedStack = this.entity.getHeldItem(EnumHand.MAIN_HAND).copy();
                        seedStack.setCount(1);
                        this.entity.getHeldItem(EnumHand.MAIN_HAND).shrink(1);
                        BlockState BlockState1 = blockItem.getBlock().getStateForPlacement(entity.world, targetBlock, entity.getHorizontalFacing(), 0, 0, 0, stack.getMetadata(), entity, EnumHand.MAIN_HAND);
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
        if (entity.hasHome()) {
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
