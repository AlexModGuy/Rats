package com.github.alexthe666.rats.server.misc;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerTrades;

// PORT-STUB: 1.21 reworked VillagerTrades constructors to use ItemCost (fromItem+count wrapped together)
// instead of the (Item, count) pair. The full plague-doctor trade table needs re-authoring against
// the new ItemsAndEmeraldsToItems / ItemsForEmeralds signatures + ItemCost. Empty trade map until then.
public class PlagueDoctorTrades extends VillagerTrades {
	public static final Int2ObjectMap<VillagerTrades.ItemListing[]> PLAGUE_DOCTOR_TRADES;
	public static final VillagerTrades.ItemListing COMBINER_TRADE = (entity, random) -> null;
	public static final VillagerTrades.ItemListing SEPARATOR_TRADE = (entity, random) -> null;
	public static final VillagerTrades.ItemListing UPGRADE_COMBINED_TRADE = (entity, random) -> null;

	static {
		PLAGUE_DOCTOR_TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(
				1, new VillagerTrades.ItemListing[]{},
				2, new VillagerTrades.ItemListing[]{}
		));
	}
}
