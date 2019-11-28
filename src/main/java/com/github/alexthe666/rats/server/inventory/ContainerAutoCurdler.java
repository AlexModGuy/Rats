package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerAutoCurdler extends SyncedFieldContainer {

    private final IInventory tileRatCraftingTable;
    private int cookTime;

    public ContainerAutoCurdler(IInventory inv, PlayerEntity player) {
        super(inv);
        this.tileRatCraftingTable = inv;
        this.addSlotToContainer(new Slot(tileRatCraftingTable, 0, 8, 35));
        this.addSlotToContainer(new SlotFurnaceOutput(player, tileRatCraftingTable, 1, 129, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileRatCraftingTable);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.cookTime != this.tileRatCraftingTable.getField(1)) {
                icontainerlistener.sendWindowProperty(this, 1, this.tileRatCraftingTable.getField(1));
            }
        }

        this.cookTime = this.tileRatCraftingTable.getField(1);
    }


    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }
}
