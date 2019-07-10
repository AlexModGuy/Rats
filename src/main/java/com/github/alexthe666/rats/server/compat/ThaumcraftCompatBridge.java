package com.github.alexthe666.rats.server.compat;

import com.github.alexthe666.rats.server.compat.thaumcraft.ThaumcraftCompat;
import net.minecraftforge.fml.common.Loader;

public class ThaumcraftCompatBridge {
    private static final String TC_MOD_ID = "thaumcraft";

    public static void loadThaumcraftCompat() {
        if (Loader.isModLoaded(TC_MOD_ID)) {
            ThaumcraftCompat.register();
        }
    }

}
