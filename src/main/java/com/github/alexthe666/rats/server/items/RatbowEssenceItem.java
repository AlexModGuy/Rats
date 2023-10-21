package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.client.render.RatsRenderType;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class RatbowEssenceItem extends Item {
	public RatbowEssenceItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		RatsRenderType.GlintType type = RatsRenderType.GlintType.getGlintBasedOnKeyword(stack.getHoverName().getString());
		if (type != null && type.changesItemTexture()) {
			tooltip.add(Component.translatable(RatsLangConstants.RATBOW_ESSENCE_FLAG, WordUtils.capitalize(type.name().toLowerCase(Locale.ROOT))).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
		}
	}
}
