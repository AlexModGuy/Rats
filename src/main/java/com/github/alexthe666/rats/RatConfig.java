package com.github.alexthe666.rats;

public class RatConfig {
	//CLIENT
	public static boolean plagueHearts = true;
	public static boolean funnyFluteSound = true;
	public static boolean synesthesiaShader = true;
	public static boolean ratFartNoises = true;
	public static boolean ratGodGlint = true;
	public static boolean ratNonbelieverGlint = true;
	public static boolean ratAngelGlint = true;

	// SERVER
	public static int ratSpawnDecrease = 5;
	public static int piperSpawnDecrease = 2;
	public static boolean ratsSpawnLikeMonsters = true;
	public static boolean cheesemaking = true;
	public static int milkCauldronTime = 150;
	public static boolean ratsDigBlocks = true;
	public static boolean ratsBreakCrops = true;
	public static boolean ratsStealItems = true;
	public static boolean ratsContaminateFood = true;
	public static boolean golemsTargetRats = true;
	public static boolean villagePetShops = true;
	public static boolean villageGarbageHeaps = true;
	public static boolean ratsBreakBlockOnHarvest = true;
	public static boolean plagueSpread = true;
	public static float ratStrengthThreshold = 4.0F;
	public static int ratFluteDistance = 2;
	public static int ratCageCramming = 4;
	public static int ratBreedingCooldown = 24000;
	public static int ratUpdateDelay = 100;
	public static int defaultRatRadius = 16;
	public static int maxRatRadius = 32;
	public static double garbageSpawnRate = 0.15F;
	public static boolean ratDragonFire = true;
	public static int maxRatLitterSize = 1;

	public static int ratRFTransferBasic = 1000;
	public static int ratRFTransferAdvanced = 5000;
	public static int ratRFTransferElite = 10000;
	public static int ratRFTransferExtreme = 100000;
	public static int upgradeRegenRate = 30;
	public static double warriorHealthUpgrade = 40.0D;
	public static double warriorDamageUpgrade = 5.0D;
	public static double warriorArmorUpgrade = 10.0D;
	public static double godHealthUpgrade = 500.0D;
	public static double godDamageUpgrade = 50.0D;
	public static double godArmorUpgrade = 50.0D;
	public static double dragonHealthUpgrade = 50.0D;
	public static double dragonDamageUpgrade = 8.0D;
	public static double dragonArmorUpgrade = 15.0D;
	public static double demonHealthUpgrade = 40.0D;
	public static double demonDamageUpgrade = 4.0D;
	public static double voodooHealthUpgrade = 100.0D;
	public static double ratVoodooDistance = 32;
	public static double ratinatorArmorUpgrade = 80.0D;
	public static double nonbelieverHealthUpgrade = 1000.0D;
	public static double nonbelieverDamageUpgrade = 100.0D;
	public static double nonbelieverArmorUpgrade = 100.0D;

	public static boolean blackDeathLightning = true;
	public static boolean bdConstantRatSpawns = true;
	public static int bdMaxRatSpawns = 15;
	public static boolean bdConstantCloudSpawns = true;
	public static int bdMaxCloudSpawns = 3;
	public static boolean bdConstantBeastSpawns = false;
	public static int bdMaxBeastSpawns = 4;

	public static boolean ratKingReabsorbsRats = true;
	public static double ratKingReabsorbHealRate = 0.0D;
	public static boolean ratKingConstantRatSpawns = true;
	public static int ratKingMaxRatSpawns = 10;

	public static int automatonShootChance = 2;
	public static int automatonMeleeDistance = 7;
	public static int automatonRangedDistance = 10;

	public static boolean neoratlanteanSummonLaserPortals = true;
	public static int neoratlanteanLaserAttackCooldown = 100;
	public static boolean neoratlanteanSummonFakeLightning = true;
	public static int neoratlanteanLightningAttackCooldown = 40;
	public static boolean neoratlanteanThrowBlocks = true;
	public static int neoratlanteanBlockAttackCooldown = 40;
	public static boolean neoratlanteanAddHarmfulEffects = true;
	public static int neoratlanteanEffectAttackCooldown = 100;

	public static int dutchratSwordThrowChance = 5;
	public static int dutchratRestrictionRadius = 20;

