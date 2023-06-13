package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class PlagueDoctorTrades {
	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> PLAGUE_DOCTOR_TRADES;
	public static final VillagerTrades.ItemListing COMBINER_TRADE = new ItemsAndEmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_GOD.get(), 1, 40, RatsBlockRegistry.UPGRADE_COMBINER.get().asItem(), 1, 1, 30);
	public static final VillagerTrades.ItemListing SEPARATOR_TRADE = new ItemsAndEmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get(), 1, 4, RatsBlockRegistry.UPGRADE_SEPARATOR.get().asItem(), 1, 1, 30);
	public static final VillagerTrades.ItemListing UPGRADE_COMBINED_TRADE = new EmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), 6, 1, 1);

	static {
		PLAGUE_DOCTOR_TRADES = createTrades(ImmutableMap.of(1,
				new VillagerTrades.ItemListing[]{
						new ItemsToEmeralds(RatsItemRegistry.RAW_RAT.get(), 10, 12, 1),
						new ItemsToEmeralds(Items.BONE, 10, 12, 1),
						new ItemsToEmeralds(Items.ROTTEN_FLESH, 10, 12, 1),
						new ItemsToEmeralds(Items.SPIDER_EYE, 5, 12, 1),
						new ItemsToEmeralds(Items.GHAST_TEAR, 2, 12, 2),
						new ItemsToEmeralds(Items.PHANTOM_MEMBRANE, 4, 12, 2),
						new ItemsToEmeralds(Items.POISONOUS_POTATO, 2, 12, 2),
						new ItemsToEmeralds(RatsItemRegistry.CONTAMINATED_FOOD.get(), 5, 12, 2),
						new EmeraldsToItems(RatsItemRegistry.COOKED_RAT.get(), 1, 5, 1),
						new ItemsAndEmeraldsToItems(Items.POPPY, 5, 1, RatsItemRegistry.HERB_BUNDLE.get(), 3, 12, 2),
						new EmeraldsToItems(RatsItemRegistry.TREACLE.get(), 1, 2, 1),
						new EmeraldsToItems(RatsBlockRegistry.GARBAGE_PILE.get(), 1, 4, 3),
						new EmeraldsToItems(RatsBlockRegistry.CURSED_GARBAGE.get(), 1, 2, 3),
						new EmeraldsToItems(RatsBlockRegistry.PURIFIED_GARBAGE.get(), 1, 2, 3),
						new EmeraldsToItems(RatsBlockRegistry.PIED_WOOL.get(), 3, 1, 5, 5),
						new EmeraldsToItems(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(), 15, 1, 2, 5),
						new EmeraldsToItems(RatsItemRegistry.RAT_SKULL.get(), 3, 1, 3),
				},
				//Only 3 of these appear per plague doctor
				2, new VillagerTrades.ItemListing[]{
						new EmeraldsToItems(RatsItemRegistry.PLAGUE_LEECH.get(), 2, 1, 5),
						new EmeraldsToItems(RatsItemRegistry.PLAGUE_STEW.get(), 5, 1, 7),
						new EmeraldsToItems(RatsItemRegistry.FILTH.get(), 3, 1, 7),
						new EmeraldsToItems(RatsItemRegistry.PURIFYING_LIQUID.get(), 8, 1, 7),
						new EmeraldsToItems(RatsItemRegistry.TOKEN_FRAGMENT.get(), 5, 1, 2, 5),
						new EmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_BASIC.get(), 1, 2, 5),
						new EmeraldsToItems(RatsItemRegistry.PLAGUE_ESSENCE.get(), 2, 1, 5),
						new EmeraldsToItems(RatsItemRegistry.GOLDEN_RAT_SKULL.get(), 4, 1, 7),
						new EmeraldsToItems(RatsItemRegistry.PLAGUE_TOME.get(), 32, 1, 1, 15),
				}));
	}

	private static Int2ObjectMap<VillagerTrades.ItemListing[]> createTrades(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
		return new Int2ObjectOpenHashMap<>(map);
	}

	public static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack fromItem;
		private final int fromCount;
		private final int emeraldCost;
		private final ItemStack toItem;
		private final int toCount;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsAndEmeraldsToItems(ItemLike fromItem, int amountToGive, int emeraldCost, Item toItem, int amountToReceive, int tradeUses, int xp) {
			this.fromItem = new ItemStack(fromItem);
			this.fromCount = amountToGive;
			this.emeraldCost = emeraldCost;
			this.toItem = new ItemStack(toItem);
			this.toCount = amountToReceive;
			this.maxUses = tradeUses;
			this.villagerXp = xp;
			this.priceMultiplier = 0.05F;
		}

		@Nullable
		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class EmeraldsToItems implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EmeraldsToItems(ItemLike item, int emeraldCost, int itemAmountToRecieve, int tradeUses, int xp) {
			this(new ItemStack(item), emeraldCost, itemAmountToRecieve, tradeUses, xp);
		}

		public EmeraldsToItems(ItemLike item, int emeraldCost, int itemAmountToRecieve, int xp) {
			this(new ItemStack(item), emeraldCost, itemAmountToRecieve, 12, xp);
		}

		public EmeraldsToItems(ItemStack stack, int emeraldCost, int itemAmountToRecieve, int tradeUses, int xp) {
			this(stack, emeraldCost, itemAmountToRecieve, tradeUses, xp, 0.05F);
		}

		public EmeraldsToItems(ItemStack stack, int emeraldCost, int itemAmountToRecieve, int tradeUses, int xp, float multiplier) {
			this.itemStack = stack;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = itemAmountToRecieve;
			this.maxUses = tradeUses;
			this.villagerXp = xp;
			this.priceMultiplier = multiplier;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class ItemsToEmeralds implements VillagerTrades.ItemListing {
		private final Item item;
		private final int amount;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsToEmeralds(ItemLike item, int itemAmountToGive, int uses, int xp) {
			this.item = item.asItem();
			this.amount = itemAmountToGive;
			this.maxUses = uses;
			this.villagerXp = xp;
			this.priceMultiplier = 0.05F;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			ItemStack itemstack = new ItemStack(this.item, this.amount);
			return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}
}
