package com.github.alexthe666.rats.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PartyHatItem extends Item implements DyeableLeatherItem {
	public PartyHatItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag compoundtag = stack.getTagElement("display");
		return compoundtag != null && compoundtag.contains("color", 99) ? compoundtag.getInt("color") : 0x25C9E7;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (stack.getTag() == null || !stack.getTag().getCompound("display").contains("color", 99)) {
			tooltip.add(Component.translatable("item.rats.party_hat.desc").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot slot, Entity entity) {
		return slot == EquipmentSlot.HEAD;
	}
}
