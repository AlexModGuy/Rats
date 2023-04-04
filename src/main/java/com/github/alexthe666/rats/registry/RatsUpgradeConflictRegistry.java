package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class RatsUpgradeConflictRegistry {
	private static final Map<Item, Item[]> REGISTERED_CONFLICTS = Maps.newHashMap();
	public static final Item[] HEALTH_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_HEALTH.get(), RatsItemRegistry.RAT_UPGRADE_WARRIOR.get(), RatsItemRegistry.RAT_UPGRADE_GOD.get(), RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get()};
	public static final Item[] ARMOR_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR.get(), RatsItemRegistry.RAT_UPGRADE_WARRIOR.get(), RatsItemRegistry.RAT_UPGRADE_GOD.get(), RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get()};
	public static final Item[] ATTACK_INCREASES = new Item[]{RatsItemRegistry.RAT_UPGRADE_STRENGTH.get(), RatsItemRegistry.RAT_UPGRADE_WARRIOR.get(), RatsItemRegistry.RAT_UPGRADE_GOD.get(), RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get()};
	public static final Item[] HARVEST_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_LUMBERJACK.get(), RatsItemRegistry.RAT_UPGRADE_MINER.get(), RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get(), RatsItemRegistry.RAT_UPGRADE_QUARRY.get(), RatsItemRegistry.RAT_UPGRADE_FARMER.get(), RatsItemRegistry.RAT_UPGRADE_FISHERMAN.get(), RatsItemRegistry.RAT_UPGRADE_MILKER.get(), RatsItemRegistry.RAT_UPGRADE_SHEARS.get(), RatsItemRegistry.RAT_UPGRADE_PLACER.get(), RatsItemRegistry.RAT_UPGRADE_BREEDER.get()};
	public static final Item[] TRANSPORT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_BUCKET.get(), RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET.get()};
	public static final Item[] EXPLOSION_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_TNT.get(), RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR.get(), RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get()};
	public static final Item[] PROCESSING_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CHEF.get(), RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get(), RatsItemRegistry.RAT_UPGRADE_GEMCUTTER.get(), RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get(), RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get(), RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get()};
	public static final Item[] MOUNT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get(), RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get(), RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get(), RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get()};

	public static void init() {
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_SPEED.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PLATTER.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_HEALTH.get(), HEALTH_INCREASES);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ARMOR.get(), ArrayUtils.add(ARMOR_INCREASES, RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get()));
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_STRENGTH.get(), ATTACK_INCREASES);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WARRIOR.get(), ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_GOD.get(), ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_NONBELIEVER.get(), ArrayUtils.addAll(HEALTH_INCREASES, ArrayUtils.addAll(ARMOR_INCREASES, ATTACK_INCREASES)));
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHEF.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CRAFTING.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_WHITELIST.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WHITELIST.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_BEE.get(), RatsItemRegistry.RAT_UPGRADE_DRAGON.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BEE.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), RatsItemRegistry.RAT_UPGRADE_DRAGON.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ENDER.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_LUMBERJACK.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MINER.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_QUARRY.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FARMER.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FISHERMAN.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_MILKER.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_SHEARS.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_PLACER.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BREEDER.get(), HARVEST_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_BASIC_RATLANTEAN.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get(), RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get()});
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_FERAL_BITE.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_RATINATOR.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_ARMOR.get()});
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_PSYCHIC.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_POISON.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get(), RatsItemRegistry.RAT_UPGRADE_POISON.get(), RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BUCKET.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET.get(), TRANSPORT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_TNT.get(), EXPLOSION_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR.get(), EXPLOSION_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get(), EXPLOSION_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get(), PROCESSING_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DRAGON.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), RatsItemRegistry.RAT_UPGRADE_BEE.get(), RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_NO_FLUTE.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ANGEL.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CARRAT.get(), new Item[0]);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_COMBINED.get(), RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get(), MOUNT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get(), MOUNT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get(), MOUNT_CONFLICTS);
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get(), MOUNT_CONFLICTS);
	}

	public static boolean doesConflict(Item first, Item second) {
		Item[] arr = REGISTERED_CONFLICTS.get(first);
		if (arr != null) {
			for (Item item : arr) {
				if (item == second) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Register an upgrade conflict here!
	 * <br>
	 * You can add as many or as few conflicts as you want. This will make it so a rat won't be able to accept your upgrade with any of the ones listed.
	 *
	 * @param yourUpgrade         the upgrade you want to register conflicts for
	 * @param conflictingUpgrades a list of upgrades that are not compatible with your upgrade
	 */
	public static void registerConflict(BaseRatUpgradeItem yourUpgrade, BaseRatUpgradeItem... conflictingUpgrades) {
		REGISTERED_CONFLICTS.put(yourUpgrade, conflictingUpgrades);
	}
}
