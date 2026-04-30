package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.registry.RatsToolMaterialRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;

import java.util.List;

public class PlagueScytheItem extends SwordItem {
	public PlagueScytheItem(Item.Properties properties) {
		super(RatsToolMaterialRegistry.PLAGUE_SCYTHE, properties.attributes(BUILT_ATTRIBUTES));
	}

	// 1.21: +12 damage, -0.5 speed via ItemAttributeModifiers (replaces legacy getAttributeModifiers override).
	private static final ItemAttributeModifiers BUILT_ATTRIBUTES = ItemAttributeModifiers.builder()
			.add(Attributes.ATTACK_DAMAGE,
					new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 12.0D, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND)
			.add(Attributes.ATTACK_SPEED,
					new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -0.5D, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND)
			.build();

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return 1.0F;
	}

	// 1.21: signature changed to (ItemStack, BlockState). Plague scythe is a weapon, never a mining tool.
	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return false;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
		return toolAction == ItemAbilities.SWORD_SWEEP;
	}
}