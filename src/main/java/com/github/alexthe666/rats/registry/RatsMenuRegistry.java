package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.*;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RatsMenuRegistry {

	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RatsMod.MODID);

	public static final RegistryObject<MenuType<RatMenu>> RAT_CONTAINER = MENUS.register("rat_container", () -> new MenuType<>(RatMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final RegistryObject<MenuType<RatCraftingTableMenu>> RAT_CRAFTING_TABLE_CONTAINER = MENUS.register("rat_crafting_table_container", () -> IForgeMenuType.create(RatCraftingTableMenu::new));
	public static final RegistryObject<MenuType<RatUpgradeMenu>> RAT_UPGRADE_CONTAINER = MENUS.register("rat_upgrade_container", () -> new MenuType<>(RatUpgradeMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final RegistryObject<MenuType<JuryRiggedRatUpgradeMenu>> RAT_UPGRADE_JR_CONTAINER = MENUS.register("rat_upgrade_jurry_rigged_container", () -> new MenuType<>(JuryRiggedRatUpgradeMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final RegistryObject<MenuType<UpgradeCombinerMenu>> UPGRADE_COMBINER_CONTAINER = MENUS.register("upgrade_combiner_container", () -> new MenuType<>(UpgradeCombinerMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final RegistryObject<MenuType<AutoCurdlerMenu>> AUTO_CURDLER_CONTAINER = MENUS.register("auto_curdler", () -> new MenuType<>(AutoCurdlerMenu::new, FeatureFlags.REGISTRY.allFlags()));
}