	public static int ratBaronYFlight = 20;
	public static int ratBaronShootFrequency = 2;
	public static double ratBaronBulletDamage = 0.5D;

	public static void bakeClient() {
		try {
			plagueHearts = ConfigHolder.CLIENT.plagueHearts.get();
			synesthesiaShader = ConfigHolder.CLIENT.synesthesiaShader.get();
			funnyFluteSound = ConfigHolder.CLIENT.funnyFluteSound.get();
			ratFartNoises = ConfigHolder.CLIENT.ratFartNoises.get();
			ratGodGlint = ConfigHolder.CLIENT.ratGodGlint.get();
			ratNonbelieverGlint = ConfigHolder.CLIENT.ratNonbelieverGlint.get();
			ratAngelGlint = ConfigHolder.CLIENT.ratAngelGlint.get();
		} catch (Exception e) {
			RatsMod.LOGGER.warn("An exception was caused trying to load the config for Rats.");
			e.printStackTrace();
		}
	}

	public static void bakeServer() {
		try {
			ratSpawnDecrease = ConfigHolder.SERVER.ratSpawnDecrease.get();
			piperSpawnDecrease = ConfigHolder.SERVER.piperSpawnDecrease.get();

			ratsSpawnLikeMonsters = ConfigHolder.SERVER.ratsSpawnLikeMonsters.get();
			cheesemaking = ConfigHolder.SERVER.cheesemaking.get();
			milkCauldronTime = ConfigHolder.SERVER.milkCauldronTime.get();
			ratsDigBlocks = ConfigHolder.SERVER.ratsDigBlocks.get();
			ratsBreakCrops = ConfigHolder.SERVER.ratsBreakCrops.get();
			ratsStealItems = ConfigHolder.SERVER.ratsStealItems.get();
			ratsContaminateFood = ConfigHolder.SERVER.ratsContaminateFood.get();
			golemsTargetRats = ConfigHolder.SERVER.golemsTargetRats.get();
			villagePetShops = ConfigHolder.SERVER.villagePetShops.get();
			villageGarbageHeaps = ConfigHolder.SERVER.villageGarbageHeaps.get();
			ratsBreakBlockOnHarvest = ConfigHolder.SERVER.ratsBreakBlockOnHarvest.get();
			plagueSpread = ConfigHolder.SERVER.plagueSpread.get();
			ratStrengthThreshold = ConfigHolder.SERVER.ratStrengthThreshold.get().floatValue();
			ratFluteDistance = ConfigHolder.SERVER.ratFluteDistance.get();
			ratCageCramming = ConfigHolder.SERVER.ratCageCramming.get();
			ratUpdateDelay = ConfigHolder.SERVER.ratUpdateDelay.get();
			defaultRatRadius = ConfigHolder.SERVER.defaultRatRadius.get();
			maxRatRadius = ConfigHolder.SERVER.maxRatRadius.get();
			garbageSpawnRate = ConfigHolder.SERVER.garbageSpawnRate.get();
			ratDragonFire = ConfigHolder.SERVER.ratDragonFire.get();
			maxRatLitterSize = ConfigHolder.SERVER.maxRatLitterSize.get();
			ratBreedingCooldown = ConfigHolder.SERVER.ratBreedingCooldown.get();

			upgradeRegenRate = ConfigHolder.SERVER.upgradeRegenRate.get();
			ratRFTransferBasic = ConfigHolder.SERVER.ratRFTransferBasic.get();
			ratRFTransferAdvanced = ConfigHolder.SERVER.ratRFTransferAdvanced.get();
			ratRFTransferElite = ConfigHolder.SERVER.ratRFTransferElite.get();
			ratRFTransferExtreme = ConfigHolder.SERVER.ratRFTransferExtreme.get();
			warriorHealthUpgrade = ConfigHolder.SERVER.warriorHealthUpgrade.get();
			warriorArmorUpgrade = ConfigHolder.SERVER.warriorArmorUpgrade.get();
			warriorDamageUpgrade = ConfigHolder.SERVER.warriorDamageUpgrade.get();
			godHealthUpgrade = ConfigHolder.SERVER.godHealthUpgrade.get();
			godArmorUpgrade = ConfigHolder.SERVER.godArmorUpgrade.get();
			godDamageUpgrade = ConfigHolder.SERVER.godDamageUpgrade.get();
			dragonHealthUpgrade = ConfigHolder.SERVER.dragonHealthUpgrade.get();
			dragonArmorUpgrade = ConfigHolder.SERVER.dragonArmorUpgrade.get();
			dragonDamageUpgrade = ConfigHolder.SERVER.dragonDamageUpgrade.get();
			demonHealthUpgrade = ConfigHolder.SERVER.demonHealthUpgrade.get();
			demonDamageUpgrade = ConfigHolder.SERVER.demonDamageUpgrade.get();
			voodooHealthUpgrade = ConfigHolder.SERVER.voodooHealthUpgrade.get();
			ratVoodooDistance = ConfigHolder.SERVER.ratVoodooDistance.get();
			ratinatorArmorUpgrade = ConfigHolder.SERVER.ratinatorArmorUpgrade.get();
			nonbelieverHealthUpgrade = ConfigHolder.SERVER.nonbelieverHealthUpgrade.get();
			nonbelieverArmorUpgrade = ConfigHolder.SERVER.nonbelieverArmorUpgrade.get();
			nonbelieverDamageUpgrade = ConfigHolder.SERVER.nonbelieverDamageUpgrade.get();

			blackDeathLightning = ConfigHolder.SERVER.blackDeathLightning.get();
			bdConstantRatSpawns = ConfigHolder.SERVER.bdConstantRatSpawns.get();
			bdMaxRatSpawns = ConfigHolder.SERVER.bdMaxRatSpawns.get();
			bdConstantCloudSpawns = ConfigHolder.SERVER.bdConstantCloudSpawns.get();
			bdMaxCloudSpawns = ConfigHolder.SERVER.bdMaxCloudSpawns.get();
			bdConstantBeastSpawns = ConfigHolder.SERVER.bdConstantBeastSpawns.get();
			bdMaxBeastSpawns = ConfigHolder.SERVER.bdMaxBeastSpawns.get();

			ratKingReabsorbsRats = ConfigHolder.SERVER.ratKingReabsorbsRats.get();
			ratKingReabsorbHealRate = ConfigHolder.SERVER.ratKingReabsorbHealRate.get();
			ratKingConstantRatSpawns = ConfigHolder.SERVER.ratKingConstantRatSpawns.get();
			ratKingMaxRatSpawns = ConfigHolder.SERVER.ratKingMaxRatSpawns.get();

			automatonShootChance = ConfigHolder.SERVER.automatonShootChance.get();
			automatonMeleeDistance = ConfigHolder.SERVER.automatonMeleeDistance.get();
			automatonRangedDistance = ConfigHolder.SERVER.automatonRangedDistance.get();

			neoratlanteanSummonLaserPortals = ConfigHolder.SERVER.neoratlanteanSummonLaserPortals.get();
			neoratlanteanLaserAttackCooldown = ConfigHolder.SERVER.neoratlanteanLaserAttackCooldown.get();
			neoratlanteanSummonFakeLightning = ConfigHolder.SERVER.neoratlanteanSummonFakeLightning.get();
			neoratlanteanLightningAttackCooldown = ConfigHolder.SERVER.neoratlanteanLightningAttackCooldown.get();
			neoratlanteanThrowBlocks = ConfigHolder.SERVER.neoratlanteanThrowBlocks.get();
			neoratlanteanBlockAttackCooldown = ConfigHolder.SERVER.neoratlanteanBlockAttackCooldown.get();
			neoratlanteanAddHarmfulEffects = ConfigHolder.SERVER.neoratlanteanAddHarmfulEffects.get();
			neoratlanteanEffectAttackCooldown = ConfigHolder.SERVER.neoratlanteanEffectAttackCooldown.get();

			dutchratSwordThrowChance = ConfigHolder.SERVER.dutchratSwordThrowChance.get();
			dutchratRestrictionRadius = ConfigHolder.SERVER.dutchratRestrictionRadius.get();

			ratBaronYFlight = ConfigHolder.SERVER.ratBaronYFlight.get();
			ratBaronBulletDamage = ConfigHolder.SERVER.ratBaronBulletDamage.get();
			ratBaronShootFrequency = ConfigHolder.SERVER.ratBaronShootFrequency.get();
		} catch (Exception e) {
			RatsMod.LOGGER.warn("An exception was caused trying to load the config for Rats.");
			e.printStackTrace();
		}
	}
}
