package com.github.alexthe666.rats.server.world;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;

import java.util.function.BiFunction;

public class RatlantisModDimension extends ModDimension {
    private BiFunction<World, DimensionType, ? extends Dimension> factory;

    public RatlantisModDimension(BiFunction<World, DimensionType, ? extends Dimension> factory) {
        this.factory = factory;
    }

    @Override
    public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
        return this.factory;
    }
}