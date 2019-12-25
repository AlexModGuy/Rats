package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;

public class ContainerAutoCurdler extends Container {

    private final IInventory tileRatCraftingTable;
    private int cookTime;

    public ContainerAutoCurdler(int id, IInventory inv, PlayerInventory playerInventory) {
        super(RatsContainerRegistry.AUTO_CURDLER_CONTAINER, id);
        this.tileRatCraftingTable = inv;
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
        this(i, new Inventory(2), playerInventory);
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }
}
