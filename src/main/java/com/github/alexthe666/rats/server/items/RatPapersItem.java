package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class RatPapersItem extends Item {

	public RatPapersItem(Item.Properties properties) {
		super(properties);
	}

	public boolean isFoil(ItemStack stack) {
		return isEntityBound(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (!isEntityBound(stack)) {
			tooltip.add(Component.translatable("item.rats.rat_papers.desc0").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("item.rats.rat_papers.desc1").withStyle(ChatFormatting.GRAY));
		}
		tooltip.add(Component.translatable("item.rats.rat_papers.desc2").withStyle(ChatFormatting.GRAY));
		if (stack.getTag() != null) {
			tooltip.add(Component.translatable("item.rats.rat_papers.rat_desc").withStyle(ChatFormatting.GRAY));
			String ratName = I18n.get("entity.rats.rat");
			String entity = stack.getTag().getString("RatName");
			if (stack.getTag().hasUUID("RatUUID")) {
				if (entity.isEmpty()) {
					tooltip.add(Component.literal(ratName + " (" + stack.getTag().getUUID("RatUUID") + ")").withStyle(ChatFormatting.GRAY));
				} else {
					tooltip.add(Component.literal(entity).withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}

	public static boolean isEntityBound(ItemStack stack) {
		return stack.getOrCreateTag().hasUUID("RatUUID");
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		CompoundTag nbt = stack.getOrCreateTag();
		if (target instanceof Player transferTo) {
			try {
				if (nbt.hasUUID("RatUUID")) {
					UUID ratUUID = nbt.getUUID("RatUUID");
					if (!target.level().isClientSide()) {
						Entity entity = target.level().getServer().getLevel(target.level().dimension()).getEntity(ratUUID);
						if (entity instanceof TamedRat rat) {
							if (rat.isTame() && rat.isOwnedBy(player)) {
								rat.tame(transferTo);
								stack.shrink(1);
								player.level().playSound(player, player.blockPosition(), RatsSoundRegistry.RAT_TRANSFER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
							}
						}
					}
					return InteractionResult.sidedSuccess(player.level().isClientSide());
				}
			} catch (Exception e) {
				player.displayClientMessage(Component.literal("Couldnt transfer ownership! Check the log and report this!").withStyle(ChatFormatting.RED), true);
				RatsMod.LOGGER.error("Couldnt transfer rat ownership to {}!", transferTo.getStringUUID(), e);
			}
			return InteractionResult.PASS;
		}

		if (target instanceof TamedRat rat && rat.isOwnedBy(player)) {
			if (rat.hasCustomName()) {
				nbt.putString("RatName", rat.getCustomName().getString());
			}
			nbt.putUUID("RatUUID", rat.getUUID());
			stack.setTag(nbt);

			return InteractionResult.sidedSuccess(player.level().isClientSide());
		}
		return InteractionResult.PASS;
	}
}
