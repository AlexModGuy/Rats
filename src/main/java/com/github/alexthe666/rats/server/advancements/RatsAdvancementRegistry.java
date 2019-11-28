package com.github.alexthe666.rats.server.advancements;

import net.minecraft.advancements.CriteriaTriggers;

public class RatsAdvancementRegistry {
    public static final PlagueDoctorTrigger PLAGUE_DOCTOR_TRIGGER = CriteriaTriggers.register(new PlagueDoctorTrigger());
    public static RatsAdvancementRegistry INSTANCE = new RatsAdvancementRegistry();

    private RatsAdvancementRegistry() {
    }

    public void init() {

    }
}
