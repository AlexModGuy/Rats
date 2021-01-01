package com.github.alexthe666.rats.server.entity.tile;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.inventory.ContainerRatCraftingTable;
import com.github.alexthe666.rats.server.message.MessageUpdateTileSlots;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public class TileEntityRatQuarry extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
    private static int[] STACKS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63};
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    private NonNullList<ItemStack> inventory = NonNullList.withSize(64, ItemStack.EMPTY);

    public TileEntityRatQuarry() {
        super(RatsTileEntityRegistry.RAT_QUARRY);
    }

    public int getSizeInventory() {
        return this.inventory.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index) {
        return this.inventory.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return STACKS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.inventory.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }


    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }


    @Override
    public void tick() {
        checkAndReplacePlatforms();
    }

    private void checkAndReplacePlatforms() {
        BlockPos checkingPos = this.pos.down();
        for(int i = 0; i < getRadius(); i++){
            if(world.isAirBlock(checkingPos)){
                world.setBlockState(checkingPos, RatsBlockRegistry.RAT_QUARRY_PLATFORM.getDefaultState());
            }
            checkingPos = checkingPos.offset(Direction.NORTH);
        }
        for(int i = 0; i <= getRadius(); i++){
            if(world.isAirBlock(checkingPos)){
                world.setBlockState(checkingPos, RatsBlockRegistry.RAT_QUARRY_PLATFORM.getDefaultState());
            }
            checkingPos = checkingPos.offset(Direction.WEST);
        }
    }

    public void remove() {
        if(!world.isRemote){
            BlockPos checkingPos = this.pos.down();
            for(int i = 0; i < getRadius(); i++){
                if(world.getBlockState(checkingPos).getBlock() == RatsBlockRegistry.RAT_QUARRY_PLATFORM){
                    world.destroyBlock(checkingPos, true);
                }
                checkingPos = checkingPos.offset(Direction.NORTH);
            }
            for(int i = 0; i <= getRadius(); i++){
                if(world.getBlockState(checkingPos).getBlock() == RatsBlockRegistry.RAT_QUARRY_PLATFORM){
                    world.destroyBlock(checkingPos, true);
                }
                checkingPos = checkingPos.offset(Direction.WEST);
            }
        }
        super.remove();
    }

    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.inventory);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.rat_quarry");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new ChestContainer(ContainerType.GENERIC_9X6, id, player, this, 6);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new ChestContainer(ContainerType.GENERIC_9X6, id, playerInventory, this, 6);
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handlers[0].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        func_230337_a_(this.getBlockState(), packet.getNbtCompound());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public int getRadius() {
        return 2;
    }

    public Direction stairDirection = Direction.NORTH;

    public BlockPos getNextPosForStairs(){
        int yLevel = this.pos.getY() - 1;
        BlockPos stairPos = this.pos.add(-getRadius(), -1, -getRadius());
        int passedLevels = 0;
        while(yLevel > 1){
            boolean atLevel = false;
            for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(this.pos.getX() - getRadius(), yLevel, this.pos.getZ() - getRadius()), new BlockPos(this.pos.getX() + getRadius(), yLevel, this.pos.getZ() + getRadius())).map(BlockPos::toImmutable).collect(Collectors.toList())) {
                if(world.getBlockState(pos).getBlock() == RatsBlockRegistry.RAT_QUARRY_PLATFORM){
                    atLevel = true;
                    break;
                }
            }
            if(!atLevel){
                break;
            }
            passedLevels++;
            yLevel--;
        }
        stairPos = stairPos.down(passedLevels);
        Direction buildDir = Direction.SOUTH;
        for(int i = 0; i < passedLevels; i++){
            if(i > 0 && i % (this.getRadius() * 2) == 0){
                buildDir = buildDir.rotateYCCW();
            }
            stairPos = stairPos.offset(buildDir);
        }
        stairDirection = buildDir;
        return stairPos;
    }
}
