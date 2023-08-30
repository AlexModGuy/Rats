package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class RatFluteItem extends Item {

	public RatFluteItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.TOOT_HORN;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		int commandInt = stack.getOrCreateTag().getInt("Command");
		RatCommand ratCommand = RatCommand.values()[Mth.clamp(commandInt, 0, RatCommand.values().length - 1)];
		if (player.isShiftKeyDown()) {
			commandInt++;
			if (commandInt > RatCommand.values().length - 1) {
				commandInt = 0;
			}
			stack.getOrCreateTag().putInt("Command", commandInt);
			ratCommand = RatCommand.values()[Mth.clamp(commandInt, 0, RatCommand.values().length - 1)];
			level.playSound(player, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.NEUTRAL, 1.0F, 1.25F);
			player.displayClientMessage(Component.translatable(RatsLangConstants.RAT_FLUTE_COMMAND, Component.translatable(ratCommand.getTranslateName())), true);
		} else {
			player.getCooldowns().addCooldown(this, 60);
			float chunksize = 16 * RatConfig.ratFluteDistance;
			List<Entity> list = level.getEntities(player, (new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1.0D, player.getY() + 1.0D, player.getZ() + 1.0D)).inflate(chunksize, level.getHeight(), chunksize));
			Iterator<Entity> itr = list.iterator();
			int ratCount = 0;
			while (itr.hasNext()) {
				Entity entity = itr.next();
				if (entity instanceof TamedRat rat) {
					if (rat.onHearFlute(player, ratCommand)) {
						ratCount++;
					}
				}
			}
			player.swing(hand);
			player.displayClientMessage(Component.translatable(RatsLangConstants.RAT_FLUTE_COUNT, ratCount).withStyle(ChatFormatting.GRAY), true);
			level.playSound(player, player.blockPosition(), RatsSoundRegistry.getFluteSound(), SoundSource.NEUTRAL, 1, 1.25F);
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item.rats.rat_flute.desc0").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("item.rats.rat_flute.desc1").withStyle(ChatFormatting.GRAY));
		if (stack.getTag() != null) {
			RatCommand ratCommand = RatCommand.values()[Mth.clamp(stack.getTag().getInt("Command"), 0, RatCommand.values().length - 1)];
			tooltip.add(Component.translatable(RatsLangConstants.RAT_CURRENT_COMMAND, Component.translatable(ratCommand.getTranslateName())).withStyle(ChatFormatting.GRAY));
		}
	}
}
