package com.github.alexthe666.rats.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.BooleanValue plagueHearts;
    public final ForgeConfigSpec.BooleanValue skipExperimentalSettingsGUI;
    public final ForgeConfigSpec.BooleanValue funnyFluteSound;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        this.plagueHearts = buildBoolean(builder, "Plague Heart Overlay", "all", true, "True if player UI has plague hearts render when the effect is active.");
        this.skipExperimentalSettingsGUI = buildBoolean(builder, "Skip Experimental Settings GUI", "all", true, "Skip experimental GUI settings from vanilla when loading a world.");
        this.funnyFluteSound = buildBoolean(builder, "Funny Rat Flute Sound", "all", true, "True if rats flutes can occasionally make a funny sound.");
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
