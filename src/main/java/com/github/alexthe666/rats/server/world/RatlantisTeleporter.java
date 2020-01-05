package com.github.alexthe666.rats.server.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class RatlantisTeleporter extends Teleporter {
    private final ServerWorld worldServerInstance;
    private final Random random;

    public RatlantisTeleporter(ServerWorld worldserver) {
        super(worldserver);
        this.worldServerInstance = worldserver;
        this.random = new Random(worldserver.getSeed());
    }

    @Override
    public boolean func_222268_a(Entity entity, float rotationYaw) {
        return false;
    }


    @Override
    public boolean makePortal(Entity e) {
        return true;
    }
}