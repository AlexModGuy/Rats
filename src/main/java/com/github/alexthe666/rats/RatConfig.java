package com.github.alexthe666.rats;

import net.minecraftforge.fml.config.ModConfig;

public class RatConfig {
    public static boolean spawnRats = true;
    public static boolean spawnPiper = true;
    public static boolean spawnDemonRats = true;
    public static boolean funnyFluteSound = true;
    public static boolean ratOverworldOnly = false;
    public static boolean piperOverworldOnly = true;
    public static int ratSpawnRate = 80;
    public static int ratSpawnDecrease = 5;
    public static int piperSpawnDecrease = 1;
    public static int piperSpawnRate = 6;
    public static float piperHatDropRate = 0.09F;
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
    public static int maxDestroyedLeaves = 10000;
    public static float blackDeathHealth = 400F;
    public static float blackDeathAttack = 4F;
    public static double ratDespawnFarDistance = 144F;
    public static double ratDespawnCloseDistance = 32F;
    public static int ratDespawnRandomChance = 800;
    public static boolean ratFartNoises = true;
    public static int ratRFTransferBasic = 100;
    public static int ratRFTransferAdvanced = 500;
    public static int ratRFTransferElite = 1000;
    public static int ratRFTransferExtreme = 100000;
    public static double ratVoodooDistance = 32;
    public static boolean addLoot = true;
    public static int defaultRatRadius = 16;
    public static int maxRatRadius = 32;
    public static double garbageSpawnRate = 0.15F;

    public static int maxRatPathingNodes = 5000;
    public static int ratsPathfindingThreads = 3;

    public static void bakeClient(final ModConfig config) {
        try {
            plagueHearts = ConfigHolder.CLIENT.plagueHearts.get();
            funnyFluteSound = ConfigHolder.CLIENT.funnyFluteSound.get();
        }catch (Exception e){
            RatsMod.LOGGER.warn("An exception was caused trying to load the config for Rats.");
            e.printStackTrace();
        }
    }

    public static void bakeServer(final ModConfig config) {
        try {
            spawnRats = ConfigHolder.SERVER.spawnRats.get();
            spawnPiper = ConfigHolder.SERVER.spawnPiper.get();
            spawnDemonRats = ConfigHolder.SERVER.spawnDemonRats.get();
            funnyFluteSound = ConfigHolder.SERVER.funnyFluteSound.get();
            ratSpawnRate = ConfigHolder.SERVER.ratSpawnRate.get();
            ratSpawnDecrease = ConfigHolder.SERVER.ratSpawnDecrease.get();
            piperSpawnDecrease = ConfigHolder.SERVER.piperSpawnDecrease.get();
            piperSpawnRate = ConfigHolder.SERVER.piperSpawnRate.get();
            piperHatDropRate = ConfigHolder.SERVER.piperHatDropRate.get().floatValue();

            ratsSpawnLikeMonsters = ConfigHolder.SERVER.ratsSpawnLikeMonsters.get();
            cheesemaking = ConfigHolder.SERVER.cheesemaking.get();
            plagueRats = ConfigHolder.SERVER.plagueRats.get();
            milkCauldronTime = ConfigHolder.SERVER.milkCauldronTime.get();
            ratsDigBlocks = ConfigHolder.SERVER.ratsDigBlocks.get();
            ratsBreakCrops = ConfigHolder.SERVER.ratsBreakCrops.get();
            ratsStealItems = ConfigHolder.SERVER.ratsStealItems.get();
            ratsContaminateFood = ConfigHolder.SERVER.ratsContaminateFood.get();
            golemsTargetRats = ConfigHolder.SERVER.golemsTargetRats.get();
            villagePetShops = ConfigHolder.SERVER.villagePetShops.get();
            villageGarbageHeaps = ConfigHolder.SERVER.villageGarbageHeaps.get();
            villagePlagueDoctors = ConfigHolder.SERVER.villagePlagueDoctors.get();
            disablePlastic = ConfigHolder.SERVER.disablePlastic.get();
            ratsBreakBlockOnHarvest = ConfigHolder.SERVER.ratsBreakBlockOnHarvest.get();
            plagueSpread = ConfigHolder.SERVER.plagueSpread.get();
            ratStrengthThreshold = ConfigHolder.SERVER.ratStrengthThreshold.get().floatValue();
            ratFluteDistance = ConfigHolder.SERVER.ratFluteDistance.get();
            ratCageCramming = ConfigHolder.SERVER.ratCageCramming.get();
            ratUpdateDelay = ConfigHolder.SERVER.ratUpdateDelay.get();
            tokenDropRate = ConfigHolder.SERVER.tokenDropRate.get();
            maxDestroyedLeaves = ConfigHolder.SERVER.maxDestroyedLeaves.get();
            blackDeathHealth = ConfigHolder.SERVER.blackDeathHealth.get().floatValue();
            blackDeathAttack = ConfigHolder.SERVER.blackDeathAttack.get().floatValue();
            ratDespawnFarDistance = ConfigHolder.SERVER.ratDespawnFarDistance.get();
            ratDespawnCloseDistance = ConfigHolder.SERVER.ratDespawnCloseDistance.get();
            ratDespawnRandomChance = ConfigHolder.SERVER.ratDespawnRandomChance.get();
            ratFartNoises = ConfigHolder.SERVER.ratFartNoises.get();
            ratRFTransferBasic = ConfigHolder.SERVER.ratRFTransferBasic.get();
            ratRFTransferAdvanced = ConfigHolder.SERVER.ratRFTransferAdvanced.get();
            ratRFTransferElite = ConfigHolder.SERVER.ratRFTransferElite.get();
            ratRFTransferExtreme = ConfigHolder.SERVER.ratRFTransferExtreme.get();
            ratVoodooDistance = ConfigHolder.SERVER.ratVoodooDistance.get();
            addLoot = ConfigHolder.SERVER.addLoot.get();
            defaultRatRadius = ConfigHolder.SERVER.defaultRatRadius.get();
            maxRatRadius = ConfigHolder.SERVER.maxRatRadius.get();
            garbageSpawnRate = ConfigHolder.SERVER.garbageSpawnRate.get();
            ratOverworldOnly = ConfigHolder.SERVER.ratOverworldOnly.get();
            piperOverworldOnly = ConfigHolder.SERVER.piperOverworldOnly.get();
        }catch (Exception e){
            RatsMod.LOGGER.warn("An exception was caused trying to load the config for Rats.");
            e.printStackTrace();
        }
    }
}
