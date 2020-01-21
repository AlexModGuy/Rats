package com.github.alexthe666.rats;

import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.advancements.RatsAdvancementRegistry;
import com.github.alexthe666.rats.server.compat.ChiselCompatBridge;
import com.github.alexthe666.rats.server.compat.CraftTweakerCompatBridge;
import com.github.alexthe666.rats.server.compat.ThaumcraftCompatBridge;
import com.github.alexthe666.rats.server.compat.TinkersCompatBridge;
import com.github.alexthe666.rats.server.entity.EntityIllagerPiper;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.events.ServerEvents;
import com.github.alexthe666.rats.server.inventory.GuiHandler;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.items.RatsNuggetRegistry;
import com.github.alexthe666.rats.server.message.*;
import com.github.alexthe666.rats.server.potion.PotionConfitByaldi;
import com.github.alexthe666.rats.server.potion.PotionPlague;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import com.github.alexthe666.rats.server.world.RatsWorldRegistry;
import com.github.alexthe666.rats.server.world.village.RatsVillageRegistry;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

@Mod(modid = RatsMod.MODID, name = RatsMod.NAME, dependencies = "required-after:llibrary@[" + RatsMod.LLIBRARY_VERSION + ",)", version = RatsMod.VERSION, guiFactory = "com.github.alexthe666.rats.client.gui.RatsGuiFactory")
public class RatsMod {
    public static final String MODID = "rats";
    public static final String NAME = "Rats";
    public static final String VERSION = "3.2.4";
    public static final String LLIBRARY_VERSION = "1.7.9";
    public static CreativeTabs TAB = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RatsItemRegistry.CHEESE);
        }
    };
    public static CreativeTabs TAB_UPGRADES = new CreativeTabs("rats.upgrades") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC);
        }
    };
    @Mod.Instance(value = MODID)
    public static RatsMod INSTANCE;
    @SidedProxy(clientSide = "com.github.alexthe666.rats.client.ClientProxy", serverSide = "com.github.alexthe666.rats.server.CommonProxy")
    public static CommonProxy PROXY;
    @NetworkWrapper({MessageRatCommand.class, MessageRatDismount.class, MessageIncreaseRatRecipe.class, MessageAutoCurdlerFluid.class, MessageCheeseStaffRat.class, MessageCheeseStaffSync.class, MessageSyncThrownBlock.class, MessageDancingRat.class, MessageSwingArm.class, MessageUpdateRatFluid.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    public static Logger logger;
    public static Potion CONFIT_BYALDI_POTION = new PotionConfitByaldi();
    public static Potion PLAGUE_POTION = new PotionPlague();
    public static Configuration config;
    public static RatConfig CONFIG_OPTIONS = new RatConfig();
    public static boolean iafLoaded;

    public static void loadConfig() {
        File configFile = new File(Loader.instance().getConfigDir(), "rats.cfg");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                logger.warn("Could not create a new Rats config file.");
                logger.warn(e.getLocalizedMessage());
            }
        }
        config = new Configuration(configFile);
        config.load();
    }

    public static void syncConfig() {
        CONFIG_OPTIONS.init(config);
        config.save();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadConfig();
        syncConfig();
        MinecraftForge.EVENT_BUS.register(PROXY);
        PROXY.preInit();
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        logger = event.getModLog();
        RatsAdvancementRegistry.INSTANCE.init();
        RatsRecipeRegistry.preRegister();
        CraftTweakerCompatBridge.loadTweakerCompat();
        TinkersCompatBridge.loadTinkersCompat();
        ThaumcraftCompatBridge.loadThaumcraftCompat();
        iafLoaded = Loader.isModLoaded("iceandfire");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Rats is initializing");
        PROXY.init();
        ChiselCompatBridge.loadChiselCompat();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        RatsRecipeRegistry.register();
        RatsWorldRegistry.register();
        RatsVillageRegistry.register();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit();
        RatsNuggetRegistry.init();
        TinkersCompatBridge.loadTinkersPostInitCompat();
        if (RatsMod.CONFIG_OPTIONS.spawnRats) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
                    List<Biome.SpawnListEntry> spawnList = RatsMod.CONFIG_OPTIONS.ratsSpawnLikeMonsters ? biome.getSpawnableList(EnumCreatureType.MONSTER) : biome.getSpawnableList(EnumCreatureType.CREATURE);
                    spawnList.add(new Biome.SpawnListEntry(EntityRat.class, RatsMod.CONFIG_OPTIONS.ratSpawnRate, 1, 3));
                }
            }
        }
        if (RatsMod.CONFIG_OPTIONS.spawnPiper) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.END) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
                    List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
                    if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MAGICAL) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPOOKY)) {
                        //3 times as likely to spawn in dark forests
                        spawnList.add(new Biome.SpawnListEntry(EntityIllagerPiper.class, RatsMod.CONFIG_OPTIONS.piperSpawnRate * 3, 1, 1));
                    } else {
                        spawnList.add(new Biome.SpawnListEntry(EntityIllagerPiper.class, RatsMod.CONFIG_OPTIONS.piperSpawnRate, 1, 1));
                    }
                }
            }
        }
    }

}
