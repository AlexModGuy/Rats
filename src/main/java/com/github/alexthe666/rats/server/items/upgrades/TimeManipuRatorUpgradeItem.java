package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import com.github.alexthe666.rats.server.misc.RatUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TimeManipuRatorUpgradeItem extends BaseRatUpgradeItem implements TickRatUpgrade, ChangesOverlayUpgrade {
	public TimeManipuRatorUpgradeItem(Properties properties) {
		super(properties, 2, 2);
	}

	@Override
	public @Nullable RenderType getOverlayTexture(ItemStack stack, TamedRat rat, float partialTicks) {
		float f = (float) rat.tickCount + partialTicks;
		return RenderType.energySwirl(new ResourceLocation(RatsMod.MODID, "textures/entity/psychic.png"), f * 0.01F, f * 0.01F);
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.tickCount % 5 == 0) {
			for (int x = -2; x < 2; x++) {
				for (int z = -2; z < 2; z++) {
					for (int y = -1; y < 1; y++) {
						BlockPos pos = rat.blockPosition().offset(x, y, z);
						RatUtils.accelerateTick(rat.level(), pos, rat.level().getRandom().nextInt(10), rat.level().getRandom().nextInt(2));
					}
				}
			}
		}
	}
}
