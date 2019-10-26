package com.github.alexthe666.rats.server.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RatsAdvancementRegistry {
    public static RatsAdvancementRegistry INSTANCE = new RatsAdvancementRegistry();

    public static final PlagueDoctorTrigger PLAGUE_DOCTOR_TRIGGER = CriteriaTriggers.register(new PlagueDoctorTrigger());

    private RatsAdvancementRegistry(){}

    public void init(){

    }
}
