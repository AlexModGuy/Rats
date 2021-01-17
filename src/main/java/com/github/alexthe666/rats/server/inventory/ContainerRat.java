package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.items.ItemRatUpgrade;
import com.github.alexthe666.rats.server.items.RatsUpgradeConflictRegistry;
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
        this.addSlot(new Slot(ratInventory, 0, 70, 54) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack);
            }
        });
        this.addSlot(new Slot(ratInventory, 1, 70, 18) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && (stack.getItem().canEquip(stack, EquipmentSlotType.HEAD, playerInventory.player));
            }
        });
        this.addSlot(new Slot(ratInventory, 2, 70, 36) {
            public void onSlotChanged() {
                this.inventory.markDirty();
            }

            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof BannerItem;
            }
        });
        for(int upgrade = 0; upgrade <= 2; upgrade++){
            int finalUpgrade = upgrade;
            this.addSlot(new Slot(ratInventory, 3 + upgrade, -2, 18 + upgrade * 18) {
                public void onSlotChanged() {
                    this.inventory.markDirty();
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    return super.isItemValid(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemRatUpgrade &&
                            canCoexistUpgrade(inventory, stack, finalUpgrade);
                }
            });
        }
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
        this(i, new Inventory(6), playerInventory, null);
    }

    public static boolean canCoexistUpgrade(IInventory inventory, ItemStack upgrade, int ourSlot){
        switch (ourSlot){
            case 0:
                return !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(4).getItem()) &&
                        !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(5).getItem());
            case 1:
                return !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(3).getItem()) &&
                        !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(5).getItem());
            case 2:
                return !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(3).getItem()) &&
                        !RatsUpgradeConflictRegistry.doesConflict(upgrade.getItem(), inventory.getStackInSlot(4).getItem());
        }
        return false;
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

            } else if (this.getSlot(3).isItemValid(itemstack1) && !this.getSlot(3).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(4).isItemValid(itemstack1) && !this.getSlot(4).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }

            }  else if (this.getSlot(5).isItemValid(itemstack1) && !this.getSlot(5).getHasStack()) {
                if (!this.mergeItemStack(itemstack1, 5, 6, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).isItemValid(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.ratInventory.getSizeInventory() <= 6 || !this.mergeItemStack(itemstack1, 6, this.ratInventory.getSizeInventory(), false)) {
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