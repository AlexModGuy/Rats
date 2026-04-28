package com.github.alexthe666.rats.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.*;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class RatsMenuRegistry {

	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, RatsMod.MODID);

	public static final DeferredHolder<MenuType<?>, MenuType<RatMenu>> RAT_CONTAINER = MENUS.register("rat_container", () -> new MenuType<>(RatMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final DeferredHolder<MenuType<?>, MenuType<RatCraftingTableMenu>> RAT_CRAFTING_TABLE_CONTAINER = MENUS.register("rat_crafting_table_container", () -> IMenuTypeExtension.create(RatCraftingTableMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<RatUpgradeMenu>> RAT_UPGRADE_CONTAINER = MENUS.register("rat_upgrade_container", () -> new MenuType<>(RatUpgradeMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final DeferredHolder<MenuType<?>, MenuType<JuryRiggedRatUpgradeMenu>> RAT_UPGRADE_JR_CONTAINER = MENUS.register("rat_upgrade_jurry_rigged_container", () -> new MenuType<>(JuryRiggedRatUpgradeMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final DeferredHolder<MenuType<?>, MenuType<UpgradeCombinerMenu>> UPGRADE_COMBINER_CONTAINER = MENUS.register("upgrade_combiner_container", () -> new MenuType<>(UpgradeCombinerMenu::new, FeatureFlags.REGISTRY.allFlags()));
	public static final DeferredHolder<MenuType<?>, MenuType<AutoCurdlerMenu>> AUTO_CURDLER_CONTAINER = MENUS.register("auto_curdler", () -> new MenuType<>(AutoCurdlerMenu::new, FeatureFlags.REGISTRY.allFlags()));
}
