package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.world.item.ItemStack;

public class AristocratRatUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade {
	public AristocratRatUpgradeItem(Properties properties) {
		super(properties, 1, 1);
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.coinCooldown <= 0) {
			rat.coinCooldown = rat.getRandom().nextInt(6000) + 6000;
			if (!rat.getLevel().isClientSide()) {
				rat.spawnAtLocation(new ItemStack(RatsItemRegistry.TINY_COIN.get(), 1 + rat.getRandom().nextInt(2)), 0.0F);
			}
			rat.playSound(RatsSoundRegistry.RAT_MAKE_COIN.get(), 1.0F, rat.getVoicePitch());
		} else {
			rat.coinCooldown--;
		}
	}
}
