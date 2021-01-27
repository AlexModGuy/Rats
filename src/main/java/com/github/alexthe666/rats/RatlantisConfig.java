package com.github.alexthe666.rats;

import net.minecraftforge.fml.config.ModConfig;

public class RatlantisConfig {

    /*public final ForgeConfigSpec.DoubleValue archeologistHatSpawnRate;
    public final ForgeConfigSpec.DoubleValue ratlanteanAutomatonHealth;
    public final ForgeConfigSpec.DoubleValue ratlanteanAutomatonAttack;
    public final ForgeConfigSpec.DoubleValue neoRatlanteanHealth;
    public final ForgeConfigSpec.DoubleValue neoRatlanteanAttack;
    public final ForgeConfigSpec.DoubleValue dutchratHealth;
    public final ForgeConfigSpec.DoubleValue dutchratAttack;*/

    public static boolean skipExperimentalSettingsGUI = true;
    public static float archeologistHatSpawnRate = 0.12F;
    public static float ratlanteanAutomatonHealth = 600F;
    public static float ratlanteanAutomatonAttack = 6F;
    public static float neoRatlanteanHealth = 300F;
    public static float neoRatlanteanAttack = 8F;
    public static float dutchratHealth = 400F;
    public static float dutchratAttack = 8F;

    public static void bakeClient(final ModConfig config) {
        try {
            skipExperimentalSettingsGUI = ConfigHolder.CLIENT.skipExperimentalSettingsGUI.get();

        } catch (Exception e) {

        }
    }

    public static void bakeServer(final ModConfig config) {
        try {
            archeologistHatSpawnRate = ConfigHolder.SERVER.archeologistHatSpawnRate.get().floatValue();
            ratlanteanAutomatonHealth = ConfigHolder.SERVER.ratlanteanAutomatonHealth.get().floatValue();
            ratlanteanAutomatonAttack = ConfigHolder.SERVER.ratlanteanAutomatonAttack.get().floatValue();
            neoRatlanteanHealth = ConfigHolder.SERVER.neoRatlanteanHealth.get().floatValue();
            neoRatlanteanAttack = ConfigHolder.SERVER.neoRatlanteanAttack.get().floatValue();
            dutchratHealth = ConfigHolder.SERVER.dutchratHealth.get().floatValue();
            dutchratAttack = ConfigHolder.SERVER.dutchratAttack.get().floatValue();
        }catch (Exception e){

        }

    }
}