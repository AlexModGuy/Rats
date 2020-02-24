package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;

import java.util.List;

public class RatAIRaidCrops extends RatAIMoveToBlock {
    private final EntityRat entity;
    private boolean stop = false;

    public RatAIRaidCrops(EntityRat entity) {
        super(entity, 1.0F, entity.getSearchRadius());
        this.entity = entity;
    }

    public static boolean isCrops(World world, BlockPos pos) {
        BlockState block = world.getBlockState(pos.up());
        return block.getBlock() instanceof CropsBlock;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || this.entity.isTamed() || this.entity.isInCage() || !RatConfig.ratsBreakCrops) {
            return false;
        }

        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
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
        return super.shouldContinueExecuting() && this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getIsAboveDestination() && this.destinationBlock != null) {
            BlockPos cropsPos = this.destinationBlock.up();
            BlockState block = this.entity.world.getBlockState(cropsPos);
            if (block.getBlock() instanceof CropsBlock) {
                double distance = this.entity.getDistanceSq(cropsPos.getX(), cropsPos.getY(), cropsPos.getZ());
                if (distance < 2.5F) {
                    LootContext.Builder loot = new LootContext.Builder((ServerWorld)entity.world).withParameter(LootParameters.POSITION, new BlockPos(destinationBlock)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withRandom(this.entity.getRNG()).withLuck((float)1.0F);
                    List<ItemStack> drops = block.getBlock().getDrops(block, loot);
                    if (drops.isEmpty()) {
                        stop = true;
                    } else {
                        int count = 0;
                        for(ItemStack stack : drops){
                            if(count == 0){
                                ItemStack duplicate = stack.copy();
                                duplicate.setCount(1);
                                if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                                    this.entity.entityDropItem(this.entity.getHeldItem(Hand.MAIN_HAND), 0.0F);
                                }
                                this.entity.setHeldItem(Hand.MAIN_HAND, duplicate);
                            }
                            count++;
                        }
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
