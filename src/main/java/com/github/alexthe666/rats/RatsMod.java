package com.github.alexthe666.rats;

import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.registry.worldgen.RatlantisFeatureRegistry;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.misc.RatVariants;
import com.github.alexthe666.rats.server.misc.RatsDataSerializers;
import com.github.alexthe666.rats.server.recipes.RatlantisLoadedCondition;
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
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod(RatsMod.MODID)
public class RatsMod {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "rats";
	public static final Rarity RATLANTIS_SPECIAL = Rarity.create("RATS_RATLANTIS_SPECIAL", ChatFormatting.GREEN);
	public static final MobCategory RATS = MobCategory.create("RATS_RATS", "rats", 25, true, false, 128);
	public static final MobCategory DEMON_RATS = MobCategory.create("RATS_DEMON_RATS", "demon_rats", 5, false, false, 64);

	public static final BlockSetType PIRAT_WOOD_SET = BlockSetType.register(new BlockSetType(new ResourceLocation(MODID, "pirat").toString(), SoundType.NETHER_WOOD, SoundEvents.NETHER_WOOD_DOOR_CLOSE, SoundEvents.NETHER_WOOD_DOOR_OPEN, SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE, SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF, SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON, SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF, SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON));
	public static final WoodType PIRAT_WOOD_TYPE = WoodType.register(new WoodType(new ResourceLocation(MODID, "pirat").toString(), PIRAT_WOOD_SET, SoundType.NETHER_WOOD, SoundType.NETHER_WOOD_HANGING_SIGN, SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE, SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN));

	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_RATS = GameRules.register("doRatSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PIPERS = GameRules.register("doPiperSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.BooleanValue> SPAWN_PLAGUE_DOCTORS = GameRules.register("doPlagueDoctorSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

	public static boolean ICEANDFIRE_LOADED;
	public static boolean RATLANTIS_DATAPACK_ENABLED = false;
	public static final List<Item> RATLANTIS_ITEMS = new ArrayList<>();

	public RatsMod() {
		ICEANDFIRE_LOADED = ModList.get().isLoaded("iceandfire");
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
		//melk
		ForgeMod.enableMilkFluid();

		RatVariants.RAT_VARIANTS.register(bus);

		RatsBannerPatternRegistry.PATTERNS.register(bus);
		RatsBlockRegistry.BLOCKS.register(bus);
		RatsBlockEntityRegistry.BLOCK_ENTITIES.register(bus);
		RatsEntityRegistry.ENTITIES.register(bus);
		RatsItemRegistry.ITEMS.register(bus);
		RatsDataSerializers.DATA_SERIALIZERS.register(bus);
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

		RatlantisBlockRegistry.BLOCKS.register(bus);
		RatlantisBlockEntityRegistry.BLOCK_ENTITIES.register(bus);
		RatlantisFeatureRegistry.CARVERS.register(bus);
		RatlantisEntityRegistry.ENTITIES.register(bus);
		RatlantisFeatureRegistry.FEATURES.register(bus);
		RatlantisItemRegistry.ITEMS.register(bus);
		RatlantisFeatureRegistry.PROCESSORS.register(bus);

		MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, RatsCapabilityRegistry::attachCap);
		bus.addListener(RatsCapabilityRegistry::registerCapabilities);
		bus.addListener(this::reloadConfigs);
		bus.addListener(this::setup);
		MinecraftForge.EVENT_BUS.addListener(this::addPetShops);
		bus.addListener(this::addRatlantisDatapack);
		bus.addListener(this::genericRegistryEvent);
	}

	//despite being a builtin datapack, this is still necessary because without it the Ratlantis pack doesn't show up. Whatever.
	public void addRatlantisDatapack(AddPackFindersEvent event) {
		if (event.getPackType() == PackType.SERVER_DATA) {
			var resourcePath = ModList.get().getModFileById(MODID).getFile().findResource("data", "minecraft", "datapacks", "ratlantis");
			var pack = Pack.readMetaAndCreate("ratlantis", Component.literal("Ratlantis"), false,
					name -> new PathPackResources(name, resourcePath, false), PackType.SERVER_DATA, Pack.Position.TOP, PackSource.FEATURE);
			event.addRepositorySource(packConsumer -> packConsumer.accept(pack));
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

	public void genericRegistryEvent(RegisterEvent event) {
		if (Objects.equals(event.getForgeRegistry(), ForgeRegistries.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(new RatlantisLoadedCondition.Serializer());
		}
	}

	private void setup(FMLCommonSetupEvent event) {
		RatsAdvancementsRegistry.init();
		RatsNetworkHandler.init();
		RatsDispenserRegistry.init();
		RatsUpgradeConflictRegistry.init();
		event.enqueueWork(() -> {
			RatsCauldronRegistry.init();
			GiveGiftToHero.GIFTS.put(RatsVillagerRegistry.PET_SHOP_OWNER.get(), RatsLootRegistry.PET_SHOP_HOTV);

			CauldronInteraction.WATER.put(RatsItemRegistry.PARTY_HAT.get(), CauldronInteraction.DYED_ITEM);

			FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
			pot.addPlant(RatlantisBlockRegistry.RATGLOVE_FLOWER.getId(), RatlantisBlockRegistry.POTTED_RATGLOVE_FLOWER);

			ComposterBlock.add(0.3F, RatsItemRegistry.RAT_NUGGET.get());
			ComposterBlock.add(0.5F, RatsItemRegistry.CONTAMINATED_FOOD.get());
			ComposterBlock.add(0.65F, RatlantisBlockRegistry.RATGLOVE_FLOWER.get());
			ComposterBlock.add(0.65F, RatsItemRegistry.POTATO_PANCAKE.get());
			ComposterBlock.add(0.85F, RatsItemRegistry.HERB_BUNDLE.get());
			ComposterBlock.add(0.85F, RatlantisItemRegistry.RATGLOVE_PETALS.get());
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
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/houses"), "rats:pet_shops/plains", 25, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/snowy/houses"), "rats:pet_shops/snowy", 25, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/savanna/houses"), "rats:pet_shops/savanna", 15, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/houses"), "rats:pet_shops/taiga", 15, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/desert/houses"), "rats:pet_shops/desert", 25, ProcessorLists.EMPTY);

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/zombie/houses"), "rats:pet_shops/zombie_plains", 10, ProcessorLists.ZOMBIE_PLAINS);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/snowy/zombie/houses"), "rats:pet_shops/zombie_snowy", 10, ProcessorLists.ZOMBIE_SNOWY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/savanna/zombie/houses"), "rats:pet_shops/zombie_savanna", 10, ProcessorLists.ZOMBIE_SAVANNA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/zombie/houses"), "rats:pet_shops/zombie_taiga", 10, ProcessorLists.ZOMBIE_TAIGA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/desert/zombie/houses"), "rats:pet_shops/zombie_desert", 10, ProcessorLists.ZOMBIE_DESERT);
		}

		if (RatConfig.villageGarbageHeaps) {
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/houses"), "rats:garbage_heaps/plains", 1, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/snowy/houses"), "rats:garbage_heaps/snowy", 1, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/savanna/houses"), "rats:garbage_heaps/savanna", 1, ProcessorLists.EMPTY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/houses"), "rats:garbage_heaps/taiga", 1, ProcessorLists.MOSSIFY_10_PERCENT);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/desert/houses"), "rats:garbage_heaps/desert", 1, ProcessorLists.EMPTY);

			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/plains/zombie/houses"), "rats:garbage_heaps/plains", 5, ProcessorLists.ZOMBIE_PLAINS);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/snowy/zombie/houses"), "rats:garbage_heaps/snowy", 5, ProcessorLists.ZOMBIE_SNOWY);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/savanna/zombie/houses"), "rats:garbage_heaps/savanna", 5, ProcessorLists.ZOMBIE_SAVANNA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/taiga/zombie/houses"), "rats:garbage_heaps/taiga", 5, ProcessorLists.ZOMBIE_TAIGA);
			this.addBuildingToPool(templatePoolRegistry, processorListRegistry, new ResourceLocation("minecraft:village/desert/zombie/houses"), "rats:garbage_heaps/desert", 5, ProcessorLists.ZOMBIE_DESERT);
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
}