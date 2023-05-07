package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface CombinedUpgrade {

	int getUpgradeSlots();

	default void addTooltip(ItemStack stack, List<Component> tooltip) {
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.getUpgradeSlots(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist);
			int i = 0;
			for (ItemStack itemstack : nonnulllist) {
				if (!itemstack.isEmpty()) {
					if (i <= 4) {
						++i;
						tooltip.add(Component.literal(String.format("%s", itemstack.getDisplayName().getString())));
					} else {
						break;
					}
				}
			}
			if (nonnulllist.stream().filter(stack1 -> !stack1.isEmpty()).toList().size() > 5) {
				tooltip.add(Component.translatable("item.rats.rat_upgrade_combined.and_more", nonnulllist.stream().filter(stack1 -> !stack1.isEmpty()).toList().size() - 5).withStyle(ChatFormatting.GRAY));
			}
		}
	}
}
