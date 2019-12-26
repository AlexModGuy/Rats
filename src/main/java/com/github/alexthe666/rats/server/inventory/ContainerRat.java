package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;

public class ContainerRat extends Container {
    private IInventory ratInventory;
    public EntityRat rat;

    public ContainerRat(int id, IInventory ratInventory, PlayerInventory playerInventory, EntityRat rat) {
        super(RatsContainerRegistry.RAT_CONTAINER, id);
        this.ratInventory = ratInventory;
        this.rat = rat;
        byte b0 = 3;
        ratInventory.openInventory(playerInventory.player);
        int i = (b0 - 4) * 18;
        this.addSlot(new Slot(ratInventory, 0, 61, 54) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack);
            }
        });
        this.addSlot(new Slot(ratInventory, 1, 61, 18) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && (stack.getItem().canEquip(stack, EquipmentSlotType.HEAD, playerInventory.player) || stack.getItem() instanceof BannerItem);
            }
        });
        this.addSlot(new Slot(ratInventory, 2, 61, 36) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemRatUpgrade;
            }
        });
        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
        }
    }

    public ContainerRat(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(3), playerInventory, null);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.ratInventory.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.ratInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.ratInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(2).isItemValid(itemstack1) && !this.getSlot(2).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.ratInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.ratInventory.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.ratInventory.closeInventory(playerIn);
    }

}