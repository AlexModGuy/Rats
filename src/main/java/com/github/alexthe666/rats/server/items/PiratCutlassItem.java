package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class PiratCutlassItem extends SwordItem {
	private final boolean ghost;

	public PiratCutlassItem(Item.Properties properties, boolean ghost) {
		super(tierFor(ghost), properties.attributes(SwordItem.createAttributes(tierFor(ghost), ghost ? 7 : 5, 6.0F)));
		this.ghost = ghost;
	}

	private static Tier tierFor(boolean ghost) {
		return ghost ? RatsToolMaterialRegistry.GHOST_CUTLASS : RatsToolMaterialRegistry.CUTLASS;
	}

	public boolean isGhost() {
		return this.ghost;
	}

	// PORT-STUB: 1.21 removed Item.getAttributeModifiers/BASE_ATTACK_*_UUID. Custom +6.5/+8 damage and -1 speed
	// modifiers were lost in the migration; weapon stats now match base cutlass tier values until
	// ItemAttributeModifiers data-component setup is re-added.
}
