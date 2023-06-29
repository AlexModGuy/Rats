package com.github.alexthe666.rats.mixin;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

	@Inject(at = @At("RETURN"), method = "isWearingGold", cancellable = true)
	private static void rats$isEntityHoldingGoldRat(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (Entity passenger : entity.getPassengers()) {
			if (passenger instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_IDOL.get())) {
				cir.setReturnValue(true);
			}
		}
	}
}
