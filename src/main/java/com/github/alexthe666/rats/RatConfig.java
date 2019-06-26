package com.github.alexthe666.rats;

import net.minecraftforge.common.config.Configuration;

public class RatConfig {
    public boolean spawnRats = true;
    public boolean spawnPiper = true;
    public int ratSpawnRate = 80;
    public int piperSpawnRate = 6;
    public float piperHatDropRate = 0.09F;
    public boolean ratsSpawnLikeMonsters = true;
    public boolean cheesemaking = true;
    public int milkCauldronTime = 150;
    public boolean ratsDigBlocks = true;
    public float ratStrengthThreshold = 4.0F;
    public int ratFluteDistance = 2;
    public int ratCageCramming = 4;
    public int ratUpdateTick = 10;
    public String[] blacklistedRatBlocks = new String[0];

    public void init(Configuration config) {
        this.spawnRats = config.getBoolean("Spawn Rats", "all", true, "True if rats are to spawn naturally");
        this.spawnPiper = config.getBoolean("Spawn Piper", "all", true, "True if Pied Pipers are to spawn naturally");
        this.ratSpawnRate = config.getInt("Rat Spawn Weight", "all", 80, 1, 300, "The weight of rats in vanilla's spawn rate");
        this.piperSpawnRate = config.getInt("Pied Piper Spawn Weight", "all", 6, 1, 300, "The weight of pied pipers in vanilla's spawn rate");
        this.piperHatDropRate = config.getFloat("Pied Piper Hat Drop Rate", "all", 0.09F, 0F, 1F, "percent chance for piper to drop hat on death");
        this.ratsSpawnLikeMonsters = config.getBoolean("Rats Spawn Like Monsters", "all", true, "True if rats should spawn like monsters. False if they should only spawn once per world, like pigs and sheep.");
        this.cheesemaking = config.getBoolean("Cheesemaking", "all", true, "True if cheese can be created in cauldrons");
        this.milkCauldronTime = config.getInt("Milk Curdling Time", "all", 150, 20, 1000000, "The time in ticks(20 per second) it takes for milk to turn into cheese in a cauldron");
        this.ratsDigBlocks = config.getBoolean("Rats Dig Holes", "all", true, "True if rats can dig holes");
        this.ratStrengthThreshold = config.getFloat("Rat Dig Strength", "all", 4F, 0F, 1000000F, "The max block hardness that rats are allowed to dig through. (Dirt = 0.5F, Cobblestone = 2.0F, Obsidian = 50.0F)");
        this.ratFluteDistance = config.getInt("Rat Flute Distance", "all", 2, 1, 100, "The how many chunks away can a rat here a rat flute");
        this.ratCageCramming = config.getInt("Rat Cage Max Occupancy", "all", 5, 1, 10000, "Rats will continue to breed in cages until there are this many rats in one cage block");
        this.ratUpdateTick = config.getInt("Rat Upgrade Tick", "all", 10, 1, 10000, "Rats will conduct expensive CPU operations like looking for crops or chests every fraction of a tick(20 per second). Default is 10, so rats update every half-second.");
        this.blacklistedRatBlocks = config.getStringList("Blacklisted Rat Inventory Blocks", "all", new String[0], "Blacklist for blocks that rats are not allowed to steal from. Ex. \"minecraft:chest\" or \"rats:rat_crafting_table\"");
    }
}
