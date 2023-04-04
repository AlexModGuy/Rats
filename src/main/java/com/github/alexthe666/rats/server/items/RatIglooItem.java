package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.block.RatCageBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RatIglooItem extends Item implements RatCageDecoration {
	public final DyeColor color;

	public RatIglooItem(Item.Properties properties, DyeColor color) {
		super(properties);
		this.color = color;
	}

	@Override
	public boolean canStay(Level world, BlockPos pos, RatCageBlock cageBlock) {
		return cageBlock.canFenceConnectTo(world.getBlockState(pos.below())) != 1;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item.rats.cage_decoration.desc").withStyle(ChatFormatting.GRAY));
	}
}
