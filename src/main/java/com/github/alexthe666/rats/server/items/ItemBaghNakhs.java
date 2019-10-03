package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;

public class ItemBaghNakhs extends ItemSword {

    public ItemBaghNakhs() {
        super(RatsItemRegistry.BAGHNAKHS_MATERIAL);
        this.setTranslationKey("rats.feral_bagh_nakhs");
        this.setCreativeTab(RatsMod.TAB);
        this.setRegistryName(RatsMod.MODID, "feral_bagh_nakhs");
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 6D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 6D, 0));
        }
        return multimap;
    }

}