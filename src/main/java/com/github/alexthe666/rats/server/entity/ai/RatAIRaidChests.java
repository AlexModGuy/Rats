package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RatAIRaidChests extends RatAIMoveToBlock {

    private final EntityRat entity;

    public RatAIRaidChests(EntityRat entity) {
        super(entity, 1.0F, entity.getSearchRadius());
        this.entity = entity;
    }

    public static boolean isChestRaidable(World world, BlockPos pos) {
        String[] blacklist = RatConfig.blacklistedRatBlocks;
        if (world.getBlockState(pos).getBlock() instanceof ContainerBlock) {
            Block block = world.getBlockState(pos).getBlock();
            boolean listed = false;
            for (String name : blacklist) {
                if (name.equalsIgnoreCase(block.getRegistryName().toString())) {
                    listed = true;
                    break;
                }
            }
            if (!listed) {
                TileEntity entity = world.getTileEntity(pos);
                if (entity instanceof IInventory) {
                    IInventory inventory = (IInventory) entity;
                    try {
                        if (!inventory.isEmpty() && RatUtils.doesContainFood(inventory)) {
                            return true;
                        }
                    } catch (Exception e) {
                        RatsMod.LOGGER.warn("Rats stopped a " + entity.getClass().getSimpleName() + " from causing a crash during access");
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || this.entity.isTamed() || this.entity.isInCage() || !RatConfig.ratsStealItems) {
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

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(destinationBlock.getX() + 0.5, destinationBlock.getY() + 0.5, destinationBlock.getZ() + 0.5), false, entity);
        if (rayTrace instanceof BlockRayTraceResult) {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult)rayTrace;
            BlockPos pos = blockRayTraceResult.getPos();
            BlockPos sidePos = blockRayTraceResult.getPos().offset(blockRayTraceResult.getFace());
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(destinationBlock);
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getIsAboveDestination() && this.destinationBlock != null) {
            TileEntity entity = this.entity.world.getTileEntity(this.destinationBlock);
            if (entity instanceof IInventory) {
                IInventory feeder = (IInventory) entity;
                double distance = this.entity.getDistanceSq(this.destinationBlock.getX(), this.destinationBlock.getY(), this.destinationBlock.getZ());
                if (distance < 6.25F && distance > 2.72F && canSeeChest() && entity instanceof IInventory) {
                    toggleChest(feeder, true);
                }
                if (distance <= 2.89F && canSeeChest()) {
                    toggleChest(feeder, false);
                    ItemStack stack = RatUtils.getFoodFromInventory(this.entity, feeder, this.entity.world.rand);
                    if (stack == ItemStack.EMPTY) {
                        this.destinationBlock = null;
                        this.resetTask();
                    } else {
                        ItemStack duplicate = stack.copy();
                        duplicate.setCount(1);
                        if (!this.entity.getHeldItem(Hand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                            this.entity.entityDropItem(this.entity.getHeldItem(Hand.MAIN_HAND), 0.0F);
                        }
                        this.entity.setHeldItem(Hand.MAIN_HAND, duplicate);
                        stack.shrink(1);
                        if (RatConfig.ratsContaminateFood && this.entity.getRNG().nextInt(3) == 0) {
                            int slotToReplace = RatUtils.getContaminatedSlot(this.entity, feeder, this.entity.world.rand);
                            if (slotToReplace != -1) {
                                if (feeder.getStackInSlot(slotToReplace).isEmpty()) {
                                    ItemStack stack1 = new ItemStack(RatsItemRegistry.CONTAMINATED_FOOD);
                                    feeder.setInventorySlotContents(slotToReplace, stack1);
                                } else if (feeder.getStackInSlot(slotToReplace).getItem() == RatsItemRegistry.CONTAMINATED_FOOD) {
                                    feeder.getStackInSlot(slotToReplace).grow(1);
                                }
                            }
                        }
                        this.entity.fleePos = this.destinationBlock;
                        this.destinationBlock = null;
                        this.resetTask();

                    }
                }
            }

        }
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
        return pos != null && isChestRaidable(worldIn, pos);
    }

    public void toggleChest(IInventory te, boolean open) {
        if (te instanceof ChestTileEntity) {
            ChestTileEntity chest = (ChestTileEntity) te;
            if (open) {
                this.entity.world.addBlockEvent(this.destinationBlock, chest.getBlockState().getBlock(), 1, 1);
            } else {
                this.entity.world.addBlockEvent(this.destinationBlock, chest.getBlockState().getBlock(), 1, 0);

            }
        }
    }
}
