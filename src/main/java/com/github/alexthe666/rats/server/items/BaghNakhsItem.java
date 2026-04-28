package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class BaghNakhsItem extends SwordItem {

	public BaghNakhsItem(Item.Properties properties) {
		super(RatsToolMaterialRegistry.BAGHNAKHS, properties.attributes(SwordItem.createAttributes(RatsToolMaterialRegistry.BAGHNAKHS, 3, -0.1F)));
	}

	// PORT-STUB: 1.21 removed Item.getAttributeModifiers/BASE_ATTACK_*_UUID. Custom +6/+6 modifiers
	// were lost in the migration; weapon stats now match base baghnakh tier values until ItemAttributeModifiers
	// data-component setup is re-added.
}
