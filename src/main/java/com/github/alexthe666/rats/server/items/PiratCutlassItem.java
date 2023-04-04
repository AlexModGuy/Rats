package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.server.misc.RatsToolMaterial;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class PiratCutlassItem extends SwordItem {
	private final boolean ghost;

	public PiratCutlassItem(Item.Properties properties, boolean ghost) {
		super(ghost ? RatsToolMaterial.GHOST_CUTLASS : RatsToolMaterial.CUTLASS, ghost ? 7 : 5, 6.0F, properties);
		this.ghost = ghost;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.ghost ? 8D : 6.5D, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -1, AttributeModifier.Operation.ADDITION));
		}
		return multimap;
	}

}
