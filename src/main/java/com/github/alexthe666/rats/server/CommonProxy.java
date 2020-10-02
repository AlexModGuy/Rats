package com.github.alexthe666.rats.server;

import com.github.alexthe666.rats.ConfigHolder;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatlantisConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.entity.ratlantis.RatlantisEntityRegistry;
import com.github.alexthe666.rats.server.inventory.RatsContainerRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.world.BiomeRatlantis;
import com.github.alexthe666.rats.server.world.RatsWorldRegistry;
import com.github.alexthe666.rats.server.world.gen.FeatureMarblePile;
import com.github.alexthe666.rats.server.world.structure.RatlantisStructureRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Effect> event) {
        event.getRegistry().registerAll(RatsMod.CONFIT_BYALDI_POTION, RatsMod.PLAGUE_POTION);
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        try {
            for (Field f : RatsSoundRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof SoundEvent) {
                    event.getRegistry().register((SoundEvent) obj);
                } else if (obj instanceof SoundEvent[]) {
                    for (SoundEvent sound : (SoundEvent[]) obj) {
                        event.getRegistry().register(sound);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerVillagers(RegistryEvent.Register<VillagerProfession> event) {
        // event.getRegistry().register(RatsVillageRegistry.PET_SHOP_OWNER);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        try {
            for (Field f : RatsEntityRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof EntityType) {
                    event.getRegistry().register((EntityType) obj);
                } else if (obj instanceof EntityType[]) {
                    for (EntityType type : (EntityType[]) obj) {
                        event.getRegistry().register(type);

                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        RatsEntityRegistry.initializeAttributes();
        if (RatsMod.RATLANTIS_LOADED) {
            try {
                for (Field f : RatlantisEntityRegistry.class.getDeclaredFields()) {
                    Object obj = f.get(null);
                    if (obj instanceof EntityType) {
                        event.getRegistry().register((EntityType) obj);
                    } else if (obj instanceof EntityType[]) {
                        for (EntityType type : (EntityType[]) obj) {
                            event.getRegistry().register(type);

                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            RatlantisEntityRegistry.initializeAttributes();
        }
        RatsRecipeRegistry.register();
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.RAT, 0X30333E, 0XDAABA1, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_rat"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PIED_PIPER, 0XCABC42, 0X3B6063, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_piper"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_DOCTOR, 0X2A292A, 0X515359, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_doctor"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.BLACK_DEATH, 0X000000, 0X000000, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_black_death"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_CLOUD, 0X000000, 0X52574D, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_cloud"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_BEAST, 0X000000, 0XECECEC, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_beast"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.RAT_KING, 0X30333E, 0X39342F, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_rat_king"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.DEMON_RAT, 0X6C191F, 0XFCD500, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_demon_rat"));
        event.getRegistry().register(new BannerPatternItem(RatsRecipeRegistry.RAT_PATTERN, (new Item.Properties()).maxStackSize(1).group(RatsMod.TAB)).setRegistryName("rats:rat_banner_pattern"));
        event.getRegistry().register(new BannerPatternItem(RatsRecipeRegistry.CHEESE_PATTERN, (new Item.Properties()).maxStackSize(1).group(RatsMod.TAB)).setRegistryName("rats:cheese_banner_pattern"));
        event.getRegistry().register(new BannerPatternItem(RatsRecipeRegistry.RAT_AND_CROSSBONES_BANNER, (new Item.Properties()).maxStackSize(1).group(RatsMod.getRatlantisTab())).setRegistryName("rats:rat_and_crossbones_banner_pattern"));
        if (RatsMod.RATLANTIS_LOADED) {
            event.getRegistry().register(new BannerPatternItem(RatsRecipeRegistry.RAT_AND_SICKLE_BANNER, (new Item.Properties()).maxStackSize(1).group(RatsMod.getRatlantisTab())).setRegistryName("rats:rat_and_sickle_banner_pattern"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.RATLANTEAN_SPIRIT, 0XEDBD00, 0XFFE8AF, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_ratlantean_spirit"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON, 0XE8E4D7, 0X72E955, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_ratlantean_automaton"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.FERAL_RATLANTEAN, 0X30333E, 0XECECEC, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_feral_ratlantean"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.NEO_RATLANTEAN, 0X30333E, 0X00EFEF, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_neo_ratlantean"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.PIRAT, 0X30333E, 0XAF363A, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_pirat"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.GHOST_PIRAT, 0X54AA55, 0X7BD77D, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_ghost_pirat"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.DUTCHRAT, 0XBBF9BB, 0XD0E2B5, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_dutchrat"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.RATFISH, 0X30333E, 0X7EA8BD, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_ratfish"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.RATLANTEAN_RATBOT, 0XA3A3A3, 0XFF0000, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_ratlantean_ratbot"));
            event.getRegistry().register(new SpawnEggItem(RatlantisEntityRegistry.RAT_BARON, 0X9F0A03, 0XE3E5DA, new Item.Properties().group(RatsMod.getRatlantisTab())).setRegistryName("rats:spawn_egg_rat_baron"));
        }
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            RatConfig.bakeClient(config);
            RatlantisConfig.bakeClient(config);
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            RatConfig.bakeServer(config);
            RatlantisConfig.bakeServer(config);
        }
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        if (RatsMod.RATLANTIS_LOADED) {
            RatlantisStructureRegistry.MARBLE_PILE = Registry.register(Registry.FEATURE, "rats:marble_pile", new FeatureMarblePile(NoFeatureConfig.field_236558_a_));
        }
    }

    @SubscribeEvent
    public static void registerWorldGenFeatures(RegistryEvent.Register<Structure<?>> event) {
        if (RatsMod.RATLANTIS_LOADED) {
            RatlantisStructureRegistry.init();
            event.getRegistry().registerAll(RatsWorldRegistry.RAT_RUINS, RatsWorldRegistry.FLYING_DUTCHRAT, RatsWorldRegistry.RUNWAY);
            RatsWorldRegistry.putStructureOnAList("rats:ratlantis_ruins_structure", RatsWorldRegistry.RAT_RUINS);
            RatsWorldRegistry.putStructureOnAList("rats:dutchrat_ship", RatsWorldRegistry.FLYING_DUTCHRAT);
            RatsWorldRegistry.putStructureOnAList("rats:runway", RatsWorldRegistry.RUNWAY);
        }
    }

    @SubscribeEvent
    public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
        try {
            for (Field f : RatsContainerRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof ContainerType) {
                    event.getRegistry().register((ContainerType) obj);
                } else if (obj instanceof ContainerType[]) {
                    for (ContainerType container : (ContainerType[]) obj) {
                        event.getRegistry().register(container);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerSurfaces(final RegistryEvent.Register<SurfaceBuilder<?>> event) {
        if (RatsMod.RATLANTIS_LOADED) {
            event.getRegistry().register(RatsWorldRegistry.RATLANTIS_SURFACE);
        }
    }

    public void preInit() {
    }

    public void init() {
        //RatsWorldRegistry.register();
    }

    public void postInit() {
    }

    public Object getArmorModel(int index) {
        return null;
    }

    public boolean shouldRenderNameplates() {
        return true;
    }

    public void openCheeseStaffGui() {
    }

    public EntityRat getRefrencedRat() {
        return null;
    }

    public void setRefrencedRat(EntityRat rat) {
    }

    public TileEntity getRefrencedTE() {
        return null;
    }

    public void setRefrencedTE(TileEntity te) {
    }

    public void setCheeseStaffContext(BlockPos pos, Direction facing) {
    }

    public void addParticle(String name, double x, double y, double z, double motX, double motY, double motZ) {
    }

    public ItemStack getRefrencedItem() {
        return ItemStack.EMPTY;
    }

    public void setRefrencedItem(ItemStack stack) {
    }

    public World getWorld() {
        return null;
    }

    public void handlePacketAutoCurdlerFluid(long blockPos, FluidStack fluid) {
    }

    public void handlePacketCheeseStaffRat(int entityId, boolean clear) {
    }

    public void handlePacketUpdateTileSlots(long blockPos, CompoundNBT tag) {
    }

    public Item.Properties setupTEISR(Item.Properties props) {
        return props;
    }

    public void openRadiusStaffGui() {
    }


    public float getPartialTicks() {
        return 0;
    }
}
