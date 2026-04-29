package com.github.alexthe666.rats;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.registry.worldgen.RatlantisFeatureRegistry;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod(RatsMod.MODID)
public class RatsMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "rats";
	// PORT-STUB: 1.21 Rarity is an enum (no Rarity.create); reuse vanilla EPIC for the special rarity slot.
	public static final Rarity RATLANTIS_SPECIAL = Rarity.EPIC;
	// PORT-STUB: 1.21 MobCategory is also an enum (no MobCategory.create); reuse CREATURE for rat spawn category.
	public static final MobCategory RATS = MobCategory.CREATURE;

	// PORT-STUB: 1.21 BlockSetType ctor adds canOpenByWindCharge, canButtonBeActivatedByArrows, PressurePlateSensitivity. Static register(BlockSetType) is now private.
	public static final BlockSetType PIRAT_WOOD_SET = new BlockSetType(
			ResourceLocation.fromNamespaceAndPath(MODID, "pirat").toString(),
			true, true, true,
			net.minecraft.world.level.block.state.properties.BlockSetType.PressurePlateSensitivity.EVERYTHING,
			SoundType.NETHER_WOOD,
			SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN,
			SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
			SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
			SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON);
	public static final WoodType PIRAT_WOOD_TYPE = WoodType.register(new WoodType(ResourceLocation.fromNamespaceAndPath(MODID, "pirat").toString(), PIRAT_WOOD_SET, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));

	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_RATS = GameRules.register("doRatSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PIPERS = GameRules.register("doPiperSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PLAGUE_DOCTORS = GameRules.register("doPlagueDoctorSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

	public static boolean ICEANDFIRE_LOADED;
	public static boolean RATLANTIS_DATAPACK_ENABLED = false;
	public static final List<Item> RATLANTIS_ITEMS = new ArrayList<>();
	private static final List<Pair<String, Component>> MOB_CACHE = new ArrayList<>();

	public RatsMod(IEventBus bus, net.neoforged.fml.ModContainer container) {
		ICEANDFIRE_LOADED = ModList.get().isLoaded("iceandfire");
		container.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
		container.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
		//melk
		NeoForgeMod.enableMilkFluid();

		RatVariantRegistry.RAT_VARIANTS.register(bus);

		RatsArmorMaterialRegistry.ARMOR_MATERIALS.register(bus);
		RatsBannerPatternRegistry.PATTERNS.register(bus);
		RatsBlockRegistry.BLOCKS.register(bus);
		RatsBlockEntityRegistry.BLOCK_ENTITIES.register(bus);
		RatsEntityRegistry.ENTITIES.register(bus);
		RatsItemRegistry.ITEMS.register(bus);
		RatsDataSerializerRegistry.DATA_SERIALIZERS.register(bus);
		RatsEffectRegistry.MOB_EFFECTS.register(bus);
		RatsLootRegistry.CONDITIONS.register(bus);
		RatsLootRegistry.LOOT_MODIFIERS.register(bus);
		RatsMenuRegistry.MENUS.register(bus);
		RatsParticleRegistry.PARTICLES.register(bus);
		RatsVillagerRegistry.POIS.register(bus);
		RatsVillagerRegistry.PROFESSIONS.register(bus);
		RatsRecipeRegistry.RECIPES.register(bus);
		RatsRecipeRegistry.SERIALIZERS.register(bus);
		RatsSoundRegistry.SOUNDS.register(bus);
		RatsAdvancementsRegistry.TRIGGERS.register(bus);
		RatsCreativeTabRegistry.TABS.register(bus);

		RatlantisBlockRegistry.BLOCKS.register(bus);
		RatlantisBlockEntityRegistry.BLOCK_ENTITIES.register(bus);
		RatlantisFeatureRegistry.CARVERS.register(bus);
		RatlantisEntityRegistry.ENTITIES.register(bus);
		RatlantisFeatureRegistry.FEATURES.register(bus);
		RatlantisItemRegistry.ITEMS.register(bus);
		RatlantisFeatureRegistry.PROCESSORS.register(bus);
		RatlantisFeatureRegistry.TRUNK_PLACERS.register(bus);

		com.github.alexthe666.rats.server.capability.SelectedRat.ATTACHMENTS.register(bus);
		bus.addListener(this::reloadConfigs);
		bus.addListener(this::setup);
		NeoForge.EVENT_BUS.addListener(this::addPetShops);
		bus.addListener(this::addRatlantisDatapack);
	}

	//despite being a builtin datapack, this is still necessary because without it the Ratlantis pack doesn't show up. Whatever.
	public void addRatlantisDatapack(AddPackFindersEvent event) {
		if (event.getPackType() == PackType.SERVER_DATA) {
			var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("data", "minecraft", "datapacks", "ratlantis");
			// PORT-STUB: 1.21 Pack.readMetaAndCreate signature changed. Use Pack.Metadata + Pack.ResourcesSupplier directly.
			var location = new net.minecraft.server.packs.PackLocationInfo("ratlantis",
					Component.literal("Ratlantis"), PackSource.FEATURE, java.util.Optional.empty());
			var resources = new PathPackResources(location, resourcePath);
			var metadata = Pack.readPackMetadata(location, fixedSupplier(resources), net.minecraft.SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
			if (metadata != null) {
				var pack = new Pack(location, fixedSupplier(resources), metadata, new net.minecraft.server.packs.PackSelectionConfig(RatConfig.ratlantisEnabledByDefault, Pack.Position.TOP, false));
				event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
			}
		}
	}

	private static Pack.ResourcesSupplier fixedSupplier(PathPackResources resources) {
		return new Pack.ResourcesSupplier() {
			@Override
			public net.minecraft.server.packs.PackResources openPrimary(net.minecraft.server.packs.PackLocationInfo info) {
				return resources;
			}
			@Override
			public net.minecraft.server.packs.PackResources openFull(net.minecraft.server.packs.PackLocationInfo info, Pack.Metadata metadata) {
				return resources;
			}
		};
	}

	public void reloadConfigs(ModConfigEvent event) {
		if (event.getConfig().getSpec() == ConfigHolder.SERVER_SPEC) {
			RatConfig.bakeServer();
			LOGGER.debug("Reloading Rats Server Config!");
		}
		if (event.getConfig().getSpec() == ConfigHolder.CLIENT_SPEC) {
			RatConfig.bakeClient();
			LOGGER.debug("Reloading Rats Client Config!");
		}
	}

	private void setup(FMLCommonSetupEvent event) {
		RatsAdvancementsRegistry.init();
		// PORT-STUB: 1.21 RatsNetworkHandler.init() removed; networking now wires via RegisterPayloadHandlersEvent on the bus.
		RatsUpgradeConflictRegistry.init();
		event.enqueueWork(() -> {
			RatsCauldronRegistry.init();
			RatsDispenserRegistry.init();

			// PORT-STUB: 1.21 Raid.RaiderType.create() relocated; raid wave registration now goes through the registry directly.
			GiveGiftToHero.GIFTS.put(RatsVillagerRegistry.PET_SHOP_OWNER.get(), RatsLootRegistry.PET_SHOP_HOTV);

			// PORT-STUB: 1.21 CauldronInteraction.WATER is now a CauldronInteraction.InteractionMap; mutate via .map().put(...)
			CauldronInteraction.WATER.map().put(RatsItemRegistry.PARTY_HAT.get(), CauldronInteraction.DYED_ITEM);

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
			pot.addPlant(RatlantisBlockRegistry.RATGLOVE_FLOWER.getId(), RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER);
			pot.addPlant(RatlantisBlockRegistry.PIRAT_SAPLING.getId(), RatlantisBlockRegistry.POTTED_PIRAT_SAPLING);

			ComposterBlock.add(0.3F, RatsItemRegistry.RAT_NUGGET.get());
			ComposterBlock.add(0.3F, RatlantisBlockRegistry.PIRAT_SAPLING.get());
			ComposterBlock.add(0.3F, RatlantisBlockRegistry.PIRAT_LEAVES.get());
			ComposterBlock.add(0.5F, RatsItemRegistry.CONTAMINATED_FOOD.get());
			ComposterBlock.add(0.65F, RatlantisBlockRegistry.RATGLOVE_FLOWER.get());
			ComposterBlock.add(0.65F, RatlantisItemRegistry.RATGLOVE_PETALS.get());
			ComposterBlock.add(0.85F, RatsItemRegistry.POTATO_PANCAKE.get());
			ComposterBlock.add(0.85F, RatsItemRegistry.HERB_BUNDLE.get());
			ComposterBlock.add(1.0F, RatsItemRegistry.CONFIT_BYALDI.get());
			ComposterBlock.add(1.0F, RatsItemRegistry.POTATO_KNISHES.get());

			AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			AxeItem.STRIPPABLES.put(RatlantisBlockRegistry.PIRAT_LOG.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
			AxeItem.STRIPPABLES.put(RatlantisBlockRegistry.PIRAT_WOOD.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
		});
		//wooooo caches ftw
		if (RATLANTIS_ITEMS.isEmpty()) {
			RatlantisItemRegistry.ITEMS.getEntries().forEach(item -> RATLANTIS_ITEMS.add(item.get()));
		}
	}

	//code take from TelepathicGrunt's gist: https://gist.github.com/TelepathicGrunt/4fdbc445ebcbcbeb43ac748f4b18f342
	//1.18.2 version used and modified so it works in 1.19.4
	public void addPetShops(ServerAboutToStartEvent event) {
		Registry<StructureTemplatePool> templatePoolRegistry = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();
		Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();

		if (RatConfig.villagePetShops) {
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/plains/houses"), "rats:pet_shops/plains", RatConfig.villagePetShopWeight, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/snowy/houses"), "rats:pet_shops/snowy", RatConfig.villagePetShopWeight, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/savanna/houses"), "rats:pet_shops/savanna", RatConfig.villagePetShopWeight, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/taiga/houses"), "rats:pet_shops/taiga", RatConfig.villagePetShopWeight, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/desert/houses"), "rats:pet_shops/desert", RatConfig.villagePetShopWeight, ProcessorLists.EMPTY);

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/plains/zombie/houses"), "rats:pet_shops/zombie_plains", RatConfig.zombieVillagePetShopWeight, ProcessorLists.ZOMBIE_PLAINS);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/snowy/zombie/houses"), "rats:pet_shops/zombie_snowy", RatConfig.zombieVillagePetShopWeight, ProcessorLists.ZOMBIE_SNOWY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/savanna/zombie/houses"), "rats:pet_shops/zombie_savanna", RatConfig.zombieVillagePetShopWeight, ProcessorLists.ZOMBIE_SAVANNA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/taiga/zombie/houses"), "rats:pet_shops/zombie_taiga", RatConfig.zombieVillagePetShopWeight, ProcessorLists.ZOMBIE_TAIGA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/desert/zombie/houses"), "rats:pet_shops/zombie_desert", RatConfig.zombieVillagePetShopWeight, ProcessorLists.ZOMBIE_DESERT);
		}

		if (RatConfig.villageGarbageHeaps) {
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/plains/houses"), "rats:garbage_heaps/plains", RatConfig.villageGarbageHeapWeight, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/snowy/houses"), "rats:garbage_heaps/snowy", RatConfig.villageGarbageHeapWeight, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/savanna/houses"), "rats:garbage_heaps/savanna", RatConfig.villageGarbageHeapWeight, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/taiga/houses"), "rats:garbage_heaps/taiga", RatConfig.villageGarbageHeapWeight, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/desert/houses"), "rats:garbage_heaps/desert", RatConfig.villageGarbageHeapWeight, ProcessorLists.EMPTY);

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/plains/zombie/houses"), "rats:garbage_heaps/plains", RatConfig.zombieVillageGarbageHeapWeight, ProcessorLists.ZOMBIE_PLAINS);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/snowy/zombie/houses"), "rats:garbage_heaps/snowy", RatConfig.zombieVillageGarbageHeapWeight, ProcessorLists.ZOMBIE_SNOWY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/savanna/zombie/houses"), "rats:garbage_heaps/savanna", RatConfig.zombieVillageGarbageHeapWeight, ProcessorLists.ZOMBIE_SAVANNA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/taiga/zombie/houses"), "rats:garbage_heaps/taiga", RatConfig.zombieVillageGarbageHeapWeight, ProcessorLists.ZOMBIE_TAIGA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.parse("minecraft:village/desert/zombie/houses"), "rats:garbage_heaps/desert", RatConfig.zombieVillageGarbageHeapWeight, ProcessorLists.ZOMBIE_DESERT);
		}
	}

	//code take from TelepathicGrunt's gist: https://gist.github.com/TelepathicGrunt/4fdbc445ebcbcbeb43ac748f4b18f342
	//1.18.2 version used, and modified, so it works in 1.19.4
	//additions: a StructureProcessorList parameter to allow us to add a processor. (original code always used an empty processor, but some houses actually use processors)
	private void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight, ResourceKey<StructureProcessorList> processor) {
		Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(processor);

		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL, emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
		LOGGER.debug("Rats: Successfully added {} to village pool {}", nbtPieceRL, poolRL.toString());
	}

	public static List<Pair<String, Component>> getCachedMobList(@Nullable Level level) {
		if (level != null && MOB_CACHE.isEmpty()) {
			List<Pair<String, Component>> unsortedCache = new ArrayList<>();
			for (var entry : BuiltInRegistries.ENTITY_TYPE.entrySet()) {
				try {
					Entity entity = entry.getValue().create(level);
					if (entry.getValue() == EntityType.PLAYER || entity instanceof Mob) {
						unsortedCache.add(Pair.of(entry.getKey().location().toString(), entry.getValue().getDescription()));
					}
				} catch (NullPointerException e) {
					RatsMod.LOGGER.error("Couldnt cache an instance of the mob {}", entry.getKey().location(), e);
				}
			}
			MOB_CACHE.addAll(unsortedCache.stream().sorted(Comparator.comparing(o -> o.getSecond().getString())).toList());
			LOGGER.debug("Cached {} mob ids for later use.", MOB_CACHE.size());
		}
		return MOB_CACHE;
	}
}
