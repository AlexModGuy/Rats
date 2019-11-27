package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RatAIRaidCrops extends RatAIMoveToBlock {
    private final EntityRat entity;
    private boolean stop = false;

    public RatAIRaidCrops(EntityRat entity) {
        super(entity, 1.0F, 20);
        this.entity = entity;
    }

    public static boolean isCrops(World world, BlockPos pos) {
        BlockState block = world.getBlockState(pos.up());
        return block.getBlock() instanceof BlockCrops;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || this.entity.isTamed() || this.entity.isInCage() || !RatConfig.ratsBreakCrops) {
            return false;
        }

        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            return false;
        }
        if (this.runDelay <= 0) {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity)) {
                return false;
            }
        }
        return super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return super.shouldContinueExecuting() && this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty();
    }

    @Override
    public void updateTask() {
        super.updateTask();

        if (this.getIsAboveDestination() && this.destinationBlock != null) {
            BlockPos cropsPos = this.destinationBlock.up();
            BlockState block = this.entity.world.getBlockState(cropsPos);
            if (block.getBlock() instanceof BlockCrops) {
                double distance = this.entity.getDistance(cropsPos.getX(), cropsPos.getY(), cropsPos.getZ());
                if (distance < 1.5F) {
                    ItemStack stack = new ItemStack(block.getBlock().getItemDropped(block, this.entity.getRNG(), 0));
                    if (stack == ItemStack.EMPTY || !entity.canRatPickupItem(stack)) {
                        //
                        stop = true;
                    } else {
                        ItemStack duplicate = stack.copy();
                        duplicate.setCount(1);
                        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                            this.entity.entityDropItem(this.entity.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
                        }
                        this.entity.setHeldItem(EnumHand.MAIN_HAND, duplicate);
                        this.entity.world.destroyBlock(cropsPos, false);
                    }
                    this.entity.fleePos = cropsPos;
                    this.runDelay = 10;
                }
            }
        }

    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return isCrops(worldIn, pos);
    }
}
