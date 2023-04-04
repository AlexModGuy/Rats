package com.github.alexthe666.rats.server.items.upgrades;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.ChangesTextureUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.GlowingEyesUpgrade;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.TickRatUpgrade;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class EnderRatUpgradeItem extends BaseRatUpgradeItem implements ChangesTextureUpgrade, GlowingEyesUpgrade, TickRatUpgrade {
	public EnderRatUpgradeItem(Properties properties) {
		super(properties, 1, 3);
	}

	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_ender_upgrade.png");
	}

	@Override
	public boolean makesEyesGlowByDefault() {
		return true;
	}

	@Override
	public RenderType getEyeTexture() {
		return RenderType.eyes(new ResourceLocation(RatsMod.MODID, "textures/entity/rat/rat_eye_ender_upgrade.png"));
	}

	@Override
	public void tick(TamedRat rat) {
		if (!rat.getLevel().isClientSide()) {
			if (rat.getNavigation().getPath() != null && rat.getNavigation().getPath().getEndNode() != null && !rat.isPassenger()) {
				Vec3 target = new Vec3(rat.getNavigation().getPath().getEndNode().x, rat.getNavigation().getPath().getEndNode().y, rat.getNavigation().getPath().getEndNode().z);
				if (rat.getRatDistanceCenterSq(target.x(), target.y(), target.z()) > 20 || !rat.isDirectPathBetweenPoints(target)) {
					rat.attemptTeleport(target.x(), target.y(), target.z());
					rat.getNavigation().stop();
				}
			}
		} else {
			rat.getLevel().addParticle(ParticleTypes.PORTAL, rat.getRandomX(0.25D), rat.getRandomY() - 0.25D, rat.getRandomZ(0.25D), (rat.getRandom().nextDouble() - 0.5D) * 2.0D, -rat.getRandom().nextDouble(), (rat.getRandom().nextDouble() - 0.5D) * 2.0D);
		}
	}
}
