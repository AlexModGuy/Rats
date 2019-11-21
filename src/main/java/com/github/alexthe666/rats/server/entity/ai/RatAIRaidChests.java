package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RatAIRaidChests extends RatAIMoveToBlock {

    private final EntityRat entity;

    public RatAIRaidChests(EntityRat entity) {
        super(entity, 1.0F, 20);
        this.entity = entity;
    }

    public static boolean isChestRaidable(World world, BlockPos pos) {
        String[] blacklist = RatsMod.CONFIG_OPTIONS.blacklistedRatBlocks;
        if (world.getBlockState(pos).getBlock() instanceof BlockContainer) {
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
                        RatsMod.logger.warn("Rats stopped a " + inventory.getName() + " from causing a crash during access");
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || this.entity.isTamed() || this.entity.isInCage() || !RatsMod.CONFIG_OPTIONS.ratsStealItems) {
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

    public boolean canSeeChest() {
        RayTraceResult rayTrace = RatUtils.rayTraceBlocksIgnoreRatholes(entity.world, entity.getPositionVector(), new Vec3d(destinationBlock.getX() + 0.5, destinationBlock.getY() + 0.5, destinationBlock.getZ() + 0.5), false);
        if (rayTrace != null && rayTrace.hitVec != null) {
            BlockPos sidePos = rayTrace.getBlockPos();
            BlockPos pos = new BlockPos(rayTrace.hitVec);
            return entity.world.isAirBlock(sidePos) || entity.world.isAirBlock(pos) || this.entity.world.getTileEntity(pos) == this.entity.world.getTileEntity(destinationBlock);
        }
        return true;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.getIsAboveDestination() && this.destinationBlock != null) {
            TileEntity entity = this.entity.world.getTileEntity(this.destinationBlock);
            if (entity instanceof IInventory) {
                IInventory feeder = (IInventory) entity;
                double distance = this.entity.getDistance(this.destinationBlock.getX(), this.destinationBlock.getY(), this.destinationBlock.getZ());
                if (distance < 2.5F && distance >= 1.5F && canSeeChest()) {
                    toggleChest(feeder, true);
                }
                if (distance < 1.5F && canSeeChest()) {
                    toggleChest(feeder, false);
                    ItemStack stack = RatUtils.getFoodFromInventory(this.entity, feeder, this.entity.world.rand);
                    if (stack == ItemStack.EMPTY) {
                        this.destinationBlock = null;
                        this.resetTask();
                    } else {
                        ItemStack duplicate = stack.copy();
                        duplicate.setCount(1);
                        if (!this.entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && !this.entity.world.isRemote) {
                            this.entity.entityDropItem(this.entity.getHeldItem(EnumHand.MAIN_HAND), 0.0F);
                        }
                        this.entity.setHeldItem(EnumHand.MAIN_HAND, duplicate);
                        stack.shrink(1);
                        if (RatsMod.CONFIG_OPTIONS.ratsContaminateFood && this.entity.getRNG().nextInt(3) == 0) {
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
        if (te instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest) te;
            if (open) {
                chest.numPlayersUsing++;
                this.entity.world.addBlockEvent(this.destinationBlock, chest.getBlockType(), 1, chest.numPlayersUsing);
            } else {
                if (chest.numPlayersUsing > 0) {
                    chest.numPlayersUsing = 0;
                    this.entity.world.addBlockEvent(this.destinationBlock, chest.getBlockType(), 1, chest.numPlayersUsing);
                }
            }
        }
    }
}
