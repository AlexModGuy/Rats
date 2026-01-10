package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class BaghNakhsItem extends SwordItem {

	public BaghNakhsItem(Item.Properties properties) {
		super(RatsToolMaterialRegistry.BAGHNAKHS, properties.attributes(
			SwordItem.createAttributes(RatsToolMaterialRegistry.BAGHNAKHS, 6, 6.0F)
		));
	}

}






