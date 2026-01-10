package com.github.alexthe666.rats.server.items.upgrades.interfaces;

import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface EquipmentListenerUpgrade {

	/**
	 * Fires when equipment is changed on a rat. Can be used to refresh info when the held/worn stack changes.
	 *
	 * @param rat      the rat that had its slots changed
	 * @param slot     the equipment slot that changed. It will either be the held item (main hand), the hat (helmet), or the banner (offhand). Upgrade changes are not detected.
	 * @param oldStack the stack the rat used to have in its selective slot
	 * @param newStack the stack the rat is now holding in its selective slot
	 */
	void onItemChanged(TamedRat rat, EquipmentSlot slot, ItemStack oldStack, ItemStack newStack);
}







