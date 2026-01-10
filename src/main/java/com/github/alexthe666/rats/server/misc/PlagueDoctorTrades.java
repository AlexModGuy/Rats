package com.github.alexthe666.rats.server.misc;

import com.github.alexthe666.rats.registry.RatsBlockRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class PlagueDoctorTrades extends VillagerTrades {
	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> PLAGUE_DOCTOR_TRADES;
	public static final VillagerTrades.ItemListing COMBINER_TRADE = new ItemsAndEmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_GOD.get(), 1, 40, RatsBlockRegistry.UPGRADE_COMBINER.get().asItem(), 1, 1, 30, 0.05F);
	public static final VillagerTrades.ItemListing SEPARATOR_TRADE = new ItemsAndEmeraldsToItems(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get(), 1, 4, RatsBlockRegistry.UPGRADE_SEPARATOR.get().asItem(), 1, 1, 30, 0.05F);
	public static final VillagerTrades.ItemListing UPGRADE_COMBINED_TRADE = new ItemsForEmeralds(RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), 6, 1, 1);

	static {
		PLAGUE_DOCTOR_TRADES = createTrades(ImmutableMap.of(1,
				new VillagerTrades.ItemListing[]{
						new EmeraldForItems(RatsItemRegistry.RAW_RAT.get(), 10, 12, 1),
						new EmeraldForItems(Items.BONE, 10, 12, 1),
						new EmeraldForItems(Items.ROTTEN_FLESH, 10, 12, 1),
						new EmeraldForItems(Items.SPIDER_EYE, 5, 12, 1),
						new EmeraldForItems(Items.GHAST_TEAR, 2, 12, 2),
						new EmeraldForItems(Items.PHANTOM_MEMBRANE, 4, 12, 2),
						new EmeraldForItems(Items.POISONOUS_POTATO, 2, 12, 2),
						new EmeraldForItems(RatsItemRegistry.CONTAMINATED_FOOD.get(), 5, 12, 2),
						new ItemsForEmeralds(RatsItemRegistry.COOKED_RAT.get(), 1, 5, 1),
						new ItemsAndEmeraldsToItems(Items.POPPY, 5, 1, RatsItemRegistry.HERB_BUNDLE.get(), 3, 12, 2, 0.05F),
						new ItemsForEmeralds(RatsItemRegistry.TREACLE.get(), 1, 2, 1),
						new ItemsForEmeralds(RatsBlockRegistry.GARBAGE_PILE.get().asItem(), 1, 4, 3),
						new ItemsForEmeralds(RatsBlockRegistry.CURSED_GARBAGE.get().asItem(), 1, 2, 3),
						new ItemsForEmeralds(RatsBlockRegistry.PURIFIED_GARBAGE.get().asItem(), 1, 2, 3),
						new ItemsForEmeralds(RatsBlockRegistry.PIED_WOOL.get(), 3, 1, 5, 5),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get(), 15, 1, 2, 5),
						new ItemsForEmeralds(RatsItemRegistry.RAT_SKULL.get(), 3, 1, 3),
				},
				//Only 3 of these appear per plague doctor
				2, new VillagerTrades.ItemListing[]{
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_LEECH.get(), 2, 1, 5),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_STEW.get(), 5, 1, 7),
						new ItemsForEmeralds(RatsItemRegistry.FILTH.get(), 3, 1, 7),
						new ItemsForEmeralds(RatsItemRegistry.PURIFYING_LIQUID.get(), 8, 1, 7),
						new ItemsForEmeralds(RatsItemRegistry.TOKEN_FRAGMENT.get(), 5, 1, 2, 5),
						new ItemsForEmeralds(RatsItemRegistry.RAT_UPGRADE_BASIC.get(), 1, 2, 5),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_ESSENCE.get(), 2, 1, 5),
						new ItemsForEmeralds(RatsItemRegistry.GOLDEN_RAT_SKULL.get(), 4, 1, 7),
						new ItemsForEmeralds(RatsItemRegistry.PLAGUE_TOME.get(), 32, 1, 1, 15),
				}));
	}

	private static Int2ObjectMap<VillagerTrades.ItemListing[]> createTrades(ImmutableMap<Integer, VillagerTrades.ItemListing[]> map) {
		return new Int2ObjectOpenHashMap<>(map);
	}
}







