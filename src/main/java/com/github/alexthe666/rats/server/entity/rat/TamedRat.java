package com.github.alexthe666.rats.server.entity.rat;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.client.events.ForgeClientEvents;
import com.github.alexthe666.rats.registry.*;
import com.github.alexthe666.rats.server.block.RatCageBlock;
import com.github.alexthe666.rats.server.block.RatTubeBlock;
import com.github.alexthe666.rats.server.entity.RatMount;
import com.github.alexthe666.rats.server.entity.ai.goal.*;
import com.github.alexthe666.rats.server.entity.ai.navigation.control.*;
import com.github.alexthe666.rats.server.entity.ai.navigation.navigation.EtherealRatNavigation;
import com.github.alexthe666.rats.server.entity.ai.navigation.navigation.RatFlightNavigation;
import com.github.alexthe666.rats.server.entity.ai.navigation.navigation.RatNavigation;
import com.github.alexthe666.rats.server.entity.mount.RatBiplaneMount;
import com.github.alexthe666.rats.server.entity.mount.RatMountBase;
import com.github.alexthe666.rats.server.items.OreRatNuggetItem;
import com.github.alexthe666.rats.server.items.RatSackItem;
import com.github.alexthe666.rats.server.items.RatStaffItem;
import com.github.alexthe666.rats.server.items.upgrades.BucketRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.EnergyRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.MountRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.OreDoublingRatUpgradeItem;
import com.github.alexthe666.rats.server.items.upgrades.interfaces.*;
import com.github.alexthe666.rats.server.message.ManageRatStaffPacket;
import com.github.alexthe666.rats.server.message.RatsNetworkHandler;
import com.github.alexthe666.rats.server.message.SetDancingRatPacket;
import com.github.alexthe666.rats.server.misc.RatUpgradeUtils;
import com.github.alexthe666.rats.server.misc.RatUtils;
import com.github.alexthe666.rats.server.misc.RatVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TamedRat extends InventoryRat {

	private static final EntityDataAccessor<Boolean> TOGA = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> VISUAL_FLAG = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> DYED = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> DYE_COLOR = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Boolean> DANCING = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> DANCE_MOVES = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> HELD_RF = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> RESPAWN_COUNTDOWN = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Optional<GlobalPos>> PICKUP_POS = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Optional<GlobalPos>> DEPOSIT_POS = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.OPTIONAL_GLOBAL_POS);
	private static final EntityDataAccessor<Boolean> IS_IN_WHEEL = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<String> SPECIAL_DYE = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> MOUNT_RESPAWN_COOLDOWN = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(TamedRat.class, EntityDataSerializers.BOOLEAN);

	public Direction depositFacing = Direction.UP;
	public boolean crafting = false;
	public boolean climbingTube = false;
	public int cookingProgress = 0;
	public int coinCooldown = 0;
	public int breedCooldown = 0;
	public BlockPos jukeboxPos;
	public FluidStack transportingFluid = FluidStack.EMPTY;
	private Goal harvestGoal;
	private Goal pickupGoal;
	private Goal depositGoal;
	private Goal attackGoal;
	/*
	   0 = tamed navigator
	   1 = flight navigator
	   2 = tube navigator
	   3 = aquatic navigator
	   4 = ethereal navigator
	   5 = cage navigator
	 */
	protected int navigatorType;
	public int rangedAttackCooldown = 0;
	public int visualCooldown = 0;
	private int poopCooldown = 0;
	public int pickpocketCooldown = 0;
	public int randomEffectCooldown = 0;
	private int updateNavigationCooldown;
	public boolean isCurrentlyWorking;

	public TamedRat(EntityType<? extends TamableAnimal> type, Level level) {
		super(type, level);
		Arrays.fill(this.armorDropChances, 2.0F);
		Arrays.fill(this.handDropChances, 2.0F);
		this.xpReward = 0;
		this.updateNavigationCooldown = 100;
		this.setupDynamicAI();
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
		this.harvestGoal = new RatHarvestCropsGoal(this);
		this.pickupGoal = new RatPickupGoal(this, RatPickupGoal.PickupType.INVENTORY);
		this.depositGoal = new RatDepositGoal(this, RatDepositGoal.DepositType.INVENTORY);
		this.attackGoal = new RatMeleeAttackGoal(this, 1.3D, true);
		this.goalSelector.addGoal(0, new RatFloatGoal(this));
		this.goalSelector.addGoal(1, this.attackGoal);
		this.goalSelector.addGoal(2, new RatFollowOwnerGoal(this, 1.25D, 10.0F, 3.0F));
		this.goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.25D));
		this.goalSelector.addGoal(3, this.harvestGoal);
		this.goalSelector.addGoal(4, this.depositGoal);
		this.goalSelector.addGoal(5, this.pickupGoal);
		this.goalSelector.addGoal(6, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(7, new RatWanderGoal(this, 1.25D));
		this.goalSelector.addGoal(7, new RatPatrolGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F) {
			@Override
			public boolean canUse() {
				return ((TamedRat) this.mob).sleepProgress <= 0.0F && super.canUse();
			}
		});
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this) {
			@Override
			public boolean canUse() {
				return ((TamedRat) this.mob).sleepProgress <= 0.0F && super.canUse();
			}
		});
		this.targetSelector.addGoal(0, new RatTargetItemsGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, true, entity -> EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(entity) && !entity.isBaby() && TamedRat.this.canMove() && TamedRat.this.shouldHuntAnimal()));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, true, entity -> entity instanceof Enemy && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(entity) && TamedRat.this.canMove() && TamedRat.this.shouldHuntMonster()));
		this.targetSelector.addGoal(2, new RatOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new RatOwnerHurtTargetGoal(this));
	}

	protected void setupDynamicAI() {
		AtomicReference<Goal> newHarvest = new AtomicReference<>(new RatHarvestCropsGoal(this));
		AtomicReference<Goal> newDeposit = new AtomicReference<>(new RatDepositGoal(this, RatDepositGoal.DepositType.INVENTORY));
		AtomicReference<Goal> newPickup = new AtomicReference<>(new RatPickupGoal(this, RatPickupGoal.PickupType.INVENTORY));
		AtomicReference<Goal> newAttack = new AtomicReference<>(new RatMeleeAttackGoal(this, 1.45D, true));

		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof ChangesAIUpgrade, stack ->
				((ChangesAIUpgrade) stack.getItem()).addNewWorkGoals(this).forEach(goal -> {
					if (!(goal instanceof RatWorkGoal workGoal)) {
						throw new IllegalArgumentException("Rat Goals must implement the interface RatWorkGoal! Goal" + goal.getClass().getName() + "doesnt do this!");
					}
					switch (workGoal.getRatTaskType()) {
						case ATTACK -> newAttack.set(goal);
						case DEPOSIT -> newDeposit.set(goal);
						case PICKUP -> newPickup.set(goal);
						case HARVEST -> newHarvest.set(goal);
					}
				})
		);

		if (!this.level().isClientSide()) {
			this.goalSelector.removeGoal(this.harvestGoal);
			this.goalSelector.removeGoal(this.depositGoal);
			this.goalSelector.removeGoal(this.pickupGoal);
			this.goalSelector.removeGoal(this.attackGoal);

			this.attackGoal = newAttack.get();
			this.depositGoal = newDeposit.get();
			this.pickupGoal = newPickup.get();
			this.harvestGoal = newHarvest.get();

			this.goalSelector.addGoal(1, this.attackGoal);
			this.goalSelector.addGoal(3, this.depositGoal);
			this.goalSelector.addGoal(4, this.pickupGoal);
			this.goalSelector.addGoal(5, this.harvestGoal);

			if (this.hasFlightUpgrade()) {
				this.switchNavigator(1);
			} else if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get())) {
				this.switchNavigator(3);
			} else if (RatUpgradeUtils.hasUpgrade(this, RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL.get())) {
				this.switchNavigator(4);
			} else {
				this.switchNavigator(this.isInCage() ? 5 : this.isInTube() ? 2 : 0);
			}
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(TOGA, false);
		this.getEntityData().define(DANCING, false);
		this.getEntityData().define(DANCE_MOVES, 0);
		this.getEntityData().define(HELD_RF, 0);
		this.getEntityData().define(RESPAWN_COUNTDOWN, 0);
		this.getEntityData().define(VISUAL_FLAG, false);
		this.getEntityData().define(DYED, false);
		this.getEntityData().define(FLYING, false);
		this.getEntityData().define(DYE_COLOR, (byte) 0);
		this.getEntityData().define(DEPOSIT_POS, Optional.empty());
		this.getEntityData().define(PICKUP_POS, Optional.empty());
		this.getEntityData().define(IS_IN_WHEEL, false);
		this.getEntityData().define(SPECIAL_DYE, "rainbow");
		this.getEntityData().define(MOUNT_RESPAWN_COOLDOWN, 20);
	}

	public void switchNavigator(int type) {
		if (type == 0) { //tamed
			this.moveControl = new RatMoveControl(this);
			this.navigation = new RatNavigation(this, this.level());
			this.navigatorType = 0;
		} else if (type == 1) { //flying
			this.moveControl = new RatFlightMoveControl(this, 1.0F);
			this.navigation = new RatFlightNavigation(this, this.level());
			this.navigatorType = 1;
		} else if (type == 2) { //tube
			this.moveControl = new RatTubeMoveControl(this);
			this.navigation = new RatNavigation(this, this.level());
			this.navigatorType = 2;
		} else if (type == 3) { //aquatic
			this.moveControl = new SmoothSwimmingMoveControl(this, 360, 360, 10.0F, 1.0F, true);
			this.navigation = new AmphibiousPathNavigation(this, this.level());
			this.navigatorType = 3;
		} else if (type == 4) { //ethereal
			this.moveControl = new EtherealRatMoveControl(this);
			this.navigation = new EtherealRatNavigation(this, this.level());
			this.navigatorType = 4;
		} else if (type == 5) { //cage
			this.moveControl = new RatCageMoveControl(this);
			this.navigation = new RatNavigation(this, this.level());
			this.navigatorType = 5;
		}
	}

	@Override
	public boolean isHoldingFood() {
		return !this.getMainHandItem().isEmpty() && RatUpgradeUtils.forEachUpgradeBool(this, (stack) -> stack.isRatHoldingFood(this), RatUtils.isRatFood(this.getMainHandItem()));
	}

	@Override
	protected boolean isVisuallySitting() {
		return super.isVisuallySitting() || this.isDancing();
	}

	@Override
	public boolean isHoldingItemInHands() {
		return (this.isHoldingFood() || (!this.getMainHandItem().isEmpty() && this.cookingProgress > 0) || this.holdsItemInHandUpgrade() || this.getMBTransferRate() > 0) && this.sleepProgress <= 0.0F;
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		if (this.getVehicle() instanceof LivingEntity living && this.isRidingSpecialMount()) {
			return living.doHurtTarget(entity);
		}
		boolean flag = entity.hurt(this.damageSources().mobAttack(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
		if (flag) {
			this.doEnchantDamageEffects(this, entity);
			this.getMainHandItem().hurtAndBreak(1, this, rat -> rat.broadcastBreakEvent(EquipmentSlot.MAINHAND));
			RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof PostAttackUpgrade, stack -> ((PostAttackUpgrade) stack.getItem()).afterHit(this, (LivingEntity) entity));
		}
		return flag;
	}

	public boolean isInCage() {
		return this.level().getBlockState(this.blockPosition()).getBlock() instanceof RatCageBlock;
	}

	public boolean isInTube() {
		return this.level().getBlockState(this.blockPosition()).getBlock() instanceof RatTubeBlock;
	}

	@Override
	public boolean onClimbable() {
		return this.isInTube() ? this.climbingTube : super.onClimbable();
	}

	@Override
	public int getArmorValue() {
		return super.getArmorValue() * 3;
	}

	@Override
	public boolean removeWhenFarAway(double dist) {
		return false;
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_SCULKED.get()) ? MovementEmission.SOUNDS : MovementEmission.ALL;
	}

	@Override
	public boolean canDrownInFluidType(FluidType type) {
		return type == ForgeMod.WATER_TYPE.get() && (!RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get()) || !RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_UNDERWATER.get()));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("NavCooldown", this.updateNavigationCooldown);
		tag.putInt("CookingProgress", this.cookingProgress);
		tag.putInt("BreedCooldown", this.breedCooldown);
		tag.putInt("CoinCooldown", this.coinCooldown);
		tag.putInt("PickpocketCooldown", this.pickpocketCooldown);
		tag.putInt("MountCooldown", this.getMountCooldown());
		tag.putInt("TransportingRF", this.getHeldRF());
		tag.putInt("RespawnCountdown", this.getRespawnCountdown());
		tag.putInt("Command", this.getCommandInteger());
		tag.putBoolean("VisualFlag", this.getVisualFlag());
		tag.putBoolean("Dancing", this.isDancing());
		tag.putBoolean("Toga", this.hasToga());
		tag.putBoolean("Dyed", this.isDyed());
		tag.putByte("DyeColor", (byte) this.getDyeColor());
		tag.putString("SpecialDye", this.getSpecialDye());
		this.getDepositPos().flatMap(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error)).ifPresent(tag1 -> tag.put("DepositPos", tag1));
		this.getPickupPos().flatMap(pos -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, pos).resultOrPartial(RatsMod.LOGGER::error)).ifPresent(tag1 -> tag.put("PickupPos", tag1));
		tag.putInt("RandomEffectCooldown", this.randomEffectCooldown);
		if (this.transportingFluid != null) {
			CompoundTag fluidTag = new CompoundTag();
			this.transportingFluid.writeToNBT(fluidTag);
			tag.put("TransportingFluid", fluidTag);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.updateNavigationCooldown = tag.getInt("NavCooldown");
		this.cookingProgress = tag.getInt("CookingProgress");
		this.breedCooldown = tag.getInt("BreedCooldown");
		this.coinCooldown = tag.getInt("CoinCooldown");
		this.pickpocketCooldown = tag.getInt("PickpocketCooldown");
		this.setMountCooldown(tag.getInt("MountCooldown"));
		this.randomEffectCooldown = tag.getInt("RandomEffectCooldown");
		this.setHeldRF(tag.getInt("TransportingRF"));
		this.setRespawnCountdown(tag.getInt("RespawnCountdown"));
		this.setCommandInteger(tag.getInt("Command"));
		this.setDancing(tag.getBoolean("Dancing"));
		this.setVisualFlag(tag.getBoolean("VisualFlag"));
		this.setToga(tag.getBoolean("Toga"));
		this.setDyed(tag.getBoolean("Dyed"));
		this.setDyeColor((tag.getByte("DyeColor")));
		this.setSpecialDye(tag.getString("SpecialDye"));
		if (tag.contains("DepositPos")) {
			this.setDepositPos(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("DepositPos")).resultOrPartial(RatsMod.LOGGER::error).orElse(null));
		}
		if (tag.contains("PickupPos")) {
			this.setPickupPos(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("PickupPos")).resultOrPartial(RatsMod.LOGGER::error).orElse(null));
		}
		if (tag.contains("DepositFacing")) {
			this.depositFacing = Direction.values()[tag.getInt("DepositFacing")];
		}
		if (tag.contains("TransportingFluid")) {
			CompoundTag fluidTag = tag.getCompound("TransportingFluid");
			if (!fluidTag.isEmpty()) {
				this.transportingFluid = FluidStack.loadFluidStackFromNBT(fluidTag);
			}
		}
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isBaby() && this.getCommand() != RatCommand.SIT) {
			this.setCommand(RatCommand.SIT);
		}
		if (this.breedCooldown > 0) {
			this.breedCooldown--;
		}
		if (this.isOrderedToSit() && this.getTarget() != null) {
			this.setTarget(null);
		}

		if (this.getTarget() != null && this.getMountCooldown() > 0) {
			this.setTarget(null);
		}

		if (this.getRespawnCountdown() > 0) {
			if (this.level().isClientSide() && this.tickCount % 5 == 0) {
				double d0 = this.position().x();
				double d1 = this.position().y() + 0.25D;
				double d2 = this.position().z();
				double d3 = ((double) this.getRandom().nextFloat() - 0.5D) * 0.15D;
				double d4 = ((double) this.getRandom().nextFloat() - 0.5D) * 0.15D;
				double d5 = ((double) this.getRandom().nextFloat() - 0.5D) * 0.15D;
				this.level().addParticle(ParticleTypes.END_ROD, d0, d1, d2, d3, d4, d5);
			}
			this.setRespawnCountdown(this.getRespawnCountdown() - 1);
		}
		this.setNoAi(this.getRespawnCountdown() > 0);

		if (!this.level().isClientSide() && this.getMountEntityType() != null && !this.isPassenger() && this.getMountCooldown() == 0) {
			Entity entity = this.getMountEntityType().create(this.level());
			entity.copyPosition(this);
			if (entity instanceof Mob mob && this.level() instanceof ServerLevelAccessor accessor) {
				ForgeEventFactory.onFinalizeSpawn(mob, accessor, this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
			}
			this.level().addFreshEntity(entity);

			this.level().broadcastEntityEvent(this, (byte) 127);
			this.startRiding(entity, true);
		}

		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof TickRatUpgrade, stack -> ((TickRatUpgrade) stack.getItem()).tick(this));

		if (RatConfig.upgradeRegenRate > 0) {
			RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof StatBoostingUpgrade, stack -> {
				if (((StatBoostingUpgrade) stack.getItem()).regeneratesHealth() && this.getHealth() < this.getMaxHealth() && this.tickCount % RatConfig.upgradeRegenRate == 0) {
					this.heal(1.0F);
				}
			});
		}

		if (this.updateNavigationCooldown-- == 0) {
			this.updateNavigationCooldown = 60;
			int savedNav = this.navigatorType;
			if (this.isInCage()) {
				this.switchNavigator(5);
			} else if (this.isInTube()) {
				this.switchNavigator(2);
			} else if (this.hasFlightUpgrade()) {
				this.switchNavigator(1);
			} else {
				this.switchNavigator(savedNav);
			}
		}

		this.setNoGravity(this.isFlying());
		if (this.isFlying()) {
			if (this.isOrderedToSit() || this.verticalCollisionBelow || this.onGround()) {
				this.setFlying(false);
			}
			if (Math.abs(this.getDeltaMovement().x()) < 0.01D && Math.abs(this.getDeltaMovement().z()) < 0.01D) {
				if (Math.abs(this.getDeltaMovement().y()) > 0.0D) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.1D, 1.0D));
				}
			}
		}

		if (this.isInWheel() && !this.level().getBlockState(this.blockPosition()).is(RatsBlockRegistry.RAT_CAGE_WHEEL.get())) {
			this.setInWheel(false);
		}

		if (this.getMountCooldown() > 0) {
			this.setMountCooldown(this.getMountCooldown() - 1);
		}
		if (this.rangedAttackCooldown > 0) {
			this.rangedAttackCooldown--;
		}
		if (this.visualCooldown > 0) {
			this.visualCooldown--;
		}
		if (this.poopCooldown > 0) {
			this.poopCooldown--;
		}
		if (this.isDancing() && this.getAnimation() != this.getDanceAnimation()) {
			this.setAnimation(this.getDanceAnimation());
		}
		if (this.isDancing() && (this.jukeboxPos == null || this.jukeboxPos.distToCenterSqr(this.getX(), this.getY(), this.getZ()) > 256.0D || !this.level().getBlockState(this.jukeboxPos).is(Blocks.JUKEBOX))) {
			this.setDancing(false);
		}
		if (!this.level().isClientSide() && this.level().getBlockState(this.blockPosition()).is(RatsBlockRegistry.RAT_QUARRY_PLATFORM.get()) && this.level().isEmptyBlock(this.blockPosition().above())) {
			this.setPos(this.getX(), this.getY() + 1, this.getZ());
			this.getNavigation().stop();
		}

		if (this.jumping && !this.level().isClientSide() && this.level().getBlockState(this.blockPosition().above()).is(RatsBlockRegistry.RAT_QUARRY_PLATFORM.get()) && this.level().isEmptyBlock(this.blockPosition().above(2))) {
			this.setPos(this.getX(), this.getY() + 1, this.getZ());
			this.getNavigation().stop();
		}
	}

	@Override
	public boolean fireImmune() {
		return this.getRespawnCountdown() > 0 || super.fireImmune();
	}

	@Override
	public boolean isPushedByFluid(FluidType type) {
		return !RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_QUARRY.get()) && !RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get()) && super.isPushedByFluid(type);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isBaby() || this.isInvulnerableTo(source) || (source.is(DamageTypes.IN_WALL) && this.isPassenger())) {
			return false;
		} else {
			Entity entity = source.getEntity();

			if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
				amount = (amount + 1.0F) / 2.0F;
			}

			boolean flag = super.hurt(source, amount);

			if (flag && this.getVehicle() != null && this.isRidingSpecialMount()) {
				this.getVehicle().hurt(source, amount);
				this.invulnerableTime = 20;
				return false;
			}

			return flag;
		}
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(ForgeSpawnEggItem.fromEntityType(RatsEntityRegistry.RAT.get()));
	}

	public void setFlying(boolean flying) {
		this.getEntityData().set(FLYING, flying);
	}

	public boolean isFlying() {
		return this.getEntityData().get(FLYING);
	}

	public void setToga(boolean toga) {
		this.getEntityData().set(TOGA, toga);
	}

	public boolean hasToga() {
		return this.getEntityData().get(TOGA);
	}

	public boolean getVisualFlag() {
		return this.getEntityData().get(VISUAL_FLAG);
	}

	public void setVisualFlag(boolean flag) {
		this.getEntityData().set(VISUAL_FLAG, flag);
	}

	public boolean isDancing() {
		return this.getEntityData().get(DANCING);
	}

	public void setDancing(boolean dancing) {
		this.getEntityData().set(DANCING, dancing);
	}

	public int getDanceMoves() {
		return this.getEntityData().get(DANCE_MOVES);
	}

	public void setDanceMoves(int moves) {
		this.getEntityData().set(DANCE_MOVES, moves);
	}

	public int getHeldRF() {
		return this.getEntityData().get(HELD_RF);
	}

	public void setHeldRF(int rf) {
		this.getEntityData().set(HELD_RF, rf);
	}

	public int getRespawnCountdown() {
		return this.getEntityData().get(RESPAWN_COUNTDOWN);
	}

	public void setRespawnCountdown(int respawn) {
		this.getEntityData().set(RESPAWN_COUNTDOWN, respawn);
	}

	public boolean isInWheel() {
		return this.getEntityData().get(IS_IN_WHEEL);
	}

	public void setInWheel(boolean wheel) {
		this.getEntityData().set(IS_IN_WHEEL, wheel);
	}

	public boolean isDyed() {
		return this.getEntityData().get(DYED);
	}

	public void setDyed(boolean dyed) {
		this.getEntityData().set(DYED, dyed);
	}

	public int getDyeColor() {
		return this.getEntityData().get(DYE_COLOR);
	}

	public void setDyeColor(int color) {
		this.getEntityData().set(DYE_COLOR, (byte) (color));
	}

	public String getSpecialDye() {
		return this.getEntityData().get(SPECIAL_DYE);
	}

	public void setSpecialDye(String keyword) {
		this.getEntityData().set(SPECIAL_DYE, keyword);
	}


	public Optional<GlobalPos> getPickupPos() {
		return this.getEntityData().get(PICKUP_POS);
	}

	public void setPickupPos(@Nullable GlobalPos pos) {
		this.getEntityData().set(PICKUP_POS, Optional.ofNullable(pos));
	}

	public Optional<GlobalPos> getDepositPos() {
		return this.getEntityData().get(DEPOSIT_POS);
	}

	public void setDepositPos(@Nullable GlobalPos pos) {
		this.getEntityData().set(DEPOSIT_POS, Optional.ofNullable(pos));
	}

	public int getMountCooldown() {
		return this.getEntityData().get(MOUNT_RESPAWN_COOLDOWN);
	}

	public void setMountCooldown(int cooldown) {
		this.getEntityData().set(MOUNT_RESPAWN_COOLDOWN, cooldown);
	}

	@Override
	public boolean isEating() {
		return super.isEating() && this.getCommand().allowsEating;
	}

	@Override
	public void onItemEaten() {
		ItemStack handCopy = this.getMainHandItem().copy();
		this.getMainHandItem().shrink(1);
		if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_ORE_DOUBLING.get()) && OreDoublingRatUpgradeItem.isProcessable(handCopy)) {
			ItemStack nugget = OreRatNuggetItem.saveResourceToNugget(this.level(), handCopy, true).copyWithCount(2);
			if (RatConfig.ratFartNoises) {
				this.playSound(RatsSoundRegistry.RAT_POOP.get(), 0.5F + this.getRandom().nextFloat() * 0.5F, 1.0F + this.getRandom().nextFloat() * 0.5F);
			}
			if (!this.level().isClientSide()) {
				this.spawnAtLocation(nugget, 0.0F);
			}
		} else if (this.getRandom().nextFloat() <= 0.05F) {
			ItemStack nugget = new ItemStack(RatsItemRegistry.RAT_NUGGET.get());
			if (RatConfig.ratFartNoises) {
				this.playSound(RatsSoundRegistry.RAT_POOP.get(), 0.5F + this.getRandom().nextFloat() * 0.5F, 1.0F + this.getRandom().nextFloat() * 0.5F);
			}
			if (!this.level().isClientSide()) {
				this.spawnAtLocation(nugget, 0.0F);
			}
		}
	}

	public void createBabiesFrom(TamedRat mother, TamedRat father) {
		TamedRat baby = new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), this.level());
		baby.setMale(this.getRandom().nextBoolean());
		RatVariant babyColor;
		if ((father.getColorVariant().isBreedingExclusive() || mother.getColorVariant().isBreedingExclusive()) && this.getRandom().nextInt(6) == 0) {
			babyColor = RatVariant.getRandomBreedingExclusiveVariant(this.getRandom());
		} else {
			if (this.getRandom().nextInt(10) == 0) {
				babyColor = RatVariant.getRandomVariant(this.getRandom(), true);
			} else {
				babyColor = this.getRandom().nextBoolean() ? father.getColorVariant() : mother.getColorVariant();
			}
		}
		baby.setColorVariant(babyColor);
		baby.setPos(mother.getX() - 0.5F + mother.getRandom().nextFloat(), mother.getY(), mother.getZ() - 0.5F + mother.getRandom().nextFloat());
		baby.setAge(-24000);
		baby.setCommand(RatCommand.SIT);
		if (mother.isTame()) {
			baby.setTame(true);
			baby.setOwnerUUID(mother.getOwnerUUID());
		} else if (father.isTame()) {
			baby.setTame(true);
			baby.setOwnerUUID(father.getOwnerUUID());
		}
		this.level().addFreshEntity(baby);
	}

	@Override
	public boolean isPickable() {
		return !(this.getVehicle() instanceof Player);
	}

	public ItemStack getResultForRecipe(RecipeType<? extends SingleItemRecipe> recipe, ItemStack stack) {
		Optional<? extends SingleItemRecipe> optional = this.level().getRecipeManager().getRecipeFor(recipe, new SimpleContainer(stack), this.level());
		if (optional.isPresent()) {
			ItemStack itemstack = optional.get().getResultItem(this.level().registryAccess());
			if (!itemstack.isEmpty()) {
				ItemStack itemstack1 = itemstack.copy();
				itemstack1.setCount(stack.getCount() * itemstack.getCount());
				return itemstack1;
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean tryDepositItemInContainers(ItemStack burntItem) {
		if (level().getBlockEntity(this.blockPosition()) != null) {
			BlockEntity te = level().getBlockEntity(this.blockPosition());
			if (te != null) {
				LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
				if (handler.resolve().isPresent()) {
					if (ItemHandlerHelper.insertItem(handler.resolve().get(), burntItem, true).isEmpty()) {
						ItemHandlerHelper.insertItem(handler.resolve().get(), burntItem, false);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	protected void doPush(Entity entity) {
		if (!this.crafting) {
			entity.push(this);
		}
	}

	public Animation getDanceAnimation() {
		if (this.getDanceMoves() == 0) {
			return ANIMATION_DANCE;
		}
		return NO_ANIMATION;
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean playerKill) {
		if (this.hasToga()) {
			this.spawnAtLocation(new ItemStack(RatlantisItemRegistry.RAT_TOGA.get()), 0.0F);
		}
		super.dropCustomDeathLoot(source, looting, playerKill);
	}

	public void spawnAngelCopy() {
		TamedRat copy = RatsEntityRegistry.TAMED_RAT.get().create(this.level());
		CompoundTag tag = new CompoundTag();
		this.addAdditionalSaveData(tag);
		tag.putShort("HurtTime", (short) 0);
		tag.putInt("HurtByTimestamp", 0);
		tag.putShort("DeathTime", (short) 0);
		copy.readAdditionalSaveData(tag);
		copy.setHealth(copy.getMaxHealth());
		copy.copyPosition(this);
		copy.setRespawnCountdown(1200);
		if (copy.isVisuallySitting()) {
			copy.sitProgress = 20.0F;
		}
		if (this.hasCustomName()) {
			copy.setCustomName(this.getCustomName());
		}
		copy.clearFire();
		this.level().addFreshEntity(copy);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance instance) {
		if (instance.getEffect() == MobEffects.POISON && (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_POISON.get()) || RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_DAMAGE_PROTECTION.get()))) {
			return false;
		}
		return super.canBeAffected(instance);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (this.getRespawnCountdown() > 0 || itemstack.is(ForgeRegistries.ITEMS.getValue(new ResourceLocation(RatsMod.MODID, "rat_spawn_egg")))) {
			return InteractionResult.PASS;
		}
		if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_CARRAT.get())) {
			if (player.getFoodData().needsFood()) {
				player.getFoodData().eat(1, 0.1F);
				player.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
				for (int i = 0; i < 8; i++) {
					double d0 = this.getRandom().nextGaussian() * 0.02D;
					double d1 = this.getRandom().nextGaussian() * 0.02D;
					double d2 = this.getRandom().nextGaussian() * 0.02D;
					this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.CARROT)), this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight() * 2.0F) - (double) this.getBbHeight(), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
				}
				return InteractionResult.SUCCESS;
			}
		}
		if (!this.isBaby() && this.isOwnedBy(player)) {
			if (player.isSecondaryUseActive() && !this.isPassenger()) {
				if (player.getPassengers().size() < 3) {
					this.startRiding(player, true);
					player.displayClientMessage(Component.translatable("entity.rats.rat.dismount_instructions"), true);
				}
				return InteractionResult.sidedSuccess(this.level().isClientSide());
			}
			if (itemstack.is(RatsItemRegistry.RAT_PAPERS.get())) {
				InteractionResult result = itemstack.interactLivingEntity(player, this, hand);
				if (result.consumesAction()) {
					return result;
				}
			} else if (itemstack.is(RatlantisItemRegistry.RAT_TOGA.get())) {
				if (!this.hasToga()) {
					if (!player.isCreative()) {
						itemstack.shrink(1);
					}
				} else {
					if (!this.level().isClientSide()) {
						this.spawnAtLocation(new ItemStack(RatlantisItemRegistry.RAT_TOGA.get()), 0.0F);
					}
				}
				this.setToga(!this.hasToga());
				this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1F, 1.5F);
			} else if (itemstack.is(RatsBlockRegistry.DYE_SPONGE.get().asItem()) && this.isDyed()) {
				this.setDyed(false);
				this.setDyeColor(0);
				this.setSpecialDye("");
				for (int i = 0; i < 8; i++) {
					double d0 = this.getRandom().nextGaussian() * 0.02D;
					double d1 = this.getRandom().nextGaussian() * 0.02D;
					double d2 = this.getRandom().nextGaussian() * 0.02D;
					this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RatsBlockRegistry.DYE_SPONGE.get())), this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight() * 2.0F) - (double) this.getBbHeight(), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
				}
				this.playSound(RatsSoundRegistry.DYE_SPONGE_USED.get(), this.getSoundVolume(), this.getVoicePitch());
				return InteractionResult.SUCCESS;
			} else if (itemstack.is(RatsItemRegistry.RATBOW_ESSENCE.get()) && this.applySpecialDyeIfPossible(itemstack)) {
				return InteractionResult.SUCCESS;
			} else if (this.applyNormalDyeIfPossible(itemstack)) {
				return InteractionResult.SUCCESS;
			} else if (itemstack.is(RatsItemRegistry.RAT_SACK.get())) {
				if (RatSackItem.getRatsInSack(itemstack) >= RatConfig.ratSackCapacity) {
					player.displayClientMessage(Component.translatable("item.rats.rat_sack.too_full").withStyle(ChatFormatting.RED), true);
					return InteractionResult.PASS;
				} else {
					RatSackItem.packRatIntoSack(itemstack, this, RatSackItem.getRatsInSack(itemstack) + 1);
					this.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1, 1);
					this.discard();
					player.swing(hand);
					return InteractionResult.SUCCESS;
				}
			} else if (itemstack.getItem() instanceof RatStaffItem) {
				player.getCapability(RatsCapabilityRegistry.SELECTED_RAT).ifPresent(cap -> cap.setSelectedRat(this));
				player.swing(hand);
				if (!this.level().isClientSide() && player instanceof ServerPlayer sp) {
					RatsNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new ManageRatStaffPacket(this.getId(), BlockPos.ZERO, Direction.NORTH.ordinal(), false, false));
				}
				player.displayClientMessage(Component.translatable("entity.rats.rat.staff.bind", this.getName()), true);
				return InteractionResult.SUCCESS;
			} else if (itemstack.is(Items.ARROW)) {
				itemstack.shrink(1);
				ItemStack arrow = new ItemStack(RatsItemRegistry.RAT_ARROW.get());
				CompoundTag tag = new CompoundTag();
				CompoundTag ratTag = new CompoundTag();
				this.addAdditionalSaveData(ratTag);
				if (this.hasCustomName()) {
					ratTag.putString("CustomName", Component.Serializer.toJson(this.getCustomName()));
				}
				tag.put("Rat", ratTag);
				arrow.setTag(tag);
				if (itemstack.isEmpty()) {
					player.setItemInHand(hand, arrow);
				} else if (!player.getInventory().add(arrow)) {
					player.drop(arrow, false);
				}
				this.playSound(RatsSoundRegistry.RAT_HURT.get(), 1, 1);
				player.swing(hand);
				this.discard();
				return InteractionResult.SUCCESS;
			} else {
				return super.mobInteract(player, hand);
			}
		}
		return InteractionResult.PASS;
	}

	public boolean applyNormalDyeIfPossible(ItemStack stack) {
		if (stack.getItem() instanceof DyeItem item && (!this.isDyed() || this.getDyeColor() != item.getDyeColor().getId())) {
			if (!this.isDyed()) {
				this.setDyed(true);
			}
			this.setDyeColor(item.getDyeColor().getId());
			for (int i = 0; i < 8; i++) {
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight() * 2.0F) - (double) this.getBbHeight(), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
			}
			this.playSound(RatsSoundRegistry.ESSENCE_APPLIED.get(), this.getSoundVolume(), this.getVoicePitch());
			stack.shrink(1);
			return true;
		}
		return false;
	}

	public boolean applySpecialDyeIfPossible(ItemStack stack) {
		String name = stack.getHoverName().getString();
		if (!this.getSpecialDye().equals(name) || !this.isDyed()) {
			if (!this.isDyed()) {
				this.setDyed(true);
			}
			this.setDyeColor(100);
			this.setSpecialDye(name);
			for (int i = 0; i < 8; i++) {
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RatsItemRegistry.RATBOW_ESSENCE.get())), this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight() * 2.0F) - (double) this.getBbHeight(), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d0, d1, d2);
			}
			this.playSound(RatsSoundRegistry.ESSENCE_APPLIED.get(), this.getSoundVolume(), this.getVoicePitch());
			stack.shrink(1);
			return true;
		}
		return false;
	}

	@Override
	public void setTame(boolean tamed) {
		if (tamed) {
			Arrays.fill(this.armorDropChances, 1.1F);
			Arrays.fill(this.handDropChances, 1.1F);
		}
		super.setTame(tamed);
	}

	@Override
	public int getAmbientSoundInterval() {
		return RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get()) ? 1000 : 200;
	}

	@Override
	public boolean causeFallDamage(float dist, float mult, DamageSource source) {
		if (!this.isInvulnerableTo(source) && !this.isPassenger()) {
			return super.causeFallDamage(dist, mult, source);
		}
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
		if (!this.hasFlightUpgrade()) {
			super.checkFallDamage(y, onGround, state, pos);
		}
	}

	@Override
	public void rideTick() {
		super.rideTick();
		if (this.getVehicle() instanceof Player player) {
			this.updateRiding(player);
		}
	}

	public void updateRiding(Player riding) {
		int i = riding.getPassengers().indexOf(this);
		float radius = (i == 0 ? 0F : 0.4F) + (riding.isFallFlying() ? 1 : 0);
		float angle = (0.01745329251F * riding.yBodyRot + (i == 2 ? -92.5F : i == 1 ? 92.5F : 0));
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		double extraY = (riding.isCrouching() ? 1.2D : riding.isFallFlying() ? 0.25D : 1.4D);
		this.setYRot(riding.yHeadRot);
		this.yHeadRot = riding.yHeadRot;
		this.yRotO = riding.yHeadRot;
		this.setPos(riding.getX() + extraX, riding.getY() + extraY, riding.getZ() + extraZ);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 85) {
			this.crafting = true;
		} else if (id == 86) {
			this.crafting = false;
		} else if (id == 127) {
			for (int k = 0; k < 20; ++k) {
				double d2 = this.getRandom().nextGaussian() * 0.02D;
				double d0 = this.getRandom().nextGaussian() * 0.02D;
				double d1 = this.getRandom().nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.POOF, this.getX() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.getRandom().nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.getRandom().nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	protected SoundEvent getAmbientSound() {
		if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_CHRISTMAS.get())) {
			return RatsSoundRegistry.RAT_SANTA.get();
		}
		if (RatsMod.ICEANDFIRE_LOADED && RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_DRAGON.get())) {
			SoundEvent possibleDragonSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("iceandfire", "firedragon_child_idle"));
			if (possibleDragonSound != null) {
				return possibleDragonSound;
			}
		}
		return super.getAmbientSound();
	}

	protected SoundEvent getDeathSound() {
		if (RatsMod.ICEANDFIRE_LOADED && RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_DRAGON.get())) {
			SoundEvent possibleDragonSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("iceandfire", "firedragon_child_death"));
			if (possibleDragonSound != null) {
				return possibleDragonSound;
			}
		}
		return super.getDeathSound();
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		if (RatsMod.ICEANDFIRE_LOADED && RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_DRAGON.get())) {
			SoundEvent possibleDragonSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("iceandfire", "firedragon_child_hurt"));
			if (possibleDragonSound != null) {
				return possibleDragonSound;
			}
		}
		return super.getHurtSound(source);
	}

	public boolean onHearFlute(Player player, RatCommand ratCommand) {
		if (this.isOwnedBy(player) && !this.isBaby() && !RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_NO_FLUTE.get())) {
			this.setCommand(ratCommand);
			return true;
		}
		return false;
	}

	public boolean canRatPickupItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if ((RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get()) || RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_WHITELIST.get()))) {
			CompoundTag tag;
			if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get())) {
				tag = RatUpgradeUtils.getUpgrade(this, RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get()).getTag();
			} else {
				tag = RatUpgradeUtils.getUpgrade(this, RatsItemRegistry.RAT_UPGRADE_WHITELIST.get()).getTag();
			}
			String ourItemID = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem())).toString();
			if (tag != null && tag.contains("Items", 9)) {
				ListTag list = tag.getList("Items", 10);
				if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_BLACKLIST.get())) {
					for (int i = 0; i < list.size(); ++i) {
						String itemID = list.getCompound(i).getString("id");
						if (ourItemID.equals(itemID)) {
							return false;
						}
					}
					return true;
				} else {
					//whitelist
					for (int i = 0; i < list.size(); ++i) {
						String itemID = list.getCompound(i).getString("id");
						if (ourItemID.equals(itemID)) {
							return true;
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public MobType getMobType() {
		if (this.getInventory() != null) {
			if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_UNDEAD.get())) {
				return MobType.UNDEAD;
			}
			if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_AQUATIC.get())) {
				return MobType.WATER;
			}
		}
		return super.getMobType();
	}

	@Override
	public boolean isPushable() {
		return !this.isBaby() && super.isPushable();
	}

	@Override
	public boolean isCurrentlyGlowing() {
		if (this.level().isClientSide() && ForgeClientEvents.isRatSelectedOnStaff(this)) return true;
		return super.isCurrentlyGlowing();
	}

	public void attemptTeleport(double x, double y, double z) {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		this.setPos(x, y, z);
		this.level().broadcastEntityEvent(this, (byte) 84);
		boolean flag = false;
		BlockPos blockpos = this.blockPosition();

		if (this.level().isLoaded(blockpos)) {
			boolean flag1 = false;

			while (!flag1 && blockpos.getY() > 0) {
				BlockPos blockpos1 = blockpos.below();
				BlockState state = this.level().getBlockState(blockpos1);

				if (state.blocksMotion()) {
					flag1 = true;
				} else {
					this.setPos(this.getX(), this.getY() - 1, this.getZ());
					blockpos = blockpos1;
				}
			}

			if (flag1) {
				this.teleportTo(this.getX(), this.getY(), this.getZ());

				if (this.level().noCollision(this) && !this.level().containsAnyLiquid(this.getBoundingBox())) {
					flag = true;
				}
			}
		}

		if (!flag) {
			this.teleportTo(d0, d1, d2);
		} else {
			this.playSound(RatsSoundRegistry.RAT_TELEPORT.get(), 1, 1);
		}
	}

	public boolean isDirectPathBetweenPoints(Vec3 target) {
		BlockHitResult result = this.level().clip(new ClipContext(this.position(), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
		BlockPos pos = result.getBlockPos();
		BlockPos sidePos = result.getBlockPos().relative(result.getDirection());
		if (!this.level().isEmptyBlock(pos) || !this.level().isEmptyBlock(sidePos)) {
			return true;
		} else {
			return result.getType() == HitResult.Type.MISS;
		}
	}

	@Override
	public boolean canBeSeenAsEnemy() {
		return !RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_UNDEAD.get()) && super.canBeSeenAsEnemy();
	}

	@Override
	public boolean isNoAi() {
		return super.isNoAi() || this.getRespawnCountdown() > 0;
	}

	public boolean holdsItemInHandUpgrade() {
		boolean bool = RatUpgradeUtils.forEachUpgradeBool(this, stack -> stack instanceof HoldsItemUpgrade upgrade && !upgrade.isFakeHandRender(), false);

		return !this.isInWheel() && bool;
	}

	@Override
	public boolean shouldPlayIdleAnimations() {
		boolean bool = RatUpgradeUtils.forEachUpgradeBool(this, (stack) -> stack.playIdleAnimation(this), true);
		return super.shouldPlayIdleAnimations() && !this.isInTube() && !this.isInWheel() && this.cookingProgress <= 0 && bool;
	}

	public boolean hasAnyUpgrades() {
		for (EquipmentSlot slot : RatUpgradeUtils.UPGRADE_SLOTS) {
			if (!this.getItemBySlot(slot).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canDigThroughBlocks() {
		return false;
	}

	public void onUpgradeChanged() {
		this.setupDynamicAI();

		Entity vehicle = this.getVehicle();
		if (!this.level().isClientSide() && vehicle instanceof RatMount mount) {
			if (!RatUpgradeUtils.hasUpgrade(this, mount.getUpgradeItem())) {
				this.level().broadcastEntityEvent(this, (byte) 127);
				this.stopRiding();
				vehicle.discard();
			}
		}

		AttributeSupplier defaults = TamedRat.createAttributes().build();
		ForgeRegistries.ATTRIBUTES.forEach(attribute -> {
			if (this.getAttribute(attribute) != null && this.getAttributes().hasAttribute(attribute) && !Objects.requireNonNull(this.getAttribute(attribute)).getModifiers().isEmpty()) {
				if (defaults.hasAttribute(attribute)) {
					this.getAttribute(attribute).setBaseValue(defaults.getBaseValue(attribute));
				}
				Objects.requireNonNull(this.getAttribute(attribute)).removeModifiers();
			}
		});

		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof StatBoostingUpgrade, stack ->
				((StatBoostingUpgrade) stack.getItem()).getAttributeBoosts().forEach((attribute, aDouble) -> this.tryIncreaseStat(stack.getHoverName().getString(), attribute, aDouble)));

		if (this.getHeldRF() > this.getRFTransferRate()) {
			this.setHeldRF(this.getRFTransferRate());
		}
		this.heal(this.getMaxHealth());
		this.setFlying(this.isFlying() && this.hasFlightUpgrade());
		this.setNoGravity(RatUpgradeUtils.hasUpgrade(this, RatlantisItemRegistry.RAT_UPGRADE_ETHEREAL.get()) || this.isFlying());
	}

	public void tryIncreaseStat(String itemName, Attribute stat, double value) {
		Objects.requireNonNull(this.getAttribute(stat)).addPermanentModifier(new AttributeModifier(itemName + " " + Component.translatable(stat.getDescriptionId()).getString() + " Modifier", value, AttributeModifier.Operation.ADDITION));
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if (this.getRespawnCountdown() > 0) {
			return true;
		}
		AtomicBoolean upgradePrevented = new AtomicBoolean(false);
		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof DamageImmunityUpgrade, stack -> {
			if (((DamageImmunityUpgrade) stack.getItem()).isImmuneToDamageSource(this, source)) {
				upgradePrevented.set(true);
			}
		});

		if (RatUpgradeUtils.hasUpgrade(this, RatsItemRegistry.RAT_UPGRADE_CREATIVE.get())) {
			return source.getEntity() == null || source.getEntity() instanceof LivingEntity living && !this.isOwnedBy(living);
		}
		return upgradePrevented.get() || super.isInvulnerableTo(source);
	}

	@Override
	public boolean isInvulnerable() {
		return super.isInvulnerable() || this.getRespawnCountdown() > 0;
	}

	@Override
	public void setRecordPlayingNearby(BlockPos pos, boolean partying) {
		int moves = this.getRandom().nextInt(4);
		if (!this.isDancing() && partying) {
			this.setDanceMoves(moves);
		}
		this.setDancing(partying);
		this.jukeboxPos = pos;
		if (this.level().isClientSide()) {
			RatsNetworkHandler.CHANNEL.sendToServer(new SetDancingRatPacket(this.getId(), partying, pos.asLong(), moves));
		}
	}

	public boolean shouldDepositItem(ItemStack item) {
		return RatUpgradeUtils.forEachUpgradeBool(this, (stack) -> stack.shouldDepositItem(this, item), true);
	}

	public boolean shouldCollectItem(ItemStack item) {
		return RatUpgradeUtils.forEachUpgradeBool(this, (stack) -> stack.shouldCollectItem(this, item), true);
	}

	public int getRFTransferRate() {
		AtomicInteger energy = new AtomicInteger();
		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof EnergyRatUpgradeItem, stack ->
				energy.set(((EnergyRatUpgradeItem) stack.getItem()).getRFTransferRate()));

		return energy.get();
	}

	public int getMBTransferRate() {
		AtomicInteger fluid = new AtomicInteger();
		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof BucketRatUpgradeItem, stack ->
				fluid.set(((BucketRatUpgradeItem) stack.getItem()).getMbTransferRate()));

		return fluid.get();
	}

	@Override
	public boolean isAlliedTo(Entity entity) {
		LivingEntity livingentity = this.getOwner();
		if (entity == livingentity) {
			return true;
		} else if (entity instanceof RatMountBase mount) {
			return mount.getRat() != null && this.isAlliedTo(mount.getRat());
		} else if (livingentity != null && entity instanceof TamableAnimal animal) {
			return animal.isOwnedBy(livingentity);
		} else {
			return livingentity != null ? livingentity.isAlliedTo(entity) : super.isAlliedTo(entity);
		}
	}

	@Nullable
	private EntityType<?> getMountEntityType() {
		AtomicReference<EntityType<?>> type = new AtomicReference<>(null);
		RatUpgradeUtils.forEachUpgrade(this, item -> item instanceof MountRatUpgradeItem, stack -> type.set(((MountRatUpgradeItem<?>) stack.getItem()).getEntityType()));
		return type.get();
	}

	public double getRatDistanceCenterSq(double x, double y, double z) {
		double d0 = this.getX() - x - 0.5D;
		double d1 = this.getY() - y - 0.5D;
		double d2 = this.getZ() - z - 0.5D;
		if (this.getVehicle() != null && getMountEntityType() != null && this.getVehicle().getType() == getMountEntityType()) {
			d0 = this.getVehicle().getX() - x - 0.5D;
			d1 = this.getVehicle().getY() - y - 0.5D;
			d2 = this.getVehicle().getZ() - z - 0.5D;
		}
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public double getRatDistanceSq(double x, double y, double z) {
		double d0 = this.getX() - x;
		double d1 = this.getY() - y;
		double d2 = this.getZ() - z;
		if (this.getVehicle() != null && this.getMountEntityType() != null && this.getVehicle().getType() == this.getMountEntityType()) {
			d0 = this.getVehicle().getX() - x;
			d1 = this.getVehicle().getY() - y;
			d2 = this.getVehicle().getZ() - z;
		}
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	@Override
	public double distanceToSqr(Vec3 vec) {
		double d0 = this.getX() - vec.x();
		double d1 = this.getY() - vec.y();
		double d2 = this.getZ() - vec.z();
		if (this.getVehicle() != null && this.getMountEntityType() != null && this.getVehicle().getType() == this.getMountEntityType()) {
			d0 = this.getVehicle().getX() - vec.x();
			d1 = this.getVehicle().getY() - vec.y();
			d2 = this.getVehicle().getZ() - vec.z();
		}
		return d0 * d0 + d1 * d1 + d2 * d2;
	}


	public boolean isRidingSpecialMount() {
		boolean ret = false;
		if (this.getVehicle() != null && this.getMountEntityType() != null) {
			ret = this.getVehicle().getType().equals(this.getMountEntityType());
		}
		return ret;
	}

	@Override
	public boolean canAttack(LivingEntity target) {
		return !this.isRidingSpecialMount() || !RatUtils.isRidingOrBeingRiddenBy(this, target);
	}

	public double getRatHarvestDistance(double expansion) {
		return (3.5F + expansion) * this.getRatDistanceModifier();
	}

	public double getRatDistanceModifier() {
		if (this.isRidingSpecialMount()) {
			Entity entity = this.getVehicle();
			if (entity != null) {
				if (entity instanceof RatBiplaneMount) {
					return 3.95D;
				}
				return 1.5D;
			}

		}
		return 1.0D;
	}

	public boolean hasFlightUpgrade() {
		return RatUpgradeUtils.forEachUpgradeBool(this, (stack) -> stack.canFly(this), false);
	}
}
