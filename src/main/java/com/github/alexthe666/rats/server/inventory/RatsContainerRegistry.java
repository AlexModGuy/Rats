package com.github.alexthe666.rats.server.inventory;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = RatsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(RatsMod.MODID)
public class RatsContainerRegistry {

    public static final ContainerType RAT_CONTAINER = register(new ContainerType(ContainerRat::new), "rat_container");
    public static final ContainerType EMPTY_CONTAINER = register(new ContainerType(ContainerEmpty::new), "empty_container");
    public static final ContainerType RAT_CRAFTING_TABLE_CONTAINER = register(new ContainerType(ContainerRatCraftingTable::new), "rat_crafting_table_container");
    public static final ContainerType RAT_UPGRADE_CONTAINER = register(new ContainerType(ContainerRatUpgrade::new), "rat_upgrade_container");
    public static final ContainerType RAT_UPGRADE_JR_CONTAINER = register(new ContainerType(ContainerRatUpgradeJuryRigged::new), "rat_upgrade_jurry_rigged_container");
    public static final ContainerType UPGRADE_COMBINER_CONTAINER = register(new ContainerType(ContainerUpgradeCombiner::new), "upgrade_combiner_container");
    public static final ContainerType AUTO_CURDLER_CONTAINER = register(new ContainerType(ContainerAutoCurdler::new), "auto_curdler");

    public static ContainerType register(ContainerType type, String name) {
        type.setRegistryName(name);
        return type;
    }
}
