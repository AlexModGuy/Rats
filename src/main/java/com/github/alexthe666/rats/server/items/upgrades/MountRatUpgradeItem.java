package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.server.entity.RatMount;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class MountRatUpgradeItem<T extends Mob & RatMount> extends BaseRatUpgradeItem {

	private final RegistryObject<EntityType<T>> entityType;

	public MountRatUpgradeItem(Item.Properties properties, int rarity, int textLength, RegistryObject<EntityType<T>> entityType) {
		super(properties, rarity, textLength);
		this.entityType = entityType;
	}

	public EntityType<?> getEntityType() {
		return this.entityType.get();
	}

}
