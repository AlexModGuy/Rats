package com.github.alexthe666.rats.server.entity.misc;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBannerPatternRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import com.github.alexthe666.rats.server.entity.ai.navigation.navigation.PiratNavigation;
import com.github.alexthe666.rats.server.entity.monster.Pirat;
import com.github.alexthe666.rats.server.entity.projectile.CheeseCannonball;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class PiratBoat extends Mob {
	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(PiratBoat.class, EntityDataSerializers.BOOLEAN);
	public final ItemStack banner = this.generateBanner();
	private final float[] paddlePositions = new float[2];
	private boolean prevFire;
	private int fireCooldown = 0;
	public Boat.Status status;
	public Boat.Status oldStatus;
	private double waterLevel;
	private float landFriction;
	private double lastYd;

	public PiratBoat(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		this.setMaxUpStep(1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
		this.setPathfindingMalus(BlockPathTypes.WALKABLE, -1.0F);
		this.moveControl = new BoatMoveControl(this);
		this.navigation = new PiratNavigation(this, this.level());
	}

	private ItemStack generateBanner() {
		ItemStack itemstack = new ItemStack(Items.BLACK_BANNER);
		CompoundTag tag = itemstack.getOrCreateTagElement("BlockEntityTag");
		ListTag list = new BannerPattern.Builder().addPattern(RatsBannerPatternRegistry.RAT_AND_CROSSBONES_BANNER.getKey(), DyeColor.WHITE).toListTag();
		tag.put("Patterns", list);
		return itemstack;
	}

	@Override
	protected boolean shouldPassengersInheritMalus() {
		return true;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return true;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.1D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	public LivingEntity getControllingPassenger() {
		if (!this.getPassengers().isEmpty()) {
			for (Entity entity : this.getPassengers()) {
				if (entity instanceof Pirat pirat) {
					return pirat;
				}
			}
		}
		return null;
	}

	@Override
	public void sinkInFluid(FluidType type) {

	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}


	@Override
	protected float getWaterSlowDown() {
		return 1.0F;
	}

	@Override
	public void positionRider(Entity passenger, Entity.MoveFunction callback) {
		super.positionRider(passenger, callback);
		float radius = 0.25F;
		float angle = (0.01745329251F * this.getYRot());
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		callback.accept(passenger, this.getX() - extraX, this.getY() + 0.3D, this.getZ() - extraZ);
		if (passenger instanceof LivingEntity living) {
			living.yBodyRot = this.getYRot();
		}
	}

	@Override
	public void tick() {
		super.tick();
		this.oldStatus = this.status;
		this.status = this.getStatus();
		this.floatBoat();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		for (int i = 0; i <= 1; ++i) {
			if (this.getPaddleState()) {
				this.paddlePositions[i] += ((float) Math.PI / 8F);
			} else {
				this.paddlePositions[i] = 0.0F;
			}
		}

		if (this.prevFire != this.isFiring()) {
			this.fireCooldown = 4;
		}
		if (this.isFiring() && this.fireCooldown == 0) {
			this.setFiring(false);
		}
		if (this.fireCooldown > 0) {
			this.fireCooldown--;
		}
		this.prevFire = this.isFiring();
	}

	public boolean getPaddleState() {
		return this.getControllingPassenger() != null && this.isInWater() && this.walkAnimation.isMoving() && this.hurtTime <= 0;
	}

	public float getRowingTime(int side, float limbSwing) {
		return (float) Mth.clampedLerp((double) this.paddlePositions[side] - 0.39D, this.paddlePositions[side], limbSwing);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(FIRING, false);
	}

	public boolean isFiring() {
		return this.getEntityData().get(FIRING);
	}

	public void setFiring(boolean firing) {
		this.getEntityData().set(FIRING, firing);
	}

	@Override
	protected void doWaterSplashEffect() {
		//NO-OP
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.WOOD_BREAK;
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
	}

	public void shoot(LivingEntity target, Pirat owner) {
		CheeseCannonball cannonball = new CheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL.get(), this.level(), owner);
		float radius = 1.6F;
		float angle = (0.01745329251F * (this.getYRot()));
		double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + this.getX();
		double extraZ = (double) (radius * Mth.cos(angle)) + this.getZ();
		double d0 = target.getX() - extraX;
		double d1 = target.getY() - cannonball.getY();
		double d2 = target.getZ() - extraZ;
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		cannonball.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.1F, 1.0F);
		this.playSound(RatsSoundRegistry.PIRAT_SHOOT.get(), 3.0F, 2.3F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level().addFreshEntity(cannonball);
		this.setFiring(true);
	}

	public Boat.Status getStatus() {
		Boat.Status boat$status = this.isUnderwater();
		if (boat$status != null) {
			this.waterLevel = this.getBoundingBox().maxY;
			return boat$status;
		} else if (this.checkInWater()) {
			return Boat.Status.IN_WATER;
		} else {
			float f = this.getGroundFriction();
			if (f > 0.0F) {
				this.landFriction = f;
				return Boat.Status.ON_LAND;
			} else {
				return Boat.Status.IN_AIR;
			}
		}
	}

	@Nullable
	private Boat.Status isUnderwater() {
		AABB aabb = this.getBoundingBox();
		double d0 = aabb.maxY + 0.001D;
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.maxY);
		int l = Mth.ceil(d0);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					blockpos$mutableblockpos.set(k1, l1, i2);
					FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
					if (fluidstate.is(Fluids.WATER) && d0 < (double) ((float) blockpos$mutableblockpos.getY() + fluidstate.getHeight(this.level(), blockpos$mutableblockpos))) {
						if (!fluidstate.isSource()) {
							return Boat.Status.UNDER_FLOWING_WATER;
						}

						flag = true;
					}
				}
			}
		}

		return flag ? Boat.Status.UNDER_WATER : null;
	}

	private boolean checkInWater() {
		AABB aabb = this.getBoundingBox();
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.minY);
		int l = Mth.ceil(aabb.minY + 0.001D);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		boolean flag = false;
		this.waterLevel = -Double.MAX_VALUE;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					blockpos$mutableblockpos.set(k1, l1, i2);
					FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
					if (fluidstate.is(Fluids.WATER)) {
						float f = (float) l1 + fluidstate.getHeight(this.level(), blockpos$mutableblockpos);
						this.waterLevel = Math.max(f, this.waterLevel);
						flag |= aabb.minY < (double) f;
					}
				}
			}
		}

		return flag;
	}

	public void floatBoat() {
		double d1 = this.isNoGravity() ? 0.0D : (double) -0.04F;
		double d2 = 0.0D;
		float invFriction = 0.05F;
		if (this.oldStatus == Boat.Status.IN_AIR && this.status != Boat.Status.IN_AIR && this.status != Boat.Status.ON_LAND) {
			this.waterLevel = this.getY(1.0D);
			this.setPos(this.getX(), (double) (this.getWaterLevelAbove() - this.getBbHeight()) + 0.101D, this.getZ());
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
			this.lastYd = 0.0D;
			this.status = Boat.Status.IN_WATER;
		} else {
			if (this.status == Boat.Status.IN_WATER || this.status == Boat.Status.UNDER_WATER) {
				d2 = (this.waterLevel - this.getY()) / (double) this.getBbHeight();
				invFriction = 0.9F;
			} else if (this.status == Boat.Status.UNDER_FLOWING_WATER) {
				d1 = -7.0E-4D;
				invFriction = 0.9F;
			} else if (this.status == Boat.Status.IN_AIR) {
				invFriction = 0.9F;
			} else if (this.status == Boat.Status.ON_LAND) {
				invFriction = this.landFriction;
				if (this.getControllingPassenger() instanceof Player) {
					this.landFriction /= 2.0F;
				}
			}

			Vec3 vec3 = this.getDeltaMovement();
			this.setDeltaMovement(vec3.x * (double) invFriction, vec3.y + d1, vec3.z * (double) invFriction);
			if (d2 > 0.0D) {
				Vec3 vec31 = this.getDeltaMovement();
				this.setDeltaMovement(vec31.x, (vec31.y + d2 * 0.1D), vec31.z);
			}
		}
	}

	public float getWaterLevelAbove() {
		AABB aabb = this.getBoundingBox();
		int i = Mth.floor(aabb.minX);
		int j = Mth.ceil(aabb.maxX);
		int k = Mth.floor(aabb.maxY);
		int l = Mth.ceil(aabb.maxY - this.lastYd);
		int i1 = Mth.floor(aabb.minZ);
		int j1 = Mth.ceil(aabb.maxZ);
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		label39:
		for (int k1 = k; k1 < l; ++k1) {
			float f = 0.0F;

			for (int l1 = i; l1 < j; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					blockpos$mutableblockpos.set(l1, k1, i2);
					FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
					if (fluidstate.is(Fluids.WATER)) {
						f = Math.max(f, fluidstate.getHeight(this.level(), blockpos$mutableblockpos));
					}

					if (f >= 1.0F) {
						continue label39;
					}
				}
			}

			if (f < 1.0F) {
				return (float) blockpos$mutableblockpos.getY() + f;
			}
		}

		return (float) (l + 1);
	}

	public float getGroundFriction() {
		AABB aabb = this.getBoundingBox();
		AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001D, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
		int i = Mth.floor(aabb1.minX) - 1;
		int j = Mth.ceil(aabb1.maxX) + 1;
		int k = Mth.floor(aabb1.minY) - 1;
		int l = Mth.ceil(aabb1.maxY) + 1;
		int i1 = Mth.floor(aabb1.minZ) - 1;
		int j1 = Mth.ceil(aabb1.maxZ) + 1;
		VoxelShape voxelshape = Shapes.create(aabb1);
		float f = 0.0F;
		int k1 = 0;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int l1 = i; l1 < j; ++l1) {
			for (int i2 = i1; i2 < j1; ++i2) {
				int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
				if (j2 != 2) {
					for (int k2 = k; k2 < l; ++k2) {
						if (j2 <= 0 || k2 != k && k2 != l - 1) {
							blockpos$mutableblockpos.set(l1, k2, i2);
							BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
							if (!(blockstate.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), blockpos$mutableblockpos).move(l1, k2, i2), voxelshape, BooleanOp.AND)) {
								f += blockstate.getFriction(this.level(), blockpos$mutableblockpos, this);
								++k1;
							}
						}
					}
				}
			}
		}

		return f / (float) k1;
	}

	private static class BoatMoveControl extends MoveControl {
		private final PiratBoat boat;

		private BoatMoveControl(PiratBoat boat) {
			super(boat);
			this.boat = boat;
		}

		@Override
		public void tick() {
			if (this.operation == MoveControl.Operation.MOVE_TO && !this.boat.getNavigation().isDone()) {
				double d0 = this.wantedX - this.boat.getX();
				double d1 = this.wantedY - this.boat.getY();
				double d2 = this.wantedZ - this.boat.getZ();
				double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				if (d3 < (double) 1.0E-5F) {
					this.mob.setSpeed(0.0F);
				} else {
					d1 /= d3;
					float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
					this.boat.setYRot(this.rotlerp(this.boat.getYRot(), f, 90.0F));
					this.boat.yBodyRot = this.boat.getYRot();
					float f1 = 0.5F;
					this.boat.setSpeed(Mth.lerp(0.125F, this.boat.getSpeed(), f1));
					this.boat.setDeltaMovement(this.boat.getDeltaMovement().add(0.0D, (double) this.boat.getSpeed() * d1 * 0.1D, 0.0D));
				}
			} else {
				this.boat.setSpeed(0.0F);
			}
		}
	}
}
