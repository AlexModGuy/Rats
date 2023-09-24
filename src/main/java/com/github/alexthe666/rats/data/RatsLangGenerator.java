package com.github.alexthe666.rats.data;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;

import java.util.function.Supplier;

public class RatsLangGenerator extends LanguageProvider {

	public RatsLangGenerator(PackOutput output) {
		super(output, RatsMod.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		//Advancements
		this.addAdvancement("root", "Rats", "Rat Patootie");
		this.addAdvancement("rat_trap", "An Effective Classic", "Craft a Rat Trap");
		this.addAdvancement("piper", "Pay the Piper", "Encounter a rat-controlling fiend");
		this.addAdvancement("milk_cauldron", "Curdling Time", "Pour milk into a cauldron and wait for it to turn to cheese");
		this.addAdvancement("cheese", "We've Forgotten the Crackers!", "Turn a Block of Cheese into four Cheese");
		this.addAdvancement("tame_rat", "An Unlikely Alliance", "Tame a Rat by dropping Cheese near it repeatedly until it is full");
		this.addAdvancement("rat_cage", "Despite All My Rage...", "Craft a Rat Cage, used for breeding rats");
		this.addAdvancement("cheese_stick", "Cheese Touch", "Craft a Cheese Staff, used for setting drop-off and pickup locations for tamed rats");
		this.addAdvancement("rat_upgrade_platter", "Carry Weight 64", "Craft Rat Upgrade: Platter, used to make rats transport an entire stack of items at a time");
		this.addAdvancement("rat_upgrade_basic", "Rat Specialization", "Craft Rat Upgrade: Basic, used to craft advanced rat upgrades");
		this.addAdvancement("rat_upgrade_strength", "Rat Karate", "Craft Rat Upgrade: Strength, making your rat pack a powerful punch");
		this.addAdvancement("rat_upgrade_warrior", "Rat Warrior Clan", "Craft Rat Upgrade: Warrior, making your rat stronger, have armor and higher health");
		this.addAdvancement("rat_upgrade_god", "Whats a Man to a King?", "Craft Rat Upgrade: God, turning your rat into an almost unstoppable killing machine");
		this.addAdvancement("rat_upgrade_crafting", "Crafty Rats!", "Craft Rat Upgrade: Crafting, the first step in rat auto-crafting");
		this.addAdvancement("rat_crafting_table", "Rat Auto-Crafting", "Craft a Rat Crafting Table");
		this.addAdvancement("rat_upgrade_chef", "The Rat is the Cook!", "Craft a Rat Upgrade: Chef, which makes rats cook held items");
		this.addAdvancement("rat_cooking", "Anyone Can Cook", "Obtain one of the rare gourmet foods prepared by a rat chef");
		this.addAdvancement("rat_upgrade_flight", "Essentially Pigeons", "Craft a Rat Upgrade: Flight, enabling rats to take to the skies");
		this.addAdvancement("rat_cage_decoration", "A Gilded Cage", "Place a rat cage decoration in a Rat Cage");
		this.addAdvancement("rat_music_disc", "Rat Tunes", "Get a music disc by killing a Pied Piper with a rat");
		this.addAdvancement("contaminated_food", "Stop that Health Inspector!", "Find disgusting contaminated food left behind by a rat");
		this.addAdvancement("plague", "Down with the Sickness!", "Catch the plague from a Plague Rat");
		this.addAdvancement("plague_doctor", "I AM THE CURE!", "Find a Plague Doctor in a village or out in the wild");
		this.addAdvancement("plague_cure", "The Medieval Treatment", "Find a possible cure for the plague");
		this.addAdvancement("black_death", "Dance Macabre!", "Encounter the Black Death, a possessed Plague Doctor summoned by a lightning strike");
		this.addAdvancement("defeat_black_death", "Do Not Go Quietly Into the Night", "Defeat the Black Death");
		this.addAdvancement("trash_can", "This Mod is Trash", "Craft a trash can from a cauldron, some iron ingots and an iron nugget. Use it to create garbage piles!");
		this.addAdvancement("garbage_pile", "Too Much Trash in Your Face? There's No Trash in Space!", "Obtain a garbage pile from putting blocks in a trash can. They can be used to build rat-exclusive mob farms!");
		this.addAdvancement("ball_of_filth", "Cleanliness is Close to Godliness!", "Craft a Ball of Filth from an ungodly amount of Garbage");
		this.addAdvancement("rat_king", "King of the World!", "Slay the Rat King");
		this.addAdvancement("ratbow_essence", "In Stunning Technicolor!", "Craft a Ratbow Essence, which can be used to dye a rat a constant shifting rainbow");
		this.addAdvancement("nether_cheese", "Spicy Pepper Jack", "Obtain nether cheese from a demon rat in the nether");
		this.addAdvancement("rat_upgrade_demon", "I Seen This Before...", "Craft a Rat Upgrade: Demon, a well rounded nether-proof rat upgrade");

		//Blocks
		this.addBlockWithDesc(RatsBlockRegistry.AUTO_CURDLER, "Curdling Station", "Automatic cheese production", "§o\"Milk goes in, cheese comes out\"");
		this.addBlock(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE, "Block of Blue Cheese");
		this.addBlock(RatsBlockRegistry.BLOCK_OF_CHEESE, "Block of Cheese");
		this.addBlock(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE, "Block of Nether Cheese");this.addBlock(RatsBlockRegistry.BLUE_CHEESE_CAULDRON, "Cauldron of Blue Cheese");
		this.addBlock(RatsBlockRegistry.CHEESE_CAULDRON, "Cauldron of Cheese");
		this.addBlockWithDesc(RatsBlockRegistry.COMPRESSED_GARBAGE, "Compressed Garbage", "Spawns double the amount of Rats than normal Garbage Piles");
		this.addBlockWithDesc(RatsBlockRegistry.CURSED_GARBAGE, "Diseased Garbage", "Spawn Plague Rats on block");
		this.addBlockWithDesc(RatsBlockRegistry.DYE_SPONGE, "Dye Sponge", "Removes dye from Rats or Sheep");
		this.addBlock(RatsBlockRegistry.FISH_BARREL, "Barrel of Fish");
		this.addBlockWithDesc(RatsBlockRegistry.GARBAGE_PILE, "Garbage Pile", "Spawn Rats on block");
		this.addBlock(RatsBlockRegistry.JACK_O_RATERN, "Jack O' Ratern");
		this.addBlock(RatsBlockRegistry.MANHOLE, "Manhole Cover");
		this.addBlock(RatsBlockRegistry.MARBLED_CHEESE_RAW, "Raw Marbled Cheese");
		this.addBlock(RatsBlockRegistry.MILK_CAULDRON, "Cauldron of Milk");
		this.addBlock(RatsBlockRegistry.NETHER_CHEESE_CAULDRON, "Cauldron of Nether Cheese");
		this.addBlockWithDesc(RatsBlockRegistry.PIED_GARBAGE, "Pied Garbage", "Spawn Pied Pipers on block");
		this.addBlock(RatsBlockRegistry.PIED_WOOL, "Pied Wool");
		this.addBlockWithDesc(RatsBlockRegistry.PURIFIED_GARBAGE, "Purified Garbage", "Spawn Rats without plague on block");
		this.addBlockWithDesc(RatsBlockRegistry.RAT_ATTRACTOR, "Rat Attractor", "Attracts wild rats nearby when powered with redstone");
		this.addBlockWithBothDescs(RatsBlockRegistry.RAT_CAGE, "Rat Cage", "While having a rat sitting on your head or shoulder (sneak + click a rat), click on the cage to deposit the rat inside. Click again to release the rat. Rat cages connect to each other, and two opposite gender rats in the same cage have a chance to breed.", "Interact with while having a rat on your shoulder to deposit it in the cage", "Interact again to release rats");
		this.addBlock(RatsBlockRegistry.RAT_CAGE_BREEDING_LANTERN, "Rat Cage with Lantern");
		this.addBlock(RatsBlockRegistry.RAT_CAGE_DECORATED, "Decorated Rat Cage");
		this.addBlock(RatsBlockRegistry.RAT_CAGE_WHEEL, "Rat Cage with Wheel");
		this.addBlockWithJEIDesc(RatsBlockRegistry.RAT_CRAFTING_TABLE, "Rat Crafting Table", "Used in autocrafting, a rat crafting table requires a rat with the Rat Upgrade: Crafting sitting above the table to function. Create a ghost recipe in the crafting grid and add the required materials below it to get it working. This process can be automated with rats transporting and gathering items or the use of hoppers and other modded pipes.");
		this.addBlock(RatsBlockRegistry.RAT_HOLE, "Rat Hole");
		this.addBlockWithDesc(RatsBlockRegistry.RAT_QUARRY, "Rat Quarry", "Rats will dig quarry immediately below block", "Set quarry rats to 'Harvest' then assign this block as their deposit position with a cheese staff");
		this.addBlock(RatsBlockRegistry.RAT_QUARRY_PLATFORM, "Rat Quarry Platform");
		this.addBlockWithJEIDesc(RatsBlockRegistry.RAT_TRAP, "Rat Trap", "Place any food item rats will eat in the rat trap to attract wild rats. When they die inside the trap, it needs to be reset. Right click the trap or power it with redstone to reset it.");
		this.addBlockWithDesc(RatsBlockRegistry.RAT_TUBE_COLOR, "Rat Tube", "Right click to place Rat Tube", "Right click Rat Tube to create an entrance", "Rats will only go inside Rat Tubes that have entrances");
		this.addBlock(RatsBlockRegistry.RAT_UPGRADE_BLOCK, "Rat Upgrade Block");
		this.addBlockWithDesc(RatsBlockRegistry.TRASH_CAN, "Trash Can", "Right click with an empty hand to open or close", "Right click with blocks in hand to throw them away", "Creates garbage piles when enough blocks are discarded");
		this.addBlock(RatsBlockRegistry.UPGRADE_COMBINER, "Upgrade Combiner");
		this.addBlockWithDesc(RatsBlockRegistry.UPGRADE_SEPARATOR, "Upgrade Separator", "Throw a combined rat upgrade on top to shatter it");

		//Entities
		this.addEntityAndEgg(RatsEntityRegistry.BLACK_DEATH, "Black Death");
		this.addEntityAndEgg(RatsEntityRegistry.DEMON_RAT, "Demon Rat");
		this.addEntityAndEgg(RatsEntityRegistry.PIED_PIPER, "Pied Piper");
		this.addEntityAndEgg(RatsEntityRegistry.PLAGUE_BEAST, "Plague Beast");
		this.addEntityAndEgg(RatsEntityRegistry.PLAGUE_CLOUD, "Plague Cloud");
		this.addEntityAndEgg(RatsEntityRegistry.PLAGUE_DOCTOR, "Plague Doctor");
		this.add("entity.rats.plague_rat", "Plagued Rat");
		this.addEntityAndEgg(RatsEntityRegistry.RAT, "Wild Rat");
		this.addEntityAndEgg(RatsEntityRegistry.RAT_KING, "Rat King");

		this.addEntityType(RatsEntityRegistry.PLAGUE_SHOT, "Plague Shot");
		this.addEntityType(RatsEntityRegistry.PURIFYING_LIQUID, "Purifying Liquid");
		this.addEntityType(RatsEntityRegistry.RAT_ARROW, "Rat-Tipped Arrow");
		this.addEntityType(RatsEntityRegistry.RAT_CAPTURE_NET, "Rapture Net");
		this.addEntityType(RatsEntityRegistry.RAT_DRAGON_FIRE, "Rat Dragon Flames");
		this.addEntityType(RatsEntityRegistry.RAT_MOUNT_BEAST, "Rat Beast Mount");
		this.addEntityType(RatsEntityRegistry.RAT_MOUNT_CHICKEN, "Rat Chicken Mount");
		this.addEntityType(RatsEntityRegistry.RAT_MOUNT_GOLEM, "Rat Golem Mount");
		this.addEntityType(RatsEntityRegistry.RAT_STRIDER_MOUNT, "Rat Strider Mount");
		this.addEntityType(RatsEntityRegistry.RAT_SHOT, "Thrown Rat");
		this.addEntityType(RatsEntityRegistry.SMALL_ARROW, "Arrow");
		this.addEntityType(RatsEntityRegistry.TAMED_RAT, "Tamed Rat");

		//Items
		this.addItemWithDesc(RatsItemRegistry.ARCHEOLOGIST_HAT, "Archeologist Hat", "Drops from Husks or skeletons in jungles");
		this.addItem(RatsItemRegistry.ASSORTED_VEGETABLES, "Assorted Vegetables");
		this.addItemWithDesc(RatsItemRegistry.BLACK_DEATH_MASK, "Black Death Mask", "Plague Rats will no longer target you when worn");
		this.addItem(RatsItemRegistry.BLUE_CHEESE, "Blue Cheese");
		this.addItemWithJEIDesc(RatsItemRegistry.CENTIPEDE, "Centipede", "Right click a Little Black Worm onto some coarse dirt to turn it into a Centipede.");
		this.addItem(RatsItemRegistry.CHARGED_CREEPER_CHUNK, "Charged Creeper Chunk");
		this.addItemWithJEIDesc(RatsItemRegistry.CHEESE, "Cheese", "Used to tame rats. Throw cheese on the ground and let the wild rat eat it to tame one.");
		this.addItemWithBothDescs(RatsItemRegistry.CHEESE_STICK, "Cheese Staff", "Used to set rat deposit and pickup inventories and rat home points. Right click on a rat with it to bind, then right click on a block or container (chest, furnace, machine, etc) for more item transport related options.", "Use on a rat to bind it", "Interact with a block to allow the rat to deposit or pick up items in it");
		this.addItem(RatsItemRegistry.CHEF_TOQUE, "Chef Toque");
		this.addItem(RatsItemRegistry.CONFIT_BYALDI, "Confit Byaldi");
		this.addItemWithDesc(RatsItemRegistry.CONTAMINATED_FOOD, "Contaminated Food", "A wild rodent has touched this food.", "Probably not a good idea to eat this.");
		this.addItem(RatsItemRegistry.COOKED_RAT, "Cooked Rat");
		this.addItem(RatsItemRegistry.CORRUPT_RAT_SKULL, "Corrupt Rat Skull");
		this.addItemWithDesc(RatsItemRegistry.CREATIVE_CHEESE, "Creative Cheese", "Right click a rat to instantly tame it");
		this.addItemWithDesc(RatsItemRegistry.CRIMSON_FLUID, "Crimson Liquid", "Transforms demon rats into normal rats");
		this.addItem(RatsItemRegistry.DRAGON_WING, "Dragon Wing");
		this.addItem(RatsItemRegistry.EXTERMINATOR_HAT, "Exterminator Hat");
		this.addItem(RatsItemRegistry.FARMER_HAT, "Farmer Hat");
		this.addItem(RatsItemRegistry.FEATHERY_WING, "Feathery Wing");
		this.addItem(RatsItemRegistry.FILTH, "Filth");
		this.addItemWithDesc(RatsItemRegistry.FILTH_CORRUPTION, "Ball of Filth", "Transforms a rat into a Rat King");
		this.addItem(RatsItemRegistry.FISHERMAN_HAT, "Fisherman's Hat");
		this.addItemWithDesc(RatsItemRegistry.GILDED_RAT_FLUTE, "Gilded Rat Flute", "Fires rats when used");
		this.addItem(RatsItemRegistry.GOLDEN_RAT_SKULL, "Golden Rat Skull");
		this.addItem(RatsItemRegistry.HALO_HAT, "Golden Halo");
		this.addItem(RatsItemRegistry.HERB_BUNDLE, "Bundle of Sweet-Smelling Herbs");
		this.addItem(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS, "Little Black Squash Balls");
		this.addItem(RatsItemRegistry.LITTLE_BLACK_WORM, "Little Black Worm");
		this.addMusicDisc(RatsItemRegistry.MUSIC_DISC_LIVING_MICE, "C418 - Living Mice");
		this.addMusicDisc(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS, "C418 - Mice on Venus");
		this.addItem(RatsItemRegistry.NETHER_CHEESE, "Nether Cheese");
		this.addItemWithDesc(RatsItemRegistry.PARTY_HAT, "Party Hat", "Can be dyed any color");
		this.addItemWithDesc(RatsItemRegistry.PATROL_STICK, "Patrol Staff", "Use on a rat to bind it", "Interact with a block to set it as part of a rat's patrol route");
		this.addItemWithDesc(RatsItemRegistry.PIPER_HAT, "Piper Hat", "Wild rats will no longer fear you when worn");
		this.addItem(RatsItemRegistry.PIRAT_HAT, "Pirat Hat");
		this.addItemWithDesc(RatsItemRegistry.PLAGUE_DOCTOR_MASK, "Plague Doctor Mask", "Reduces chance of getting plague from rats on contact");
		this.addItemWithDesc(RatsItemRegistry.PLAGUE_DOCTORATE, "Plague Doctorate", "Transforms a villager into a Plague Doctor");
		this.addItemWithDesc(RatsItemRegistry.PLAGUE_ESSENCE, "Plague Essence", "Rare drop from Plague Rats");
		this.addItem(RatsItemRegistry.PLAGUE_LEECH, "Plague Leech");
		this.addItemWithDesc(RatsItemRegistry.PLAGUE_SCYTHE, "Black Death Scythe", "Summons Plague Clouds when swung");
		this.addItem(RatsItemRegistry.PLAGUE_STEW, "Plague Stew");
		this.addItemWithDesc(RatsItemRegistry.PLAGUE_TOME, "Plague Tome", "Very rare drop from Plague Rats", "Transforms a Plague Doctor into the Black Death");
		this.addItem(RatsItemRegistry.PLASTIC_WASTE, "Plastic Waste");
		this.addItem(RatsItemRegistry.POTATO_KNISHES, "Potato Knishes");
		this.addItem(RatsItemRegistry.POTATO_PANCAKE, "Potato Pancake");
		this.addItemWithDesc(RatsItemRegistry.PURIFYING_LIQUID, "Purifying Liquid", "Cures mobs with plague and zombie villagers");
		this.addItemWithDesc(RatsItemRegistry.RADIUS_STICK, "Rat Radius Staff", "Use on a rat to bind it", "Interact with a block to modify a rat's search and harvest range");
		this.addItem(RatsItemRegistry.RAT_ARROW, "Rat on Arrow");
		this.addItem(RatsItemRegistry.RAT_BREEDING_LANTERN, "Rat Breeding Lantern");
		this.addItemWithDesc(RatsItemRegistry.RAT_BURGER, "Rat Burger", "§o\"Just don't ask them where the meat comes from.\"");
		this.addItemWithDesc(RatsItemRegistry.RAT_CAPTURE_NET, "Rapture Net", "Rounds up all tamed rats within 16 blocks into one neat sack!");
		this.addItemWithDesc(RatsItemRegistry.RAT_FEZ, "Rat Fez", "Standard Headwear of the Sec-rat society...", "§mDefinitely not throwing shade at another mod");
		this.addItemWithBothDescs(RatsItemRegistry.RAT_FLUTE, "Rat Flute", "Used to command rats en-masse. Warning: will result in all nearby tamed rats to you stopping their task and following the set command. Shift + right-click to change the set command.", "Commands all tamed rats within the local area", "Sneak and right click to change selected command");
		for (int i = 0; i < DyeColor.values().length; i++) {
			String dye = WordUtils.capitalize(DyeColor.byId(i).getName().replace('_', ' '));
			this.addItem(RatsItemRegistry.RAT_HAMMOCKS[i], dye + " Rat Hammock");
			this.addItem(RatsItemRegistry.RAT_IGLOOS[i], dye + " Rat Igloo");
			this.addItem(RatsItemRegistry.RAT_TUBES[i], dye + " Rat Tube");
		}
		this.addItem(RatsItemRegistry.RAT_KING_CROWN, "Rat King's Crown");
		this.addItem(RatsItemRegistry.RAT_NUGGET, "Rat §o\"Nugget\"");
		this.addItemWithDesc(RatsItemRegistry.RAT_NUGGET_ORE, "Rat §o\"Nugget\"", "§oRight click to find out what's inside...");
		this.addItemWithDesc(RatsItemRegistry.RAT_PAPERS, "Rat Papers", "Used to transfer rats between owners", "Right click on rat to bind", "Right click on other player to give bounded rat to them");
		this.addItem(RatsItemRegistry.RAT_PAW, "Rat Paw");
		this.addItem(RatsItemRegistry.RAT_PELT, "Rat Pelt");
		this.addItem(RatsItemRegistry.RAT_SACK, "Rat Sack");
		this.addItem(RatsItemRegistry.RAT_SEED_BOWL, "Rat Seed Bowl");
		this.addItemWithDesc(RatsItemRegistry.RAT_SKULL, "Rat Skull", "Rare drop from rats");
		this.addItem(RatsItemRegistry.RAT_WATER_BOTTLE, "Rat Water Bottle");
		this.addItemWithDesc(RatsItemRegistry.RAT_WHEEL, "Rat Wheel", "Rats will spin the wheel and generate power");
		this.addItemWithDesc(RatsItemRegistry.RAT_WHISTLE, "Rat Whistle", "Sends all nearby tamed rats to their home position when applicable");
		this.addItemWithDesc(RatsItemRegistry.RATBOW_ESSENCE, "Ratbow Essence", "Dyes rats to a \"Rainbow\" color");
		this.addItem(RatsItemRegistry.RAW_PLASTIC, "Raw Plastic");
		this.addItem(RatsItemRegistry.RAW_RAT, "Raw Rat");
		this.addItem(RatsItemRegistry.SANTA_HAT, "Santa Hat");
		this.addItemWithDesc(RatsItemRegistry.STRING_CHEESE, "String Cheese", "§o\"A world without string is chaos.\"");
		this.addItem(RatsItemRegistry.TANGLED_RAT_TAILS, "Tangled Rat Tails");
		this.addItem(RatsItemRegistry.TINY_COIN, "Tiny Coin");
		this.addItem(RatsItemRegistry.TOKEN_FRAGMENT, "Mysterious Token Fragment");
		this.addItem(RatsItemRegistry.TOKEN_PIECE, "Mysterious Token Chunk");
		this.addItem(RatsItemRegistry.TOP_HAT, "Top Hat");
		this.addItem(RatsItemRegistry.TREACLE, "Old Treacle");
		this.addBannerPattern("rat", "Rat");
		this.addBannerPattern("cheese", "Cheese");
		this.addBannerPattern("rat_and_crossbones", "Rat and Crossbones");

		this.addItem(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY, "Rat Upgrade: Advanced Energy Transfer");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ANGEL, "Rat Upgrade: Angel", "Rat respawns a minute after death", "When rat dies, its ghost will appear nearby", "Ghost will return to life within a minute");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_AQUATIC, "Rat Upgrade: Aquatic", "Rat gains scales and the ability to swim adeptly", "Rat can breathe underwater", "Pretty much turns rats into fish");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT, "Rat Upgrade: Aristoc-RAT", "Rat drops Tiny Coins randomly or when killing enemies");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_ARMOR, "Rat Upgrade: Armor");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ASBESTOS, "Rat Upgrade: Asbesto-Rat", "Rat will not take damage from:", " -Fire", " -Lava", " -Magma");
		this.addItemWithBothDescs(RatsItemRegistry.RAT_UPGRADE_BASIC, "Rat Upgrade: Basic", "The most basic rat upgrade. While it has no effects in itself, it is a crafting ingredient required for advanced upgrades.", "Has no effects");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY, "Rat Upgrade: Basic Energy Transfer");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT, "Rat Upgrade: Basic Mount", "Has no effects");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT, "Rat Upgrade: Beast Mount", "§6Mount Upgrade§r§7: rat gains a rat beast mount", "Beast mount has 80 health and moves quickly");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BEE, "Rat Upgrade: Bee", "Rat gains the power of flight", "Rat gains poisonous attack", "Rat will randomly cause crops and plants to grow");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET, "Rat Upgrade: Mega-Bucket", "Rat will transfer fluids instead of items", "Rat will transfer 5000 mb at a time");
		this.addItemWithBothDescs(RatsItemRegistry.RAT_UPGRADE_BLACKLIST, "Rat Upgrade: Blacklist", "Rats will only pickup or hold items that are not on this list. To access it, click the upgrade and deposit the blacklisted items inside.", "Rats will only pickup/transfer items that are not on this list", "Right click to set items", "Contains:");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BOTTLER, "Rat Upgrade: Bottler", "Rat will pick up water or honey from nearby blocks when holding glass bottles and is set to the 'Harvest' command");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BOW, "Rat Upgrade: Bow", "Rats uses a bow in combat");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BREEDER, "Rat Upgrade: Breeder", "Rats will use any held food to breed animals when set to the 'Harvest' command");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_BUCKET, "Rat Upgrade: Bucket", "Rat will transfer fluids instead of items", "Rat will transfer 1000 mb at a time");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CARRAT, "Rat Upgrade: Carrat", "Right click rat when hungry to eat! (does not harm the rat)");
		this.addItemWithBothDescs(RatsItemRegistry.RAT_UPGRADE_CHEF, "Rat Upgrade: Chef", "Rats with the chef upgrade will cook any held item. Once the item is cooked, they will either try and drop it in an inventory below them, or simply throw it on the ground. This upgrade also gives the rat the skill set required to make a few select delicacies.", "Rat will cook held items", "§bUnlocks Special ability: Confit Byaldi");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT, "Rat Upgrade: Chicken Mount", "§6Mount Upgrade§r§7: rat gains a chicken mount", "Chicken mount moves quickly");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS, "Rat Upgrade: Festive", "§o\"Ho Ho Ho! Merry Christmas!\"", "Rat will gift a random present every hour", "Rat will hold gift or put it in chest below");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_COMBINED, "Rat Upgrade: Combined", "Combination of the following upgrades:");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE, "Rat Upgrade: Combined (Creative)", "§dRight click to add components");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CRAFTING, "Rat Upgrade: Crafting", "Rat will process crafting recipes while sitting on Rat Crafting Table", "§bUnlocks Special ability: Rat Crafting Table");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CREATIVE, "Rat Upgrade: Creative", "Rat cannot take damage except from owner");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_CROSSBOW, "Rat Upgrade: Crossbow", "Rats uses a crossbow in combat", "Crossbow is slower than bow, but has higher damage");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION, "Rat Upgrade: Damage Protection", "Rat will not take damage from:", " -Fire, Lava, Magma damage", " -Poison, Magic, Wither damage", " -Suffocation, Drowning, Fall damage");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_DEMON, "Rat Upgrade: Demon", "Rats is lava and fire proof", "Rats will ignite enemies");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER, "Rat Upgrade: Disenchanter", "Rat will disenchant any enchanted held item");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_DJ, "Rat Upgrade: DJ", "Rat will play held record");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_DRAGON, "Rat Upgrade: Dragon", "Rat can fly and breathe fire", "Rat will not take fire damage");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY, "Rat Upgrade: Elite Energy Transfer");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ENCHANTER, "Rat Upgrade: Enchanter", "Rat will enchant any held item", "Nearby bookshelves help improve level");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ENDER, "Rat Upgrade: Ender", "Rats gain the ability to teleport at will", "Allows extremely fast rat transport", "Turns rats black with glowing purple eyes");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY, "Rat Upgrade: Extreme Energy Transfer");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_FARMER, "Rat Upgrade: Planter", "Rats will plant any held seeds or saplings when set to the 'Harvest' command", "Rats will use bonemeal on crops and saplings as well");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_FISHERMAN, "Rat Upgrade: Fisherman", "Rats will move to water sources and start fishing", "Rats can catch fish, junk and treasure items");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_FLIGHT, "Rat Upgrade: Flight", "Rats gain the ability to flap rudimentary wings and fly", "Rats will move faster while flying and will not be bound the the constraints of land based travel", "Pretty much turns rats into pigeons");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_GARDENER, "Rat Upgrade: Gardener", "Rats will break nearby tall grass and flowers when set to the 'Harvest' command");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_GOD, "Rat Upgrade: Battle-God");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT, "Rat Upgrade: Golem Mount", "§6Mount Upgrade§r§7: rat gains a golem mount", "Golem mount has 100 health");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_HEALTH, "Rat Upgrade: Health");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_IDOL, "Rat Upgrade: Idol", "Rat gains golden appearance", "Piglins will not target or attack this rat nor will they target you if the rat is sitting on you");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED, "Rat Upgrade: Jury-Rigged", "Combine two rat upgrades", "Combined upgrades cannot be edited or removed");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK, "Rat Upgrade: Lumberjack", "Rats will fell trees when set to the 'Harvest' command", "If a rat has the 'Replanter' upgrade, it will plant saplings back where the tree used to be");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_MILKER, "Rat Upgrade: Milkmaid", "Rat will milk cows when set to Harvest", "Rat will transfer fluids instead of items", "Rat will transfer 1000 mb at a time");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_MOB_FILTER, "Rat Upgrade: Mob Filter", "Rats will only target mobs that are on this list", "Right click to open selection GUI");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE, "Rat Upgrade: Flute Denier", "Rat will not listen to flute commands");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING, "Rat Upgrade: Ore Doubling", "Rat will eat ore blocks and create §o\"nuggets\"", "§o\"nuggets\"§r§7 can then be right clicked for associated ore item", "Rat will not deposit any held ore block into chest, will eat it instead", "Easy to automate");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_PICKPOCKET, "Rat Upgrade: Pickpocket", "Rats will randomly take one of a Villager's trade results when set to the 'Harvest' command.", "Pickpocketing will sometimes result in your reputation going down or Iron Golems targeting your rat");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_PLACER, "Rat Upgrade: Block Placer", "Rats will place any held blocks when set to the 'Harvest' command", "Rats will place blocks at its 'Home' Position");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_PLATTER, "Rat Upgrade: Platter", "§bRat will transfer entire stacks of items at a time");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_POISON, "Rat Upgrade: Poison Resistance", "Rat will not take damage from:", " -Poison", " -Magic", " -Wither");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_QUARRY, "Rat Upgrade: Quarry", "Rats digs a quarry when set to 'Harvest' command", "Set rat to deposit in Rat Quarry block");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_REPLANTER, "Rat Upgrade: Replanter", "Rat does not destroy crops when harvesting them");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_SCULKED, "Rat Upgrade: Sculked", "Rat gains sculk-like appearance", "Rats will emit no vibrations when performing tasks and cannot be detected by Wardens");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_SHEARS, "Rat Upgrade: Shears", "Rat will shear sheep, mooshrooms, etc. when set to 'Harvest' command", "Rats will collect honeycomb from nearby bee nests and hives");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_SPEED, "Rat Upgrade: Speed");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_STRENGTH, "Rat Upgrade: Strength");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT, "Rat Upgrade: Strider Mount", "§6Mount Upgrade§r§7: rat gains a strider mount", "Strider mount has 20 health and can walk on lava");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_SUPPORT, "Rat Upgrade: Supporter", "Rat will throw healing potions when your health is low", "Rat may occasionally throw other potion buffs if you have full health");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR, "Rat Upgrade: Tick Accele-RAT-or", "Rat will slightly increase tick speed of any block it stands over or under", "Includes crops and machines");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_TIME_MANIPULATOR, "Rat Upgrade: Time Manipu-Rat-or", "Rat will greatly increase tick speed of any block in a 5x3x5 area around it", "Includes crops and machines");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_TNT, "Rat Upgrade: TNT", "Rat will explode upon attacking", "May kill rat and injure any nearby entities");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR, "Rat Upgrade: TNT Expert", "Rat will explode upon attacking", "Will not damage rat or allies directly", "Rat will not take damage from explosions or falling");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_UNDEAD, "Rat Upgrade: Undead", "Rat gains skeletal appearance", "Rat cannot be targeted by monsters");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_UNDERWATER, "Rat Upgrade: Extra Breath", "Rat will not take damage from:", " -Suffocation", " -Drowning");
		this.addItemWithDesc(RatsItemRegistry.RAT_UPGRADE_VOODOO, "Rat Upgrade: Voodoo", "Rat takes damage instead of its owner, only when within a certain distance", "Damage is evenly distributed amongst multiple rats with this upgrade");
		this.addItem(RatsItemRegistry.RAT_UPGRADE_WARRIOR, "Rat Upgrade: Warrior");
		this.addItemWithBothDescs(RatsItemRegistry.RAT_UPGRADE_WHITELIST, "Rat Upgrade: Whitelist", "Rats will only pickup or hold items that are on this list. To access it, click the upgrade and deposit the whitelisted items inside.", "Rats will only pickup/transfer items that are on this list", "Right click to set items", "Contains:");

		//Subtitles
		this.addSubtitle(RatsSoundRegistry.AIR_RAID_SIREN, "Air-Raid Siren wails");
		this.addSubtitle(RatsSoundRegistry.BIPLANE_DEATH, "Biplane breaks down");
		this.addSubtitle(RatsSoundRegistry.BIPLANE_HURT, "Biplane cracks");
		this.addSubtitle(RatsSoundRegistry.BIPLANE_LOOP, "Biplane buzzes");
		this.addSubtitle(RatsSoundRegistry.BIPLANE_SHOOT, "Biplane fires");
		this.addSubtitle(RatsSoundRegistry.BLACK_DEATH_IDLE, "Black Death rasps");
		this.addSubtitle(RatsSoundRegistry.BLACK_DEATH_DIE, "Black Death dissipates");
		this.addSubtitle(RatsSoundRegistry.BLACK_DEATH_HURT, "Black Death screams");
		this.addSubtitle(RatsSoundRegistry.BLACK_DEATH_SUMMON, "Plague Doctor transforms");
		this.addSubtitle(RatsSoundRegistry.BLUE_CHEESE_MADE, "Cheese molds");
		this.addSubtitle(RatsSoundRegistry.CHEESE_MADE, "Milk Curdles");
		this.addSubtitle(RatsSoundRegistry.CHEESE_CAULDRON_EMPTY, "Cauldron empties");
		this.addSubtitle(RatsSoundRegistry.NETHER_CHEESE_MADE, "Cheese burns");
		this.addSubtitle(RatsSoundRegistry.DUTCHRAT_IDLE, "Flying Dutchrat Laughs");
		this.addSubtitle(RatsSoundRegistry.DUTCHRAT_DIE, "Flying Dutchrat croaks");
		this.addSubtitle(RatsSoundRegistry.DUTCHRAT_HURT, "Flying Dutchrat groans");
		this.addSubtitle(RatsSoundRegistry.DUTCHRAT_LAUGH, "Flying Dutchrat bellows");
		this.addSubtitle(RatsSoundRegistry.DUTCHRAT_BELL, "Flying Dutchrat Bell rings");
		this.addSubtitle(RatsSoundRegistry.DYE_SPONGE_USED, "Dye removed");
		this.addSubtitle(RatsSoundRegistry.LASER, "Laser shoots");
		this.addSubtitle(RatsSoundRegistry.NEORATLANTEAN_IDLE, "Neo-Ratlantean buzzes");
		this.addSubtitle(RatsSoundRegistry.NEORATLANTEAN_DIE, "Neo-Ratlantean dies");
		this.addSubtitle(RatsSoundRegistry.NEORATLANTEAN_HURT, "Neo-Ratlantean hurts");
		this.addSubtitle(RatsSoundRegistry.NEORATLANTEAN_LOOP, "Neo-Ratlantean hums");
		this.addSubtitle(RatsSoundRegistry.NEORATLANTEAN_SUMMON, "Neo-Ratlantean summoned");
		this.addSubtitle(RatsSoundRegistry.PIED_PIPER_DEATH, "Pied Piper dies");
		this.addSubtitle(RatsSoundRegistry.PIED_PIPER_HURT, "Pied Piper hurts");
		this.addSubtitle(RatsSoundRegistry.PIPER_LOOP, "Pied Piper plays flute");
		this.addSubtitle(RatsSoundRegistry.PIRAT_SHOOT, "Pirat Cannon fires");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_CLOUD_DEATH, "Plague Cloud dies");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_CLOUD_HURT, "Plague Cloud hurts");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_CLOUD_SHOOT, "Plague Scythe spreads plague");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_DISAPPEAR, "Plague Doctor vanishes");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_DRINK, "Plague Doctor gulps");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_DRINK_POTION, "Plague Doctor sips");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_REAPPEAR, "Plague Doctor reappears");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_SUMMON, "Villager transforms");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_DOCTOR_THROW, "Plague Doctor throws vial");
		this.addSubtitle(RatsSoundRegistry.PLAGUE_SPREAD, "Plague spreads");
		this.addSubtitle(RatsSoundRegistry.POTION_EFFECT_BEGIN, "Nostalgia starts");
		this.addSubtitle(RatsSoundRegistry.POTION_EFFECT_END, "Nostalgia fades");
		this.addSubtitle(RatsSoundRegistry.RAT_IDLE, "Rat squeaks");
		this.addSubtitle(RatsSoundRegistry.RAT_MAKE_COIN, "Rat drops coin");
		this.addSubtitle(RatsSoundRegistry.RAT_CRAFT, "Rat crafts");
		this.addSubtitle(RatsSoundRegistry.RAT_DIE, "Rat dies");
		this.addSubtitle(RatsSoundRegistry.RAT_DIG, "Rat scratches");
		this.addSubtitle(RatsSoundRegistry.RAT_DRINK, "Rat drinks");
		this.addSubtitle(RatsSoundRegistry.RAT_EAT, "Rat eats");
		this.addSubtitle(RatsSoundRegistry.RAT_GROWL, "Plague Rat screeches");
		this.addSubtitle(RatsSoundRegistry.RAT_HURT, "Rat squeaks in pain");
		this.addSubtitle(RatsSoundRegistry.RAT_POOP, "Rat farts");
		this.addSubtitle(RatsSoundRegistry.RAT_PIRATE, "Rat aaarrrs");
		this.addSubtitle(RatsSoundRegistry.RAT_SANTA, "Rat bellows");
		this.addSubtitle(RatsSoundRegistry.RAT_SHOOT, "Rat shoots fire");
		this.addSubtitle(RatsSoundRegistry.RAT_TELEPORT, "Rat teleports");
		this.addSubtitle(RatsSoundRegistry.RAT_TRANSFER, "Rat changes owner");
		this.addSubtitle(RatsSoundRegistry.RAT_BEAST_GROWL, "Rat Beast growls");
		this.addSubtitle(RatsSoundRegistry.RAT_FLUTE, "Rat Flute blows");
		this.addSubtitle(RatsSoundRegistry.RAT_KING_SHOOT, "Rat King throws rat");
		this.addSubtitle(RatsSoundRegistry.RAT_KING_SUMMON, "Rat gets dirty");
		this.addSubtitle(RatsSoundRegistry.RAT_NET_THROW, "Rapture Net thrown");
		this.addSubtitle(RatsSoundRegistry.RAT_NUGGET_ORE, "Rat Nugget yields treasure");
		this.addSubtitle(RatsSoundRegistry.RAT_TRAP_ADD_BAIT, "Rat Trap bait placed");
		this.addSubtitle(RatsSoundRegistry.RAT_TRAP_CLOSE, "Rat Trap slams shut");
		this.addSubtitle(RatsSoundRegistry.RAT_TRAP_OPEN, "Rat Trap opens");
		this.addSubtitle(RatsSoundRegistry.RAT_TRAP_REMOVE_BAIT, "Rat Trap bait removed");
		this.addSubtitle(RatsSoundRegistry.RAT_WHISTLE, "Rat Whistle Tweets");
		this.addSubtitle(RatsSoundRegistry.ESSENCE_APPLIED, "Rat gets dyed");
		this.addSubtitle(RatsSoundRegistry.RATFISH_DEATH, "Ratfish dies");
		this.addSubtitle(RatsSoundRegistry.RATFISH_FLOP, "Ratfish flops");
		this.addSubtitle(RatsSoundRegistry.RATFISH_HURT, "Ratfish hurts");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_AUTOMATON_IDLE, "Ratlantean Automaton bellows");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_AUTOMATON_DIE, "Ratlantean Automaton dies");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_AUTOMATON_HURT, "Ratlantean Automaton damages");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_FLAME_SHOOT, "Ratlantean Flame shoots");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_RATBOT_IDLE, "Ratlantean Ratbot beeps");
		this.addSubtitle(RatsSoundRegistry.RATBOT_DEATH, "Ratlantean Ratbot dies");
		this.addSubtitle(RatsSoundRegistry.RATBOT_HURT, "Ratlantean Ratbot hurts");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_SPIRIT_IDLE, "Ratlantean Spirit wails");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE, "Ratlantean Spirit dies");
		this.addSubtitle(RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT, "Ratlantean Spirit hurts");
		this.addSubtitle(RatsSoundRegistry.RATTLING_GUN_SHOOT, "Rattling Gun fires");
		this.addSubtitle(RatsSoundRegistry.TRASH_CAN_EMPTY, "Trash Can empties");
		this.addSubtitle(RatsSoundRegistry.TRASH_CAN_FILL, "Trash Can fills");
		this.addSubtitle(RatsSoundRegistry.TRASH_CAN, "Trash Can lid swings");


		//Containers
		this.add(RatsLangConstants.CURDLER, "Curdling Station");
		this.add(RatsLangConstants.CURDLER_MB, "Mb");
		this.add(RatsLangConstants.RAT_CRAFTING_TABLE, "Rat Crafting Table");
		this.add(RatsLangConstants.CRAFTING_INPUT, "Input");
		this.add(RatsLangConstants.CRAFTING_NEEDS_RAT, "Requires a rat with a crafting upgrade above the table to function");
		this.add(RatsLangConstants.QUARRY, "Rat Quarry");
		this.add(RatsLangConstants.UPGRADE_COMBINER, "Rat Upgrade Combiner");
		this.add(RatsLangConstants.COMBINER_CANNOT_COMBINE, "Cannot combine these upgrades");
		this.add(RatsLangConstants.MOB_FILTER, "Mob Filter");
		this.add(RatsLangConstants.FILTER_WHITELIST, "Toggle Whitelist");
		this.add(RatsLangConstants.FILTER_SELECTED_MOBS, "Show Selected Mobs");
		this.add(RatsLangConstants.ARCHEOLOGIST_JEI, "Archeologist Rat");
		this.add(RatsLangConstants.CHEESEMAKING_JEI, "Cheesemaking");
		this.add(RatsLangConstants.CHEF_JEI, "Chef Rat");
		this.add(RatsLangConstants.GEMCUTTER_JEI, "Gemcutter Rat");

		//staff messages
		this.add(RatsLangConstants.RAT_STAFF_BIND, "Staff is now bound to %s");
		this.add(RatsLangConstants.RAT_STAFF_NO_RAT, "Staff is not bound to any rat");
		this.add(RatsLangConstants.RAT_STAFF_DEPOSIT_POS, "Deposit items in %s from the %s");
		this.add(RatsLangConstants.RAT_STAFF_PICKUP_POS, "Take items from %s");
		this.add(RatsLangConstants.RAT_STAFF_SET_HOME, "Set home point for rat to %s");
		this.add(RatsLangConstants.RAT_STAFF_REMOVE_TRANSPORT_POS, "Reset rat transport positions");
		this.add(RatsLangConstants.RAT_STAFF_REMOVE_HOME, "Reset rat home point");
		this.add(RatsLangConstants.RAT_STAFF_RADIUS, "Radius: %s");
		this.add(RatsLangConstants.RAT_STAFF_RESET_RADIUS, "Reset Radius");
		this.add(RatsLangConstants.RAT_STAFF_SET_RADIUS, "Set rat radius origin to %s");
		this.add(RatsLangConstants.RAT_STAFF_ADD_NODE, "Add patrol node at %s");
		this.add(RatsLangConstants.RAT_STAFF_REMOVE_NODE, "Remove patrol node at %s");
		this.add(RatsLangConstants.RAT_STAFF_REMOVE_NODES, "Remove all patrol nodes");

		//rat commands
		this.add(RatsLangConstants.RAT_CURRENT_COMMAND, "Current Command: %s");
		this.add(RatsLangConstants.RAT_COMMAND_SET, "Set Command:");
		this.add("entity.rats.rat.command.wander", "Wander");
		this.add("entity.rats.rat.command.wander.desc", "The rat will move around similar to wild behavior.");
		this.add("entity.rats.rat.command.sit", "Stay Here");
		this.add("entity.rats.rat.command.sit.desc", "The rat will sit down and not move.");
		this.add("entity.rats.rat.command.follow", "Follow Me");
		this.add("entity.rats.rat.command.follow.desc", "The rat will follow its owner.");
		this.add("entity.rats.rat.command.hunt", "Hunt Mobs");
		this.add("entity.rats.rat.command.hunt.desc", "When given a Mob Filter upgrade, the rat will kill all mobs it lists. The rat will also place their remains in a deposit container, if assigned one. Use the §6Cheese Staff§r to set a deposit inventory.");
		this.add("entity.rats.rat.command.gather", "Gather Items");
		this.add("entity.rats.rat.command.gather.desc", "The rat will search the area for dropped items (craft whitelist or blacklist upgrades to limit what they pick up). You can use a §6Cheese Staff§r to set a deposit inventory.");
		this.add("entity.rats.rat.command.harvest", "Harvest");
		this.add("entity.rats.rat.command.harvest.desc", "The rat will search for mature crops nearby to harvest. You can use a §6Cheese Staff§r to set a deposit inventory where it will drop off any crops it collects. Many upgrades will repurpose this command to allow rats to perform various jobs, such as chopping down trees or breeding animals.");
		this.add("entity.rats.rat.command.transport", "Transport Items");
		this.add("entity.rats.rat.command.transport.desc", "Interacting with a rat with a §6Cheese Staff§r will bind it to the rat. Clicking on a block with an inventory will open a GUI to mark that inventory for the rat to take from/deposit items into. Once the rat has a marked deposit inventory and pickup inventory, it will transport items from one to the other.");
		this.add("entity.rats.rat.command.patrol", "Patrol");
		this.add("entity.rats.rat.command.patrol.desc", "The rat will patrol a set path and fight any monsters it encounters. Right-click the rat with a §6Patrol Staff§r to bind it and right-click any blocks with the staff to add points to patrol along.");

		//item tooltips
		this.add(RatsLangConstants.CAGE_DEPOSIT, "Deposited %s Rat(s) in cage");
		this.add(RatsLangConstants.CAGE_WITHDRAW, "Released %s Rat(s) from cage");
		this.add(RatsLangConstants.CAGE_DECORATION, "Rat Cage decoration");
		this.add(RatsLangConstants.CHEESE_STAFF_SELECTED, "Selected Rat: %s (%s)");
		this.add(RatsLangConstants.PLAGUE_HEAL_CHANCE, "Has a %s percent chance to remove plague effect");
		this.add(RatsLangConstants.RAT_FLUTE_COMMAND, "Changed command to: %s");
		this.add(RatsLangConstants.RAT_FLUTE_COUNT, "Commanded %s rats");
		this.add(RatsLangConstants.RAT_PAPERS_BOUND_RAT, "Bound Rat: %s");
		this.add(RatsLangConstants.AND_MORE, "... and %s more");
		this.add(RatsLangConstants.RAT_SACK_CONTAINED_RATS, "Contains %s/%s rat(s)");
		this.add(RatsLangConstants.RAT_SACK_RELEASED_RATS, "Released %s Rat(s) from sack");
		this.add(RatsLangConstants.RAT_SACK_TOO_FULL, "Rat Sack can't hold any more rats!");

		this.add(RatsLangConstants.RAT_UPGRADE_REGENS, "Extremely fast health regeneration");
		this.add(RatsLangConstants.RAT_UPGRADE_STAT_BOOST, "+%s Rat %s");
		this.add(RatsLangConstants.MOUNT_RESPAWN_TIMER, "Cooldown on mount respawning when slain");
		this.add(RatsLangConstants.RAT_UPGRADE_ENERGY_DESC0, "Rat will transfer energy instead of items");
		this.add(RatsLangConstants.RAT_UPGRADE_ENERGY_DESC1, "Works with RF and RE");
		this.add(RatsLangConstants.RAT_UPGRADE_ENERGY_TRANSFER, "Max transfer rate: %s kRF");
		this.add(RatsLangConstants.MOB_FILTER_BLACKLIST, "Blacklist");
		this.add(RatsLangConstants.MOB_FILTER_MODE, "Mode: %s");
		this.add(RatsLangConstants.MOB_FILTER_SELECTED_MOBS, "Selected Mobs:");
		this.add(RatsLangConstants.MOB_FILTER_WHITELIST, "Whitelist");
		this.add("item.rats.ratlantis_armor.desc0", "Protector of Ratlantis");
		this.add("item.rats.ratlantis_armor.desc1", "A spectral rat will protect you at all times, and keep enemies at bay.");
		this.add("item.rats.ratlantis_armor.desc2", "Effect stacks for all pieces of armor.");
		this.add("item.rats.ratlantis_disabled.desc0", "Ratlantis content is disabled in this world.");
		this.add("item.rats.ratlantis_disabled.desc1", "None of the items, blocks, or entities from ratlantis can be obtained legitimately in survival.");
		this.add("item.rats.ratlantis_disabled.desc2", "f you would like to have Ratlantis related content in your world make sure to enable the builtin datapack on world creation or turn the 'ratlantisEnabledByDefault' config option to true.");

		//Misc
		this.addEffect(RatsEffectRegistry.PLAGUE, "Plague");
		this.addEffect(RatsEffectRegistry.SYNESTHESIA, "Synesthesia");
		this.add("gamerule.doPiperSpawning", "Spawn Pied Pipers");
		this.add("gamerule.doPlagueDoctorSpawning", "Spawn Wandering Plague Doctors");
		this.add("gamerule.doRatSpawning", "Spawn Wild Rats");
		this.add("entity.minecraft.villager.rats.pet_shop_owner", "Pet Shop Owner");
		this.add(RatsLangConstants.RATS_TAB, "Rats");
		this.add(RatsLangConstants.RAT_UPGRADE_TAB, "Rat Upgrades");
		this.add(RatsLangConstants.RATLANTIS_TAB, "Ratlantis");
		this.add(RatsLangConstants.RAT_ANGEL_RESPAWN, "%s respawned as an angel");
		this.add(RatsLangConstants.RAT_DISMOUNT, "Sneak and punch air to remove rats from shoulder");
		this.add(RatsLangConstants.MOB_GRIEFING_WARNING, "NOTICE: this world has Mob Griefing turned off. Rats may not work as expected! Tamed rats will not be able to perform many tasks you would normally expect them to because of this.\\nTo fix this, all you need to do is enable the mob griefing gamerule.\\nThis message will not appear again.");
		this.add("rats.direction.down", "bottom");
		this.add("rats.direction.east", "east");
		this.add("rats.direction.north", "north");
		this.add("rats.direction.south", "south");
		this.add("rats.direction.up", "top");
		this.add("rats.direction.west", "west");
		this.add(RatsLangConstants.RATS_PACK, "Rats default resources");
		this.add(RatsLangConstants.RATLANTIS_PACK, "Enables Ratlantis and all Ratlantis related features");
		this.add("trim_material.rats.gem_of_ratlantis", "Gem of Ratlantis Material");
		this.add("trim_material.rats.oratchalcum", "Oratchalcum Material");

		// ***************************** //
		// --------- RATLANTIS --------- //
		// ***************************** //
		this.addRatlantisAdvancement("root", "Ratlantis", "An Empire Lost");
		this.addRatlantisAdvancement("token", "What lottery? The lottery, that's what lottery! Are you stupid? Only lottery that matters! Oh my god smell that air!", "Obtain a Chunky Cheese Token from a rat! Good job!");
		this.addRatlantisAdvancement("ratlantis", "Ratlantis!", "Use Chunky Cheese Token anywhere to create a portal to Ratlantis. Enter through the portal!");
		this.addRatlantisAdvancement("rat_upgrade_archeologist", "Ulterior Motive", "Craft Rat Upgrade: Archeologist, enabling an alternative to Ratlantis");
		this.addRatlantisAdvancement("ratglove_petals", "Flower Power", "Craft Ratglove Petals from nine Ratglove Flowers");
		this.addRatlantisAdvancement("ratlantean_spirit", "Ashes of a Trillion Dead Souls", "Encounter the long dead ghost of a Ratlantean");
		this.addRatlantisAdvancement("feral_ratlantean", "Going Native", "Encounter what now remains of Ratlantis' once-noble denizens");
		this.addRatlantisAdvancement("gem_of_ratlantis", "Closer to Godliness", "Craft the Gem of Ratlantis from Ratglove Petals and Emeralds");
		this.addRatlantisAdvancement("marbled_cheese_golem_core", "We can Rebuild...", "Craft a Ratlantean Automaton Core! Now, place a Ratlantean Automaton Head on top and surround the core with Marbled Cheese!");
		this.addRatlantisAdvancement("marbled_cheese_golem", "Man(or Rat) vs Machine", "Kill a Ratlantean Automaton");
		this.addRatlantisAdvancement("rat_upgrade_ratinator", "Like Tears in the Rain", "Craft a Rat Upgrade: Ratinator, bringing a deadly rat cyborg to life");
		this.addRatlantisAdvancement("upgrade_combiner", "MAXIMUM POWER!!!", "Craft a Rat Upgrade Combiner, and start mixing upgrades together");
		this.addRatlantisAdvancement("vial_of_sentience", "The Risks I Took Were Calculated...", "Create a Vial of Consciousness with Ratlantean Spirit Flame and Feral Rat Claws. Make sure to throw it at a Feral Ratlantean!");
		this.addRatlantisAdvancement("neoratlantean", "But Man am I Bad at Math", "Kill the Neo-Ratlantean");
		this.addRatlantisAdvancement("rat_upgrade_psychic", "Not a USB Stick", "Craft a Rat Upgrade: Psychic, and have your very own psychic rat");
		this.addRatlantisAdvancement("pirat", "Are You Ready Kids?", "Encounter the Pirat, a swashbuckling, sea-faring rat");
		this.addRatlantisAdvancement("rat_upgrade_buccaneer", "Aye-Aye Captain!", "Craft a Rat Upgrade: Buccaneer, giving unnecessary 16th century firepower to a rat");
		this.addRatlantisAdvancement("rat_upgrade_nonbeliever", "Whats a God to a Nonbeliever?", "Craft a Rat Upgrade: Nonbeliever, the one rat upgrade to rule them all");
		this.addRatlantisAdvancement("ghost_pirat", "Shiver Me Timbers", "Encounter a Ghost Pirat, the cursed spirits of long dead sailors");
		this.addRatlantisAdvancement("dutchrat_wheel", "LEDLE LEDLE LEDLE LEEEE", "Defeat the Flying Dutchrat");
		this.addRatlantisAdvancement("ratlantean_ratbot", "The Stainless Steel Rat", "Encounter a Ratlantean Ratbot, a relic from a past age of automation");
		this.addRatlantisAdvancement("oratchalcum_ingot", "Feel The Power!", "Obtain an Oratchalcum Ingot");
		this.addRatlantisAdvancement("ratlantis_armor", "The Gilded God!", "Obtain an the entire Ratlantis armor set");
		this.addRatlantisAdvancement("defeat_rat_baron", "Death or Glory", "Defeat the Rat Baron");
		this.addRatlantisAdvancement("rat_upgrade_mount_biplane", "How-Rat Hughes", "Craft a Rat Upgrade: Biplane Mount and get your very own Rat Flying Ace");

		//Blocks
		this.add(RatlantisBlockRegistry.AIR_RAID_SIREN.get(), "Air-Raid Siren");
		this.add(RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get(), "Black Marbled Cheese");
		this.add(RatlantisBlockRegistry.BRAIN_BLOCK.get(), "Brain Block");
		this.add(RatlantisBlockRegistry.CHEESE_ORE.get(), "Cheese Ore");
		this.addBlockWithDesc(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN, "Chunky Cheese Token", "EXTREMELY rare drop rate from rat", "Right click anywhere to open a portal to Ratlantis");
		this.addBlockWithDesc(RatlantisBlockRegistry.COMPRESSED_RAT, "Block of Ratglove", "§o\"In times like these we are thankful, but we must temper our thirst for our pursuit bears wondrous fruit, some ideas must rot where they fall.\"");
		this.add(RatlantisBlockRegistry.DUTCHRAT_BELL.get(), "Flying Dutchrat's Bell");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE.get(), "Marbled Cheese");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get(), "Marbled Cheese Slab");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get(), "Marbled Cheese Stairs");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get(), "Marbled Cheese Brick");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get(), "Marbled Cheese Brick Slab");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get(), "Marbled Cheese Brick Stairs");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get(), "Cracked Marbled Cheese Brick");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get(), "Cracked Marbled Cheese Brick Slab");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get(), "Cracked Marbled Cheese Brick Stairs");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get(), "Mossy Marbled Cheese Brick");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get(), "Mossy Marbled Cheese Brick Slab");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get(), "Mossy Marbled Cheese Brick Stairs");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get(), "Chiseled Marbled Cheese");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get(), "Chiseled Marbled Cheese Brick");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get(), "Marbled Cheese Pillar");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get(), "Marbled Cheese Tile");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get(), "Marbled Cheese Dirt");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get(), "Marbled Cheese Grass");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get(), "Ratlantean Automaton Core");
		this.add(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get(), "Ratlantean Automaton Head");
		this.add(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get(), "Block of Oratchalcum");
		this.add(RatlantisBlockRegistry.ORATCHALCUM_ORE.get(), "Oratchalcum Ore");
		this.createWoodSet("pirat", "Ghost Pirat");
		this.add(RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER.get(), "Potted Ratglove Flower");
		this.add(RatlantisBlockRegistry.RATGLOVE_FLOWER.get(), "Ratglove Flower");
		this.add(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get(), "Ratlantean Gem Ore");
		this.add(RatlantisBlockRegistry.RATLANTIS_PORTAL.get(), "Ratlantis Portal");
		this.add(RatlantisBlockRegistry.RATLANTIS_REACTOR.get(), "Ratlantis Reactor");
		this.add(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get(), "Ratlantean Upgrade Block");

		//Entities
		this.addEntityAndEgg(RatlantisEntityRegistry.DUTCHRAT, "Flying Dutchrat");
		this.addEntityAndEgg(RatlantisEntityRegistry.FERAL_RATLANTEAN, "Feral Ratlantean");
		this.addEntityAndEgg(RatlantisEntityRegistry.GHOST_PIRAT, "Ghost Pirat");
		this.addEntityAndEgg(RatlantisEntityRegistry.NEO_RATLANTEAN, "Neo-Ratlantean");
		this.addEntityAndEgg(RatlantisEntityRegistry.PIRAT, "Pirat");
		this.addEntityAndEgg(RatlantisEntityRegistry.RAT_BARON, "Rat Baron");
		this.addEntityAndEgg(RatlantisEntityRegistry.RATFISH, "Ratfish");
		this.addEntityAndEgg(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON, "Ratlantean Automaton");
		this.addEntityAndEgg(RatlantisEntityRegistry.RATLANTEAN_RATBOT, "Ratlantean Ratbot");
		this.addEntityAndEgg(RatlantisEntityRegistry.RATLANTEAN_SPIRIT, "Ratlantean Spirit");

		this.addEntityType(RatlantisEntityRegistry.BOAT, "Boat");
		this.addEntityType(RatlantisEntityRegistry.CHEESE_CANNONBALL, "Cheese Cannonball");
		this.addEntityType(RatlantisEntityRegistry.CHEST_BOAT, "Chest Boat");
		this.addEntityType(RatlantisEntityRegistry.DUTCHRAT_SWORD, "Thrown Ghost Sword");
		this.addEntityType(RatlantisEntityRegistry.LASER_BEAM, "Laser");
		this.addEntityType(RatlantisEntityRegistry.LASER_PORTAL, "Telekinetic Portal");
		this.addEntityType(RatlantisEntityRegistry.PIRAT_BOAT, "Pirat Boat");
		this.addEntityType(RatlantisEntityRegistry.RAT_BARON_PLANE, "Rat Baron's Plane");
		this.addEntityType(RatlantisEntityRegistry.RAT_MOUNT_AUTOMATON, "Rat Automaton Mount");
		this.addEntityType(RatlantisEntityRegistry.RAT_MOUNT_BIPLANE, "Rat Biplane Mount");
		this.addEntityType(RatlantisEntityRegistry.RAT_PROTECTOR, "Rat Ghost");
		this.addEntityType(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM, "Laser");
		this.addEntityType(RatlantisEntityRegistry.RATLANTEAN_FLAME, "Soul Flame");
		this.addEntityType(RatlantisEntityRegistry.RATLANTIS_ARROW, "Arrow");
		this.addEntityType(RatlantisEntityRegistry.RATTLING_GUN, "Rattling Gun");
		this.addEntityType(RatlantisEntityRegistry.RATTLING_GUN_BULLET, "Bullet");
		this.addEntityType(RatsEntityRegistry.THROWN_BLOCK, "Telekinetic Block");
		this.addEntityType(RatlantisEntityRegistry.VIAL_OF_SENTIENCE, "Vial of Consciousness");

		//Items
		this.addItem(RatlantisItemRegistry.ANCIENT_SAWBLADE, "Ancient Sawblade");
		this.addItemWithDesc(RatlantisItemRegistry.ARCANE_TECHNOLOGY, "Arcane Technology", "§o\"Any sufficiently advanced technology is indistinguishable from magic.\"", "Who knows what magnificent marvels the ancient civilization of Ratlantis created?");
		this.addItem(RatlantisItemRegistry.AVIATOR_HAT, "Aviator Cap");
		this.addItem(RatlantisItemRegistry.BIPLANE_WING, "Biplane Wing");
		this.addItem(RatlantisItemRegistry.CHARGED_RATBOT_BARREL, "Charged Ratbot Barrel");
		this.addItemWithDesc(RatlantisItemRegistry.CHEESE_CANNONBALL, "Cheese Cannonball", "Fired from a Pirat cannon");
		this.addItem(RatlantisItemRegistry.DUTCHRAT_WHEEL, "Flying Dutchrat's Ship Wheel");
		this.addItem(RatlantisItemRegistry.FERAL_BAGH_NAKHS, "Feral Ratlantean Bagh-Nakhs");
		this.addItem(RatlantisItemRegistry.FERAL_RAT_CLAW, "Feral Rat Claw");
		this.addItem(RatlantisItemRegistry.GEM_OF_RATLANTIS, "Gem of Ratlantis");
		this.addItem(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS, "Ghost Pirat Cutlass");
		this.addItem(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM, "Ghost Pirat Ectoplasm");
		this.addItem(RatlantisItemRegistry.GHOST_PIRAT_HAT, "Ghost Pirat Hat");
		this.addItemWithDesc(RatlantisItemRegistry.IDOL_OF_RATLANTIS, "Avatar of Ratlantis", "Power incarnate");
		this.addItem(RatlantisItemRegistry.MILITARY_HAT, "Ratlantean Officer Hat");
		this.addItem(RatlantisItemRegistry.ORATCHALCUM_INGOT, "Oratchalcum Ingot");
		this.addItem(RatlantisItemRegistry.ORATCHALCUM_NUGGET, "Oratchalcum Nugget");
		this.addItem(RatlantisItemRegistry.PIRAT_CUTLASS, "Pirat Cutlass");
		this.addItemWithDesc(RatlantisItemRegistry.PSIONIC_RAT_BRAIN, "Psionic Rat Brain", "§o\"The inner machinations of my mind are an enigma.\"", "A powerful mind capable of tinkering with the very fabric of reality.");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_TOGA, "Rat Toga", "Right click on a rat to equip it with a toga", "Right click it again with a toga to de-equip");
		this.addItem(RatlantisItemRegistry.RATBOT_BARREL, "Ratbot Barrel");
		this.addItem(RatlantisItemRegistry.RATFISH, "Ratfish");
		this.addItem(RatlantisItemRegistry.RATFISH_BUCKET, "Bucket of Ratfish");
		this.addItem(RatlantisItemRegistry.RATGLOVE_PETALS, "Ratglove Petals");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTEAN_FLAME, "Ratlantean Spirit Flame", "Imbued with the ancient souls of the Ratlanteans... and Cheese.");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_AXE, "Axe of Ratlantis", "Ratlantean Leafbreaker", "Mining leaf blocks will not damage this tool.");
		this.addItem(RatlantisItemRegistry.RATLANTIS_BOOTS, "Boots of Ratlantis");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_BOW, "Bow of Ratlantis", "Ratlantean Quiver", "Fires stronger arrows twice as fast.");
		this.addItem(RatlantisItemRegistry.RATLANTIS_CHESTPLATE, "Chestplate of Ratlantis");
		this.addItem(RatlantisItemRegistry.RATLANTIS_HELMET, "Helmet of Ratlantis");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_HOE, "Hoe of Ratlantis", "Bounty of Ratlantis", "Tills an additional 8 dirt blocks.");
		this.addItem(RatlantisItemRegistry.RATLANTIS_LEGGINGS, "Leggings of Ratlantis");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_PICKAXE, "Pickaxe of Ratlantis", "Ratlantean Caver", "Mining stone blocks will not damage this tool.");
		this.addItem(RatlantisItemRegistry.RATLANTIS_RAT_SKULL, "Skull of Ratlantis");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_SHOVEL, "Shovel of Ratlantis", "Ratlantean Earthmower", "Mining sand blocks will not damage this tool.");
		this.addItemWithDesc(RatlantisItemRegistry.RATLANTIS_SWORD, "Sword of Ratlantis", "Spectral Alliance", "Each attack summons a spectral rat that deals an additional 6 damage.");
		this.addItemWithDesc(RatlantisItemRegistry.RATTLING_GUN, "Rattling Gun", "Place a rat on the gunner's seat by right clicking with a rat on your shoulder");
		this.addItem(RatlantisItemRegistry.RAW_ORATCHALCUM, "Raw Oratchalcum");
		this.addItemWithDesc(RatlantisItemRegistry.VIAL_OF_SENTIENCE, "Vial of Consciousness", "A serum that can, §lin theory§r§7, reverse the de-evolution of a Ratlantean.");
		this.addBannerPattern("rat_and_sickle", "Rat and Sickle");

		this.addItemWithBothDescs(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST, "Rat Upgrade: Archeologist", "Rats with the archeologist upgrade will research their held item, trying to discover Ratlantean artifacts from it. Once an artifact is discovered, they will either try and drop it in an inventory below them, or simply throw it on the ground.", "Researches held item, transforms it into Ratlantean artifact if possible");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT, "Rat Upgrade: Automaton Mount", "§6Mount Upgrade§r§7: rat gains an automaton mount", "Automaton mount has 250 health and moves slowly");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN, "Rat Upgrade: Basic Ratlantean", "Has no effects");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT, "Rat Upgrade: Biplane Mount", "§6Mount Upgrade§r§7: rat gains a biplane mount", "Biplane mount has 300 health and can fly");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER, "Rat Upgrade: Buccaneer", "Rat gains an automatic firing cannon", "§cCAUTION: EXPLOSIVE");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL, "Rat Upgrade: Ethereal", "Rat can phase through blocks", "Rat can float through the air");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE, "Rat Upgrade: Feral Bite", "Rat attack deals 5 extra damage, inflicts plague and poison debuff");
		this.addItem(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER, "Rat Upgrade: Nonbeliever");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC, "Rat Upgrade: Psychic", "Summons portals that attack targets");
		this.addItemWithDesc(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR, "Rat Upgrade: Ratinator", "Half Machine, Half Rodent.", "Fires lasers at targets");


		//Misc
		this.add("biome.rats.ratlantis", "Ratlantis");
		this.add("dimension.rats.ratlantis", "Ratlantis");
	}

	public void addMusicDisc(Supplier<Item> disc, String description) {
		this.addItem(disc, "Music Disc");
		this.add(disc.get().getDescriptionId() + ".desc", description);
	}

	public void addAdvancement(String key, String title, String desc) {
		this.add("advancement.rats." + key, title);
		this.add("advancement.rats." + key + ".desc", desc);
	}

	public void addBlockWithJEIDesc(Supplier<Block> block, String blockName, String jeiLine) {
		this.addBlock(block, blockName);
		this.add(block.get().getDescriptionId() + ".jei_desc", jeiLine);
	}

	public void addBlockWithDesc(Supplier<Block> block, String blockName, String... descLines) {
		this.addBlock(block, blockName);
		if (descLines.length == 1) {
			this.add(block.get().getDescriptionId() + ".desc", descLines[0]);
		} else {
			for (int i = 0; i < descLines.length; i++) {
				this.add(block.get().getDescriptionId() + ".desc" + i, descLines[i]);
			}
		}
	}

	public void addBlockWithBothDescs(Supplier<Block> block, String blockName, String jeiLine, String... descLines) {
		this.addBlock(block, blockName);
		this.add(block.get().getDescriptionId() + ".jei_desc", jeiLine);
		if (descLines.length == 1) {
			this.add(block.get().getDescriptionId() + ".desc", descLines[0]);
		} else {
			for (int i = 0; i < descLines.length; i++) {
				this.add(block.get().getDescriptionId() + ".desc" + i, descLines[i]);
			}
		}
	}

	public void addItemWithDesc(Supplier<Item> item, String itemName, String... descLines) {
		this.addItem(item, itemName);
		if (descLines.length == 1) {
			this.add(item.get().getDescriptionId() + ".desc", descLines[0]);
		} else {
			for (int i = 0; i < descLines.length; i++) {
				this.add(item.get().getDescriptionId() + ".desc" + i, descLines[i]);
			}
		}
	}

	public void addItemWithJEIDesc(Supplier<Item> item, String itemName, String jeiLine) {
		this.addItem(item, itemName);
		this.add(item.get().getDescriptionId() + ".jei_desc", jeiLine);
	}

	public void addItemWithBothDescs(Supplier<Item> item, String itemName, String jeiLine, String... descLines) {
		this.addItem(item, itemName);
		this.add(item.get().getDescriptionId() + ".jei_desc", jeiLine);
		if (descLines.length == 1) {
			this.add(item.get().getDescriptionId() + ".desc", descLines[0]);
		} else {
			for (int i = 0; i < descLines.length; i++) {
				this.add(item.get().getDescriptionId() + ".desc" + i, descLines[i]);
			}
		}
	}

	public void addRatlantisAdvancement(String key, String title, String desc) {
		this.add("advancement.ratlantis." + key + ".title", title);
		this.add("advancement.ratlantis." + key + ".desc", desc);
	}

	public void createWoodSet(String woodPrefix, String woodName) {
		this.add("block.rats." + woodPrefix + "_log", woodName + " Log");
		this.add("block.rats." + woodPrefix + "_wood", woodName + " Wood");
		this.add("block.rats.stripped_" + woodPrefix + "_log", "Stripped " + woodName + " Log");
		this.add("block.rats.stripped_" + woodPrefix + "_wood", "Stripped " + woodName + " Wood");
		this.add("block.rats." + woodPrefix + "_planks", woodName + " Planks");
		this.add("block.rats." + woodPrefix + "_slab", woodName + " Slab");
		this.add("block.rats." + woodPrefix + "_stairs", woodName + " Stairs");
		this.add("block.rats." + woodPrefix + "_button", woodName + " Button");
		this.add("block.rats." + woodPrefix + "_fence", woodName + " Fence");
		this.add("block.rats." + woodPrefix + "_fence_gate", woodName + " Fence Gate");
		this.add("block.rats." + woodPrefix + "_pressure_plate", woodName + " Pressure Plate");
		this.add("block.rats." + woodPrefix + "_trapdoor", woodName + " Trapdoor");
		this.add("block.rats." + woodPrefix + "_door", woodName + " Door");
		this.add("block.rats." + woodPrefix + "_sign", woodName + " Sign");
		this.add("block.rats." + woodPrefix + "_wall_sign", woodName + " Wall Sign");
		this.add("item.rats." + woodPrefix + "_boat", woodName + " Boat");
		this.add("item.rats." + woodPrefix + "_chest_boat", woodName + " Chest Boat");
		this.add("block.rats." + woodPrefix + "_hanging_sign", woodName + " Hanging Sign");
		this.add("block.rats." + woodPrefix + "_wall_hanging_sign", woodName + " Wall Hanging Sign");
	}

	public void addBannerPattern(String patternPrefix, String patternName) {
		this.add("item.rats." + patternPrefix + "_banner_pattern", "Banner Pattern");
		this.add("item.rats." + patternPrefix + "_banner_pattern.desc", patternName);
		for (DyeColor color : DyeColor.values()) {
			this.add("block.minecraft.banner.rats." + patternPrefix + "." + color.getName(), WordUtils.capitalize(color.getName().replace('_', ' ')) + " " + patternName);
		}
	}

	public void addEntityAndEgg(RegistryObject<? extends EntityType<?>> entity, String name) {
		this.addEntityType(entity, name);
		this.add("item.rats." + entity.getId().getPath() + "_spawn_egg", name + " Spawn Egg");
	}

	public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
		String[] splitSoundName  = sound.getId().getPath().split("\\.", 3);
		this.add("subtitles.rats." + splitSoundName[0] + "." + splitSoundName[2], name);
	}
}
