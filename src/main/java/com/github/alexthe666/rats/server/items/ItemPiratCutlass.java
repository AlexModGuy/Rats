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

public class ItemPiratCutlass extends SwordItem {
    private boolean ghost;

    public ItemPiratCutlass(boolean ghost) {
        super(RatsItemRegistry.PIRAT_CUTLASS_MATERIAL, ghost ? 7 : 5, 6.0F, new Item.Properties().group(RatsMod.getRatlantisTab()));
        this.setRegistryName(RatsMod.MODID,  ghost? "ghost_pirat_cutlass" : "pirat_cutlass");
        this.ghost = ghost;
    }


    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)(ghost ? 8D : 6.5D), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.field_233825_h_, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-1, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

}
