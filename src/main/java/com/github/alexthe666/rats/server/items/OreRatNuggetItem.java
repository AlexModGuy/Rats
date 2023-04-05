package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
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
		if (poopItem.getOrCreateTag().contains("OreItem")) {
			CompoundTag poopTag = poopItem.getOrCreateTag().getCompound("OreItem");
			ItemStack oreItem = ItemStack.of(poopTag);
			if (oreItem.isEmpty()) {
				return fallback;
			} else {
				return oreItem;
			}
		}
		return fallback;
	}

	public static ItemStack getIngot(Level level, ItemStack stack) {
		Container container = new SimpleContainer(stack);
		SmeltingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level).orElse(null);
		if (recipe != null && !recipe.getResultItem(level.registryAccess()).isEmpty()) {
			return recipe.getResultItem(level.registryAccess()).copy();
		}
		return ItemStack.EMPTY;
	}

	public static ItemStack saveResourceToNugget(Level level, ItemStack resource, boolean smeltOreFirst) {
		if (smeltOreFirst) {
			Container container = new SimpleContainer(resource.copy());
			SmeltingRecipe recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, container, level).orElse(null);
			if (recipe != null && !recipe.getResultItem(level.registryAccess()).isEmpty()) {
				resource = recipe.getResultItem(level.registryAccess()).copy();
			}
		}
		ItemStack stack = new ItemStack(RatsItemRegistry.RAT_NUGGET_ORE.get());
		CompoundTag nuggetTag = new CompoundTag();
		CompoundTag oreTag = new CompoundTag();
		resource.save(oreTag);
		nuggetTag.put("OreItem", oreTag);
		stack.setTag(nuggetTag);
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(Component.translatable("item.rats.rat_nugget_ore.desc").withStyle(ChatFormatting.GRAY));
	}
}
