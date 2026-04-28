package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
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

	private static CompoundTag readTag(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (!isEntityBound(stack)) {
			tooltip.add(Component.translatable("item.rats.rat_papers.desc0").withStyle(ChatFormatting.GRAY));
			tooltip.add(Component.translatable("item.rats.rat_papers.desc1").withStyle(ChatFormatting.GRAY));
		}
		tooltip.add(Component.translatable("item.rats.rat_papers.desc2").withStyle(ChatFormatting.GRAY));
		CompoundTag nbt = readTag(stack);
		if (!nbt.isEmpty()) {
			String ratName = I18n.get("entity.rats.tamed_rat");
			String entity = nbt.getString("RatName");
			Component rat = Component.empty();
			if (nbt.hasUUID("RatUUID")) {
				if (entity.isEmpty()) {
					rat = Component.literal(ratName + " (" + nbt.getUUID("RatUUID") + ")").withStyle(ChatFormatting.GRAY);
				} else {
					rat = Component.literal(entity).withStyle(ChatFormatting.GRAY);
				}
			}
			tooltip.add(Component.translatable(RatsLangConstants.RAT_PAPERS_BOUND_RAT, rat.getString()).withStyle(ChatFormatting.GRAY));
		}
	}

	public static boolean isEntityBound(ItemStack stack) {
		return readTag(stack).hasUUID("RatUUID");
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		CompoundTag nbt = readTag(stack);
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
			stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));

			return InteractionResult.sidedSuccess(player.level().isClientSide());
		}
		return InteractionResult.PASS;
	}
}
