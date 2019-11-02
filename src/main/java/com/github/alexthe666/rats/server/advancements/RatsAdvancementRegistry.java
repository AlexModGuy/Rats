package com.github.alexthe666.rats.server.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.IOException;

public class RatsAdvancementRegistry {
    public static RatsAdvancementRegistry INSTANCE = new RatsAdvancementRegistry();

    public static final PlagueDoctorTrigger PLAGUE_DOCTOR_TRIGGER = CriteriaTriggers.register(new PlagueDoctorTrigger());

    private RatsAdvancementRegistry(){}

    public void init(){

    }
}
