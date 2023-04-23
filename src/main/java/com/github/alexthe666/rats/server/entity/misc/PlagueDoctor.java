package com.github.alexthe666.rats.server.entity.misc;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.server.entity.monster.boss.BlackDeath;
import com.github.alexthe666.rats.server.entity.PlagueLegion;
import com.github.alexthe666.rats.server.entity.projectile.PurifyingLiquid;
import com.github.alexthe666.rats.server.entity.ai.goal.PlagueDoctorFollowGolemGoal;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.misc.PlagueDoctorTrades;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class PlagueDoctor extends AbstractVillager implements RangedAttackMob {

	private static final EntityDataAccessor<Boolean> WILL_DESPAWN = SynchedEntityData.defineId(PlagueDoctor.class, EntityDataSerializers.BOOLEAN);
	private static final Predicate<LivingEntity> PLAGUE_PREDICATE = entity -> entity != null && entity.hasEffect(RatsEffectRegistry.PLAGUE.get()) || entity instanceof PlagueLegion || (entity instanceof Rat rat && rat.hasPlague());

	private BlockPos wanderTarget;
	private int despawnDelay;
	private boolean eating = false;
	private int munchCounter;
	private boolean restockedToday;

	public PlagueDoctor(EntityType<? extends AbstractVillager> type, Level level) {
		super(type, level);
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
		this.setCanPickUpLoot(true);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new UseItemGoal<>(this, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), RatsSoundRegistry.PLAGUE_DOCTOR_DISAPPEAR.get(), doctor -> !this.getLevel().isDay() && !doctor.isInvisible()));
		this.goalSelector.addGoal(0, new UseItemGoal<>(this, new ItemStack(Items.MILK_BUCKET), RatsSoundRegistry.PLAGUE_DOCTOR_REAPPEAR.get(), doctor -> this.getLevel().isDay() && doctor.isInvisible()));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.5D, 0.5D));
		this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
		this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
		this.goalSelector.addGoal(2, new PlagueDoctor.MoveToGoal(this, 2.0D, 0.35D));
		this.goalSelector.addGoal(3, new PlagueDoctorFollowGolemGoal(this));
		this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(new ItemStack(Blocks.POPPY)), false));
		this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35D));
		this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, ZombieVillager.class, 0, true, false, entity -> entity != null && entity.isAlive() && !((ZombieVillager) entity).isConverting()));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, PLAGUE_PREDICATE));
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
		return null;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(WILL_DESPAWN, false);
	}

	public boolean willDespawn() {
		return this.getEntityData().get(WILL_DESPAWN);
	}

	public void setWillDespawn(boolean despawn) {
		this.getEntityData().set(WILL_DESPAWN, despawn);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.5D);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.getLevel().isClientSide() && this.willDespawn()) {
			this.handleDespawn();
		}

		if (!this.getLevel().isClientSide() && this.getHealth() < this.getMaxHealth() && this.getItemInHand(InteractionHand.MAIN_HAND).getFoodProperties(this) != null && this.getRandom().nextInt(25) == 0) {
			this.eating = true;
		}

		if (this.getTarget() != null && (!this.getTarget().isAlive() || !PLAGUE_PREDICATE.test(this.getTarget()))) {
			this.setTarget(null);
		}

		if (this.eating) {
			ItemStack stack = this.getItemInHand(InteractionHand.MAIN_HAND);

			if (this.tickCount % 4 == 0) {
				this.munchCounter++;
				this.playSound(this.getEatingSound(stack), 0.75F, 1.0F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.4F);
				this.gameEvent(GameEvent.EAT);
				this.getLevel().broadcastEntityEvent(this, (byte) 77);
			}

			if (this.munchCounter == 10) {
				this.heal(stack.getFoodProperties(this).getNutrition());
				this.addEatEffect(stack, this.getLevel(), this);
				stack.shrink(1);
				this.munchCounter = 0;
				this.eating = false;
			}
		}

		if (!this.willDespawn()) {
			if (this.getLevel().isNight() && this.restockedToday) this.restockedToday = false;

			if (!this.restockedToday && this.exhaustedAnyTrades() && this.getLevel().isDay()) {
				for (MerchantOffer merchantoffer : this.getOffers()) {
					merchantoffer.resetUses();
				}
				this.restockedToday = true;
			}
		}
	}

	private boolean exhaustedAnyTrades() {
		for (MerchantOffer merchantoffer : this.getOffers()) {
			if (merchantoffer.needsRestock()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 77) {
			ItemStack stack = this.getItemBySlot(EquipmentSlot.MAINHAND);
			if (!stack.isEmpty()) {
				for (int i = 0; i < 8; ++i) {
					Vec3 vec3 = new Vec3((this.getRandom().nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
					vec3 = vec3.xRot(-this.getXRot() * Mth.DEG_TO_RAD);
					vec3 = vec3.yRot(-this.getYHeadRot() * Mth.DEG_TO_RAD);
					double d0 = -this.getRandom().nextFloat() * 0.6D - 0.3D;
					Vec3 vec31 = new Vec3((this.getRandom().nextFloat() - 0.5D) * 0.2D, d0, 0.6D);
					vec31 = vec31.xRot(-this.getXRot() * Mth.DEG_TO_RAD);
					vec31 = vec31.yRot(-this.getYHeadRot() * Mth.DEG_TO_RAD);
					vec31 = vec31.add(this.getX(), this.getEyeY(), this.getZ());
					if (this.getLevel() instanceof ServerLevel server)
						server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), vec31.x(), vec31.y(), vec31.z(), 1, vec3.x(), vec3.y() + 0.05D, vec3.z(), 0.0D);
					else
						this.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), vec31.x(), vec31.y(), vec31.z(), vec3.x(), vec3.y() + 0.05D, vec3.z());
				}
			}
		} else {
			super.handleEntityEvent(id);
		}

	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("DespawnDelay", this.despawnDelay);
		compound.putBoolean("WillDespawn", this.willDespawn());
		compound.putBoolean("RestockedToday", this.restockedToday);
		if (this.wanderTarget != null) {
			compound.put("WanderTarget", NbtUtils.writeBlockPos(this.wanderTarget));
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DespawnDelay", 99)) {
			this.despawnDelay = compound.getInt("DespawnDelay");
		}
		this.setWillDespawn(compound.getBoolean("WillDespawn"));
		this.restockedToday = compound.getBoolean("RestockedToday");

		if (compound.contains("WanderTarget")) {
			this.wanderTarget = NbtUtils.readBlockPos(compound.getCompound("WanderTarget"));
		}

		this.setAge(Math.max(0, this.getAge()));
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	public void setDespawnDelay(int delay) {
		this.despawnDelay = delay;
	}

	private void handleDespawn() {
		if (this.willDespawn() && !this.isTrading() && --this.despawnDelay <= 0) {
			this.playSound(RatsSoundRegistry.PLAGUE_DOCTOR_DISAPPEAR.get());
			this.discard();
		}
	}

	@Override
	public boolean canBeAffected(MobEffectInstance effect) {
		return effect.getEffect() != RatsEffectRegistry.PLAGUE.get() && super.canBeAffected(effect);
	}

	@Override
	public int getAmbientSoundInterval() {
		return 400;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTrading() ? SoundEvents.VILLAGER_TRADE : SoundEvents.VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.VILLAGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.VILLAGER_DEATH;
	}

	@Override
	protected SoundEvent getDrinkingSound(ItemStack stack) {
		return stack.is(Items.POTION) ? RatsSoundRegistry.PLAGUE_DOCTOR_DRINK_POTION.get() : RatsSoundRegistry.PLAGUE_DOCTOR_DRINK.get();
	}

	@Override
	protected SoundEvent getTradeUpdatedSound(boolean yes) {
		return yes ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO;
	}

	@Override
	public SoundEvent getNotifyTradeSound() {
		return SoundEvents.VILLAGER_YES;
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		double d0 = target.getY() + (double) target.getEyeHeight() - 1.1D;
		double d1 = target.getX() + target.getDeltaMovement().x() - this.getX();
		double d2 = d0 - this.getY();
		double d3 = target.getZ() + target.getDeltaMovement().z() - this.getZ();
		float f = Mth.sqrt((float) (d1 * d1 + d3 * d3));
		PurifyingLiquid entitypotion = new PurifyingLiquid(this.getLevel(), this, false);
		entitypotion.setXRot(entitypotion.getXRot() + 20.0F);
		entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
		this.getLevel().playSound(null, this.getX(), this.getY(), this.getZ(), RatsSoundRegistry.PLAGUE_DOCTOR_THROW.get(), this.getSoundSource(), 1.0F, 0.8F + this.getRandom().nextFloat() * 0.4F);
		this.getLevel().addFreshEntity(entitypotion);
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightning) {
		if (RatConfig.blackDeathLightning) {
			if (this.isAlive() && level.getCurrentDifficultyAt(this.blockPosition()).getDifficulty() != Difficulty.PEACEFUL) {
				BlackDeath death = new BlackDeath(RatsEntityRegistry.BLACK_DEATH.get(), level);
				death.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
				ForgeEventFactory.onFinalizeSpawn(death, level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.CONVERSION, null, null);
				death.setNoAi(this.isNoAi());
				if (!this.getMainHandItem().isEmpty()) {
					this.spawnAtLocation(this.getMainHandItem());
				}
				if (this.hasCustomName()) {
					death.setCustomName(this.getCustomName());
				}
				level.addFreshEntity(death);
				for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(death.blockPosition()).inflate(16.0D))) {
					RatsAdvancementsRegistry.BLACK_DEATH_SUMMONED.trigger(player);
				}
				this.discard();
			}
		}
	}

	@Override
	protected void rewardTradeXp(MerchantOffer offer) {
		if (offer.shouldRewardExp()) {
			this.getLevel().addFreshEntity(new ExperienceOrb(this.getLevel(), this.getX(), this.getY() + 0.5D, this.getZ(), offer.getXp()));
		}
	}

	@Nullable
	public BlockPos getWanderTarget() {
		return wanderTarget;
	}

	public void setWanderTarget(@Nullable BlockPos blockpos1) {
		this.wanderTarget = blockpos1;
	}

	public class MoveToGoal extends Goal {
		final PlagueDoctor plagueDoctor;
		final double range;
		final double speed;

		MoveToGoal(PlagueDoctor doctor, double followRange, double speedModifier) {
			this.plagueDoctor = doctor;
			this.range = followRange;
			this.speed = speedModifier;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public void stop() {
			this.plagueDoctor.setWanderTarget(null);
			PlagueDoctor.this.getNavigation().stop();
		}

		public boolean canUse() {
			BlockPos blockpos = this.plagueDoctor.getWanderTarget();
			return blockpos != null && this.isInRange(blockpos, this.range);
		}

		public void tick() {
			BlockPos blockpos = this.plagueDoctor.getWanderTarget();
			if (blockpos != null && PlagueDoctor.this.getNavigation().isDone()) {
				if (this.isInRange(blockpos, 10.0D)) {
					Vec3 vec3d = (new Vec3((double) blockpos.getX() - this.plagueDoctor.getX(), (double) blockpos.getY() - this.plagueDoctor.getY(), (double) blockpos.getZ() - this.plagueDoctor.getZ())).normalize();
					Vec3 vec3d1 = vec3d.scale(10.0D).add(this.plagueDoctor.getX(), this.plagueDoctor.getY(), this.plagueDoctor.getZ());
					PlagueDoctor.this.getNavigation().moveTo(vec3d1.x, vec3d1.y, vec3d1.z, this.speed);
				} else {
					PlagueDoctor.this.getNavigation().moveTo(blockpos.getX(), blockpos.getY(), blockpos.getZ(), this.speed);
				}
			}

		}

		private boolean isInRange(BlockPos pos, double range) {
			return !pos.closerToCenterThan(this.plagueDoctor.position(), range);
		}
	}

	@Override
	public boolean showProgressBar() {
		return false;
	}

	@Override
	protected void updateTrades() {
		VillagerTrades.ItemListing[] level1 = PlagueDoctorTrades.PLAGUE_DOCTOR_TRADES.get(1);
		VillagerTrades.ItemListing[] level2 = PlagueDoctorTrades.PLAGUE_DOCTOR_TRADES.get(2);
		if (level1 != null && level2 != null) {
			MerchantOffers merchantoffers = this.getOffers();
			this.addOffersFromItemListings(merchantoffers, level1, 5);
			int i = this.getRandom().nextInt(level2.length);
			int j = this.getRandom().nextInt(level2.length);
			int k = this.getRandom().nextInt(level2.length);
			int rolls = 0;
			while ((j == i) && rolls < 100) {
				j = this.getRandom().nextInt(level2.length);
				rolls++;
			}
			rolls = 0;
			while ((k == i || k == j) && rolls < 100) {
				k = this.getRandom().nextInt(level2.length);
				rolls++;
			}
			VillagerTrades.ItemListing rareTrade1 = level2[i];
			VillagerTrades.ItemListing rareTrade2 = level2[j];
			VillagerTrades.ItemListing rareTrade3 = level2[k];
			MerchantOffer merchantoffer1 = rareTrade1.getOffer(this, this.getRandom());
			if (merchantoffer1 != null) {
				merchantoffers.add(merchantoffer1);
			}
			MerchantOffer merchantoffer2 = rareTrade2.getOffer(this, this.getRandom());
			if (merchantoffer2 != null) {
				merchantoffers.add(merchantoffer2);
			}
			MerchantOffer merchantoffer3 = rareTrade3.getOffer(this, this.getRandom());
			if (merchantoffer3 != null) {
				merchantoffers.add(merchantoffer3);
			}

			if (!RatsMod.RATLANTIS_DATAPACK_ENABLED) {
				merchantoffers.add(PlagueDoctorTrades.COMBINER_TRADE.getOffer(this, this.getRandom()));
				merchantoffers.add(PlagueDoctorTrades.SEPARATOR_TRADE.getOffer(this, this.getRandom()));
				merchantoffers.add(PlagueDoctorTrades.UPGRADE_COMBINED_TRADE.getOffer(this, this.getRandom()));
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(RatsItemRegistry.PLAGUE_TOME.get())) {
			if (!this.isBaby() && !this.getLevel().isClientSide()) {
				BlackDeath death = new BlackDeath(RatsEntityRegistry.BLACK_DEATH.get(), this.getLevel());
				death.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
				ForgeEventFactory.onFinalizeSpawn(death, (ServerLevelAccessor) this.getLevel(), this.getLevel().getCurrentDifficultyAt(death.blockPosition()), MobSpawnType.TRIGGERED, null, null);
				if (this.hasCustomName()) {
					death.setCustomName(this.getCustomName());
				}
				if (!this.getMainHandItem().isEmpty()) {
					this.spawnAtLocation(this.getMainHandItem());
				}
				this.getLevel().addFreshEntity(death);
				RatsAdvancementsRegistry.BLACK_DEATH_SUMMONED.trigger((ServerPlayer) player);
				this.getLevel().playSound(null, this.blockPosition(), RatsSoundRegistry.BLACK_DEATH_SUMMON.get(), SoundSource.HOSTILE, 1.5F, 1.0F);
				this.discard();
				return InteractionResult.SUCCESS;
			}
		} else if (itemstack.is(Items.NAME_TAG)) {
			this.setWillDespawn(false);
			itemstack.interactLivingEntity(player, this, hand);
			return InteractionResult.SUCCESS;
		} else if (!(itemstack.is(Items.VILLAGER_SPAWN_EGG) && itemstack.is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "plague_doctor_spawn_egg")))) &&
				this.isAlive() && !this.isTrading() && !this.isBaby()) {
			if (hand == InteractionHand.MAIN_HAND) {
				player.awardStat(Stats.TALKED_TO_VILLAGER);
			}
			if (this.getOffers().isEmpty()) {
				return super.mobInteract(player, hand);
			} else {
				if (!this.getLevel().isClientSide()) {
					this.setTradingPlayer(player);
					this.openTradingScreen(player, this.getDisplayName(), 1);
				}

				return InteractionResult.sidedSuccess(this.getLevel().isClientSide());
			}
		}
		return super.mobInteract(player, hand);
	}
}