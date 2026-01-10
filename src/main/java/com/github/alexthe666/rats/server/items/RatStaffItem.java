package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsCapabilityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.capability.SelectedRat;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
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
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		var level = Minecraft.getInstance().level;
		if (level != null && level.isClientSide()) {
			SelectedRat selectedRatCap = Minecraft.getInstance().player.getCapability(RatsCapabilityRegistry.SELECTED_RAT);
			if (selectedRatCap != null) {
				TamedRat rat = selectedRatCap.getSelectedRat();
				if (rat != null) {
					tooltip.add(Component.translatable(RatsLangConstants.CHEESE_STAFF_SELECTED, rat.getDisplayName(), rat.getUUID().toString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
				}
			}
		}
	}
}







