package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.registry.RatsParticleRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RatlanteanSpirit extends Monster implements IAnimatedEntity, Ratlanteans {

	public static final Animation ANIMATION_ATTACK = Animation.create(10);
	private int animationTick;
	private Animation currentAnimation;

	public RatlanteanSpirit(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.moveControl = new RatlanteanSpirit.AIMoveControl(this);
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new RatlanteanSpirit.AIFireballAttack(this));
		this.goalSelector.addGoal(8, new RatlanteanSpirit.AIMoveRandom());
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.15D)
				.add(Attributes.ATTACK_DAMAGE, 4.0D)
				.add(Attributes.FOLLOW_RANGE, 24.0D)
				.add(Attributes.ARMOR, 0);
	}


	public void move(MoverType typeIn, Vec3 pos) {
		super.move(typeIn, pos);
		this.checkInsideBlocks();
	}

	public void tick() {
		this.noPhysics = true;
		super.tick();
		this.noPhysics = false;
		this.setNoGravity(true);
		AnimationHandler.INSTANCE.updateAnimations(this);
		if (this.getLevel().isClientSide() && this.getLevel().getNearestPlayer(this, 64.0D) != null) {

			this.getLevel().addParticle(RatsParticleRegistry.RAT_GHOST.get(),
					this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
					this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()),
					this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2F) - (double) this.getBbWidth(),
					0.92F, 0.82, 0.0F);
		}
	}

	@Override
	public int getAnimationTick() {
		return animationTick;
	}

	@Override
	public void setAnimationTick(int tick) {
		animationTick = tick;
	}

	@Override
	public Animation getAnimation() {
		return currentAnimation;
	}

	@Override
	public void setAnimation(Animation animation) {
		currentAnimation = animation;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[]{ANIMATION_ATTACK};
	}

	protected SoundEvent getAmbientSound() {
		return RatsSoundRegistry.RATLANTEAN_SPIRIT_IDLE.get();
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT.get();
	}

	protected SoundEvent getDeathSound() {
		return RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE.get();
	}

	class AIMoveControl extends MoveControl {
		public AIMoveControl(RatlanteanSpirit vex) {
			super(vex);
		}

		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO) {
				Vec3 vec3d = new Vec3(this.getWantedX() - RatlanteanSpirit.this.getX(), this.getWantedY() - RatlanteanSpirit.this.getY(), this.getWantedZ() - RatlanteanSpirit.this.getZ());
				double d0 = vec3d.length();
				double edgeLength = RatlanteanSpirit.this.getBoundingBox().getSize();
				if (d0 < edgeLength) {
					this.operation = MoveControl.Operation.WAIT;
					RatlanteanSpirit.this.setDeltaMovement(RatlanteanSpirit.this.getDeltaMovement().scale(0.5D));
				} else {
					RatlanteanSpirit.this.setDeltaMovement(RatlanteanSpirit.this.getDeltaMovement().add(vec3d.scale(this.speedModifier * 0.1D / d0)));
					if (RatlanteanSpirit.this.getTarget() == null) {
						Vec3 vec3d1 = RatlanteanSpirit.this.getDeltaMovement();
						RatlanteanSpirit.this.setYRot(-((float) Mth.atan2(vec3d1.x, vec3d1.z)) * (180F / (float) Math.PI));
						RatlanteanSpirit.this.yBodyRot = RatlanteanSpirit.this.getYRot();
					} else {
						double d4 = RatlanteanSpirit.this.getTarget().getX() - RatlanteanSpirit.this.getX();
						double d5 = RatlanteanSpirit.this.getTarget().getZ() - RatlanteanSpirit.this.getZ();
						RatlanteanSpirit.this.setYRot(-((float) Mth.atan2(d4, d5)) * (180F / (float) Math.PI));
						RatlanteanSpirit.this.yBodyRot = RatlanteanSpirit.this.getYRot();
					}
				}
			}
		}
	}

	class AIMoveRandom extends Goal {

		public AIMoveRandom() {
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canUse() {
			return !RatlanteanSpirit.this.getMoveControl().hasWanted() && RatlanteanSpirit.this.getRandom().nextInt(2) == 0;
		}

		public boolean canContinueToUse() {
			return false;
		}

		public void tick() {
			BlockPos blockpos = RatlanteanSpirit.this.blockPosition();

			for (int i = 0; i < 3; ++i) {
				BlockPos blockpos1 = blockpos.offset(RatlanteanSpirit.this.getRandom().nextInt(15) - 7, RatlanteanSpirit.this.getRandom().nextInt(11) - 5, RatlanteanSpirit.this.getRandom().nextInt(15) - 7);

				if (RatlanteanSpirit.this.getLevel().isEmptyBlock(blockpos1)) {
					RatlanteanSpirit.this.getMoveControl().setWantedPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

					if (RatlanteanSpirit.this.getTarget() == null) {
						RatlanteanSpirit.this.getLookControl().setLookAt((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}

	class AIFireballAttack extends Goal {
		private final RatlanteanSpirit parentEntity;
		public int attackTimer;

		public AIFireballAttack(RatlanteanSpirit ghast) {
			this.parentEntity = ghast;
		}

		public boolean canUse() {
			return this.parentEntity.getTarget() != null;
		}

		public void start() {
			this.attackTimer = 0;
		}

		public void stop() {
		}

		public void tick() {
			LivingEntity living = this.parentEntity.getTarget();
			if (living.distanceToSqr(this.parentEntity) >= 4096.0D || !this.parentEntity.hasLineOfSight(living)) {
				RatlanteanSpirit.this.getMoveControl().setWantedPosition(living.getX(), living.getY(), living.getZ(), 0.5D);
			}
			if (living.distanceToSqr(this.parentEntity) < 4096.0D) {
				Level level = this.parentEntity.getLevel();
				++this.attackTimer;

				if (this.attackTimer == 20) {
					Vec3 vec3 = this.parentEntity.getViewVector(1.0F);
					double d2 = living.getX() - (this.parentEntity.getX() + vec3.x * 4.0D);
					double d3 = living.getY(0.5D) - (0.5D + this.parentEntity.getY(0.5D));
					double d4 = living.getZ() - (this.parentEntity.getZ() + vec3.z * 4.0D);
					RatlanteanFlame flame = new RatlanteanFlame(level, this.parentEntity, d2, d3, d4);
					flame.setPos(this.parentEntity.getX() + vec3.x * 4.0D, this.parentEntity.getY(0.5D) + 0.5D, flame.getZ() + vec3.z * 4.0D);
					level.addFreshEntity(flame);
					this.attackTimer = -10;
				}
			} else if (this.attackTimer > 0) {
				--this.attackTimer;
			}
		}
	}

	public static boolean checkSpiritSpawnRules(EntityType<? extends Mob> entity, LevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource random) {
		BlockPos blockpos = pos.below();
		return type == MobSpawnType.SPAWNER || accessor.getBlockState(blockpos).isValidSpawn(accessor, blockpos, entity) && random.nextInt(5) == 0;
	}
}
