package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsCapabilityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RatStaffItem extends LoreTagItem {
	public RatStaffItem(Properties properties) {
		super(properties, 2, false);
	}

	public int getStaff(ItemStack stack) {
		if (stack.is(RatsItemRegistry.PATROL_STICK.get())) return 2;
		if (stack.is(RatsItemRegistry.RADIUS_STICK.get())) return 1;
		return 0;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		if (level != null && level.isClientSide() && Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).resolve().isPresent()) {
			TamedRat rat = Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).resolve().get().getSelectedRat();
			if (rat != null) {
				tooltip.add(Component.translatable("item.rats.cheese_stick.bound_rat", rat.getDisplayName(), rat.getUUID().toString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
			}
		}
	}
}
