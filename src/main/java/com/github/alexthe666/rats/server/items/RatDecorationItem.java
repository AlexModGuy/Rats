package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RatDecorationItem extends Item implements RatCageDecoration {

	public RatDecorationItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public Direction getSupportedFace(Direction inputDir) {
		if (this == RatsItemRegistry.RAT_WATER_BOTTLE.get()) {
			return inputDir;
		} else if (this == RatsItemRegistry.RAT_SEED_BOWL.get() || this == RatsItemRegistry.RAT_WHEEL.get()) {
			return Direction.DOWN;
		} else if (this == RatsItemRegistry.RAT_BREEDING_LANTERN.get()) {
			return Direction.UP;
		} else {
			return inputDir;
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item.rats.cage_decoration.desc").withStyle(ChatFormatting.GRAY));
		if (this == RatsItemRegistry.RAT_WHEEL.get()) {
			tooltip.add(Component.translatable("item.rats.rat_wheel.desc").withStyle(ChatFormatting.GRAY));
		}
	}
}
