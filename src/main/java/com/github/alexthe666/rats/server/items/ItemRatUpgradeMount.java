package com.github.alexthe666.rats.server.items;

import net.minecraft.entity.EntityType;

public class ItemRatUpgradeMount extends ItemRatUpgrade {

    private EntityType entityType;

    public ItemRatUpgradeMount(String name, int rarity, int textLength, EntityType entityType) {
        super(name, rarity, textLength);
        this.entityType = entityType;
    }

    public EntityType getEntityType(){
        return entityType;
    }

}
