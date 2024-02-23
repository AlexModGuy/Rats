package com.github.alexthe666.rats.server.entity.monster;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.data.tags.RatsEntityTags;
import com.github.alexthe666.rats.registry.RatsEffectRegistry;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PlagueBeast extends FeralRatlantean {

	protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(PlagueBeast.class, EntityDataSerializers.OPTIONAL_UUID);

	public PlagueBeast(EntityType<? extends Monster> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.ARMOR, 4.0D);
	}

	@Override
	public void tick() {
		super.tick();
		double d0 = 0D;
		double d1 = this.getRandom().nextGaussian() * 0.05D + 0.5D;
		double d2 = 0D;
		this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
		if (this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof BlackDeath death) {
			if (death.getTarget() != null && death.getTarget().isAlive()) {
				this.setTarget(death.getTarget());
			} else {
				float radius = (float) 8 - (float) Math.sin(death.tickCount * 0.4D) * 0.5F;
				int maxRatStuff = 360 / Math.max(death.getBeastsSummoned(), 1);
				int ratIndex = this.getId() % Math.max(death.getBeastsSummoned(), 1);
				float angle = (0.01745329251F * (ratIndex * maxRatStuff + tickCount * 4.1F));
				double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + death.getX();
				double extraZ = (double) (radius * Mth.cos(angle)) + death.getZ();
				BlockPos runToPos = BlockPos.containing(extraX, death.getY(), extraZ);
				int steps = 0;
				while (this.level().getBlockState(runToPos).isSolidRender(this.level(), runToPos) && steps < 10) {
					runToPos = runToPos.above();
					steps++;
				}
				this.getNavigation().moveTo(extraX, runToPos.getY(), extraZ, 1.0F);
			}
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		if (reason.shouldDestroy()) {
			if (this.isAlive() && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof BlackDeath death && RatConfig.bdConstantBeastSpawns) {
				death.setBeastsSummoned(death.getBeastsSummoned() - 1);
			}
		}
		super.remove(reason);
	}

	@Override
	public void doExtraEffect(LivingEntity target) {
		target.addEffect(new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 1200));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(OWNER_UNIQUE_ID, Optional.empty());
	}

	@Override
	public boolean hasToga() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerId() == null) {
			compound.putString("OwnerUUID", "");
		} else {
			compound.putString("OwnerUUID", this.getOwnerId().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		UUID uuid;
		if (compound.hasUUID("Owner")) {
			uuid = compound.getUUID("Owner");
		} else {
			String s = compound.getString("Owner");
			uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
		}

		if (uuid != null) {
			try {
				this.setOwnerId(uuid);
			} catch (Throwable ignored) {
			}
		}
	}

	@Nullable
	public UUID getOwnerId() {
		return this.getEntityData().get(OWNER_UNIQUE_ID).orElse(null);
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.getEntityData().set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Nullable
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerId();
			LivingEntity player = uuid == null ? null : this.level().getPlayerByUUID(uuid);
			if (player != null) {
				return player;
			} else {
				if (!this.level().isClientSide()) {
					Entity entity = this.level().getServer().getLevel(this.level().dimension()).getEntity(uuid);
					if (entity instanceof LivingEntity living) {
						return living;
					}
				}
			}
		} catch (IllegalArgumentException var2) {
			return null;
		}
		return null;
	}

	@Override
	public boolean isAlliedTo(Entity entity) {
		return super.isAlliedTo(entity) || entity.getType().is(RatsEntityTags.PLAGUE_LEGION);
	}
}
