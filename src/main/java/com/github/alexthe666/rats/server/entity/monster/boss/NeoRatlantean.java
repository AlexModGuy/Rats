package com.github.alexthe666.rats.server.entity.monster.boss;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.data.ratlantis.tags.RatlantisEntityTags;
import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.misc.LaserPortal;
import com.github.alexthe666.rats.server.entity.projectile.ThrownBlock;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SyncThrownBlockPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class NeoRatlantean extends Monster {

	private static final Predicate<LivingEntity> NOT_RATLANTEAN = entity -> entity.isAlive() && !entity.getType().is(RatlantisEntityTags.RATLANTEAN);
	private static final EntityDataAccessor<Integer> COLOR_VARIANT = SynchedEntityData.defineId(NeoRatlantean.class, EntityDataSerializers.INT);
	private final ServerBossEvent bossInfo = (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS));
	private int attackSelection = 0;
	private int summonCooldown = 0;
	private int humTicks = 0;

	public NeoRatlantean(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.setHealth(this.getMaxHealth());
		this.getNavigation().setCanFloat(true);
		this.xpReward = 80;
		this.moveControl = new RatlanteanMoveControl(this);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (this.tickCount % 100 == 0) {
			this.heal(1);
		}
		this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(COLOR_VARIANT, 0);
	}

	public int getColorVariant() {
		return this.getEntityData().get(COLOR_VARIANT);
	}

	public void setColorVariant(int color) {
		this.getEntityData().set(COLOR_VARIANT, color);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("ColorVariant", this.getColorVariant());
		compound.putInt("AttackSelection", attackSelection);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorVariant(compound.getInt("ColorVariant"));
		attackSelection = compound.getInt("AttackSelection");
		if (this.hasCustomName()) {
			this.bossInfo.setName(this.getDisplayName());
		}
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossInfo.setName(this.getDisplayName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	@Nullable
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		this.setColorVariant(this.getRandom().nextInt(4));
		return data;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide()) {
			this.level().addParticle(RatsParticleRegistry.LIGHTNING.get(),
					this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					this.getY() + this.getEyeHeight() + (this.getRandom().nextFloat() * 0.35F),
					this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() / 2,
					0.0F, 0.0F, 0.0F);
		}
		if (this.summonCooldown > 0) {
			this.summonCooldown--;
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.humTicks % 80 == 0) {
			this.playSound(RatsSoundRegistry.NEORATLANTEAN_LOOP.get(), 1, 1);
		}
		this.humTicks++;
		if (!this.level().isClientSide() && this.getTarget() != null) {
			Entity entity = this.getTarget();
			if (RatConfig.neoratlanteanSummonLaserPortals && this.attackSelection == 0 && this.summonCooldown == 0) {
				this.summonCooldown = RatConfig.neoratlanteanLaserAttackCooldown;
				int bounds = 5;
				for (int i = 0; i < this.getRandom().nextInt(2) + 2; i++) {
					LaserPortal laserPortal = new LaserPortal(RatlantisEntityRegistry.LASER_PORTAL.get(), this.level(), entity.getX() + this.getRandom().nextInt(bounds * 2) - bounds, this.getY() + 2, entity.getZ() + this.getRandom().nextInt(bounds * 2) - bounds, this);
					this.level().addFreshEntity(laserPortal);
				}
				this.resetAttacks();
			}
			if (RatConfig.neoratlanteanSummonFakeLightning && this.attackSelection == 1 && this.summonCooldown == 0) {
				int bounds = 20;
				if (!this.level().isClientSide()) {
					LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
					bolt.moveTo(entity.position());
					bolt.setVisualOnly(true);
					this.level().addFreshEntity(bolt);
					for (int i = 0; i < this.getRandom().nextInt(3) + 2; i++) {
						LightningBolt boltAgain = EntityType.LIGHTNING_BOLT.create(this.level());
						boltAgain.moveTo(new Vec3(entity.getX() + this.getRandom().nextInt(bounds * 2) - bounds, entity.getY(), entity.getZ() + this.getRandom().nextInt(bounds * 2) - bounds));
						boltAgain.setVisualOnly(true);
						this.level().addFreshEntity(boltAgain);
					}
				}

				this.summonCooldown = RatConfig.neoratlanteanLightningAttackCooldown;
				this.resetAttacks();
			}
			if (RatConfig.neoratlanteanThrowBlocks && this.attackSelection == 2 && this.summonCooldown == 0) {
				int searchRange = 10;
				BlockPos ourPos = this.blockPosition();
				List<BlockPos> listOfAll = new ArrayList<>();
				for (BlockPos pos : BlockPos.betweenClosedStream(ourPos.offset(-searchRange, -searchRange, -searchRange), ourPos.offset(searchRange, searchRange, searchRange)).map(BlockPos::immutable).toList()) {
					BlockState state = this.level().getBlockState(pos);
					if (!this.level().isEmptyBlock(pos) && this.canPickupBlock(this.level(), state, pos) && this.level().isEmptyBlock(pos.above())) {
						listOfAll.add(pos);
					}
				}
				if (listOfAll.size() > 0) {
					BlockPos pos = listOfAll.get(this.getRandom().nextInt(listOfAll.size()));
					ThrownBlock thrownBlock = new ThrownBlock(RatsEntityRegistry.THROWN_BLOCK.get(), this.level(), this.level().getBlockState(pos), this);
					thrownBlock.setPos(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
					if (!this.level().isClientSide()) {
						this.level().addFreshEntity(thrownBlock);
					}
					RatsNetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncThrownBlockPacket(thrownBlock.getId(), pos.asLong()));
					this.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					this.summonCooldown = RatConfig.neoratlanteanBlockAttackCooldown;
				}
				this.resetAttacks();
			}
			if (RatConfig.neoratlanteanAddHarmfulEffects && this.attackSelection == 3 && this.summonCooldown == 0) {
				this.getTarget().addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
				this.getTarget().addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
				this.getTarget().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200));
				this.summonCooldown = RatConfig.neoratlanteanEffectAttackCooldown;
				this.resetAttacks();
			}
		}
	}

	public void resetAttacks() {
		this.attackSelection = this.getRandom().nextInt(4);
	}

	public boolean canPickupBlock(Level level, BlockState state, BlockPos pos) {
		return !state.is(BlockTags.WITHER_IMMUNE) && state.isSolidRender(level, pos) && state.getDestroySpeed(level, pos) >= 0.0F && state.getDestroySpeed(level, pos) < 100.0F;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FollowTargetGoal(this));
		this.goalSelector.addGoal(2, new RandomlyMoveGoal());
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, NOT_RATLANTEAN));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 300.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.5D)
				.add(Attributes.ATTACK_DAMAGE, 8.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ARMOR, 0.0D);
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.NEORATLANTEAN_IDLE.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.NEORATLANTEAN_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.NEORATLANTEAN_DIE.get();
	}

	@Override
	public int getAmbientSoundInterval() {
		return 10;
	}

	class RatlanteanMoveControl extends MoveControl {
		public RatlanteanMoveControl(NeoRatlantean ratlantean) {
			super(ratlantean);
		}

		public void tick() {
			if (this.operation == Operation.MOVE_TO) {
				Vec3 vec3 = new Vec3(this.getWantedX() - NeoRatlantean.this.getX(), this.getWantedY() - NeoRatlantean.this.getY(), this.getWantedZ() - NeoRatlantean.this.getZ());
				double d0 = vec3.length();
				if (d0 < NeoRatlantean.this.getBoundingBox().getSize()) {
					this.operation = Operation.WAIT;
					NeoRatlantean.this.setDeltaMovement(NeoRatlantean.this.getDeltaMovement().scale(0.5D));
				} else {
					NeoRatlantean.this.setDeltaMovement(NeoRatlantean.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.05D / d0)));
					if (NeoRatlantean.this.getTarget() == null) {
						Vec3 vec31 = NeoRatlantean.this.getDeltaMovement();
						NeoRatlantean.this.setYRot(-((float) Mth.atan2(vec31.x(), vec31.z())) * (180F / (float) Math.PI));
					} else {
						double d2 = NeoRatlantean.this.getTarget().getX() - NeoRatlantean.this.getX();
						double d1 = NeoRatlantean.this.getTarget().getZ() - NeoRatlantean.this.getZ();
						NeoRatlantean.this.setYRot(-((float) Mth.atan2(d2, d1)) * (180F / (float) Math.PI));
					}
					NeoRatlantean.this.yBodyRot = NeoRatlantean.this.getYRot();
				}
			}
		}
	}

	public class FollowTargetGoal extends Goal {
		private final NeoRatlantean parentEntity;
		private double followDist;

		public FollowTargetGoal(NeoRatlantean ratlantean) {
			this.parentEntity = ratlantean;
		}

		@Override
		public boolean canUse() {
			this.followDist = NeoRatlantean.this.getBoundingBox().getSize();
			LivingEntity living = this.parentEntity.getTarget();
			double maxFollow = this.followDist * 5;
			return living != null && (living.distanceTo(this.parentEntity) >= maxFollow || !this.parentEntity.hasLineOfSight(living));
		}

		@Override
		public void tick() {
			LivingEntity living = this.parentEntity.getTarget();
			double maxFollow = followDist * 5;
			if (living != null && (living.distanceTo(this.parentEntity) >= maxFollow || !this.parentEntity.hasLineOfSight(living))) {
				NeoRatlantean.this.getMoveControl().setWantedPosition(living.getX() + NeoRatlantean.this.getRandom().nextInt(3) - 6, living.getY() + 3, living.getZ() + NeoRatlantean.this.getRandom().nextInt(3) - 6, 1.0D);
			}
		}
	}

	public class RandomlyMoveGoal extends Goal {

		public RandomlyMoveGoal() {
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse() {
			return !NeoRatlantean.this.getMoveControl().hasWanted() && NeoRatlantean.this.getRandom().nextInt(reducedTickDelay(5)) == 0;
		}

		@Override
		public boolean canContinueToUse() {
			return false;
		}

		@Override
		public void tick() {
			BlockPos blockpos = NeoRatlantean.this.blockPosition();

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(NeoRatlantean.this.getRandom().nextInt(5) - 8, NeoRatlantean.this.getRandom().nextInt(4) - 6, NeoRatlantean.this.getRandom().nextInt(5) - 8);

				if (NeoRatlantean.this.level().isEmptyBlock(blockpos1)) {
					NeoRatlantean.this.getMoveControl().setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

					if (NeoRatlantean.this.getTarget() == null) {
						NeoRatlantean.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}
}
