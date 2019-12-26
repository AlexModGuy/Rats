package com.github.alexthe666.rats.client.gui;

import com.github.alexthe666.rats.server.inventory.RatsContainerRegistry;
import net.minecraft.client.gui.ScreenManager;

public class RatsGuiRegistry {

    public static void register(){
        ScreenManager.registerFactory(RatsContainerRegistry.RAT_CONTAINER, GuiRat::new);
        ScreenManager.registerFactory(RatsContainerRegistry.RAT_CRAFTING_TABLE_CONTAINER, GuiRatCraftingTable::new);
        ScreenManager.registerFactory(RatsContainerRegistry.RAT_UPGRADE_CONTAINER, GuiRatUpgrade::new);
        ScreenManager.registerFactory(RatsContainerRegistry.UPGRADE_COMBINER_CONTAINER, GuiUpgradeCombiner::new);
        ScreenManager.registerFactory(RatsContainerRegistry.AUTO_CURDLER_CONTAINER, GuiAutoCurdler::new);
    }
}
