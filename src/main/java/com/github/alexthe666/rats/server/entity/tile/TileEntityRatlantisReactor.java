package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRatlantisReactor extends TileEntity implements ITickableTileEntity {

    public TileEntityRatlantisReactor() {
        super(RatlantisTileEntityRegistry.RATLANTIS_REACTOR);
    }

    @Override
    public void tick() {

    }
}
