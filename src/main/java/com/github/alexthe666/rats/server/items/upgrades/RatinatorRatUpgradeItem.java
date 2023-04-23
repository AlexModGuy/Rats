package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.entity.projectile.LaserBeam;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesOverlayUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Map;

public class RatinatorRatUpgradeItem extends StatBoostingRatUpgradeItem implements ChangesOverlayUpgrade, TickRatUpgrade, GlowingEyesUpgrade {
	public RatinatorRatUpgradeItem(Properties properties) {
		super(properties, 2, 2, () -> Map.of(Attributes.ARMOR, RatConfig.ratinatorArmorUpgrade), false);
	}

	@Override
	public RenderType getOverlayTexture(TamedRat rat, float partialTicks) {
		return RenderType.entitySmoothCutout(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/upgrades/ratinator.png"));
	}

	@Override
	public void tick(TamedRat rat) {
		if (rat.rangedAttackCooldown == 0 && rat.getTarget() != null) {
			rat.rangedAttackCooldown = 10;
			float radius = 0.3F;
			for (int i = 0; i < 2; i++) {
				float angle = (0.01745329251F * (rat.yBodyRot + (i == 0 ? 90 : -90)));
				double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + rat.getX();
				double extraZ = (double) (radius * Mth.cos(angle)) + rat.getZ();
				double extraY = 0.2 + rat.getY();
				double targetRelativeX = rat.getTarget().getX() - extraX;
				double targetRelativeY = rat.getTarget().getY() + rat.getTarget().getBbHeight() / 2 - extraY;
				double targetRelativeZ = rat.getTarget().getZ() - extraZ;
				rat.playSound(RatsSoundRegistry.LASER.get(), 1.0F, 0.75F + rat.getRandom().nextFloat() * 0.5F);
				LaserBeam beam = new LaserBeam(RatlantisEntityRegistry.LASER_BEAM.get(), rat.getLevel(), rat);
				beam.setRGB(1.0F, 0.0F, 0.0F);
				beam.setBaseDamage(2.0F);
				beam.setPos(extraX, extraY, extraZ);
				beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
				if (!rat.getLevel().isClientSide()) {
					rat.getLevel().addFreshEntity(beam);
				}
			}
		}
	}

	@Override
	public RenderType getEyeTexture() {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/eyes/ratinator.png"));
	}
}
