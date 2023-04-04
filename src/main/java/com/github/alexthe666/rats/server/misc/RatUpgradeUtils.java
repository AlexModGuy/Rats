package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.CombinedRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.JuryRiggedRatUpgradeItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
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
				if (stack.getItem() instanceof CombinedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (stack1.getItem() == item) {
								return stack1;
							}
						}
					}
				}
				if (stack.getItem() instanceof JuryRiggedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (stack1.getItem() == item) {
								return stack1;
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

	public static void forEachUpgrade(TamedRat rat, Predicate<Item> upgrade, Consumer<ItemStack> function) {
		for (EquipmentSlot slot : UPGRADE_SLOTS) {
			ItemStack stack = rat.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				if (upgrade.test(stack.getItem())) {
					function.accept(stack);
				}
				if (stack.getItem() instanceof CombinedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (upgrade.test(stack.getItem())) {
								function.accept(stack1);
							}
						}
					}
				}
				if (stack.getItem() instanceof JuryRiggedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (upgrade.test(stack.getItem())) {
								function.accept(stack1);
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
				if (stack.getItem() instanceof CombinedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (stack1.getItem() instanceof BaseRatUpgradeItem upgrade && function.apply(upgrade) != def) {
								return function.apply(upgrade);
							}
						}
					}
				} else if (stack.getItem() instanceof JuryRiggedRatUpgradeItem) {
					CompoundTag CompoundNBT1 = stack.getTag();
					if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
						NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
						ContainerHelper.loadAllItems(CompoundNBT1, nonnulllist);
						for (ItemStack stack1 : nonnulllist) {
							if (stack1.getItem() instanceof BaseRatUpgradeItem upgrade && function.apply(upgrade) != def) {
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
