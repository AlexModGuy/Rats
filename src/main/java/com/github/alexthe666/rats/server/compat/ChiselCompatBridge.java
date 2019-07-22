package com.github.alexthe666.rats.server.compat;

import com.github.alexthe666.rats.server.compat.thaumcraft.ThaumcraftCompat;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ChiselCompatBridge {

    public static void loadChiselCompat() {
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_raw|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_pillar|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_chiseled|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_tile|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_brick|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_brick_mossy|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_brick_cracked|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_brick_chiseled|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "dirt|rats:marbled_cheese_dirt|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_dirt|0");
        FMLInterModComms.sendMessage("chisel", "variation:add", "marbled_cheese|rats:marbled_cheese_grass|0");
    }
}
