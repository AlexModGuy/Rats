package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public interface CombinedUpgrade {

	int getUpgradeSlots();

	default void addTooltip(ItemStack stack, List<Component> tooltip) {
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

		if (tag.contains("Items", 9)) {
			NonNullList<ItemStack> nonnulllist = NonNullList.withSize(this.getUpgradeSlots(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, nonnulllist, net.minecraft.core.RegistryAccess.EMPTY);
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
				tooltip.add(Component.translatable(RatsLangConstants.AND_MORE, nonnulllist.stream().filter(stack1 -> !stack1.isEmpty()).toList().size() - 5).withStyle(ChatFormatting.GRAY));
			}
		}
	}
}
