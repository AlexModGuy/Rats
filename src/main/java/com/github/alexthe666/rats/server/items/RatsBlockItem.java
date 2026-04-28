package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.render.RatsBEWLR;
import com.github.alexthe666.rats.registry.RatlantisBlockRegistry;
import com.github.alexthe666.rats.server.block.CustomItemRarity;
import com.github.alexthe666.rats.server.block.WearableOnHead;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class RatsBlockItem extends BlockItem {

	public RatsBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	// PORT-STUB: 1.21 Item.canEquip removed; equip-by-class is now driven by EQUIPMENT data component on the item.
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return this.getBlock() instanceof WearableOnHead && EquipmentSlot.HEAD == armorType;
	}

	// PORT-STUB: 1.21 Item.getRarity(ItemStack) removed; rarity now comes from DataComponents.RARITY.
	public Rarity getRarity(ItemStack stack) {
		return this.getBlock() instanceof CustomItemRarity rarity ? rarity.getRarity() : Rarity.COMMON;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (this.getBlock() == RatlantisBlockRegistry.CHUNKY_CHEESE_TOKEN.get() && !RatsMod.RATLANTIS_DATAPACK_ENABLED) {
			return InteractionResult.PASS;
		}
		return super.useOn(context);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return new RatsBEWLR();
			}
		});
	}
}
