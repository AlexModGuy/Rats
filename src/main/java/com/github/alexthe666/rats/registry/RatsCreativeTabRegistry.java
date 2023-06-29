package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.events.ModClientEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RatsCreativeTabRegistry {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RatsMod.MODID);

	public static final RegistryObject<CreativeModeTab> RATS = TABS.register("rats", () -> CreativeModeTab.builder()
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.rats.rats"))
			.icon(() -> new ItemStack(RatsItemRegistry.CHEESE.get()))
			.displayItems((parameters, output) -> {
				output.accept(RatsItemRegistry.CHEESE.get());
				output.accept(RatsItemRegistry.BLUE_CHEESE.get());
				output.accept(RatsItemRegistry.NETHER_CHEESE.get());
				output.accept(RatsBlockRegistry.BLOCK_OF_CHEESE.get());
				output.accept(RatsBlockRegistry.BLOCK_OF_BLUE_CHEESE.get());
				output.accept(RatsBlockRegistry.BLOCK_OF_NETHER_CHEESE.get());
				output.accept(RatsBlockRegistry.RAT_TRAP.get());
				output.accept(RatsBlockRegistry.AUTO_CURDLER.get());
				output.accept(RatsBlockRegistry.RAT_CRAFTING_TABLE.get());

				output.accept(RatsItemRegistry.RAW_RAT.get());
				output.accept(RatsItemRegistry.COOKED_RAT.get());
				output.accept(RatsItemRegistry.RAT_PELT.get());
				output.accept(RatsItemRegistry.RAT_PAW.get());
				output.accept(RatsItemRegistry.RAT_SKULL.get());
				output.accept(RatsItemRegistry.GOLDEN_RAT_SKULL.get());
				output.accept(RatsItemRegistry.CORRUPT_RAT_SKULL.get());
				output.accept(RatsItemRegistry.TANGLED_RAT_TAILS.get());
				output.accept(RatsItemRegistry.RAT_ARROW.get());

				output.accept(RatsBlockRegistry.TRASH_CAN.get());
				output.accept(RatsBlockRegistry.GARBAGE_PILE.get());
				output.accept(RatsBlockRegistry.CURSED_GARBAGE.get());
				output.accept(RatsBlockRegistry.PURIFIED_GARBAGE.get());
				output.accept(RatsBlockRegistry.PIED_GARBAGE.get());
				output.accept(RatsBlockRegistry.COMPRESSED_GARBAGE.get());
				output.accept(RatsItemRegistry.FILTH.get());
				output.accept(RatsItemRegistry.FILTH_CORRUPTION.get());
				output.accept(RatsItemRegistry.CONTAMINATED_FOOD.get());

				output.accept(RatsItemRegistry.STRING_CHEESE.get());
				output.accept(RatsItemRegistry.RAT_BURGER.get());
				output.accept(RatsItemRegistry.ASSORTED_VEGETABLES.get());
				output.accept(RatsItemRegistry.CONFIT_BYALDI.get());
				output.accept(RatsItemRegistry.POTATO_PANCAKE.get());
				output.accept(RatsItemRegistry.LITTLE_BLACK_SQUASH_BALLS.get());
				output.accept(RatsItemRegistry.LITTLE_BLACK_WORM.get());
				output.accept(RatsItemRegistry.CENTIPEDE.get());
				output.accept(RatsItemRegistry.POTATO_KNISHES.get());

				output.accept(RatsItemRegistry.HERB_BUNDLE.get());
				output.accept(RatsItemRegistry.TREACLE.get());
				output.accept(RatsItemRegistry.PLAGUE_LEECH.get());
				output.accept(RatsItemRegistry.PLAGUE_STEW.get());
				output.accept(RatsItemRegistry.PLAGUE_DOCTORATE.get());
				output.accept(RatsItemRegistry.PLAGUE_TOME.get());
				output.accept(RatsItemRegistry.PLAGUE_ESSENCE.get());
				output.accept(RatsItemRegistry.PURIFYING_LIQUID.get());
				output.accept(RatsItemRegistry.CRIMSON_FLUID.get());

				output.accept(RatsItemRegistry.CHEF_TOQUE.get());
				output.accept(RatsItemRegistry.PIPER_HAT.get());
				output.accept(RatsItemRegistry.ARCHEOLOGIST_HAT.get());
				output.accept(RatsItemRegistry.FARMER_HAT.get());
				output.accept(RatsItemRegistry.FISHERMAN_HAT.get());
				output.accept(RatsItemRegistry.RAT_FEZ.get());
				output.accept(RatsItemRegistry.TOP_HAT.get());
				output.accept(RatsItemRegistry.SANTA_HAT.get());
				output.accept(RatsItemRegistry.HALO_HAT.get());
				output.accept(RatsItemRegistry.PARTY_HAT.get());
				output.accept(RatsItemRegistry.PIRAT_HAT.get());
				output.accept(RatsItemRegistry.RAT_KING_CROWN.get());
				output.accept(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get());
				output.accept(RatsItemRegistry.BLACK_DEATH_MASK.get());
				output.accept(RatsItemRegistry.EXTERMINATOR_HAT.get());

				output.accept(RatsItemRegistry.CHEESE_STICK.get());
				output.accept(RatsItemRegistry.RADIUS_STICK.get());
				output.accept(RatsItemRegistry.PATROL_STICK.get());
				output.accept(RatsItemRegistry.RAT_WHISTLE.get());
				output.accept(RatsItemRegistry.RAT_FLUTE.get());
				output.accept(RatsItemRegistry.GILDED_RAT_FLUTE.get());
				output.accept(RatsItemRegistry.PLAGUE_SCYTHE.get());
				output.accept(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS.get());
				output.accept(RatsItemRegistry.MUSIC_DISC_LIVING_MICE.get());

				output.accept(RatsBlockRegistry.RAT_CAGE.get());
				output.accept(RatsItemRegistry.PLASTIC_WASTE.get());
				output.accept(RatsItemRegistry.RAW_PLASTIC.get());
				output.accept(RatsItemRegistry.RAT_BREEDING_LANTERN.get());
				output.accept(RatsItemRegistry.RAT_SEED_BOWL.get());
				output.accept(RatsItemRegistry.RAT_WATER_BOTTLE.get());
				output.accept(RatsItemRegistry.RAT_WHEEL.get());
				registerColoredItems(output, "rat_tube");
				registerColoredItems(output, "rat_igloo");
				registerColoredItems(output, "rat_hammock");

				output.accept(RatsItemRegistry.RAT_CAPTURE_NET.get());
				output.accept(RatsItemRegistry.RAT_SACK.get());
				output.accept(RatsItemRegistry.FEATHERY_WING.get());
				output.accept(RatsItemRegistry.DRAGON_WING.get());
				output.accept(RatsItemRegistry.CHARGED_CREEPER_CHUNK.get());
				output.accept(RatsItemRegistry.RATBOW_ESSENCE.get());
				output.accept(RatsItemRegistry.RAT_PAPERS.get());
				output.accept(RatsItemRegistry.TINY_COIN.get());
				output.accept(RatsItemRegistry.TOKEN_FRAGMENT.get());
				output.accept(RatsItemRegistry.TOKEN_PIECE.get());

				output.accept(RatsBlockRegistry.MARBLED_CHEESE_RAW.get());
				output.accept(RatsBlockRegistry.FISH_BARREL.get());
				output.accept(RatsBlockRegistry.DYE_SPONGE.get());
				output.accept(RatsBlockRegistry.PIED_WOOL.get());
				output.accept(RatsBlockRegistry.JACK_O_RATERN.get());
				output.accept(RatsBlockRegistry.UPGRADE_COMBINER.get());
				output.accept(RatsBlockRegistry.UPGRADE_SEPARATOR.get());
				output.accept(RatsBlockRegistry.MANHOLE.get());
				output.accept(RatsBlockRegistry.RAT_QUARRY.get());
				output.accept(RatsBlockRegistry.RAT_ATTRACTOR.get());
				output.accept(RatsBlockRegistry.RAT_UPGRADE_BLOCK.get());
				output.accept(RatsItemRegistry.RAT_BANNER_PATTERN.get());
				output.accept(RatsItemRegistry.CHEESE_BANNER_PATTERN.get());
				output.accept(RatsItemRegistry.RAC_BANNER_PATTERN.get());
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "demon_rat_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_king_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "pied_piper_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "plague_doctor_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "black_death_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "plague_cloud_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "plague_beast_spawn_egg"))));
				registerOreNuggets(output);
			}).build());

	public static final RegistryObject<CreativeModeTab> UPGRADES = TABS.register("rats_upgrades", () -> CreativeModeTab.builder()
			.withTabsBefore(RATS.getKey())
			.title(Component.translatable("itemGroup.rats.upgrades"))
			.icon(() -> new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC.get()))
			.displayItems((parameters, output) -> {
				output.accept(RatsItemRegistry.RAT_UPGRADE_BASIC.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_SPEED.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_HEALTH.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ARMOR.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_STRENGTH.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_WARRIOR.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_GOD.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BOW.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CROSSBOW.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_AQUATIC.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_DRAGON.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BEE.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_DEMON.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ENDER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_UNDEAD.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CARRAT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_ANGEL.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_POISON.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_TNT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ARISTOCRAT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_VOODOO.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_BREEDER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_FISHERMAN.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK.get());
				//output.accept(RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get());
				//output.accept(RatsItemRegistry.RAT_UPGRADE_MINER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_FARMER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_QUARRY.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_REPLANTER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_SHEARS.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_PLACER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CRAFTING.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CHEF.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_GEMCUTTER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_BUCKET.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_MILKER.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_WHITELIST.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_TICK_ACCELERATOR.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_PLATTER.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_BASIC_MOUNT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get());

				output.accept(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get());
				output.accept(RatsItemRegistry.RAT_UPGRADE_COMBINED.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL.get());
				output.accept(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get());
			}).build());


	public static final RegistryObject<CreativeModeTab> RATLANTIS = TABS.register("ratlantis", () -> CreativeModeTab.builder()
			.withTabsBefore(UPGRADES.getKey())
			.title(Component.translatable("itemGroup.rats.ratlantis"))
			.icon(() -> new ItemStack(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get()))
			.displayItems((parameters, output) -> {

				output.accept(RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get());
				output.accept(RatlantisBlockRegistry.RATGLOVE_FLOWER.get());
				output.accept(RatlantisItemRegistry.RATGLOVE_PETALS.get());
				output.accept(RatlantisItemRegistry.FERAL_RAT_CLAW.get());
				output.accept(RatlantisItemRegistry.RATLANTEAN_FLAME.get());
				output.accept(RatlantisItemRegistry.GEM_OF_RATLANTIS.get());
				output.accept(RatlantisItemRegistry.RAW_ORATCHALCUM.get());
				output.accept(RatlantisItemRegistry.ORATCHALCUM_NUGGET.get());
				output.accept(RatlantisItemRegistry.ORATCHALCUM_INGOT.get());

				output.accept(RatlantisItemRegistry.RATLANTIS_HELMET.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_CHESTPLATE.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_LEGGINGS.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_BOOTS.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_SWORD.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_SHOVEL.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_PICKAXE.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_AXE.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_HOE.get());

				output.accept(RatlantisItemRegistry.RATLANTIS_BOW.get());
				output.accept(RatlantisItemRegistry.FERAL_BAGH_NAKHS.get());
				output.accept(RatlantisItemRegistry.PIRAT_CUTLASS.get());
				output.accept(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS.get());
				output.accept(RatlantisItemRegistry.AVIATOR_HAT.get());
				output.accept(RatlantisItemRegistry.GHOST_PIRAT_HAT.get());
				output.accept(RatlantisItemRegistry.MILITARY_HAT.get());
				output.accept(RatlantisItemRegistry.RAT_TOGA.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_RAT_HEAD.get());

				output.accept(RatlantisItemRegistry.CHEESE_CANNONBALL.get());
				output.accept(RatlantisItemRegistry.RATBOT_BARREL.get());
				output.accept(RatlantisItemRegistry.CHARGED_RATBOT_BARREL.get());
				output.accept(RatlantisItemRegistry.GHOST_PIRAT_ECTOPLASM.get());
				output.accept(RatlantisItemRegistry.RATFISH.get());
				output.accept(RatlantisItemRegistry.RATFISH_BUCKET.get());
				output.accept(RatlantisItemRegistry.RATTLING_GUN.get());
				output.accept(RatlantisItemRegistry.VIAL_OF_SENTIENCE.get());
				output.accept(RatlantisItemRegistry.RAS_BANNER_PATTERN.get());

				output.accept(RatlantisBlockRegistry.DUTCHRAT_BELL.get());
				output.accept(RatlantisBlockRegistry.AIR_RAID_SIREN.get());
				output.accept(RatlantisItemRegistry.ARCANE_TECHNOLOGY.get());
				output.accept(RatlantisItemRegistry.ANCIENT_SAWBLADE.get());
				output.accept(RatlantisItemRegistry.PSIONIC_RAT_BRAIN.get());
				output.accept(RatlantisItemRegistry.DUTCHRAT_WHEEL.get());
				output.accept(RatlantisItemRegistry.BIPLANE_WING.get());
				output.accept(RatlantisItemRegistry.RATLANTIS_RAT_SKULL.get());
				output.accept(RatlantisItemRegistry.IDOL_OF_RATLANTIS.get());

				output.accept(RatlantisBlockRegistry.CHEESE_ORE.get());
				output.accept(RatlantisBlockRegistry.RATLANTEAN_GEM_ORE.get());
				output.accept(RatlantisBlockRegistry.ORATCHALCUM_ORE.get());
				output.accept(RatlantisBlockRegistry.ORATCHALCUM_BLOCK.get());
				output.accept(RatlantisBlockRegistry.BRAIN_BLOCK.get());
				output.accept(RatlantisBlockRegistry.COMPRESSED_RAT.get());
				output.accept(RatlantisBlockRegistry.RATLANTIS_UPGRADE_BLOCK.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_STAIRS.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_SLAB.get());

				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_STAIRS.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_SLAB.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_STAIRS.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CRACKED_SLAB.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_STAIRS.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_MOSSY_SLAB.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_CHISELED.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_BRICK_CHISELED.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_PILLAR.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_TILE.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_GOLEM_CORE.get());
				output.accept(RatlantisBlockRegistry.BLACK_MARBLED_CHEESE.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_GRASS.get());
				output.accept(RatlantisBlockRegistry.MARBLED_CHEESE_DIRT.get());

				output.accept(RatlantisBlockRegistry.PIRAT_LOG.get());
				output.accept(RatlantisBlockRegistry.PIRAT_WOOD.get());
				output.accept(RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
				output.accept(RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
				output.accept(RatlantisBlockRegistry.PIRAT_PLANKS.get());
				output.accept(RatlantisBlockRegistry.PIRAT_STAIRS.get());
				output.accept(RatlantisBlockRegistry.PIRAT_SLAB.get());
				output.accept(RatlantisBlockRegistry.PIRAT_FENCE.get());
				output.accept(RatlantisBlockRegistry.PIRAT_FENCE_GATE.get());
				output.accept(RatlantisBlockRegistry.PIRAT_DOOR.get());
				output.accept(RatlantisBlockRegistry.PIRAT_TRAPDOOR.get());
				output.accept(RatlantisBlockRegistry.PIRAT_PRESSURE_PLATE.get());
				output.accept(RatlantisBlockRegistry.PIRAT_BUTTON.get());
				output.accept(RatlantisItemRegistry.PIRAT_SIGN.get());
				output.accept(RatlantisItemRegistry.PIRAT_HANGING_SIGN.get());
				output.accept(RatlantisItemRegistry.PIRAT_BOAT.get());
				output.accept(RatlantisItemRegistry.PIRAT_CHEST_BOAT.get());

				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "feral_ratlantean_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "ratlantean_spirit_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "pirat_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "ghost_pirat_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "ratfish_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "ratlantean_ratbot_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "ratlantean_automaton_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "neo_ratlantean_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "dutchrat_spawn_egg"))));
				output.accept(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_baron_spawn_egg"))));
			}).build());


	private static void registerOreNuggets(CreativeModeTab.Output output) {
		output.accept(RatsItemRegistry.RAT_NUGGET.get());
		List<ItemStack> uniqueOres = new ArrayList<>();
		for (Item item : ForgeRegistries.ITEMS.tags().getTag(Tags.Items.ORES)) {
			ItemStack oreDrop = ModClientEvents.getIngotHereBecauseClassloading(item.getDefaultInstance());
			if (!uniqueOres.contains(oreDrop) && !oreDrop.isEmpty()) {
				uniqueOres.add(oreDrop);
			}
		}
		for (ItemStack ore : uniqueOres) {
			output.accept(ModClientEvents.saveIngotHereBecauseClassloading(ore));
		}
	}

	private static void registerColoredItems(CreativeModeTab.Output output, String itemType) {
		for (DyeColor color : DyeColor.values()) {
			output.accept(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, itemType + "_" + color.getName())));
		}
	}

	@SubscribeEvent
	public static void registerToExistingTabs(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS && event.hasPermissions()) {
			event.accept(RatsItemRegistry.CREATIVE_CHEESE.get());
			event.accept(RatsItemRegistry.RAT_UPGRADE_CREATIVE.get());
			event.accept(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get());
		}
	}
}
