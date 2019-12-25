package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

public class ContainerEmpty extends Container {

    public ContainerEmpty() {
        super(RatsContainerRegistry.EMPTY_CONTAINER, 101);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
