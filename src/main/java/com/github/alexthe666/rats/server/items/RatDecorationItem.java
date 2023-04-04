package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.block.RatCageBlock;
import com.github.alexthe666.rats.server.block.RatCageDecoratedBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
	public boolean canStay(Level level, BlockPos pos, RatCageBlock cageBlock) {
		if (this == RatsItemRegistry.RAT_WATER_BOTTLE.get()) {
			if (cageBlock instanceof RatCageDecoratedBlock && level.getBlockState(pos).getBlock() instanceof RatCageDecoratedBlock) {
				Direction facing = level.getBlockState(pos).getValue(RatCageDecoratedBlock.FACING);
				return cageBlock.canFenceConnectTo(level.getBlockState(pos.relative(facing))) == 0;
			} else {
				return true;
			}
		} else if (this == RatsItemRegistry.RAT_SEED_BOWL.get()) {
			return cageBlock.canFenceConnectTo(level.getBlockState(pos.below())) != 1;
		} else if (this == RatsItemRegistry.RAT_WHEEL.get()) {
			return cageBlock.canFenceConnectTo(level.getBlockState(pos.below())) == 0;
		} else if (this == RatsItemRegistry.RAT_BREEDING_LANTERN.get()) {
			return cageBlock.canFenceConnectTo(level.getBlockState(pos.above())) == 0;
		} else {
			return false;
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
