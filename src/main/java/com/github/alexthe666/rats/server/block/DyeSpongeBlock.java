package com.github.alexthe666.rats.server.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;

public class DyeSpongeBlock extends Block {

	public DyeSpongeBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
}







