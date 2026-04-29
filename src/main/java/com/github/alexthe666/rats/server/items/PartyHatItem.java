package com.github.alexthe666.rats.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartyHatItem extends Item {

	private static final int DEFAULT_COLOR = 0x25C9E7;

	public PartyHatItem(Properties properties) {
		super(properties.component(DataComponents.DYED_COLOR, new DyedItemColor(DEFAULT_COLOR, false)));
	}

	public int getColor(ItemStack stack) {
		return DyedItemColor.getOrDefault(stack, DEFAULT_COLOR);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
		if (color == null || color.rgb() == DEFAULT_COLOR) {
			tooltip.add(Component.translatable("item.rats.party_hat.desc").withStyle(ChatFormatting.GRAY));
		}
	}

	// PORT-STUB: 1.21 Item.getEquipmentSlot / canEquip removed; equipment slot is now driven by DataComponents.EQUIPPABLE.
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
		return slot == EquipmentSlot.HEAD;
	}
}
