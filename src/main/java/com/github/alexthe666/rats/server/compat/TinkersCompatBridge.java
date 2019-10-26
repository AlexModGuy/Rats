package com.github.alexthe666.rats.server.compat;

import com.github.alexthe666.rats.server.compat.tinkers.TinkersCompat;
import com.github.alexthe666.rats.server.compat.tinkers.TinkersCompatClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class TinkersCompatBridge {
    private static final String TC_MOD_ID = "tconstruct";

    public static void loadTinkersCompat() {
        if (Loader.isModLoaded(TC_MOD_ID)) {
            TinkersCompat.register();
        }
    }

    public static void loadTinkersPostInitCompat() {
        if (Loader.isModLoaded(TC_MOD_ID)) {
            TinkersCompat.post();
        }
    }

    public static void loadTinkersClientCompat() {
        if (Loader.isModLoaded(TC_MOD_ID)) {
            TinkersCompatClient.preInit();
        }
    }

    public static boolean onPlayerSwing(EntityLivingBase swinger, ItemStack stack){
        if (Loader.isModLoaded(TC_MOD_ID)) {
            return TinkersCompat.onPlayerSwing(swinger, stack);
        }
        return false;
    }
}
