package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlagueScytheItem extends SwordItem {
	public PlagueScytheItem(Item.Properties properties) {
		// 1.21: SwordItem(Tier, Properties); damage/speed configured via Properties.attributes(...).
		super(RatsToolMaterialRegistry.PLAGUE_SCYTHE, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 1.0F;
	}

	// PORT-STUB: 1.21 Item.isCorrectToolForDrops takes (ItemStack, BlockState); the no-arg override pattern was dropped.
	public boolean isCorrectToolForDrops(BlockState state) {
		return false;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
		return toolAction == ItemAbilities.SWORD_SWEEP;
	}
}