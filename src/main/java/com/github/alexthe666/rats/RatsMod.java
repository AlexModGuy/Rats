package com.github.alexthe666.rats;

import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.registry.worldgen.RatlantisFeatureRegistry;
import com.google.common.collect.Maps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import com.mojang.datafixers.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod(RatsMod.MODID)
public class RatsMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "rats";
	// In 1.21.1, custom Rarity cannot be created with .create() - using UNCOMMON as substitute
	public static final Rarity RATLANTIS_SPECIAL = Rarity.UNCOMMON;
	// In 1.21.1, custom MobCategory cannot be created - using CREATURE as substitute
	public static final MobCategory RATS = MobCategory.CREATURE;

	public static final BlockSetType PIRAT_WOOD_SET = BlockSetType.register(new BlockSetType(ResourceLocation.fromNamespaceAndPath(MODID, "pirat").toString()));
	public static final WoodType PIRAT_WOOD_TYPE = WoodType.register(new WoodType(ResourceLocation.fromNamespaceAndPath(MODID, "pirat").toString(), PIRAT_WOOD_SET));

	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_RATS = GameRules.register("doRatSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PIPERS = GameRules.register("doPiperSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PLAGUE_DOCTORS = GameRules.register("doPlagueDoctorSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

	public static boolean ICEANDFIRE_LOADED;
	public static boolean RATLANTIS_DATAPACK_ENABLED = false;
	public static final List<Item> RATLANTIS_ITEMS = new ArrayList<>();
	private static final List<Pair<String, Component>> MOB_CACHE = new ArrayList<>();

	public RatsMod(IEventBus bus, ModContainer modContainer) {
		ICEANDFIRE_LOADED = ModList.get().isLoaded("iceandfire");
		modContainer.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
		modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
		//melk
		NeoForgeMod.enableMilkFluid();

		RatVariantRegistry.RAT_VARIANTS.register(bus);

		RatsArmorMaterialRegistry.ARMOR_MATERIALS.register(bus);
		// BannerPatterns are now registered via datapack registry bootstrap
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
		RatsCreativeTabRegistry.TABS.register(bus);
		RatsAdvancementsRegistry.TRIGGERS.register(bus);

		RatlantisBlockRegistry.BLOCKS.register(bus);
		RatlantisBlockEntityRegistry.BLOCK_ENTITIES.register(bus);
		RatlantisFeatureRegistry.CARVERS.register(bus);
		RatlantisEntityRegistry.ENTITIES.register(bus);
		RatlantisFeatureRegistry.FEATURES.register(bus);
		RatlantisItemRegistry.ITEMS.register(bus);
		RatlantisFeatureRegistry.PROCESSORS.register(bus);
		RatlantisFeatureRegistry.TRUNK_PLACERS.register(bus);

		bus.addListener(RatsCapabilityRegistry::registerCapabilities);
		bus.addListener(this::reloadConfigs);
		bus.addListener(this::setup);
		bus.addListener(this::registerRegistries);
		NeoForge.EVENT_BUS.addListener(this::addPetShops);
		bus.addListener(this::addRatlantisDatapack);
	}

	public void registerRegistries(NewRegistryEvent event) {
		event.register(RatVariantRegistry.RAT_VARIANT_REGISTRY);
	}

	//despite being a builtin datapack, this is still necessary because without it the Ratlantis pack doesn't show up. Whatever.
	public void addRatlantisDatapack(AddPackFindersEvent event) {
		if (event.getPackType() == PackType.SERVER_DATA) {
			var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("data", "minecraft", "datapacks", "ratlantis");
			// TODO: Pack.readMetaAndCreate() API changed significantly in 1.21.1
			// The new API uses Pack.create() with different parameters including PackInfo
			// Need to investigate the correct 1.21.1 pattern for built-in datapacks
			/*
			event.addRepositorySource(packConsumer -> {
				Pack pack = Pack.readMetaAndCreate(
					ResourceLocation.fromNamespaceAndPath(MODID, "ratlantis"),
					Component.literal("Ratlantis"),
					RatConfig.ratlantisEnabledByDefault,
					new PathPackResources.PathResourcesSupplier(resourcePath),
					PackType.SERVER_DATA,
					Pack.Position.TOP,
					PackSource.FEATURE
				);
				if (pack != null) {
					packConsumer.accept(pack);
				}
			});
			*/
		}
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
		// Network handler now uses @SubscribeEvent pattern - no init() needed
		RatsUpgradeConflictRegistry.init();
		event.enqueueWork(() -> {
			RatsCauldronRegistry.init();
			RatsDispenserRegistry.init();

			// TODO: Raid.RaiderType.create() doesn't exist in 1.21.1 - requires mixin or alternative approach
			// Raid.RaiderType.create("RATS_PIPER", RatsEntityRegistry.PIED_PIPER.get(), new int[]{0, 0, 1, 0, 0, 1, 1, 2});
			
			// TODO: GiveGiftToHero.GIFTS is private in 1.21.1 - requires access transformer
			// GiveGiftToHero.GIFTS.put(RatsVillagerRegistry.PET_SHOP_OWNER.get(), RatsLootRegistry.PET_SHOP_HOTV);

			CauldronInteraction.WATER.map().put(RatsItemRegistry.PARTY_HAT.get(), CauldronInteraction.DYED_ITEM);

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
			pot.addPlant(RatlantisBlockRegistry.RATGLOVE_FLOWER.getId(), RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER);
			pot.addPlant(RatlantisBlockRegistry.PIRAT_SAPLING.getId(), RatlantisBlockRegistry.POTTED_PIRAT_SAPLING);

			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.RAT_NUGGET.get(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(RatlantisBlockRegistry.PIRAT_SAPLING.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(RatlantisBlockRegistry.PIRAT_LEAVES.get().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.CONTAMINATED_FOOD.get(), 0.5F);
			ComposterBlock.COMPOSTABLES.put(RatlantisBlockRegistry.RATGLOVE_FLOWER.get().asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(RatlantisItemRegistry.RATGLOVE_PETALS.get(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.POTATO_PANCAKE.get(), 0.85F);
			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.HERB_BUNDLE.get(), 0.85F);
			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.CONFIT_BYALDI.get(), 1.0F);
			ComposterBlock.COMPOSTABLES.put(RatsItemRegistry.POTATO_KNISHES.get(), 1.0F);

			// TODO: AxeItem.STRIPPABLES is protected in 1.21.1 - requires access transformer
			// AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
			// AxeItem.STRIPPABLES.put(RatlantisBlockRegistry.PIRAT_LOG.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_LOG.get());
			// AxeItem.STRIPPABLES.put(RatlantisBlockRegistry.PIRAT_WOOD.get(), RatlantisBlockRegistry.STRIPPED_PIRAT_WOOD.get());
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
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/plains/houses"), "rats:pet_shops/plains", RatConfig.villagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("mossify_10_percent")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/snowy/houses"), "rats:pet_shops/snowy", RatConfig.villagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/savanna/houses"), "rats:pet_shops/savanna", RatConfig.villagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/taiga/houses"), "rats:pet_shops/taiga", RatConfig.villagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("mossify_10_percent")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/desert/houses"), "rats:pet_shops/desert", RatConfig.villagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/plains/zombie/houses"), "rats:pet_shops/zombie_plains", RatConfig.zombieVillagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_plains")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/snowy/zombie/houses"), "rats:pet_shops/zombie_snowy", RatConfig.zombieVillagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_snowy")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/savanna/zombie/houses"), "rats:pet_shops/zombie_savanna", RatConfig.zombieVillagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_savanna")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/taiga/zombie/houses"), "rats:pet_shops/zombie_taiga", RatConfig.zombieVillagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_taiga")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/desert/zombie/houses"), "rats:pet_shops/zombie_desert", RatConfig.zombieVillagePetShopWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_desert")));
		}

		if (RatConfig.villageGarbageHeaps) {
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/plains/houses"), "rats:garbage_heaps/plains", RatConfig.villageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("mossify_10_percent")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/snowy/houses"), "rats:garbage_heaps/snowy", RatConfig.villageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/savanna/houses"), "rats:garbage_heaps/savanna", RatConfig.villageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/taiga/houses"), "rats:garbage_heaps/taiga", RatConfig.villageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("mossify_10_percent")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/desert/houses"), "rats:garbage_heaps/desert", RatConfig.villageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("empty")));

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/plains/zombie/houses"), "rats:garbage_heaps/plains", RatConfig.zombieVillageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_plains")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/snowy/zombie/houses"), "rats:garbage_heaps/snowy", RatConfig.zombieVillageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_snowy")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/savanna/zombie/houses"), "rats:garbage_heaps/savanna", RatConfig.zombieVillageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_savanna")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/taiga/zombie/houses"), "rats:garbage_heaps/taiga", RatConfig.zombieVillageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_taiga")));
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, ResourceLocation.fromNamespaceAndPath("minecraft", "village/desert/zombie/houses"), "rats:garbage_heaps/desert", RatConfig.zombieVillageGarbageHeapWeight, ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.withDefaultNamespace("zombie_desert")));
		}
	}

	//code take from TelepathicGrunt's gist: https://gist.github.com/TelepathicGrunt/4fdbc445ebcbcbeb43ac748f4b18f342
	//1.18.2 version used, and modified, so it works in 1.19.4
	//additions: a StructureProcessorList parameter to allow us to add a processor. (original code always used an empty processor, but some houses actually use processors)
	// TODO: In 1.21.1, StructureTemplatePool.templates and rawTemplates are private
	// This method requires access transformers or a different approach (e.g., using template pool JSON files)
	private void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight, ResourceKey<StructureProcessorList> processor) {
		Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(processor);

		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL, emptyProcessorList).apply(StructureTemplatePool.Projection.RIGID);

		// TODO: pool.templates and pool.rawTemplates are private - requires access transformer
		// For now, village structures need to be added via data-driven JSON files instead
		// Or add to accesstransformer.cfg:
		// public net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool templates
		// public net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool rawTemplates
		/*
		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
		*/
		LOGGER.debug("Rats: Village pool injection disabled - use JSON template pools instead. Would add {} to {}", nbtPieceRL, poolRL.toString());
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







