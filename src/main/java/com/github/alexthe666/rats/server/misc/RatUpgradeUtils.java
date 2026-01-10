package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class RatUpgradeUtils {

	public static final EquipmentSlot[] UPGRADE_SLOTS = new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	public static ItemStack getUpgrade(TamedRat rat, Item item) {

		for (EquipmentSlot slot : UPGRADE_SLOTS) {
			ItemStack stack = rat.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				if (stack.getItem() == item) {
					return stack;
				}
				if (stack.getItem() instanceof CombinedUpgrade combined) {
					NonNullList<ItemStack> upgradeList = loadItemsFromStack(stack, combined.getUpgradeSlots());
					for (ItemStack selectedUpgrade : upgradeList) {
						if (selectedUpgrade.getItem() == item) {
							return selectedUpgrade;
						}
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	private static NonNullList<ItemStack> loadItemsFromStack(ItemStack stack, int size) {
		NonNullList<ItemStack> upgradeList = NonNullList.withSize(size, ItemStack.EMPTY);
		// Try to load from CONTAINER component first (new 1.21 system)
		ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
		if (contents != null) {
			contents.copyInto(upgradeList);
			return upgradeList;
		}
		// Fallback: try CUSTOM_DATA for legacy data
		CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
		if (customData != null) {
			CompoundTag tag = customData.copyTag();
			if (tag.contains("Items", Tag.TAG_LIST)) {
				ListTag listTag = tag.getList("Items", Tag.TAG_COMPOUND);
				for (int i = 0; i < listTag.size(); i++) {
					CompoundTag itemTag = listTag.getCompound(i);
					int slot = itemTag.getByte("Slot") & 255;
					if (slot < size) {
						// Parse item from NBT using simple approach
						if (itemTag.contains("id")) {
							var itemId = net.minecraft.resources.ResourceLocation.parse(itemTag.getString("id"));
							var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId);
							int count = itemTag.contains("count") ? itemTag.getInt("count") : (itemTag.contains("Count") ? itemTag.getByte("Count") : 1);
							upgradeList.set(slot, new ItemStack(item, count));
						}
					}
				}
			}
		}
		return upgradeList;
	}

	public static boolean hasUpgrade(TamedRat rat, Item item) {
		if (rat.hasAnyUpgrades()) {
			return getUpgrade(rat, item) != ItemStack.EMPTY;
		} else {
			return false;
		}
	}

	public static void forEachUpgrade(TamedRat rat, Predicate<Item> upgrade, BiConsumer<ItemStack, EquipmentSlot> function) {
		for (EquipmentSlot slot : UPGRADE_SLOTS) {
			ItemStack stack = rat.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				if (upgrade.test(stack.getItem())) {
					function.accept(stack, slot);
				}
				if (stack.getItem() instanceof CombinedUpgrade combined) {
					NonNullList<ItemStack> upgradeList = loadItemsFromStack(stack, combined.getUpgradeSlots());
					for (ItemStack selectedUpgrade : upgradeList) {
						if (upgrade.test(selectedUpgrade.getItem())) {
							function.accept(selectedUpgrade, slot);
						}
					}
				}
			}
		}
	}

	public static boolean forEachUpgradeBool(TamedRat rat, Function<BaseRatUpgradeItem, Boolean> function, boolean def) {
		for (EquipmentSlot slot : UPGRADE_SLOTS) {
			ItemStack stack = rat.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof CombinedUpgrade combined) {
					NonNullList<ItemStack> upgradeList = loadItemsFromStack(stack, combined.getUpgradeSlots());
					for (ItemStack selectedUpgrade : upgradeList) {
						if (selectedUpgrade.getItem() instanceof BaseRatUpgradeItem upgrade && function.apply(upgrade) != def) {
							return function.apply(upgrade);
						}
					}
				} else if (stack.getItem() instanceof BaseRatUpgradeItem upgrade && function.apply(upgrade) != def) {
					return function.apply(upgrade);
				}
			}
		}
		return def;
	}
}







