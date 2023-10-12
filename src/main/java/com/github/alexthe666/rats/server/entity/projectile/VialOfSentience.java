package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatlantisItemRegistry;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.entity.monster.FeralRatlantean;
import com.github.alexthe666.rats.server.entity.monster.boss.NeoRatlantean;
import com.github.alexthe666.rats.server.misc.RatsLangConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class VialOfSentience extends ThrowableItemProjectile {

	public VialOfSentience(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public VialOfSentience(Level level, LivingEntity thrower) {
		super(RatlantisEntityRegistry.VIAL_OF_SENTIENCE.get(), thrower, level);
	}

	protected void onHit(HitResult result) {
		if (!this.level().isClientSide()) {
			AABB axisalignedbb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, axisalignedbb);

			if (!list.isEmpty()) {
				for (LivingEntity living : list) {
					if (living.isAffectedByPotions()) {
						double d0 = this.distanceToSqr(living);
						if (d0 < 16.0D) {
							if (living instanceof FeralRatlantean) {
								if (RatConfig.summonBaronOnlyInRatlantis && !this.level().dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY)) {
									if (this.getOwner() instanceof Player player) {
										player.displayClientMessage(Component.translatable(RatsLangConstants.NEO_RATLANTIS_ONLY), true);
									}
								} else {
									NeoRatlantean ratlantean = new NeoRatlantean(RatlantisEntityRegistry.NEO_RATLANTEAN.get(), this.level());
									ratlantean.setColorVariant(((FeralRatlantean) living).getColorVariant());
									ratlantean.copyPosition(living);
									living.discard();
									if (!this.level().isClientSide()) {
										this.level().addFreshEntity(ratlantean);
									}
								}
							} else {
								living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 4));
								living.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 600));
								living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600));
							}
						}
					}
				}
			}
		}
		this.level().levelEvent(2002, this.blockPosition(), 0XFEFE7E);
		this.discard();
	}

	@Override
	protected Item getDefaultItem() {
		return RatlantisItemRegistry.VIAL_OF_SENTIENCE.get();
	}

	public ItemStack getItem() {
		return new ItemStack(RatlantisItemRegistry.VIAL_OF_SENTIENCE.get());
	}
}
