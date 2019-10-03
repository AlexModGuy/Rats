package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class ItemPiratCutlass extends ItemSword {
    public ItemPiratCutlass() {
        super(RatsItemRegistry.PIRAT_CUTLASS_MATERIAL);
        this.setTranslationKey("rats.pirat_cutlass");
        this.setCreativeTab(RatsMod.TAB);
        this.setRegistryName(RatsMod.MODID, "pirat_cutlass");
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 6.5D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1D, 0));
        }
        return multimap;
    }

}
