package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class PiratCutlassItem extends SwordItem {
	private final boolean ghost;

	public PiratCutlassItem(Item.Properties properties, boolean ghost) {
		super(tierFor(ghost), properties.attributes(buildAttributes(ghost)));
		this.ghost = ghost;
	}

	private static Tier tierFor(boolean ghost) {
		return ghost ? RatsToolMaterialRegistry.GHOST_CUTLASS : RatsToolMaterialRegistry.CUTLASS;
	}

	// 1.21: rebuild the +8/+6.5 damage and -1 speed modifiers via ItemAttributeModifiers — the legacy
	// Item.getAttributeModifiers override is gone. Damage values are absolute (override the base SwordItem
	// modifiers from createAttributes), so we don't add the tier bonus here.
	private static ItemAttributeModifiers buildAttributes(boolean ghost) {
		double damage = ghost ? 8.0D : 6.5D;
		return ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE,
						new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, damage, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED,
						new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -1.0D, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.MAINHAND)
				.build();
	}

	public boolean isGhost() {
		return this.ghost;
	}
}
