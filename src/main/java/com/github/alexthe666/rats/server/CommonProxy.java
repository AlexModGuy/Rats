package com.github.alexthe666.rats.server;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.BlockGenericSlab;
import com.github.alexthe666.rats.server.blocks.RatsBlockRegistry;
import com.github.alexthe666.rats.server.entity.*;
import com.github.alexthe666.rats.server.items.RatsNuggetRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.items.RatsUpgradeConflictRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.github.alexthe666.rats.server.world.RatsWorldRegistry;
import com.github.alexthe666.rats.server.world.village.RatsVillageRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
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
    public static void registerVillagers(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        if (RatsMod.CONFIG_OPTIONS.villagePetShops) {
            event.getRegistry().register(RatsVillageRegistry.PET_SHOP_OWNER);
        }
        event.getRegistry().register(RatsVillageRegistry.PLAGUE_DOCTOR);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : RatsBlockRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    ItemBlock itemBlock = new ItemBlock((Block) obj);
                    if (obj instanceof BlockGenericSlab) {
                        itemBlock = ((BlockGenericSlab) obj).getItemBlock();
                    }
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        ItemBlock itemBlock = new ItemBlock(block);
                        if (block instanceof BlockGenericSlab) {
                            itemBlock = ((BlockGenericSlab) obj).getItemBlock();
                        }
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : RatsItemRegistry.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    if ((obj != RatsItemRegistry.PLASTIC_WASTE && obj != RatsItemRegistry.RAW_PLASTIC) || !RatsMod.CONFIG_OPTIONS.disablePlastic) {
                        event.getRegistry().register((Item) obj);
                    }
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register((item));

                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        RatsUpgradeConflictRegistry.init();
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().register(RatsWorldRegistry.RATLANTIS_BIOME);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
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
        EntitySpawnPlacementRegistry.setPlacementType(EntityPirat.class, EntityLiving.SpawnPlacementType.IN_WATER);
    }

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

    public void preInit() {
    }

    public void init() {

    }

    public void postInit() {
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(RatsMod.MODID)) {
            RatsMod.syncConfig();
        }
    }

    public Object getArmorModel(int index) {
        return null;
    }

    public boolean shouldRenderNameplates() {
        return true;
    }

    public void openCheeseStaffGui() {
    }

    public void openRadiusStaffGui() {
    }

    public EntityRat getRefrencedRat() {
        return null;
    }

    public void setRefrencedRat(EntityRat rat) {
    }

    public void setCheeseStaffContext(BlockPos pos, EnumFacing facing) {
    }

    public void spawnParticle(String name, double x, double y, double z, double motX, double motY, double motZ) {
    }
}
