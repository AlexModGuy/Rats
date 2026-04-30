package com.github.alexthe666.rats.server;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {

	public final ModConfigSpec.BooleanValue ratlantisEnabledByDefault;

	public final ModConfigSpec.IntValue ratSpawnDecrease;
	public final ModConfigSpec.IntValue piperSpawnDecrease;
	public final ModConfigSpec.BooleanValue ratsSpawnLikeMonsters;
	public final ModConfigSpec.BooleanValue cheesemaking;
	public final ModConfigSpec.IntValue milkCauldronTime;
	public final ModConfigSpec.IntValue ratSackCapacity;
	public final ModConfigSpec.BooleanValue ratsDigBlocks;
	public final ModConfigSpec.BooleanValue ratsBreakCrops;
	public final ModConfigSpec.BooleanValue ratsStealItems;
	public final ModConfigSpec.BooleanValue ratsClimbOverFences;
	public final ModConfigSpec.BooleanValue ratsContaminateFood;
	public final ModConfigSpec.BooleanValue golemsTargetRats;
	public final ModConfigSpec.BooleanValue villagePetShops;
	public final ModConfigSpec.IntValue villagePetShopWeight;
	public final ModConfigSpec.IntValue zombieVillagePetShopWeight;
	public final ModConfigSpec.BooleanValue villageGarbageHeaps;
	public final ModConfigSpec.IntValue villageGarbageHeapWeight;
	public final ModConfigSpec.IntValue zombieVillageGarbageHeapWeight;
	public final ModConfigSpec.BooleanValue ratsBreakBlockOnHarvest;
	public final ModConfigSpec.BooleanValue plagueSpread;
	public final ModConfigSpec.DoubleValue ratStrengthThreshold;
	public final ModConfigSpec.IntValue ratFluteDistance;
	public final ModConfigSpec.IntValue ratCageCramming;
	public final ModConfigSpec.IntValue ratUpdateDelay;
	public final ModConfigSpec.DoubleValue garbageSpawnRate;
	public final ModConfigSpec.IntValue maxRatLitterSize;
	public final ModConfigSpec.IntValue ratBreedingCooldown;
	public final ModConfigSpec.BooleanValue ratsSpawnWithSantaHats;
	public final ModConfigSpec.BooleanValue ratsSpawnWithPartyHats;
	public final ModConfigSpec.IntValue maxRatRadius;

	public final ModConfigSpec.IntValue ratArmorMultiplier;
	public final ModConfigSpec.BooleanValue ratsChargeHeldItems;
	public final ModConfigSpec.IntValue ratRFTransferBasic;
	public final ModConfigSpec.IntValue ratChargeBasic;
	public final ModConfigSpec.IntValue ratRFTransferAdvanced;
	public final ModConfigSpec.IntValue ratChargeAdvanced;
	public final ModConfigSpec.IntValue ratRFTransferElite;
	public final ModConfigSpec.IntValue ratChargeElite;
	public final ModConfigSpec.IntValue ratRFTransferExtreme;
	public final ModConfigSpec.IntValue ratChargeExtreme;
	public final ModConfigSpec.IntValue upgradeRegenRate;
	public final ModConfigSpec.DoubleValue warriorHealthUpgrade;
	public final ModConfigSpec.DoubleValue warriorDamageUpgrade;
	public final ModConfigSpec.DoubleValue warriorArmorUpgrade;
	public final ModConfigSpec.DoubleValue godHealthUpgrade;
	public final ModConfigSpec.DoubleValue godDamageUpgrade;
	public final ModConfigSpec.DoubleValue godArmorUpgrade;
	public final ModConfigSpec.DoubleValue dragonHealthUpgrade;
	public final ModConfigSpec.DoubleValue dragonDamageUpgrade;
	public final ModConfigSpec.DoubleValue dragonArmorUpgrade;
	public final ModConfigSpec.DoubleValue demonHealthUpgrade;
	public final ModConfigSpec.DoubleValue demonDamageUpgrade;
	public final ModConfigSpec.DoubleValue voodooHealthUpgrade;
	public final ModConfigSpec.DoubleValue ratVoodooDistance;
	public final ModConfigSpec.DoubleValue ratinatorArmorUpgrade;
	public final ModConfigSpec.DoubleValue nonbelieverHealthUpgrade;
	public final ModConfigSpec.DoubleValue nonbelieverDamageUpgrade;
	public final ModConfigSpec.DoubleValue nonbelieverArmorUpgrade;
	public final ModConfigSpec.BooleanValue ratPsychicThrowsBlocks;
	public final ModConfigSpec.DoubleValue carratDamagePerBite;

	public final ModConfigSpec.BooleanValue blackDeathLightning;
	public final ModConfigSpec.BooleanValue bdConstantRatSpawns;
	public final ModConfigSpec.IntValue bdMaxRatSpawns;
	public final ModConfigSpec.BooleanValue bdConstantCloudSpawns;
	public final ModConfigSpec.IntValue bdMaxCloudSpawns;
	public final ModConfigSpec.BooleanValue bdConstantBeastSpawns;
	public final ModConfigSpec.IntValue bdMaxBeastSpawns;

	public final ModConfigSpec.BooleanValue ratKingReabsorbsRats;
	public final ModConfigSpec.DoubleValue ratKingReabsorbHealRate;
	public final ModConfigSpec.BooleanValue ratKingConstantRatSpawns;
	public final ModConfigSpec.IntValue ratKingMaxRatSpawns;

	public final ModConfigSpec.BooleanValue summonAutomatonOnlyInRatlantis;
	public final ModConfigSpec.IntValue automatonShootChance;
	public final ModConfigSpec.IntValue automatonMeleeDistance;
	public final ModConfigSpec.IntValue automatonRangedDistance;

	public final ModConfigSpec.BooleanValue summonNeoOnlyInRatlantis;
	public final ModConfigSpec.BooleanValue neoratlanteanSummonLaserPortals;
	public final ModConfigSpec.IntValue neoratlanteanLaserAttackCooldown;
	public final ModConfigSpec.BooleanValue neoratlanteanSummonFakeLightning;
	public final ModConfigSpec.IntValue neoratlanteanLightningAttackCooldown;
	public final ModConfigSpec.BooleanValue neoratlanteanThrowBlocks;
	public final ModConfigSpec.IntValue neoratlanteanBlockAttackCooldown;
	public final ModConfigSpec.BooleanValue neoratlanteanAddHarmfulEffects;
	public final ModConfigSpec.IntValue neoratlanteanEffectAttackCooldown;

	public final ModConfigSpec.BooleanValue summonDutchratOnlyInRatlantis;
	public final ModConfigSpec.IntValue dutchratSwordThrowChance;
	public final ModConfigSpec.IntValue dutchratRestrictionRadius;

	public final ModConfigSpec.BooleanValue summonBaronOnlyInRatlantis;
	public final ModConfigSpec.IntValue ratBaronYFlight;
	public final ModConfigSpec.IntValue ratBaronShootFrequency;
	public final ModConfigSpec.DoubleValue ratBaronBulletDamage;


	public ServerConfig(final ModConfigSpec.Builder builder) {
		builder.push("Ratlantis");
		this.ratlantisEnabledByDefault = buildBoolean(builder, "Ratlantis Datapack Enabled by Default", true, "If true, the ratlantis datapack will automatically be enabled when creating a new world.");

		builder.pop().push("Spawning");
		this.garbageSpawnRate = buildDouble(builder, "Garbage Pile Spawn Chance", 0.15F, 0F, 1.0F, "Percentage for every random tick to spawn a rat for a garbage pile.");
		this.ratsSpawnLikeMonsters = buildBoolean(builder, "Rats Spawn Like Monsters", true, "True if rats should spawn like monsters, meaning they will also despawn when get far away (this only applies to wild rats of course). False if they should only spawn once per world, like pigs and sheep.");
		this.ratSpawnDecrease = buildInt(builder, "Rat Spawn Decrease", 5, 0, Integer.MAX_VALUE, "A separate random roll that only spawns rats if a one-out-of-X chance, x being this number. raise this number to make them more rare.");
		this.piperSpawnDecrease = buildInt(builder, "Piper Spawn Decrease", 2, 0, Integer.MAX_VALUE, "A separate random roll that only spawns pipers if a one-out-of-X chance, x being this number. raise this number to make them more rare.");

		builder.pop().push("Villages");
		this.golemsTargetRats = buildBoolean(builder, "Golems Target Rats", true, "True if iron golems will attack wild rats");
		this.villagePetShops = buildBoolean(builder, "Village Pet Shops", true, "True if pet shops can spawn in villages");
		this.villagePetShopWeight = buildInt(builder, "Village Pet Shop Weight", 15, 0, 100, "Defines how often Pet Shops should spawn in villages. A higher number means Pet Shops will spawn more often.");
		this.zombieVillagePetShopWeight = buildInt(builder, "Zombie Village Pet Shop Weight", 10, 0, 100, "Defines how often Pet Shops should spawn in zombie villages. A higher number means Pet Shops will spawn more often.");
		this.villageGarbageHeaps = buildBoolean(builder, "Village Garbage Heap", true, "True if garbage heaps can spawn in villages");
		this.villageGarbageHeapWeight = buildInt(builder, "Village Garbage Heap Weight", 1, 0, 100, "Defines how often Garbage Heaps should spawn in villages. A higher number means Garbage Heaps will spawn more often.");
		this.zombieVillageGarbageHeapWeight = buildInt(builder, "Zombie Village Garbage Heap Weight", 5, 0, 100, "Defines how often Garbage Heaps should spawn in villages. A higher number means Garbage Heaps will spawn more often.");

		builder.pop().push("Cheesemaking");
		this.cheesemaking = buildBoolean(builder, "Cheesemaking", true, "True if cheese can be created in cauldrons");
		this.milkCauldronTime = buildInt(builder, "Milk Curdling Time", 150, 20, 1000000, "The time in ticks(20 per second) it takes for milk to turn into cheese in a cauldron");
		this.ratFluteDistance = buildInt(builder, "Rat Flute Distance", 2, 1, 100, "The how many chunks away can a rat here a rat flute");

		builder.pop().push("Black Death");
		this.blackDeathLightning = buildBoolean(builder, "Black Death Lightning Strike", true, "If true, a plague doctor will be converted into the Black Death when struck by lightning.");
		this.bdConstantRatSpawns = buildBoolean(builder, "Black Death Constantly Spawns Rats", false, "If true, Black Death will respawn Plague Rats when it has less than the max defined Plague Rats at its command.");
		this.bdMaxRatSpawns = buildInt(builder, "Black Death Max Plague Rats", 15, 0, 100, "Defines the amount of Plague Rats Black Death can have at its command at a time.");
		this.bdConstantCloudSpawns = buildBoolean(builder, "Black Death Constantly Spawns Plague Clouds", true, "If true, Black Death will respawn Plague Clouds when it has less than the max defined Plague Clouds at its command.");
		this.bdMaxCloudSpawns = buildInt(builder, "Black Death Max Plague Clouds", 4, 0, 100, "Defines the amount of Plague Clouds Black Death can have at its command at a time.");
		this.bdConstantBeastSpawns = buildBoolean(builder, "Black Death Constantly Spawns Plague Beasts", false, "If true, Black Death will respawn Plague Beasts when it has less than the max defined Plague Beasts at its command.");
		this.bdMaxBeastSpawns = buildInt(builder, "Black Death Plague Beasts", 3, 0, 100, "Defines the amount of Plague Beasts Black Death can have at its command at a time.");

		builder.pop().push("Rat King");
		this.ratKingReabsorbsRats = buildBoolean(builder, "Rat King Reabsorbs Rats", true, "If true, the Rat King will reabsorb rats that circle around it if it doesnt have an active target.");
		this.ratKingReabsorbHealRate = buildDouble(builder, "Rat King Absorption Heal Rate", 0.0F, 0.0F, Float.MAX_VALUE, "Defines the amount of health the Rat King heals when reabsorbing a rat");
		this.ratKingConstantRatSpawns = buildBoolean(builder, "Rat King Max Constantly Spawns Rats", true, "If true, the Rat King will respawn Rats when it has less than the max defined Plague Rats at its command.");
		this.ratKingMaxRatSpawns = buildInt(builder, "Rat King Max Rats", 10, 0, 100, "Defines the amount of Rats the Rat King can have at its command at a time.");

		builder.pop().push("Ratlantean Automaton");
		this.summonAutomatonOnlyInRatlantis = buildBoolean(builder, "Summon Ratlantean Automaton only in Ratlantis", false, "If true, the Ratlantean Automaton will only be summonable in Ratlantis. Building it in any other dimension will have no effect.");
		this.automatonShootChance = buildInt(builder, "Ratlantean Automaton Shooting Chance", 2, 0, Integer.MAX_VALUE, "How often the Ratlantean Automaton will shoot a laser. It will shoot a laser every 1 in X attacks, X being the number defined.");
		this.automatonMeleeDistance = buildInt(builder, "Ratlantean Automaton Melee Attack Distance", 7, 0, Integer.MAX_VALUE, "The distance the Ratlantean Automaton can hit you with its saw.");
		this.automatonRangedDistance = buildInt(builder, "Ratlantean Automaton Ranged Attack Distance", 10, 0, Integer.MAX_VALUE, "The distance the Ratlantean Automaton can hit you with its laser.");

		builder.pop().push("Neo-Ratlantean");
		this.summonNeoOnlyInRatlantis = buildBoolean(builder, "Summon Neo-Ratlantean only in Ratlantis", false, "If true, the Neo-Ratlantean will only be summonable in Ratlantis. Throwing the vial in any other dimension will have no effect.");
		this.neoratlanteanSummonLaserPortals = buildBoolean(builder, "Neo-Ratlantean Laser Portals", true, "If true, the Neo-Ratlantean will utilize Laser Portals in its fight that shoot lasers at you.");
		this.neoratlanteanLaserAttackCooldown = buildInt(builder, "Neo-Ratlantean Attack Cooldown after Laser Portals", 100, 0, Integer.MAX_VALUE, "The time it will take the Neo-Ratlantean to perform another attack after using the Laser Portal attack. Time is in ticks.");
		this.neoratlanteanSummonFakeLightning = buildBoolean(builder, "Neo-Ratlantean Fake Lightning", true, "If true, the Neo-Ratlantean will occasionally summon lightning bolts nearby. The lightning is completely harmless.");
		this.neoratlanteanLightningAttackCooldown = buildInt(builder, "Neo-Ratlantean Attack Cooldown after Fake Lightning", 40, 0, Integer.MAX_VALUE, "The time it will take the Neo-Ratlantean to perform another attack after using the Fake Lightning attack. Time is in ticks.");
		this.neoratlanteanThrowBlocks = buildBoolean(builder, "Neo-Ratlantean Throws Blocks", true, "If true, the Neo-Ratlantean will pick up blocks and throw them in your direction.");
		this.neoratlanteanBlockAttackCooldown = buildInt(builder, "Neo-Ratlantean Attack Cooldown after Throwing Blocks", 40, 0, Integer.MAX_VALUE, "The time it will take the Neo-Ratlantean to perform another attack after using the Block Throwing attack. Time is in ticks.");
		this.neoratlanteanAddHarmfulEffects = buildBoolean(builder, "Neo-Ratlantean Adds Harmful Effects", true, "If true, the Neo-Ratlantean will randomly afflict the use with wither, glowing, and levitation for 10 seconds.");
		this.neoratlanteanEffectAttackCooldown = buildInt(builder, "Neo-Ratlantean Attack Cooldown after Effects", 100, 0, Integer.MAX_VALUE, "The time it will take the Neo-Ratlantean to perform another attack after using the Mob Effect attack. Time is in ticks.");

		builder.pop().push("Flying Dutchrat");
		this.summonDutchratOnlyInRatlantis = buildBoolean(builder, "Summon Flying Dutchrat only in Ratlantis", false, "If true, the Flying Dutchrat will only be summonable in Ratlantis. Ringing the bell in any other dimension will have no effect.");
		this.dutchratRestrictionRadius = buildInt(builder, "Flying Dutchrat Restriction Radius", 20, 0, 64, "How many blocks away the dutchrat can fly away from its home point, which is the spot where the bell was when summoned.");
		this.dutchratSwordThrowChance = buildInt(builder, "Flying Dutchrat Sword Throw Chance", 5, 0, Integer.MAX_VALUE, "How often the Flying Dutchrat will throw a sword. It will throw a sword every 1 in X attacks, X being the number defined.");

		builder.pop().push("Rat Baron");
		this.summonBaronOnlyInRatlantis = buildBoolean(builder, "Summon Automaton only in Ratlantis", false, "If true, the Rat Baron will only be summonable in Ratlantis. Activating the siren in any other dimension will have no effect.");
		this.ratBaronYFlight = buildInt(builder, "Rat Baron Y Hover Height", 20, 0, 100, "The height above the ground the rat baron plane will hover around. A random amount of blocks between 0 and 10 will be added to the number provided at random points to keep things interesting.");
		this.ratBaronShootFrequency = buildInt(builder, "Rat Baron Shot Frequency", 2, 1, Integer.MAX_VALUE, "How often the Rat Baron Plane will fire a bullet. It will fire a bullet every X ticks, X being the number defined.");
		this.ratBaronBulletDamage = buildDouble(builder, "Rat Baron Bullet Damage", 0.5D, 0.0D, Double.MAX_VALUE, "The amount of damage each bullet fired from the Rat Baron plane does. Setting to 0 will make the bullets purely visual.");

		builder.pop().push("Rat Griefing");
		this.ratsBreakCrops = buildBoolean(builder, "Rats Raid Crops", true, "True if wild rats will destroy and eat crops");
		this.ratsStealItems = buildBoolean(builder, "Rats Steal From Chests", true, "True if wild rats will steal from chests");
		this.ratsClimbOverFences = buildBoolean(builder, "Rats Climb over Fences and Walls", true, "True if wild rats should be allowed to climb over tall blocks, such as fences and walls.");
		this.ratsContaminateFood = buildBoolean(builder, "Rats Contaminate Food", true, "True if wild rats contaminate food when they steal from chests");
		this.ratsDigBlocks = buildBoolean(builder, "Rats Dig Holes", true, "True if rats can dig holes");
		this.plagueSpread = buildBoolean(builder, "Other Mobs can spread Plague", true, "True if infected mobs with plague can spread it by interacting or attacking.");
		this.ratStrengthThreshold = buildDouble(builder, "Rat Dig Strength", 6F, 0F, 1000000F, "The max block hardness that rats are allowed to dig through. (Dirt = 0.5F, Cobblestone = 2.0F, Obsidian = 50.0F)");
		this.ratsBreakBlockOnHarvest = buildBoolean(builder, "Rats Break Crops on Harvest", true, "True if tamed rats will destroy crops when they harvest them");

		builder.pop().push("Rat Upgrades");
		this.ratArmorMultiplier = buildInt(builder, "Rats Armor Multiplier", 3, 1, 1000, "Defines how much of a multiplier rats get when armor is put on them. For example, putting a diamond helmet on when this is set to 3 gives the rat 9 armor points.");
		this.ratsChargeHeldItems = buildBoolean(builder, "Rats Charge Held Items", true, "If true, rats will recharge items held in their main hand when they have an energy upgrade.");
		this.ratRFTransferBasic = buildInt(builder, "Rat RF Transfer Rate Basic", 1000, 1, Integer.MAX_VALUE, "How much RF a rat with a basic energy transfer upgrade can transport at a time.");
		this.ratChargeBasic = buildInt(builder, "Rat Item Charge Rate Basic", 10, 1, Integer.MAX_VALUE, "How much RF per tick a rat can charge their held item with a basic energy transfer upgrade.");
		this.ratRFTransferAdvanced = buildInt(builder, "Rat RF Transfer Rate Advanced", 5000, 1, Integer.MAX_VALUE, "How much RF a rat with an advanced energy transfer upgrade can transport at a time.");
		this.ratChargeAdvanced = buildInt(builder, "Rat Item Charge Rate Advanced", 50, 1, Integer.MAX_VALUE, "How much RF per tick a rat can charge their held item with an advanced energy transfer upgrade.");
		this.ratRFTransferElite = buildInt(builder, "Rat RF Transfer Rate Elite", 10000, 1, Integer.MAX_VALUE, "How much RF a rat with an elite energy transfer upgrade can transport at a time.");
		this.ratChargeElite = buildInt(builder, "Rat Item Charge Rate Elite", 100, 1, Integer.MAX_VALUE, "How much RF per tick a rat can charge their held item with an elite energy transfer upgrade.");
		this.ratRFTransferExtreme = buildInt(builder, "Rat RF Transfer Rate Extreme", 100000, 1, Integer.MAX_VALUE, "How much RF a rat with an extreme energy transfer upgrade can transport at a time.");
		this.ratChargeExtreme = buildInt(builder, "Rat Item Charge Rate Extreme", 500, 1, Integer.MAX_VALUE, "How much RF per tick a rat can charge their held item with an extreme energy transfer upgrade.");
		this.ratVoodooDistance = buildDouble(builder, "Voodoo Doll Rat distance", 32.0D, 0.0D, Double.MAX_VALUE, "How far away from players the Rat Upgrade: Voodoo Doll is effective.");
		this.warriorHealthUpgrade = buildDouble(builder, "Warrior Health Upgrade", 40.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the Warrior Upgrade.");
		this.warriorArmorUpgrade = buildDouble(builder, "Warrior Armor Upgrade", 2.0D, 0.0D, 30.0D, "Sets a Rat's Armor to this value when they have the Warrior Upgrade.");
		this.warriorDamageUpgrade = buildDouble(builder, "Warrior Damage Upgrade", 5.0D, 0.0D, 2048.0D, "Sets a Rat's attack damage to this value when they have the Warrior Upgrade.");
		this.godHealthUpgrade = buildDouble(builder, "God Health Upgrade", 150.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the God Upgrade.");
		this.godArmorUpgrade = buildDouble(builder, "God Armor Upgrade", 10.0D, 0.0D, 30.0D, "Sets a Rat's Armor to this value when they have the God Upgrade.");
		this.godDamageUpgrade = buildDouble(builder, "God Damage Upgrade", 15.0D, 0.0D, 2048.0D, "Sets a Rat's attack damage to this value when they have the God Upgrade.");
		this.dragonHealthUpgrade = buildDouble(builder, "Dragon Health Upgrade", 50.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the God Upgrade.");
		this.dragonArmorUpgrade = buildDouble(builder, "Dragon Armor Upgrade", 5.0D, 0.0D, 30.0D, "Sets a Rat's Armor to this value when they have the God Upgrade.");
		this.dragonDamageUpgrade = buildDouble(builder, "Dragon Damage Upgrade", 8.0D, 0.0D, 2048.0D, "Sets a Rat's attack damage to this value when they have the God Upgrade.");
		this.demonHealthUpgrade = buildDouble(builder, "Demon Health Upgrade", 40.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the Demon Upgrade.");
		this.demonDamageUpgrade = buildDouble(builder, "Demon Damage Upgrade", 4.0D, 0.0D, 2048.0D, "Sets a Rat's attack damage to this value when they have the Demon Upgrade.");
		this.voodooHealthUpgrade = buildDouble(builder, "Voodoo Health Upgrade", 100.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the Voodoo Upgrade.");
		this.ratinatorArmorUpgrade = buildDouble(builder, "Ratinator Armor Upgrade", 15.0D, 0.0D, 30.0D, "Sets a Rat's Armor to this value when they have the Ratinator Upgrade.");
		this.nonbelieverHealthUpgrade = buildDouble(builder, "Nonbeliever Health Upgrade", 350.0D, 0.0D, 1024.0D, "Sets a Rat's max health to this value when they have the Nonbeliever Upgrade.");
		this.nonbelieverArmorUpgrade = buildDouble(builder, "Nonbeliever Armor Upgrade", 20.0D, 0.0D, 30.0D, "Sets a Rat's Armor to this value when they have the Nonbeliever Upgrade.");
		this.nonbelieverDamageUpgrade = buildDouble(builder, "Nonbeliever Damage Upgrade", 40.0D, 0.0D, 2048.0D, "Sets a Rat's attack damage to this value when they have the Nonbeliever Upgrade.");
		this.upgradeRegenRate = buildInt(builder, "Upgrade Regeneration Rate", 30, 0, Integer.MAX_VALUE, "Rats that have an upgrade that regenerates health will regen half a heart every X ticks, X being the number defined here. Set to 0 to disable health regeneration.");
		this.ratPsychicThrowsBlocks = buildBoolean(builder, "Psychic Throws Blocks", false, "If true, Rats that have the Psychic upgrade will pick up blocks and throw them towards enemies.");
		this.carratDamagePerBite = buildDouble(builder, "Damage per Carrat Upgrade bite", 0.0D, 0.0D, Double.MAX_VALUE, "How much damage a rat should take when eaten while having the Carrat Upgrade.");

		builder.pop().push("Misc Rat Adjustments");
		this.maxRatRadius = buildInt(builder, "Max Staff Radius", 16, 1, 64, "Defines how large the radius staff can get. Do note that the bigger the radius, the larger the performance impact as rats need to search a bigger area for tasks. Going above 16 is not recommended.");
		this.ratSackCapacity = buildInt(builder, "Rat Sack Capacity", 16, 1, 64, "Defines the amount of rats you can store in a sack at one time.");
		this.ratCageCramming = buildInt(builder, "Rat Cage Max Occupancy", 4, 1, 10000, "Rats will continue to breed in cages until there are this many rats in one cage block");
		this.maxRatLitterSize = buildInt(builder, "Rat Litter Size", 1, 1, 10, "The max amount of Baby Rats will be produced when Rats breed. You will get anywhere between 1 and this number of baby rats.");
		this.ratBreedingCooldown = buildInt(builder, "Rat Breeding Cooldown", 24000, 0, Integer.MAX_VALUE, "The amount of time it takes after a rat successfully breeds for it to breed again. Time is in ticks. (24000 ticks = 1200 seconds = 20 minutes)");
		this.ratUpdateDelay = buildInt(builder, "Rat Update Delay", 100, 1, 10000, "Rats will conduct expensive CPU operations like looking for crops or chests, once every this number of ticks(with added standard deviation for servers)");
		this.ratsSpawnWithSantaHats = buildBoolean(builder, "Rats Spawn with Santa Hats", false, "If true, wild rats will rarely spawn wearing a santa hat. Rats will spawn with Santa hats during the Christmas season regardless, this will enable you to find them year round.");
		this.ratsSpawnWithPartyHats = buildBoolean(builder, "Rats Spawn with Party Hats", false, "If true, wild rats will rarely spawn wearing a randomly colored party hat. Rats will spawn with Party hats during the New Year's Eve and special dates regardless, this will enable you to find them year round.");
	}

	private static ModConfigSpec.BooleanValue buildBoolean(ModConfigSpec.Builder builder, String name, boolean defaultValue, String comment) {
		return builder.comment(comment).translation(name).define(name, defaultValue);
	}

	private static ModConfigSpec.IntValue buildInt(ModConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment) {
		return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
	}

	private static ModConfigSpec.DoubleValue buildDouble(ModConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment) {
		return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
	}
}
