package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;

public class PiratCutlassItem extends SwordItem {
	private final boolean ghost;

	public PiratCutlassItem(Item.Properties properties, boolean ghost) {
		super(ghost ? RatsToolMaterialRegistry.GHOST_CUTLASS : RatsToolMaterialRegistry.CUTLASS, 
			properties.attributes(
				SwordItem.createAttributes(
					ghost ? RatsToolMaterialRegistry.GHOST_CUTLASS : RatsToolMaterialRegistry.CUTLASS, 
					ghost ? 8 : 6, 
					-1.0F
				)
			)
		);
		this.ghost = ghost;
	}

}







