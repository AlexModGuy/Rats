package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RatSackItem extends Item {

	public static int RAT_SACK_ROOM = 16;

	public RatSackItem(Item.Properties properties) {
		super(properties);
	}

	public static void packRatIntoSack(ItemStack sack, TamedRat rat, int ratCount) {
		CompoundTag tag = sack.getTag();
		if (tag == null) {
			tag = new CompoundTag();
			sack.setTag(tag);
		}
		CompoundTag ratTag = new CompoundTag();
		rat.addAdditionalSaveData(ratTag);
		if (rat.hasCustomName()) {
			ratTag.putString("CustomName", Component.Serializer.toJson(rat.getCustomName()));
		}
		tag.put("Rat_" + ratCount, ratTag);
	}

	public static int getRatsInSack(ItemStack sack) {
		int ratCount = 0;
		if (sack.getTag() != null) {
			for (String tagInfo : sack.getTag().getAllKeys()) {
				if (tagInfo.contains("Rat")) {
					ratCount++;
				}
			}
		}
		return ratCount;
	}

	public static int ejectRatsFromSack(ItemStack stack, Level level, BlockPos pos) {
		int ratCount = 0;
		if (stack.getTag() != null) {
			for (String tagInfo : stack.getTag().getAllKeys()) {
				if (tagInfo.contains("Rat")) {
					ratCount++;
					CompoundTag ratTag = stack.getTag().getCompound(tagInfo);
					TamedRat rat = new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), level);
					rat.readAdditionalSaveData(ratTag);
					if (!ratTag.getString("CustomName").isEmpty()) {
						rat.setCustomName(Component.Serializer.fromJson(ratTag.getString("CustomName")));
					}
					rat.moveTo(pos.getX() - 0.25F + (level.getRandom().nextFloat() * 0.5F), pos.getY(), pos.getZ() - 0.25F + (level.getRandom().nextFloat() * 0.5F), 0, 0);
					if (!level.isClientSide()) {
						level.addFreshEntity(rat);
					}
				}
			}
		}
		return ratCount;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		int ratCount = 0;
		List<String> ratNames = new ArrayList<>();
		if (stack.getTag() != null) {
			for (String tagInfo : stack.getTag().getAllKeys()) {
				if (tagInfo.contains("Rat")) {
					CompoundTag ratTag = stack.getTag().getCompound(tagInfo);
					ratCount++;
					String ratName = I18n.get("entity.rats.rat");
					if (!ratTag.getString("CustomName").isEmpty()) {
						Component ratNameTag = Component.Serializer.fromJson(ratTag.getString("CustomName"));
						if (ratNameTag != null) {
							ratName = ratNameTag.getString();
						}
					}
					ratNames.add(ratName);
				}
			}
		}
		tooltip.add(Component.translatable("item.rats.rat_sack.contains", ratCount, RAT_SACK_ROOM).withStyle(ChatFormatting.GRAY));
		if (!ratNames.isEmpty()) {
			for (int i = 0; i < ratNames.size(); i++) {
				if (i < 3) {
					tooltip.add(Component.translatable("item.rats.rat_sack.contain_rat", ratNames.get(i)).withStyle(ChatFormatting.GRAY));
				} else {
					break;
				}
			}
			if (ratNames.size() > 3) {
				tooltip.add(Component.translatable("item.rats.rat_sack.and_more").withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
		if (stack.is(RatsItemRegistry.RAT_SACK.get()) && getRatsInSack(stack) > 0) {
			int ratCount = ejectRatsFromSack(stack, context.getLevel(), context.getClickedPos().relative(context.getClickedFace()));

			if (ratCount > 0) {
				context.getPlayer().swing(context.getHand());
				context.getPlayer().displayClientMessage(Component.translatable("entity.rats.rat.sack.release", ratCount), true);
				stack.setTag(new CompoundTag());
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onDestroyed(ItemEntity entity, DamageSource damageSource) {
		ItemStack stack = entity.getItem();
		if (getRatsInSack(stack) > 0) {
			ejectRatsFromSack(stack, entity.getLevel(), entity.blockPosition());
		}
	}
}
