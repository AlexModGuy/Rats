package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.message.MessageAutoCurdlerFluid;
import com.github.alexthe666.rats.server.message.MessageIncreaseRatRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;

public class TileEntityAutoCurdler extends TileEntity implements ITickable, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[]{0};
    private static final int[] SLOTS_BOTTOM = new int[]{1};
    public int ticksExisted;
    public FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 5);
    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.Direction.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.Direction.DOWN);
    private NonNullList<ItemStack> curdlerStacks = NonNullList.withSize(2, ItemStack.EMPTY);
    private int cookTime;
    private int totalCookTime;
    private int prevFluid = 0;

    public TileEntityAutoCurdler() {
    }

    public static boolean isMilk(ItemStack stack) {
        if (stack.getItem() == Items.MILK_BUCKET) {
            return true;
        }
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        return fluidStack != null && (fluidStack.getFluid().getUnlocalizedName().contains("milk") || fluidStack.getFluid().getUnlocalizedName().contains("Milk"));
    }

    public int getSizeInventory() {
        return this.curdlerStacks.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.curdlerStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return this.curdlerStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.curdlerStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.curdlerStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.curdlerStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.curdlerStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.markDirty();
        }
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound);
        this.curdlerStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.curdlerStacks);
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        tank.writeToNBT(compound);
        compound.setInteger("CookTime", (short) this.cookTime);
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.curdlerStacks);
        return compound;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    public boolean isBurning() {
        return this.tank.getFluidAmount() >= 1000 && tank.getFluid() != null && isMilkFluid(tank.getFluid());
    }

    public void update() {
        if (!world.isRemote) {
            if (prevFluid != tank.getFluidAmount()) {
                RatsMod.NETWORK_WRAPPER.sendToAll(new MessageAutoCurdlerFluid(this.getPos().toLong(), tank.getFluid()));
                prevFluid = tank.getFluidAmount();
            }

        }
        this.totalCookTime = RatConfig.milkCauldronTime;
        ticksExisted++;
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (!this.world.isRemote) {
            if (this.isBurning()) {
                if (this.canSmelt()) {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = RatConfig.milkCauldronTime;
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
        }
        if (curdlerStacks.get(0).getItem() == Items.MILK_BUCKET) {
            FluidBucketWrapper milkWrapper = new FluidBucketWrapper(new ItemStack(Items.MILK_BUCKET));
            if (tank.fill(milkWrapper.getFluid().copy(), false) != 0) {
                tank.fill(milkWrapper.getFluid().copy(), true);
                curdlerStacks.set(0, new ItemStack(Items.BUCKET));
            }

        } else if (isMilk(curdlerStacks.get(0))) {
            FluidStack fluidStack = FluidUtil.getFluidContained(curdlerStacks.get(0));
            if (fluidStack != null) {
                IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(curdlerStacks.get(0));
                if (fluidHandler.drain(Integer.MAX_VALUE, false) != null && fluidHandler.drain(Integer.MAX_VALUE, false).amount > 0) {
                    if (tank.fill(fluidStack.copy(), false) != 0) {
                        tank.fill(fluidStack.copy(), true);
                        fluidHandler.drain(Integer.MAX_VALUE, true);
                    }
                }
            }
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack toAdd = new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE);
            if (this.getStackInSlot(1).isItemEqual(toAdd) && this.getStackInSlot(1).getCount() < 64) {
                this.getStackInSlot(1).grow(1);
            } else if (this.getStackInSlot(1).isEmpty()) {
                this.setInventorySlotContents(1, toAdd.copy());
            }
            tank.drain(1000, true);
        }
    }

    public boolean canSmelt() {
        if (tank.getFluidAmount() < 1000) {
            return false;
        } else {
            ItemStack itemstack = new ItemStack(RatsBlockRegistry.BLOCK_OF_CHEESE);

            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = this.curdlerStacks.get(1);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    public void openInventory(EntityPlayer player) {
        if (!world.isRemote) {
            RatsMod.NETWORK_WRAPPER.sendToAll(new MessageAutoCurdlerFluid(this.getPos().toLong(), tank.getFluid()));
        }
    }

    public void closeInventory(EntityPlayer player) {
        if (!world.isRemote) {
            RatsMod.NETWORK_WRAPPER.sendToAll(new MessageAutoCurdlerFluid(this.getPos().toLong(), tank.getFluid()));
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 1) {
            return false;
        } else {
            return isMilk(stack);
        }
    }

    private boolean isMilkFluid(FluidStack fluid) {
        return fluid.getUnlocalizedName().contains("milk") || fluid.getUnlocalizedName().contains("Milk");
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.tank.getFluidAmount();
            case 1:
                return this.cookTime;
            case 2:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                if (tank.getFluid() != null) {
                    this.tank.getFluid().amount = value;
                }
                break;
            case 1:
                this.cookTime = value;
                break;
            case 2:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount() {
        return 3;
    }

    public void clear() {
        this.curdlerStacks.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return SLOTS_TOP;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "container.auto_curdler";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.Direction facing) {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.Direction facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN)
                return (T) handlerBottom;
            else
                return (T) handlerTop;
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

}
