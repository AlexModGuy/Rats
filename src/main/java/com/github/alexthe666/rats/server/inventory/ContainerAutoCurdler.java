package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.tile.TileEntityAutoCurdler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerAutoCurdler extends Container {

    public final IInventory tileRatCraftingTable;
    public IIntArray vars;
    public ContainerAutoCurdler(int id, IInventory inv, PlayerInventory playerInventory, IIntArray vars) {
        super(RatsContainerRegistry.AUTO_CURDLER_CONTAINER, id);
        this.tileRatCraftingTable = inv;
        this.vars = vars;
        this.addSlot(new Slot(tileRatCraftingTable, 0, 8, 35));
        this.addSlot(new FurnaceResultSlot(playerInventory.player, tileRatCraftingTable, 1, 129, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ContainerAutoCurdler(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(2), playerInventory, ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).furnaceData);
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }
    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).cookTime;
        int j = ((TileEntityAutoCurdler) RatsMod.PROXY.getRefrencedTE()).totalCookTime;
        //System.out.println(i);
        return j != 0 && i != 0 ? i * 50 / j : 0;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (TileEntityAutoCurdler.isMilk(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 1 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 38 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public int getCookTime() {
        return vars.get(2);
    }

    public int getTotalCookTime() {
        return vars.get(3);
    }
}
