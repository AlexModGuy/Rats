package com.github.alexthe666.rats.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public final ForgeConfigSpec.BooleanValue spawnRats;
    public final ForgeConfigSpec.BooleanValue ratsDespawn;
    public final ForgeConfigSpec.BooleanValue spawnPiper;
    public final ForgeConfigSpec.IntValue ratSpawnRate;
    public final ForgeConfigSpec.IntValue ratSpawnDecrease;
    public final ForgeConfigSpec.IntValue piperSpawnDecrease;
    public final ForgeConfigSpec.IntValue piperSpawnRate;
    public final ForgeConfigSpec.DoubleValue piperHatDropRate;
    public final ForgeConfigSpec.DoubleValue plagueEssenceDropRate;
    public final ForgeConfigSpec.DoubleValue archeologistHatSpawnRate;
    public final ForgeConfigSpec.DoubleValue ratlanteanAutomatonHealth;
    public final ForgeConfigSpec.DoubleValue ratlanteanAutomatonAttack;
    public final ForgeConfigSpec.DoubleValue neoRatlanteanHealth;
    public final ForgeConfigSpec.DoubleValue neoRatlanteanAttack;
    public final ForgeConfigSpec.DoubleValue dutchratHealth;
    public final ForgeConfigSpec.DoubleValue dutchratAttack;
    public final ForgeConfigSpec.BooleanValue ratsSpawnLikeMonsters;
    public final ForgeConfigSpec.BooleanValue cheesemaking;
    public final ForgeConfigSpec.BooleanValue plagueRats;
    public final ForgeConfigSpec.IntValue milkCauldronTime;
    public final ForgeConfigSpec.BooleanValue ratsDigBlocks;
    public final ForgeConfigSpec.BooleanValue ratsBreakCrops;
    public final ForgeConfigSpec.BooleanValue ratsStealItems;
    public final ForgeConfigSpec.BooleanValue ratsContaminateFood;
    public final ForgeConfigSpec.BooleanValue golemsTargetRats;
    public final ForgeConfigSpec.BooleanValue villagePetShops;
    public final ForgeConfigSpec.BooleanValue villageGarbageHeaps;
    public final ForgeConfigSpec.BooleanValue villagePlagueDoctors;
    public final ForgeConfigSpec.BooleanValue disablePlastic;
    public final ForgeConfigSpec.BooleanValue ratsBreakBlockOnHarvest;
    public final ForgeConfigSpec.BooleanValue plagueSpread;
    public final ForgeConfigSpec.BooleanValue plagueHearts;
    public final ForgeConfigSpec.DoubleValue ratStrengthThreshold;
    public final ForgeConfigSpec.IntValue ratFluteDistance;
    public final ForgeConfigSpec.IntValue ratCageCramming;
    public final ForgeConfigSpec.IntValue ratUpdateDelay;
    public final ForgeConfigSpec.IntValue tokenDropRate;
    public final ForgeConfigSpec.BooleanValue disableRatlantis;
    public final ForgeConfigSpec.IntValue ratlantisDimensionId;
    public final ForgeConfigSpec.IntValue ratlantisPortalExitDimension;
    public final ForgeConfigSpec.IntValue maxDestroyedLeaves;
    public final ForgeConfigSpec.DoubleValue blackDeathHealth;
    public final ForgeConfigSpec.DoubleValue blackDeathAttack;
    public final ForgeConfigSpec.DoubleValue ratDespawnFarDistance;
    public final ForgeConfigSpec.DoubleValue ratDespawnCloseDistance;
    public final ForgeConfigSpec.IntValue ratDespawnRandomChance;
    public final ForgeConfigSpec.BooleanValue ratFartNoises;
    public final ForgeConfigSpec.IntValue ratRFTransferBasic;
    public final ForgeConfigSpec.IntValue ratRFTransferAdvanced;
    public final ForgeConfigSpec.IntValue ratRFTransferElite;
    public final ForgeConfigSpec.IntValue ratRFTransferExtreme;
    public final ForgeConfigSpec.DoubleValue ratVoodooDistance;
    public final ForgeConfigSpec.BooleanValue addLoot;

    public ClientConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        this.spawnRats = buildBoolean(builder, "Spawn Rats", "all", true, "True if rats are to spawn naturally");
        this.ratsDespawn = buildBoolean(builder, "Rat Immediate Despawn Distance", "all", true, "True if wild, untamed rats can despawn.");
        this.ratSpawnDecrease = buildInt(builder, "Rat Spawn Decrease", "all", 5, 0, Integer.MAX_VALUE, "A seperate random roll that only spawns rats if a one-out-of-X chance, x being this number. raise this number to make them more rare.");
        this.plagueRats = buildBoolean(builder, "Plague Rats", "all", true, "True if plague rats are to spawn naturally");
        this.spawnPiper = buildBoolean(builder, "Spawn Piper", "all", true, "True if Pied Pipers are to spawn naturally");
        this.piperSpawnDecrease = buildInt(builder, "Piper Spawn Decrease", "all", 5, 0, Integer.MAX_VALUE, "A seperate random roll that only spawns pipers if a one-out-of-X chance, x being this number. raise this number to make them more rare.");
        this.ratSpawnRate = buildInt(builder, "Rat Spawn Weight", "all", 80, 1, 300, "The weight of rats in vanilla's spawn rate");
        this.piperSpawnRate = buildInt(builder, "Pied Piper Spawn Weight", "all", 6, 1, 300, "The weight of pied pipers in vanilla's spawn rate");
        this.piperHatDropRate = buildDouble(builder, "Pied Piper Hat Drop Rate", "all", 0.09F, 0F, 1F, "percent chance for piper to drop hat on death");
        this.plagueEssenceDropRate = buildDouble(builder, "Plague Essence Drop Rate", "all", 0.1F, 0F, 1F, "percent chance for plague rat to drop plague essence on death");
        this.archeologistHatSpawnRate = buildDouble(builder, "Archeologist Hat Spawn Rate", "all", 0.12F, 0F, 1F, "percent chance for a husk or jungle skeleton to spawn with an archeologist hat");
        this.ratlanteanAutomatonHealth = buildDouble(builder, "Ratlantean Automaton Max Health", "all", 600F, 0F, Float.MAX_VALUE, "Ratlantean Automaton Max Health");
        this.ratlanteanAutomatonAttack = buildDouble(builder, "Ratlantean Automaton Attack Damage", "all", 6F, 0F, Float.MAX_VALUE, "Ratlantean Automaton Attack Damage");
        this.neoRatlanteanHealth = buildDouble(builder, "Neo-Ratlantean Max Health", "all", 300F, 0F, Float.MAX_VALUE, "Neo-Ratlantean Max Health");
        this.neoRatlanteanAttack = buildDouble(builder, "Neo-Ratlantean Automaton Attack Damage", "all", 8F, 0F, Float.MAX_VALUE, "Neo-Ratlantean Attack Damage");
        this.dutchratHealth = buildDouble(builder, "Flying Dutchrat Max Health", "all", 400F, 0F, Float.MAX_VALUE, "Flying Dutchrat Max Health");
        this.dutchratAttack = buildDouble(builder, "Flying Dutchrat Automaton Attack Damage", "all", 10F, 0F, Float.MAX_VALUE, "Flying Dutchrat Attack Damage");
        this.ratsBreakCrops = buildBoolean(builder, "Rats Raid Crops", "all", true, "True if wild rats will destroy and eat crops");
        this.ratsStealItems = buildBoolean(builder, "Rats Steal From Chests", "all", true, "True if wild rats will steal from chests");
        this.ratsContaminateFood = buildBoolean(builder, "Rats Contaminate Food", "all", true, "True if wild rats contaminate food when they steal from chests");
        this.golemsTargetRats = buildBoolean(builder, "Golems Target Rats", "all", true, "True if iron golems will attack wild rats");
        this.villagePetShops = buildBoolean(builder, "Village Pet Shops", "all", true, "True if pet shops can spawn in villages");
        this.villageGarbageHeaps = buildBoolean(builder, "Village Garbage Heap", "all", true, "True if garbage heaps can spawn in villages");
        this.villagePlagueDoctors = buildBoolean(builder, "Village Plague Doctors", "all", true, "True if plague doctor huts can spawn in villages");
        this.ratsDigBlocks = buildBoolean(builder, "Rats Dig Holes", "all", true, "True if rats can dig holes");
        this.ratsSpawnLikeMonsters = buildBoolean(builder, "Rats Spawn Like Monsters", "all", true, "True if rats should spawn like monsters. False if they should only spawn once per world, like pigs and sheep.");
        this.ratsBreakBlockOnHarvest = buildBoolean(builder, "Rats Break Crops on Harvest", "all", true, "True if tamed rats will destroy crops when they harvest them");
        this.ratFartNoises = buildBoolean(builder, "Rat Fart Noises", "all", true, "True if rats should rarely make a funny noise when creating rats nuggets.");
        this.plagueSpread = buildBoolean(builder, "Other Mobs can spread Plague", "all", true, "True if infected mobs with plague can spread it by interacting or attacking.");
        this.plagueHearts = buildBoolean(builder, "Plague Heart Overlay", "all", true, "True if player UI has plague hearts render when the effect is active.");
        this.cheesemaking = buildBoolean(builder, "Cheesemaking", "all", true, "True if cheese can be created in cauldrons");
        this.milkCauldronTime = buildInt(builder, "Milk Curdling Time", "all", 150, 20, 1000000, "The time in ticks(20 per second) it takes for milk to turn into cheese in a cauldron");
        this.ratStrengthThreshold = buildDouble(builder, "Rat Dig Strength", "all", 4F, 0F, 1000000F, "The max block hardness that rats are allowed to dig through. (Dirt = 0.5F, Cobblestone = 2.0F, Obsidian = 50.0F)");
        this.ratFluteDistance = buildInt(builder, "Rat Flute Distance", "all", 2, 1, 100, "The how many chunks away can a rat here a rat flute");
        this.ratCageCramming = buildInt(builder, "Rat Cage Max Occupancy", "all", 5, 1, 10000, "Rats will continue to breed in cages until there are this many rats in one cage block");
        this.ratUpdateDelay = buildInt(builder, "Rat Upgrade Delay", "all", 100, 1, 10000, "Rats will conduct expensive CPU operations like looking for crops or chests, once every this number of ticks(with added standard deviation for servers)");
        //this.blacklistedRatBlocks = config.getStringList("Blacklisted Rat Inventory Blocks", "all", new String[0], "Blacklist for blocks that rats are not allowed to steal from. Ex. \"minecraft:chest\" or \"rats:rat_crafting_table\"");
        //this.blacklistedRatDimensions = config.get("all", "Blacklisted Rat Spawn Dimensions", new int[0], "Blacklist for dimensions that rats and pipers cannot spawn in").getIntList();
        this.tokenDropRate = buildInt(builder, "Rat Token Drop Rate", "all", 10000, 1, Integer.MAX_VALUE, "1/This number chance for a rat to drop a Token");
        this.disableRatlantis = buildBoolean(builder, "Disable Ratlantis", "all", false, "True if Ratlantis dimension is disabled - alternative methods of getting resources will be provided. WARNING: Leave the dimension and restart the game before changing this. You must be fun at parties.");
        this.disablePlastic = buildBoolean(builder, "Disable Plastic", "all", false, "True if Plastic item is disabled - alternative methods of getting rat cage deco will be provided. WARNING: Leave the restard the game after changing this. You must be fun at parties.");
        this.ratlantisDimensionId = buildInt(builder, "Ratlantis Dimension ID", "all", -8, Integer.MIN_VALUE, Integer.MAX_VALUE, "Ratlantis Dimension ID");
        this.ratlantisPortalExitDimension = buildInt(builder, "Ratlantis Portal Exit Dimension ID", "all", 0, Integer.MIN_VALUE, Integer.MAX_VALUE, "What Dimension ID you are teleported to upon leaving Ratlantis");
        this.maxDestroyedLeaves = buildInt(builder, "Rat Upgrade Lumberjack: Max Tree Blocks", "all", 10000, 0, Integer.MAX_VALUE, "How many blocks the Lumberjack Rat is able to destroy when felling a tree. Be careful when changing this to a large number.");
        this.blackDeathHealth = buildDouble(builder, "Black Death Max Health", "all", 400F, 0F, Float.MAX_VALUE, "Black Death Max Health");
        this.blackDeathAttack = buildDouble(builder, "Black Death Automaton Attack Damage", "all", 4F, 0F, Float.MAX_VALUE, "Black Death Attack Damage");
        this.ratDespawnFarDistance = buildDouble(builder, "Rat Immediate Despawn Distance", "all", 96F, 0F, Float.MAX_VALUE, "Distance that untamed rats will immediately despawn. For Vanilla mobs, this is 128 blocks.");
        this.ratDespawnCloseDistance = buildDouble(builder, "Rat Potential Despawn Distance", "all", 20F, 0F, Float.MAX_VALUE, "Distance that untamed rats could possibly despawn. For Vanilla mobs, this is 32 blocks.");
        this.ratDespawnRandomChance = buildInt(builder, "Rat Potential Despawn Chance", "all", 400, 1, Integer.MAX_VALUE, "When a rat is farther than its potential despawn distance, a random roll is taken to see if it despawns. Lower this number will make rats more likely to despawn.");
        this.ratRFTransferBasic = buildInt(builder, "Rat RF Transfer Rate Basic (kRF)", "all", 100, 1, Integer.MAX_VALUE, "How much kRF (1000 RF) a rat with a basic energy transfer upgrade can transport at a time.");
        this.ratRFTransferAdvanced = buildInt(builder, "Rat RF Transfer Rate Advanced (kRF)", "all", 500, 1, Integer.MAX_VALUE, "How much kRF (1000 RF) a rat with an advanced energy transfer upgrade can transport at a time.");
        this.ratRFTransferElite = buildInt(builder, "Rat RF Transfer Rate Elite (kRF)", "all", 1000, 1, Integer.MAX_VALUE, "How much kRF (1000 RF) a rat with an elite energy transfer upgrade can transport at a time.");
        this.ratRFTransferExtreme = buildInt(builder, "Rat RF Transfer Rate Extreme (kRF)", "all", 100000, 1, Integer.MAX_VALUE, "How much kRF (1000 RF) a rat with an extreme energy transfer upgrade can transport at a time.");
        this.ratVoodooDistance = buildDouble(builder, "Voodoo Doll Rat distance", "all", 32F, 0F, Float.MAX_VALUE, "How far away from players the Rat Upgrade: Voodoo Doll is effective.");
        this.addLoot = buildBoolean(builder, "Add Loot", "all", true, "True if loot from rats can spawn in chests");

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
