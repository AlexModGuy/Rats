package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
					CompoundTag tag = stack.getTag();
					if (tag != null && tag.contains("Items", 9)) {
						NonNullList<ItemStack> upgradeList = NonNullList.withSize(combined.getUpgradeSlots(), ItemStack.EMPTY);
						ContainerHelper.loadAllItems(tag, upgradeList);
						for (ItemStack selectedUpgrade : upgradeList) {
							if (selectedUpgrade.getItem() == item) {
								return selectedUpgrade;
							}
						}
					}
				}
			}
		}
		return ItemStack.EMPTY;
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
					CompoundTag tag = stack.getTag();
					if (tag != null && tag.contains("Items", 9)) {
						NonNullList<ItemStack> upgradeList = NonNullList.withSize(combined.getUpgradeSlots(), ItemStack.EMPTY);
						ContainerHelper.loadAllItems(tag, upgradeList);
						for (ItemStack selectedUpgrade : upgradeList) {
							if (upgrade.test(selectedUpgrade.getItem())) {
								function.accept(selectedUpgrade, slot);
							}
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
					CompoundTag tag = stack.getTag();
					if (tag != null && tag.contains("Items", 9)) {
						NonNullList<ItemStack> upgradeList = NonNullList.withSize(combined.getUpgradeSlots(), ItemStack.EMPTY);
						ContainerHelper.loadAllItems(tag, upgradeList);
						for (ItemStack selectedUpgrade : upgradeList) {
							if (selectedUpgrade.getItem() instanceof BaseRatUpgradeItem upgrade && function.apply(upgrade) != def) {
								return function.apply(upgrade);
							}
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
