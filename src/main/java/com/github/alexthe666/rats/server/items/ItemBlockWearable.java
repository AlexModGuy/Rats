package com.github.alexthe666.rats.server.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlockWearable extends BlockItem {
    public ItemBlockWearable(Block obj, Properties props) {
        super(obj, props);
    }

    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity)
    {
        return EquipmentSlotType.HEAD == armorType;
    }

}
