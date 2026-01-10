package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OreRatNuggetItem extends Item {

	public OreRatNuggetItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		level.playSound(null, player.getX(), player.getY(), player.getZ(), RatsSoundRegistry.RAT_NUGGET_ORE.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
		ItemStack poopStack = getStoredItem(itemstack, new ItemStack(Items.IRON_INGOT));
		if (!player.getInventory().add(poopStack)) {
			player.drop(poopStack, false);
		}
		if (!player.isCreative()) {
			itemstack.shrink(1);
		}
		return InteractionResultHolder.success(itemstack);
	}

	public static ItemStack getStoredItem(ItemStack poopItem, ItemStack fallback) {
		CustomData customData = poopItem.get(DataComponents.CUSTOM_DATA);
		if (customData != null && customData.contains("OreItem")) {
			net.minecraft.nbt.CompoundTag tag = customData.copyTag();
			if (tag.contains("OreItem")) {
				net.minecraft.nbt.CompoundTag oreTag = tag.getCompound("OreItem");
				if (oreTag.contains("id")) {
					var itemId = net.minecraft.resources.ResourceLocation.parse(oreTag.getString("id"));
					var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId);
					int count = oreTag.contains("count") ? oreTag.getInt("count") : (oreTag.contains("Count") ? oreTag.getByte("Count") : 1);
					return new ItemStack(item, count);
				}
			}
		}
		return fallback;
	}

	public static ItemStack getStoredItem(ItemStack poopItem, ItemStack fallback, net.minecraft.core.HolderLookup.Provider provider) {
		CustomData customData = poopItem.get(DataComponents.CUSTOM_DATA);
		if (customData != null && customData.contains("OreItem")) {
			net.minecraft.nbt.CompoundTag tag = customData.copyTag();
			if (tag.contains("OreItem")) {
				net.minecraft.nbt.Tag oreTag = tag.get("OreItem");
				if (oreTag != null) {
					return ItemStack.parseOptional(provider, (net.minecraft.nbt.CompoundTag) oreTag);
				}
			}
		}
		return fallback;
	}

	public static ItemStack getIngot(Level level, ItemStack stack) {
		SingleRecipeInput input = new SingleRecipeInput(stack);
		return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, level)
			.map(holder -> holder.value().getResultItem(level.registryAccess()).copy())
			.filter(result -> !result.isEmpty())
			.orElse(ItemStack.EMPTY);
	}

	public static ItemStack saveResourceToNugget(ItemStack resource, net.minecraft.core.HolderLookup.Provider provider) {
		ItemStack stack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE.get());
		net.minecraft.nbt.CompoundTag nuggetTag = new net.minecraft.nbt.CompoundTag();
		net.minecraft.nbt.Tag oreTag = resource.save(provider);
		nuggetTag.put("OreItem", oreTag);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nuggetTag));
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (flag.isCreative()) {
			ItemStack ingot = getStoredItem(stack, new ItemStack(Items.AIR));
			tooltip.add(Component.translatable(RatsLangConstants.ORE_NUGGET_CONTAINS, ingot.getDisplayName().getString()).withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item.rats.rat_nugget_ore.desc").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
		}
	}
}







