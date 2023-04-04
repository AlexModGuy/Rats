package com.github.alexthe666.rats.server.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PlagueLeechItem extends PlagueHealerItem {

	public PlagueLeechItem(Item.Properties properties) {
		super(properties, 0.5F);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (!player.isCreative()) {
			player.getItemInHand(hand).shrink(1);
			player.hurt(level.damageSources().cactus(), 2);
		}
		return super.use(level, player, hand);
	}

}
