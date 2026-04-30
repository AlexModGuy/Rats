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

	// 1.21: NeoForge IItemExtension.getEquipmentSlot(ItemStack) lets non-ArmorItem items declare a slot for
	// auto-equip-on-shift-click. Mob.getEquipmentSlotForItem reads this, so PartyHat goes to the head slot.
	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
		return slot == EquipmentSlot.HEAD;
	}
}
