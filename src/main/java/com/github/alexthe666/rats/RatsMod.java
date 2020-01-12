package com.github.alexthe666.rats;

import com.github.alexthe666.rats.client.ClientProxy;
import com.github.alexthe666.rats.server.CommonProxy;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.*;
import com.github.alexthe666.rats.server.potion.PotionConfitByaldi;
import com.github.alexthe666.rats.server.potion.PotionPlague;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RatsMod.MODID)
@Mod.EventBusSubscriber(modid = RatsMod.MODID)
public class RatsMod {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rats";
    public static final String NAME = "Rats";
    public static final String VERSION = "4.0.0";
    public static final SimpleChannel NETWORK_WRAPPER;
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RatsItemRegistry.CHEESE);
        }
    };
    public static ItemGroup TAB_UPGRADES = new ItemGroup("rats.upgrades") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(RatsItemRegistry.RAT_UPGRADE_BASIC);
        }
    };
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static Effect CONFIT_BYALDI_POTION = new PotionConfitByaldi();
    public static Effect PLAGUE_POTION = new PotionPlague();
    public static boolean ICEANDFIRE_LOADED;
    private static int packetsRegistered = 0;

    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("rats", "main_channel"));
        String version = PROTOCOL_VERSION;
        version.getClass();
        channel = channel.clientAcceptedVersions(version::equals);
        version = PROTOCOL_VERSION;
        version.getClass();
        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> {
            return PROTOCOL_VERSION;
        }).simpleChannel();
    }

    public RatsMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
        PROXY.init();
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayerEntity player) {
        if (player.server.isDedicatedServer() || !player.getName().equals(player.server.getServerOwner())) {
            NETWORK_WRAPPER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageAutoCurdlerFluid.class, MessageAutoCurdlerFluid::write, MessageAutoCurdlerFluid::read, MessageAutoCurdlerFluid.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageCheeseStaffRat.class, MessageCheeseStaffRat::write, MessageCheeseStaffRat::read, MessageCheeseStaffRat.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageCheeseStaffSync.class, MessageCheeseStaffSync::write, MessageCheeseStaffSync::read, MessageCheeseStaffSync.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDancingRat.class, MessageDancingRat::write, MessageDancingRat::read, MessageDancingRat.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageIncreaseRatRecipe.class, MessageIncreaseRatRecipe::write, MessageIncreaseRatRecipe::read, MessageIncreaseRatRecipe.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageRatCommand.class, MessageRatCommand::write, MessageRatCommand::read, MessageRatCommand.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageRatDismount.class, MessageRatDismount::write, MessageRatDismount::read, MessageRatDismount.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSwingArm.class, MessageSwingArm::write, MessageSwingArm::read, MessageSwingArm.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSyncThrownBlock.class, MessageSyncThrownBlock::write, MessageSyncThrownBlock::read, MessageSyncThrownBlock.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateRatFluid.class, MessageUpdateRatFluid::write, MessageUpdateRatFluid::read, MessageUpdateRatFluid.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateTileSlots.class, MessageUpdateTileSlots::write, MessageUpdateTileSlots::read, MessageUpdateTileSlots.Handler::handle);
    }
}