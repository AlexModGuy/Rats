package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import java.util.Iterator;
import java.util.List;

public class RatWhistleItem extends LoreTagItem {

	public RatWhistleItem(Item.Properties properties) {
		super(properties, 1);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.getCooldowns().addCooldown(this, 5);
		float chunksize = 48 * RatConfig.ratFluteDistance;
		List<TamedRat> list = level.getEntitiesOfClass(TamedRat.class, (new AABB(player.getX(), player.getY(), player.getZ(), player.getX() + 1.0D, player.getY() + 1.0D, player.getZ() + 1.0D)).inflate(chunksize, level.getHeight(), chunksize), rat -> rat.getHomePoint().isPresent());
		if (!level.isClientSide()) {
			for (TamedRat rat : list) {
				BlockPos homePos = rat.getHomePoint().get().pos();
				double dist = Math.sqrt(rat.getRatDistanceSq(homePos.getX() + 0.5D, homePos.getY() + 0.5D, homePos.getZ() + 0.5D));
				if (dist > 2F && rat.canMove()) {
					rat.attemptTeleport(homePos.getX() + 0.5D, homePos.getY() + 1.5D, homePos.getZ() + 0.5D);
				}
			}
		}
		player.swing(hand);
		player.displayClientMessage(Component.translatable("item.rats.rat_flute.rat_count", list.size()).withStyle(ChatFormatting.GRAY), true);
		level.playSound(player, player.blockPosition(), RatsSoundRegistry.RAT_WHISTLE.get(), SoundSource.PLAYERS, 1, 1.25F);

		return InteractionResultHolder.success(stack);
	}
}
