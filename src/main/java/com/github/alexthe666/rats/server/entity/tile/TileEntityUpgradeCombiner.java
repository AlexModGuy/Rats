package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.ItemRatCombinedUpgrade;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityUpgradeCombiner extends TileEntity implements ITickable, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[]{0, 2};
    private static final int[] SLOTS_SIDE = new int[]{1};
    private static final int[] SLOTS_BOTTOM = new int[]{3};
    public int ticksExisted;
    public float ratRotation;
    public float ratRotationPrev;
    public float tRot;
    private NonNullList<ItemStack> combinerStacks = NonNullList.withSize(4, ItemStack.EMPTY);
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;

    public TileEntityUpgradeCombiner() {
    }

    public static int getItemBurnTime(ItemStack stack) {
        return stack.getItem() == RatsItemRegistry.GEM_OF_RATLANTIS ? 300 : 0;
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return new net.minecraft.util.math.AxisAlignedBB(pos, pos.add(1, 2, 1));
    }

    public int getSizeInventory() {
        return this.combinerStacks.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.combinerStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return this.combinerStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.combinerStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.combinerStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.combinerStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.combinerStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public int getCookTime(ItemStack stack) {
        return 200;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.combinerStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.combinerStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.combinerStacks.get(1));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.furnaceBurnTime);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.combinerStacks);
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
        return this.furnaceBurnTime > 0;
    }

    public void update() {
        this.totalCookTime = 1000;
        this.ratRotationPrev = this.ratRotation;
        ticksExisted++;
        boolean flag = this.isBurning();
        boolean flag1 = false;
        EntityPlayer entityplayer = this.world.getClosestPlayer((double) ((float) this.pos.getX() + 0.5F), (double) ((float) this.pos.getY() + 0.5F), (double) ((float) this.pos.getZ() + 0.5F), 10.0D, false);
        if (entityplayer != null) {
            double d0 = entityplayer.posX - (double) ((float) this.pos.getX() + 0.5F);
            double d1 = entityplayer.posZ - (double) ((float) this.pos.getZ() + 0.5F);
            this.tRot = (float) MathHelper.atan2(d1, d0);
        } else {
            this.tRot += 0.04F;
        }

        while (this.ratRotation >= (float) Math.PI) {
            this.ratRotation -= ((float) Math.PI * 2F);
        }

        while (this.ratRotation < -(float) Math.PI) {
            this.ratRotation += ((float) Math.PI * 2F);
        }
        float f2;

        for (f2 = this.tRot - this.ratRotation; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) {
        }

        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2F);
        }

        this.ratRotation += f2 * 0.4F;


        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }

        if (world.isRemote) {
            float radius = (float) Math.sin(this.ticksExisted * 0.1);
            double extraY = this.pos.getY() + 1.05;
            for (int i = 0; i < 3; i++) {
                float angle = (0.01745329251F * (this.ticksExisted * 4 + i * 120));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + this.pos.getX() + 0.5D;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + this.pos.getZ() + 0.5D;
                RatsMod.PROXY.spawnParticle("upgrade_combiner", extraX,
                        extraY,
                        extraZ,
                        -0.1F, -0.1F, 0F);
            }
        }

        if (!this.world.isRemote) {
            ItemStack fuel = this.combinerStacks.get(1);
            if (this.isBurning() || !fuel.isEmpty() && !this.combinerStacks.get(0).isEmpty()  && !this.combinerStacks.get(2).isEmpty()) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = getItemBurnTime(fuel);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning()) {
                        flag1 = true;

                        if (!fuel.isEmpty()) {
                            Item item = fuel.getItem();
                            fuel.shrink(1);

                            if (fuel.isEmpty()) {
                                ItemStack item1 = item.getContainerItem(fuel);
                                this.combinerStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.combinerStacks.get(0));
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
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = this.combinerStacks.get(0);
            ItemStack itemstack2 = this.combinerStacks.get(3);
            ItemStack itemstack1 = getCombinerResult(itemstack, this.combinerStacks.get(2));

            if (itemstack2.isEmpty()) {
                this.combinerStacks.set(3, itemstack1.copy());
            } else if (itemstack2.isItemEqual(itemstack1)) {
                itemstack2.grow(itemstack1.getCount());
            }
            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !this.combinerStacks.get(1).isEmpty() && this.combinerStacks.get(1).getItem() == Items.BUCKET) {
                this.combinerStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }
            itemstack.shrink(1);
            this.combinerStacks.get(2).shrink(1);
        }
    }

    private ItemStack getCombinerResult(ItemStack combiner, ItemStack stack) {
        if (!combiner.hasTagCompound()) {
            combiner.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbttagcompound1 = combiner.getTagCompound();
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
        if (nbttagcompound1 != null && nbttagcompound1.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
        }
        int addIndex = -1;
        for(int i = 0; i < nonnulllist.size(); i++){
            if(nonnulllist.get(i) == ItemStack.EMPTY){
                addIndex = i;
                break;
            }
        }
        if(addIndex == -1){
            return combiner.copy();
        }
        nonnulllist.set(addIndex, stack.copy());
        ItemStackHelper.saveAllItems(nbttagcompound1, nonnulllist);
        combiner.setTagCompound(nbttagcompound1);
        return combiner.copy();
    }

    public boolean canSmelt() {
        if (!this.combinerStacks.get(0).isEmpty() && this.combinerStacks.get(0).getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED) {
            return ItemRatCombinedUpgrade.canCombineWithUpgrade(this.combinerStacks.get(0), this.combinerStacks.get(2));
        }
        return false;
    }

    public void openInventory(EntityPlayer player) {
    }

    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 3) {
            return false;
        } else if (index == 2) {
            return stack.getItem() instanceof ItemRatUpgrade && !(stack.getItem() instanceof ItemRatCombinedUpgrade);
        } else if (index == 1) {
            return stack.getItem() == RatsItemRegistry.GEM_OF_RATLANTIS;
        } else {
            return stack.getItem() instanceof ItemRatCombinedUpgrade;
        }
    }


    public int getField(int id) {
        switch (id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount() {
        return 4;
    }

    public void clear() {
        this.combinerStacks.clear();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else if (side == EnumFacing.UP) {
            return SLOTS_TOP;
        } else {
            return SLOTS_SIDE;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "container.upgrade_combiner";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.NORTH);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);

    @Override
    @javax.annotation.Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(getName());
    }

}
