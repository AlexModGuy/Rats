package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlagueHealerItem extends Item {

	private final float healChance;

	public PlagueHealerItem(Item.Properties properties, float healChance) {
		super(properties);
		this.healChance = healChance;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
		if (living.hasEffect(RatsEffectRegistry.PLAGUE.get())) {
			if (level.getRandom().nextDouble() <= this.healChance) {
				living.removeEffect(RatsEffectRegistry.PLAGUE.get());
			}
		}
		if (stack.is(RatsItemRegistry.PLAGUE_STEW.get())) {
			super.finishUsingItem(stack, level, living);
			return new ItemStack(Items.BOWL);
		} else {
			return super.finishUsingItem(stack, level, living);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(RatsLangConstants.PLAGUE_HEAL_CHANCE, (int) (this.healChance * 100F)).withStyle(ChatFormatting.GRAY));
	}
}
