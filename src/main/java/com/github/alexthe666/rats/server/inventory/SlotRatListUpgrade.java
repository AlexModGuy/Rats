package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.items.ItemRatListUpgrade;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class SlotRatListUpgrade extends Slot {
    public ItemStack upgrade;
    public SlotRatListUpgrade(IInventory playerInventory, ItemStack upgrade, int id, int x, int y) {
        super(playerInventory, id, x, y);
        this.upgrade = upgrade;
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() != upgrade.getItem();
    }

}
