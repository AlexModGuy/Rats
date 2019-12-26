package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerAutoCurdler extends Container {

    public final IInventory tileRatCraftingTable;
    public final IIntArray vars;
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
        this(i, new Inventory(2), playerInventory, new IntArray(4));
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tileRatCraftingTable.isUsableByPlayer(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.vars.get(2);
        int j = this.vars.get(3);
        return j != 0 && i != 0 ? i * 50 / j : 0;
    }

}
