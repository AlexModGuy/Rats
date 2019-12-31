package com.github.alexthe666.rats.server;

import com.github.alexthe666.rats.ConfigHolder;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.inventory.RatsContainerRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.world.BiomeRatlantis;
import com.github.alexthe666.rats.server.world.RatsWorldRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(RatsMod.MODID)
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
        /*
        registerSpawnable(EntityEntryBuilder.<EntityRat>create(), event, EntityRat.class, "rat", 1, 0X30333E, 0XDAABA1);
        registerSpawnable(EntityEntryBuilder.<EntityIllagerPiper>create(), event, EntityIllagerPiper.class, "illager_piper", 2, 0XCABC42, 0X3B6063);
        registerSpawnable(EntityEntryBuilder.<EntityRatlanteanSpirit>create(), event, EntityRatlanteanSpirit.class, "ratlantean_spirit", 3, 0XEDBD00, 0XFFE8AF);
        registerUnspawnable(EntityEntryBuilder.<EntityRatlanteanFlame>create(), event, EntityRatlanteanFlame.class, "ratlantean_flame", 4);
        registerSpawnable(EntityEntryBuilder.<EntityMarbleCheeseGolem>create(), event, EntityMarbleCheeseGolem.class, "marbled_cheese_golem", 5, 0XE8E4D7, 0X72E955);
        registerUnspawnable(EntityEntryBuilder.<EntityGolemBeam>create(), event, EntityGolemBeam.class, "marbled_cheese_golem_beam", 6);
        registerSpawnable(EntityEntryBuilder.<EntityFeralRatlantean>create(), event, EntityFeralRatlantean.class, "feral_ratlantean", 7, 0X30333E, 0XECECEC);
        registerSpawnable(EntityEntryBuilder.<EntityNeoRatlantean>create(), event, EntityNeoRatlantean.class, "neo_ratlantean", 8, 0X30333E, 0X00EFEF);
        registerUnspawnable(EntityEntryBuilder.<EntityLaserBeam>create(), event, EntityLaserBeam.class, "laser_beam", 9);
        registerUnspawnable(EntityEntryBuilder.<EntityLaserPortal>create(), event, EntityLaserPortal.class, "neo_ratlantean_portal", 10);
        registerUnspawnable(EntityEntryBuilder.<EntityThrownBlock>create(), event, EntityThrownBlock.class, "thrown_block", 11);
        registerUnspawnable(EntityEntryBuilder.<EntityVialOfSentience>create(), event, EntityVialOfSentience.class, "vial_of_sentience", 12);
        registerUnspawnable(EntityEntryBuilder.<EntityPiratBoat>create(), event, EntityPiratBoat.class, "pirat_boat", 13);
        registerSpawnable(EntityEntryBuilder.<EntityPirat>create(), event, EntityPirat.class, "pirat", 14, 0X30333E, 0XAF363A);
        registerUnspawnable(EntityEntryBuilder.<EntityCheeseCannonball>create(), event, EntityCheeseCannonball.class, "cheese_cannonball", 15);
        registerSpawnable(EntityEntryBuilder.<EntityPlagueDoctor>create(), event, EntityPlagueDoctor.class, "plague_doctor", 16, 0X2A292A, 0X515359);
        registerUnspawnable(EntityEntryBuilder.<EntityPurifyingLiquid>create(), event, EntityPurifyingLiquid.class, "purifying_liquid", 17);
        registerSpawnable(EntityEntryBuilder.<EntityBlackDeath>create(), event, EntityBlackDeath.class, "black_death", 18, 0X000000, 0X000000);
        registerSpawnable(EntityEntryBuilder.<EntityPlagueCloud>create(), event, EntityPlagueCloud.class, "plague_cloud", 19, 0X000000, 0X52574D);
        registerSpawnable(EntityEntryBuilder.<EntityPlagueCloud>create(), event, EntityPlagueBeast.class, "plague_beast", 20, 0X000000, 0XECECEC);
        registerUnspawnable(EntityEntryBuilder.<EntityPlagueShot>create(), event, EntityPlagueShot.class, "plague_shot", 21);
        registerUnspawnable(EntityEntryBuilder.<EntityRatCaptureNet>create(), event, EntityRatCaptureNet.class, "rat_capture_net", 22);
        registerUnspawnable(EntityEntryBuilder.<EntityRatDragonFire>create(), event, EntityRatDragonFire.class, "rat_dragon_fire", 23);
        registerUnspawnable(EntityEntryBuilder.<EntityRatArrow>create(), event, EntityRatArrow.class, "rat_arrow", 24);
        EntitySpawnPlacementRegistry.setPlacementType(EntityPirat.class, LivingEntity.SpawnPlacementType.IN_WATER);
        */
    }


    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.RAT, 0X30333E, 0XDAABA1, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_rat"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PIED_PIPER, 0XCABC42, 0X3B6063, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_piper"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.RATLANTEAN_SPIRIT, 0XEDBD00, 0XFFE8AF, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_ratlantean_spirit"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.RATLANTEAN_AUTOMATON, 0XE8E4D7, 0X72E955, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_ratlantean_automaton"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.FERAL_RATLANTEAN, 0X30333E, 0XECECEC, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_feral_ratlantean"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.NEO_RATLANTEAN, 0X30333E, 0X00EFEF, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_neo_ratlantean"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PIRAT, 0X30333E, 0XAF363A, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_pirat"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_DOCTOR, 0X2A292A, 0X515359, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_doctor"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.BLACK_DEATH, 0X000000, 0X000000, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_black_death"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_CLOUD, 0X000000, 0X52574D, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_cloud"));
        event.getRegistry().register(new SpawnEggItem(RatsEntityRegistry.PLAGUE_BEAST, 0X000000, 0XECECEC, new Item.Properties().group(RatsMod.TAB)).setRegistryName("rats:spawn_egg_plague_beast"));

    }
    /*
    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(RatsMod.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerUnspawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(RatsMod.MODID, name), id);
        builder.name(name);
        builder.tracker(64, 1, true);
        event.getRegistry().register(builder.build());
    }

     */
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            RatConfig.bakeClient(config);
        } else if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            RatConfig.bakeServer(config);
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
        // event.getRegistry().register(RatsWorldRegistry.RATLANTIS_SURFACE);
    }

    @SubscribeEvent
    public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(RatsWorldRegistry.RATLANTIS_BIOME = new BiomeRatlantis());
    }

    @SubscribeEvent
    public static void registerModDimensions(final RegistryEvent.Register<ModDimension> event) {
      //  event.getRegistry().register(RatsWorldRegistry.RATLANTIS_DIM);
    }

        public void preInit() {
    }

    public void init() {
        RatsWorldRegistry.register();
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
}
