package com.github.alexthe666.rats.registry;

import com.github.alexthe666.rats.server.items.upgrades.BaseRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.CombinedUpgrade;
import com.google.common.collect.Maps;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Map;

//TODO migrate away from this. We need a better system to use here.
//Im thinking of forcing conflicts to be registered in the item class itself, using 2 new methods.
// - A method to define the upgrade type (enum made up of the current lists, will automatically register conflicts based on this enum to help with conflict registration a bit)
// - A list of items that conflict with this one (to allow for weirder upgrade conflicts, will automatically block them from being used together even if the other upgrade doesnt contain this one).
@Deprecated
public class RatsUpgradeConflictRegistry {
	private static final Map<Item, Item[]> REGISTERED_CONFLICTS = Maps.newHashMap();
	public static final Item[] HARVEST_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_PICKPOCKET.get(), RatsItemRegistry.RAT_UPGRADE_GARDENER.get(), RatsItemRegistry.RAT_UPGRADE_LUMBERJACK.get(), /*RatsItemRegistry.RAT_UPGRADE_MINER.get(), RatsItemRegistry.RAT_UPGRADE_MINER_ORE.get(),*/ RatsItemRegistry.RAT_UPGRADE_QUARRY.get(), RatsItemRegistry.RAT_UPGRADE_FARMER.get(), RatsItemRegistry.RAT_UPGRADE_FISHERMAN.get(), RatsItemRegistry.RAT_UPGRADE_MILKER.get(), RatsItemRegistry.RAT_UPGRADE_SHEARS.get(), RatsItemRegistry.RAT_UPGRADE_PLACER.get(), RatsItemRegistry.RAT_UPGRADE_BREEDER.get()};
	public static final Item[] TRANSPORT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY.get(), RatsItemRegistry.RAT_UPGRADE_BUCKET.get(), RatsItemRegistry.RAT_UPGRADE_BIG_BUCKET.get()};
	public static final Item[] EXPLOSION_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_TNT.get(), RatsItemRegistry.RAT_UPGRADE_TNT_SURVIVOR.get(), RatlantisItemRegistry.RAT_UPGRADE_BUCCANEER.get()};
	public static final Item[] PROCESSING_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CRAFTING.get(), RatsItemRegistry.RAT_UPGRADE_CHEF.get(), RatlantisItemRegistry.RAT_UPGRADE_ARCHEOLOGIST.get(), RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get(), RatsItemRegistry.RAT_UPGRADE_ENCHANTER.get(), RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get()};
	public static final Item[] MOUNT_CONFLICTS = new Item[]{RatsItemRegistry.RAT_UPGRADE_CHICKEN_MOUNT.get(), RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT.get(), RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT.get(), RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT.get(), RatsItemRegistry.RAT_UPGRADE_BEAST_MOUNT.get(), RatlantisItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT.get()};

	public static void init() {
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_WHITELIST.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_WHITELIST.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_BEE.get(), RatsItemRegistry.RAT_UPGRADE_DRAGON.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_BEE.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), RatsItemRegistry.RAT_UPGRADE_DRAGON.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DRAGON.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_FLIGHT.get(), RatsItemRegistry.RAT_UPGRADE_BEE.get(), RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_POISON.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()});
		REGISTERED_CONFLICTS.put(RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get(), new Item[]{RatsItemRegistry.RAT_UPGRADE_ASBESTOS.get(), RatsItemRegistry.RAT_UPGRADE_POISON.get(), RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()});
		Arrays.stream(HARVEST_CONFLICTS).toList().forEach(item -> REGISTERED_CONFLICTS.put(item, HARVEST_CONFLICTS));
		Arrays.stream(TRANSPORT_CONFLICTS).toList().forEach(item -> REGISTERED_CONFLICTS.put(item, TRANSPORT_CONFLICTS));
		Arrays.stream(EXPLOSION_CONFLICTS).toList().forEach(item -> REGISTERED_CONFLICTS.put(item, EXPLOSION_CONFLICTS));
		Arrays.stream(PROCESSING_CONFLICTS).toList().forEach(item -> REGISTERED_CONFLICTS.put(item, PROCESSING_CONFLICTS));
		Arrays.stream(MOUNT_CONFLICTS).toList().forEach(item -> REGISTERED_CONFLICTS.put(item, MOUNT_CONFLICTS));
	}

	public static boolean doesConflict(ItemStack newItem, ItemStack existingItem) {
		if (!(existingItem.getItem() instanceof BaseRatUpgradeItem)) return false;
		if (newItem.is(existingItem.getItem()) && !newItem.is(RatsItemRegistry.RAT_UPGRADE_JURY_RIGGED.get()))
			return true;
		Item[] arr = REGISTERED_CONFLICTS.get(newItem.getItem());
		if (newItem.getItem() instanceof CombinedUpgrade combined) {
			CompoundTag tag = newItem.getTag();
			if (tag != null && tag.contains("Items", 9)) {
				NonNullList<ItemStack> upgradeList = NonNullList.withSize(combined.getUpgradeSlots(), ItemStack.EMPTY);
				ContainerHelper.loadAllItems(tag, upgradeList);
				for (ItemStack selectedUpgrade : upgradeList) {
					if (doesConflict(selectedUpgrade, existingItem)) {
						return true;
					}
				}
			}
		} else if (existingItem.getItem() instanceof CombinedUpgrade combined) {
			CompoundTag tag = existingItem.getTag();
			if (tag != null && tag.contains("Items", 9)) {
				NonNullList<ItemStack> upgradeList = NonNullList.withSize(combined.getUpgradeSlots(), ItemStack.EMPTY);
				ContainerHelper.loadAllItems(tag, upgradeList);
				for (ItemStack selectedUpgrade : upgradeList) {
					if (doesConflict(selectedUpgrade, newItem)) {
						return true;
					}
				}
			}
		} else if (arr != null) {
			for (Item item : arr) {
				if (existingItem.is(item)) {
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
