package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;

public class RatInvListener implements IInventoryChangedListener {

    private EntityRat rat;

    public RatInvListener(EntityRat rat){
        this.rat = rat;
    }

    @Override
    public void onInventoryChanged(IInventory iInventory) {
        rat.refreshUpgrades = true;
    }
}
