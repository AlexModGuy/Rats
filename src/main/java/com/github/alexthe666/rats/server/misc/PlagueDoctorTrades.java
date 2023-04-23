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
	public static final VillagerTrades.ItemListing UPGRADE_COMBINED_TRADE = new ItemsForEmeralds(RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), 6, 1, 1);

	static {
		PLAGUE_DOCTOR_TRADES = createTrades(ImmutableMap.of(1,
				new VillagerTrades.ItemListing[]{
						new EmeraldForItems(RatsItemRegistry.RAW_RAT.get(), 10, 15, 1),
						new ItemsForEmeralds(Items.BONE, 3, 8, 9, 1),
						new ItemsForEmeralds(Items.ROTTEN_FLESH, 2, 10, 9, 2),
						new EmeraldForItems(Items.POISONOUS_POTATO, 5, 10, 3),
						new EmeraldForItems(RatsItemRegistry.PLAGUE_ESSENCE.get(), 3, 5, 10),
						new EmeraldForItems(RatsItemRegistry.CONTAMINATED_FOOD.get(), 1, 15, 3),
						new ItemsForEmeralds(RatsItemRegistry.COOKED_RAT.get(), 1, 5, 15, 5),
						new ItemsAndEmeraldsToItems(Blocks.POPPY.asItem(), 5, 2, RatsItemRegistry.HERB_BUNDLE.get(), 3, 10, 5),
						new ItemsForEmeralds(RatsBlockRegistry.DYE_SPONGE.get(), 3, 1, 4, 4),
						new ItemsForEmeralds(RatsItemRegistry.TREACLE.get(), 5, 3, 10, 5),
						new ItemsForEmeralds(RatsBlockRegistry.GARBAGE_PILE.get(), 5, 4, 15, 3),
						new ItemsForEmeralds(RatsBlockRegistry.RAT_CAGE.get(), 1, 2, 10, 5),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(), 15, 1, 2, 20),
						new ItemsForEmeralds(RatsItemRegistry.RAT_SKULL.get(), 3, 1, 15, 5),
				},
				//Only 3 of these appear per plague doctor
				2, new VillagerTrades.ItemListing[]{
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_LEECH.get(), 3, 1, 10, 5),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_STEW.get(), 7, 1, 8, 6),
						new ItemsForEmeralds(RatsItemRegistry.RAT_SACK.get(), 2, 1, 5, 5),
						new ItemsForEmeralds(RatsItemRegistry.PURIFYING_LIQUID.get(), 8, 1, 5, 10),
						new ItemsForEmeralds(RatsItemRegistry.TOKEN_FRAGMENT.get(), 5, 1, 10, 10),
						new ItemsForEmeralds(RatsItemRegistry.RAT_UPGRADE_BASIC.get(), 4, 2, 10, 10),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_ESSENCE.get(), 2, 1, 10, 6),
						new ItemsForEmeralds(RatsItemRegistry.CHEESE_BANNER_PATTERN.get(), 1, 1, 5, 2),
						new ItemsForEmeralds(RatsItemRegistry.RAT_BANNER_PATTERN.get(), 1, 1, 5, 2),
						new ItemsForEmeralds(RatsItemRegistry.STRING_CHEESE.get(), 2, 8, 5, 6),
						new ItemsForEmeralds(RatsItemRegistry.GOLDEN_RAT_SKULL.get(), 8, 2, 5, 6),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_TOME.get(), 32, 1, 1, 20),
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

	public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final ItemStack itemStack;
		private final int emeraldCost;
		private final int numberOfItems;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public ItemsForEmeralds(Block block, int emeraldCost, int amountReceived, int tradeUses, int xp) {
			this(new ItemStack(block), emeraldCost, amountReceived, tradeUses, xp);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int amountReceived, int xp) {
			this(new ItemStack(item), emeraldCost, amountReceived, 12, xp);
		}

		public ItemsForEmeralds(Item item, int emeraldCost, int amountReceived, int tradeUses, int xp) {
			this(new ItemStack(item), emeraldCost, amountReceived, tradeUses, xp);
		}

		public ItemsForEmeralds(ItemStack stack, int emeraldCost, int amountReceived, int tradeUses, int xp) {
			this(stack, emeraldCost, amountReceived, tradeUses, xp, 0.05F);
		}

		public ItemsForEmeralds(ItemStack stack, int emeraldCost, int amountReceived, int tradeUses, int xp, float multiplier) {
			this.itemStack = stack;
			this.emeraldCost = emeraldCost;
			this.numberOfItems = amountReceived;
			this.maxUses = tradeUses;
			this.villagerXp = xp;
			this.priceMultiplier = multiplier;
		}

		@Override
		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
		}
	}

	public static class EmeraldForItems implements VillagerTrades.ItemListing {
		private final Item item;
		private final int amount;
		private final int maxUses;
		private final int villagerXp;
		private final float priceMultiplier;

		public EmeraldForItems(ItemLike item, int amountReceived, int uses, int xp) {
			this.item = item.asItem();
			this.amount = amountReceived;
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
