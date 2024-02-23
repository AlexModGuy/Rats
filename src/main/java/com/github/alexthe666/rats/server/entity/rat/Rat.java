package com.github.alexthe666.rats.server.entity.rat;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.registry.worldgen.RatlantisDimensionRegistry;
import com.github.alexthe666.rats.server.entity.ai.goal.RatEnterTrapGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.WildRatAvoidPlayerGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.WildRatDefendPlagueDoctorGoal;
import com.github.alexthe666.rats.server.entity.ai.goal.WildRatTargetFoodGoal;
import com.github.alexthe666.rats.server.entity.monster.boss.RatKing;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.misc.RatsDateFetcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class Rat extends DiggingRat {

	private static final EntityDataAccessor<Boolean> PLAGUE = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> TOGA = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RAT_KING_TRANSFORMATION = SynchedEntityData.defineId(Rat.class, EntityDataSerializers.BOOLEAN);
	private int ratKingTransformTicks = 0;
	public int wildTrust = 0;
	public int cheeseFeedings = 0;
	private static final Predicate<Player> AVOIDED_PLAYERS = entity -> !entity.isDiscrete() && !entity.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemRegistry.PIPER_HAT.get()) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity);

	public Rat(EntityType<? extends Rat> rat, Level level) {
		super(rat, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 8.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.35D)
				.add(Attributes.FLYING_SPEED, 0.35D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.FOLLOW_RANGE, 12.0D);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new WildRatAvoidPlayerGoal(this, entity -> AVOIDED_PLAYERS.test((Player) entity)));
		this.goalSelector.addGoal(2, new RatEnterTrapGoal(this));
		this.goalSelector.addGoal(6, new PanicGoal(this, 1.225D) {
			@Override
			protected boolean shouldPanic() {
				return !Rat.this.hasPlague() && this.mob.getLastHurtByMob() instanceof Player;
			}
		});
		this.targetSelector.addGoal(0, new WildRatTargetFoodGoal(this));
		this.targetSelector.addGoal(1, new WildRatDefendPlagueDoctorGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, entity -> {
			if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) || !Rat.this.hasPlague()) return false;
			return !entity.isAlliedTo(Rat.this) && !entity.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemRegistry.BLACK_DEATH_MASK.get()) && entity.level().getDifficulty() != Difficulty.PEACEFUL;
		}));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(PLAGUE, false);
		this.getEntityData().define(TOGA, false);
		this.getEntityData().define(RAT_KING_TRANSFORMATION, false);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getRestrictCenter() != BlockPos.ZERO) {
			BlockPos home = this.getRestrictCenter();
			compound.put("Home", this.makeDoubleList(home.getX(), home.getY(), home.getZ()));
			compound.putFloat("HomeDistance", this.getRestrictRadius());
		}

		compound.putInt("CheeseFeedings", this.cheeseFeedings);
		compound.putInt("WildTrust", this.wildTrust);
		compound.putBoolean("Plague", this.hasPlague());
		compound.putBoolean("Toga", this.hasToga());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Home", 9)) {
			ListTag nbttaglist = compound.getList("Home", 6);
			int hx = (int) nbttaglist.getDouble(0);
			int hy = (int) nbttaglist.getDouble(1);
			int hz = (int) nbttaglist.getDouble(2);
			this.restrictTo(new BlockPos(hx, hy, hz), (int) compound.getFloat("HomeDistance"));
		}
		this.wildTrust = compound.getInt("WildTrust");
		this.cheeseFeedings = compound.getInt("CheeseFeedings");
		this.setPlagued(compound.getBoolean("Plague"));
		this.setToga(compound.getBoolean("Toga"));
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.getOwner() == null || !this.getOwner().isAlive()) {
			this.setOwnerUUID(null);
		}

		if (this.hasPlague() && this.getRandom().nextFloat() < 0.3F) {
			double d0 = 0D;
			double d1 = this.getRandom().nextGaussian() * 0.05D + 0.5D;
			double d2 = 0D;
			this.level().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
		}

		if (this.isBecomingRatKing() && (!this.getMainHandItem().is(RatsItemRegistry.FILTH_CORRUPTION.get()) || this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty() == Difficulty.PEACEFUL)) {
			this.getEntityData().set(RAT_KING_TRANSFORMATION, false);
			if (this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty() == Difficulty.PEACEFUL) {
				this.spawnAtLocation(this.getMainHandItem());
				this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			}
		}
		if (this.isBecomingRatKing()) {
			this.setAnimation(ANIMATION_EAT);
			this.setRatStatus(RatStatus.EATING);
			this.eatingTicks++;

			this.ratKingTransformTicks++;
			this.handleRatKingTransform();
		}
	}

	@Override
	public int getExperienceReward() {
		return this.hasPlague() ? 10 : super.getExperienceReward();
	}

	@Nullable
	@Override
	public LivingEntity getOwner() {
		try {
			UUID uuid = this.getOwnerUUID();
			if (this.level() instanceof ServerLevel server) {
				Entity entity = server.getEntity(uuid);
				if (entity instanceof LivingEntity living) {
					return living;
				}
			}
		} catch (IllegalArgumentException e) {
			return null;
		}
		return null;
	}

	private void handleRatKingTransform() {
		if (this.level().isClientSide()) {
			if (this.ratKingTransformTicks < 120) {
				for (int i = 0; i < 15; i++) {
					Vec3 ratPos = DefaultRandomPos.getPos(this, 32, 10);
					if (ratPos == null) continue;
					if (this.level().getBlockState(BlockPos.containing(ratPos)).isAir() && !this.level().getBlockState(BlockPos.containing(ratPos).below()).isAir()) {
						this.level().addAlwaysVisibleParticle(RatsParticleRegistry.RUNNING_RAT.get(), ratPos.x(), ratPos.y(), ratPos.z(), this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
						break;
					}
				}
			}
			if (this.ratKingTransformTicks % 2 == 0) {
				for (int i = 0; i < this.ratKingTransformTicks * 2; i++) {
					double randomOff = Math.max(this.ratKingTransformTicks / 60.0D, 1.0D);
					this.level().addParticle(RatsParticleRegistry.RAT_KING_SMOKE.get(),
							this.position().x() - (randomOff / 2) + (this.getRandom().nextDouble() * randomOff),
							this.position().y() + this.getRandom().nextDouble() * 0.75D,
							this.position().z() - (randomOff / 2) + (this.getRandom().nextDouble() * randomOff),
							0.0D, 0.0D, 0.0D);
				}
			}
		}

		if (this.ratKingTransformTicks == 200 && !this.level().isClientSide()) {
			RatKing king = new RatKing(RatsEntityRegistry.RAT_KING.get(), this.level());
			king.copyPosition(this);
			ForgeEventFactory.onFinalizeSpawn(king, (ServerLevelAccessor) this.level(), this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.CONVERSION, null, null);
			this.level().addFreshEntity(king);
			this.discard();
		}
	}

	@Override
	public boolean isHoldingItemInHands() {
		return (this.isBecomingRatKing() || this.isEating()) && this.sleepProgress <= 0.0F;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return this.hasPlague();
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean playerKill) {
		if (this.hasToga()) {
			this.spawnAtLocation(new ItemStack(RatlantisItemRegistry.RAT_TOGA.get()), 0.0F);
		}
		super.dropCustomDeathLoot(source, looting, playerKill);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		data = super.finalizeSpawn(accessor, difficulty, type, data, tag);
		if (this.getRandom().nextInt(15) == 0 && this.level().getDifficulty() != Difficulty.PEACEFUL && type != MobSpawnType.CONVERSION) {
			this.setPlagued(true);
		}
		if (accessor.getLevel().dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY)) {
			this.setToga(true);
		}
		if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
			if (RatsDateFetcher.isHalloweenSeason() && this.getRandom().nextFloat() <= 0.25F) {
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Blocks.CARVED_PUMPKIN));
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			} else if (RatsDateFetcher.isChristmasSeason() && this.getRandom().nextFloat() <= (RatsDateFetcher.isChristmasDay() ? 1.0F : 0.25F) || (RatConfig.ratsSpawnWithSantaHats && this.getRandom().nextInt(100) == 0)) {
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.SANTA_HAT.get()));
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			} else if ((RatsDateFetcher.isNewYearsEve() && this.getRandom().nextFloat() <= 0.25F) || RatsDateFetcher.isAlexsBDay() || RatsDateFetcher.isGizmosBDay() || (RatConfig.ratsSpawnWithPartyHats && this.getRandom().nextInt(100) == 0)) {
				ItemStack stack = new ItemStack(RatsItemRegistry.PARTY_HAT.get());
				((DyeableLeatherItem) stack.getItem()).setColor(stack, (int) (this.getRandom().nextFloat() * 0xFFFFFF));
				this.setItemSlot(EquipmentSlot.HEAD, stack);
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			} else if (RatsDateFetcher.isPirateDay() && this.getRandom().nextFloat() <= 0.25F) {
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.PIRAT_HAT.get()));
				this.setGuaranteedDrop(EquipmentSlot.HEAD);
			}
		}
		if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
			if (RatsDateFetcher.isChristmasDay() && this.getRandom().nextFloat() <= 0.75F) {
				this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.TINY_COIN.get()));
				this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
			}
		}
		return data;
	}

	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!this.hasPlague()) {
			if (itemstack.is(RatsItemRegistry.FILTH_CORRUPTION.get()) && this.level().getCurrentDifficultyAt(this.blockPosition()).getDifficulty() != Difficulty.PEACEFUL) {
				this.playSound(RatsSoundRegistry.RAT_KING_SUMMON.get(), 1F, 1.5F);
				if (!this.getMainHandItem().isEmpty()) {
					this.spawnAtLocation(this.getMainHandItem());
					this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				}
				this.setItemInHand(InteractionHand.MAIN_HAND, player.getItemInHand(hand).copyWithCount(1));
				this.getEntityData().set(RAT_KING_TRANSFORMATION, true);
				if (!player.isCreative()) {
					itemstack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			if (!this.level().isClientSide() && itemstack.is(RatsItemRegistry.CREATIVE_CHEESE.get())) {
				TamedRat rat = RatUtils.tameRat(this, this.level());
				this.level().broadcastEntityEvent(rat, (byte) 83);
				rat.tame(player);
				rat.setCommand(RatCommand.SIT);
				return InteractionResult.SUCCESS;
			}
			if (itemstack.interactLivingEntity(player, this, hand) == InteractionResult.SUCCESS) {
				return InteractionResult.SUCCESS;
			}
		}

		return super.mobInteract(player, hand);
	}

	public boolean isBecomingRatKing() {
		return this.getEntityData().get(RAT_KING_TRANSFORMATION);
	}

	public boolean hasToga() {
		return this.getEntityData().get(TOGA);
	}

	public void setToga(boolean toga) {
		this.getEntityData().set(TOGA, toga);
	}

	public boolean hasPlague() {
		return this.getEntityData().get(PLAGUE);
	}

	public void setPlagued(boolean plague) {
		this.getEntityData().set(PLAGUE, plague);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isBecomingRatKing() || super.isInvulnerableTo(source);
	}

	@Override
	public boolean isInvulnerable() {
		return super.isInvulnerable() || this.isBecomingRatKing();
	}

	public static boolean checkRatSpawnRules(EntityType<? extends Mob> entityType, LevelAccessor accessor, MobSpawnType type, BlockPos pos, RandomSource random) {
		if (random.nextInt(16) == 0) {
			return type == MobSpawnType.SPAWNER || spawnCheck(accessor, pos, random, type);
		}
		return false;
	}

	private static boolean spawnCheck(LevelAccessor accessor, BlockPos pos, RandomSource random, MobSpawnType type) {
		if (!accessor.getLevelData().getGameRules().getBoolean(RatsMod.SPAWN_RATS)) return false;
		if (type != MobSpawnType.NATURAL) return true;
		int spawnRoll = RatConfig.ratSpawnDecrease;
		if (accessor instanceof ServerLevelAccessor server && server.getLevel().dimension().equals(RatlantisDimensionRegistry.DIMENSION_KEY))
			return spawnRoll <= 0 || random.nextInt(spawnRoll) == 0;
		if (RatConfig.ratsSpawnLikeMonsters) {
			if (accessor.getDifficulty() == Difficulty.PEACEFUL) {
				spawnRoll *= 2;
			}
			if (spawnRoll == 0 || accessor.getRandom().nextInt(spawnRoll) == 0) {
				BlockState state = accessor.getBlockState(pos.below());
				return isValidLightLevel(accessor, random, pos) && state.isValidSpawn(accessor, pos.below(), RatsEntityRegistry.RAT.get());
			}
		} else {
			spawnRoll /= 2;
			return (spawnRoll <= 0 || random.nextInt(spawnRoll) == 0);
		}

		return false;
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor accessor, MobSpawnType type) {
		return spawnCheck(accessor, this.blockPosition(), accessor.getRandom(), type);
	}

	@Override
	public Component getName() {
		if (!this.hasCustomName() && this.hasPlague()) {
			return Component.translatable("entity.rats.plague_rat");
		}
		return super.getName();
	}

	@Override
	public int getAmbientSoundInterval() {
		return this.hasPlague() ? 200 : super.getAmbientSoundInterval();
	}

	@Override
	protected @Nullable SoundEvent getAmbientSound() {
		if (this.hasPlague() && this.getTarget() != null) {
			return RatsSoundRegistry.RAT_GROWL.get();
		}
		return super.getAmbientSound();
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag && this.hasPlague()) {
			this.doEnchantDamageEffects(this, entity);
			if (entity instanceof LivingEntity living && this.rollForPlague(living)) {
				living.addEffect(new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 6000));
			}
		}
		return flag;
	}

	@Override
	protected void doPush(Entity entity) {
		if (!this.level().isClientSide() && this.hasPlague()) {
			if (entity instanceof Rat rat && !rat.hasPlague()) {
				rat.setPlagued(true);
			} else if (entity instanceof LivingEntity living && this.rollForPlague(living)) {
				living.addEffect(new MobEffectInstance(RatsEffectRegistry.PLAGUE.get(), 6000));
			}
		}
		super.doPush(entity);
	}

	private boolean rollForPlague(LivingEntity target) {
		boolean mask = target.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemRegistry.PLAGUE_DOCTOR_MASK.get()) || target.getItemBySlot(EquipmentSlot.HEAD).is(RatsItemRegistry.BLACK_DEATH_MASK.get());
		if (mask) {
			return this.getRandom().nextFloat() < 0.3F;
		}
		return true;
	}
}
