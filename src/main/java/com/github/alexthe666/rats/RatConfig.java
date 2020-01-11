package com.github.alexthe666.rats;

import net.minecraftforge.fml.config.ModConfig;

public class RatConfig {
    public static boolean spawnRats = true;
    public static boolean spawnPiper = true;
    public static int ratSpawnRate = 80;
    public static int ratSpawnDecrease = 5;
    public static int piperSpawnDecrease = 1;
    public static int piperSpawnRate = 6;
    public static float piperHatDropRate = 0.09F;
    public static float plagueEssenceDropRate = 0.05F;
    public static float archeologistHatSpawnRate = 0.12F;
    public static float ratlanteanAutomatonHealth = 600F;
    public static float ratlanteanAutomatonAttack = 6F;
    public static float neoRatlanteanHealth = 300F;
    public static float neoRatlanteanAttack = 8F;
    public static float dutchratHealth = 400F;
    public static float dutchratAttack = 8F;
    public static boolean ratsSpawnLikeMonsters = true;
    public static boolean cheesemaking = true;
    public static boolean plagueRats = true;
    public static int milkCauldronTime = 150;
    public static boolean ratsDigBlocks = true;
    public static boolean ratsBreakCrops = true;
    public static boolean ratsStealItems = true;
    public static boolean ratsContaminateFood = true;
    public static boolean golemsTargetRats = true;
    public static boolean villagePetShops = true;
    public static boolean villageGarbageHeaps = true;
    public static boolean villagePlagueDoctors = true;
    public static boolean disablePlastic = false;
    public static boolean ratsBreakBlockOnHarvest = true;
    public static boolean plagueSpread = true;
    public static boolean plagueHearts = true;
    public static float ratStrengthThreshold = 4.0F;
    public static int ratFluteDistance = 2;
    public static int ratCageCramming = 4;
    public static int ratUpdateDelay = 100;
    public static int tokenDropRate = 10000;
    public static String[] blacklistedRatBlocks = new String[0];
    public static int[] blacklistedRatDimensions = new int[0];
    public static boolean disableRatlantis = false;
    public static int ratlantisDimensionId = -8;
    public static int ratlantisPortalExitDimension = 0;
    public static int maxDestroyedLeaves = 10000;
    public static float blackDeathHealth = 400F;
    public static float blackDeathAttack = 4F;
    public static double ratDespawnFarDistance = 96F;
    public static double ratDespawnCloseDistance = 20F;
    public static int ratDespawnRandomChance = 400;
    public static boolean ratFartNoises = true;
    public static int ratRFTransferBasic = 100;
    public static int ratRFTransferAdvanced = 500;
    public static int ratRFTransferElite = 1000;
    public static int ratRFTransferExtreme = 100000;
    public static double ratVoodooDistance = 32;
    public static boolean addLoot = true;

    public static void bakeClient(final ModConfig config) {


    }

    public static void bakeServer(final ModConfig config) {
        //ExampleModConfig.serverBoolean = ConfigHolder.SERVER.serverBoolean.get();
    }
}
