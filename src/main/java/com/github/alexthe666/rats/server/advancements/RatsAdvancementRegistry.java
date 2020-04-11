package com.github.alexthe666.rats.server.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class RatsAdvancementRegistry {
    public static PlagueDoctorTrigger PLAGUE_DOCTOR_TRIGGER;
    public static RatsAdvancementRegistry INSTANCE = new RatsAdvancementRegistry();

    private RatsAdvancementRegistry() {
    }

    public static void init() {
        PLAGUE_DOCTOR_TRIGGER = CriteriaTriggers.register(new PlagueDoctorTrigger());
    }
}
