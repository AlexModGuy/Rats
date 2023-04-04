package com.github.alexthe666.rats.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LoreTagItem extends Item {

	private final int lines;
	private final boolean foil;

	public LoreTagItem(Properties properties, int lines, boolean foil) {
		super(properties);
		this.lines = lines;
		this.foil = foil;
	}

	public LoreTagItem(Properties properties, int lines) {
		this(properties, lines, false);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (this.lines > 0) {
			for (int i = 0; i < this.lines; i++) {
				tooltip.add(Component.translatable(this.getDescriptionId() + ".desc" + (this.lines == 1 ? "" : i)).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) || this.foil;
	}
}
