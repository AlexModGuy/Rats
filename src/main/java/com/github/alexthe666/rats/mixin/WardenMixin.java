package com.github.alexthe666.rats.mixin;

import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class WardenMixin {

	@Inject(method = "canTargetEntity", at = @At("HEAD"), cancellable = true)
	public void rats$wardensDontTargetSculkedRats(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof TamedRat rat && RatUpgradeUtils.hasUpgrade(rat, RatsItemRegistry.RAT_UPGRADE_SCULKED.get())) {
			cir.setReturnValue(false);
		}
	}
}
