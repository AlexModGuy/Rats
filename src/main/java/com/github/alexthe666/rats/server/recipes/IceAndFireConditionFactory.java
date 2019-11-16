package com.github.alexthe666.rats.server.recipes;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;

import java.util.function.BooleanSupplier;

public class IceAndFireConditionFactory implements IConditionFactory {

    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> !Loader.isModLoaded("iceandfire");
    }
}