package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

public class ItemPiratCutlass extends SwordItem {
    private boolean ghost;

    public ItemPiratCutlass(boolean ghost) {
        super(RatsItemRegistry.PIRAT_CUTLASS_MATERIAL, ghost ? 7 : 5, 6.0F, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID,  ghost? "ghost_pirat_cutlass" : "pirat_cutlass");
        this.ghost = ghost;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)(ghost ? 8D : 6.5D), AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-1D, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
}
