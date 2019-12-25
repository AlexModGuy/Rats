package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;

public class ContainerEmpty extends Container {

    public ContainerEmpty(int id, IInventory playerInventory) {
        super(RatsContainerRegistry.EMPTY_CONTAINER, id);
    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
