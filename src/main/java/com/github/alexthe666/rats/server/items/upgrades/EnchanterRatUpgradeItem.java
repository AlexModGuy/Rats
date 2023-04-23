package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;

public class EnchanterRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {
	public EnchanterRatUpgradeItem(Properties properties, int rarity, int textLength) {
		super(properties, rarity, textLength);
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.getMainHandItem().isEmpty()) {
			this.tryEnchanting(rat, this == RatsItemRegistry.RAT_UPGRADE_DISENCHANTER.get());
			this.createFinishedParticles(rat, ParticleTypes.ENCHANT, 2, ParticleTypes.SMOKE, 0.0F);
		}
	}

	private void tryEnchanting(TamedRat rat, boolean disenchant) {
		ItemStack heldItem = rat.getMainHandItem();
		ItemStack burntItem = ItemStack.EMPTY;
		if (heldItem.getItem() == Items.BOOK && !disenchant) {
			burntItem = heldItem.copy();
		}
		if (heldItem.getItem() == Items.ENCHANTED_BOOK && disenchant) {
			burntItem = new ItemStack(Items.BOOK, heldItem.getCount());
		}
		if (heldItem.isEnchantable() && !disenchant && !heldItem.isEnchanted()) {
			burntItem = heldItem.copy();
		}
		if (disenchant && heldItem.isEnchanted()) {
			burntItem = heldItem.copy();
			burntItem.getEnchantmentTags().clear();
		}
		if (burntItem.isEmpty()) {
			rat.cookingProgress = 0;
		} else {
			rat.cookingProgress++;
			if (rat.cookingProgress == 1000) {
				heldItem.shrink(1);
				if (!disenchant) {
					float power = 0;
					for (BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS) {
						if (EnchantmentTableBlock.isValidBookShelf(rat.getLevel(), rat.blockPosition(), blockpos)) {
							power += rat.getLevel().getBlockState(rat.blockPosition().offset(blockpos)).getEnchantPowerBonus(rat.getLevel(), rat.blockPosition().offset(blockpos));
						}
					}
					burntItem = EnchantmentHelper.enchantItem(rat.getRandom(), burntItem, (int) (2.0F + rat.getRandom().nextInt(2) + power), false);
				}
				if (heldItem.isEmpty()) {
					rat.setItemInHand(InteractionHand.MAIN_HAND, burntItem);
				} else {
					if (!rat.tryDepositItemInContainers(burntItem)) {
						if (!rat.getLevel().isClientSide()) {
							rat.spawnAtLocation(burntItem, 0.25F);
						}
					}
				}
				rat.cookingProgress = 0;
			}
		}
	}

	private float getPower(Level level, BlockPos pos) {
		return level.getBlockState(pos).getEnchantPowerBonus(level, pos);
	}
}
