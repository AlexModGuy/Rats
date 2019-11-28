package com.github.alexthe666.rats.server.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;

public class ContainerEmpty extends Container {

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
