package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

public class ItemBaghNakhs extends SwordItem {

    public ItemBaghNakhs() {
        super(RatsItemRegistry.BAGHNAKHS_MATERIAL, 3, -0.1F, new Item.Properties().group(RatsMod.getRatlantisTab()));
         this.setRegistryName(RatsMod.MODID, "feral_bagh_nakhs");
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)6, AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.field_233825_h_, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)6, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

}