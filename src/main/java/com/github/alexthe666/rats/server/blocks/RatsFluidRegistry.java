package com.github.alexthe666.rats.server.blocks;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatsFluidRegistry {

    @ObjectHolder(value = "rats:milk")
    public static final Fluid MILK_FLUID = new MilkFluid();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Fluid> event) {
        event.getRegistry().register(MILK_FLUID);
    }
}
