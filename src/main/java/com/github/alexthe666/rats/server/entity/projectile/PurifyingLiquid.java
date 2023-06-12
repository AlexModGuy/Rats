package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.data.tags.RatsEntityTags;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.rat.DemonRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;

public class PurifyingLiquid extends ThrowableItemProjectile {

	private static final EntityDataAccessor<Boolean> NETHER = SynchedEntityData.defineId(PurifyingLiquid.class, EntityDataSerializers.BOOLEAN);

	public PurifyingLiquid(EntityType<? extends ThrowableItemProjectile> type, Level level) {
		super(type, level);
	}

	public PurifyingLiquid(Level level, LivingEntity thrower, boolean nether) {
		super(RatsEntityRegistry.PURIFYING_LIQUID.get(), thrower, level);
		this.getEntityData().set(NETHER, nether);
	}

	public PurifyingLiquid(Level level, double x, double y, double z, boolean nether) {
		super(RatsEntityRegistry.PURIFYING_LIQUID.get(), x, y, z, level);
		this.getEntityData().set(NETHER, nether);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(NETHER, false);
	}

	@Override
	protected void onHit(HitResult result) {
		if (!this.level().isClientSide()) {
			AABB aabb = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
			List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
			if (!list.isEmpty()) {
				for (LivingEntity living : list) {
					if (living.isAffectedByPotions()) {
						double d0 = this.distanceToSqr(living);
						if (d0 < 16.0D) {
							if (this.getEntityData().get(NETHER)) {
								if (living instanceof DemonRat) {
									Rat rat = new Rat(RatsEntityRegistry.RAT.get(), this.level());
									rat.copyPosition(living);
									if (!this.level().isClientSide()) {
										ForgeEventFactory.onFinalizeSpawn(rat, (ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.CONVERSION, null, null);
									}
									rat.setTame(false);
									rat.setOwnerUUID(null);
									this.level().addFreshEntity(rat);
									living.discard();
								}
							} else {
								if (living instanceof Rat rat && rat.hasPlague()) {
									rat.setPlagued(false);
								}
								if (living.hasEffect(RatsEffectRegistry.PLAGUE.get())) {
									living.removeEffect(RatsEffectRegistry.PLAGUE.get());
								}
								if (living.getType().is(RatsEntityTags.PLAGUE_LEGION)) {
									living.hurt(this.damageSources().magic(), 10);
								}
								if (living instanceof ZombieVillager zomb && !zomb.isConverting()) {
									zomb.startConverting(living.getUUID(), 200);
								}
							}
						}
					}
				}
			}
			this.level().levelEvent(2002, this.blockPosition(), this.getEntityData().get(NETHER) ? 0XAD141E : 0XBFDFE2);
			this.discard();
		}
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(this.getDefaultItem());
	}

	@Override
	protected Item getDefaultItem() {
		try {
			return this.getEntityData().get(NETHER) ? RatsItemRegistry.CRIMSON_FLUID.get() : RatsItemRegistry.PURIFYING_LIQUID.get();
		} catch (Exception ignored) {
			return RatsItemRegistry.PURIFYING_LIQUID.get();
		}
	}
}