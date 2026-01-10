package com.github.alexthe666.rats.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartyHatItem extends Item {
	public static final int DEFAULT_COLOR = 0x25C9E7;

	public PartyHatItem(Properties properties) {
		super(properties.component(DataComponents.DYED_COLOR, new DyedItemColor(DEFAULT_COLOR, true)));
	}

	public static int getColor(ItemStack stack) {
		DyedItemColor dyed = stack.get(DataComponents.DYED_COLOR);
		return dyed != null ? dyed.rgb() : DEFAULT_COLOR;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		DyedItemColor dyed = stack.get(DataComponents.DYED_COLOR);
		if (dyed == null || dyed.rgb() == DEFAULT_COLOR) {
			tooltip.add(Component.translatable("item.rats.party_hat.desc").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
		return slot == EquipmentSlot.HEAD;
	}
}







