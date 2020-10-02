package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRatlanteanAutomatonHead extends TileEntity implements ITickableTileEntity {

    public int ticksExisted;


    public TileEntityRatlanteanAutomatonHead() {
        super(RatlantisTileEntityRegistry.AUTOMATON_HEAD);
    }

    @Override
    public void tick() {
        ticksExisted++;
    }
}
