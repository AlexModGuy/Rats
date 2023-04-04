package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.registry.RatlantisEntityRegistry;
import com.github.alexthe666.rats.registry.RatsBannerPatternRegistry;
import com.github.alexthe666.rats.registry.RatsSoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PiratBoat extends Boat implements Ratlanteans, Pirats {
	private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(PiratBoat.class, EntityDataSerializers.BOOLEAN);
	public final ItemStack banner = this.generateBanner();
	private boolean prevFire;
	private int fireCooldown = 0;

	public PiratBoat(EntityType<? extends Boat> type, Level level) {
		super(type, level);
	}

	private ItemStack generateBanner() {
		ItemStack itemstack = new ItemStack(Items.BLACK_BANNER);
		CompoundTag tag = itemstack.getOrCreateTagElement("BlockEntityTag");
		ListTag list = new BannerPattern.Builder().addPattern(RatsBannerPatternRegistry.RAT_AND_CROSSBONES_BANNER.getKey(), DyeColor.WHITE).toListTag();
		tag.put("Patterns", list);
		return itemstack;
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
	public void positionRider(Entity passenger) {
		super.positionRider(passenger);
		float radius = 0.25F;
		float angle = (0.01745329251F * this.getYRot());
		double extraX = radius * Mth.sin((float) (Math.PI + angle));
		double extraZ = radius * Mth.cos(angle);
		passenger.setPos(this.getX() - extraX, this.getY() + 0.3D, this.getZ() - extraZ);
		if (passenger instanceof LivingEntity living) {
			living.yBodyRot = this.getYRot();
		}
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
	public void tick() {
		this.oldStatus = this.status;
		this.status = this.getStatus();
		if (this.status != Boat.Status.UNDER_WATER && this.status != Boat.Status.UNDER_FLOWING_WATER) {
			this.outOfControlTicks = 0.0F;
		} else {
			++this.outOfControlTicks;
		}

		if (!this.getLevel().isClientSide() && this.outOfControlTicks >= 60.0F) {
			this.ejectPassengers();
		}

		if (this.getHurtTime() > 0) {
			this.setHurtTime(this.getHurtTime() - 1);
		}

		if (this.getVehicle() != null) {
			if (!this.getVehicle().isPassenger()) {
				this.getVehicle().startRiding(this, true);
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
		if (!this.isVehicle() && !this.getLevel().isClientSide()) {
			this.hurt(this.damageSources().outOfWorld(), Float.MAX_VALUE);
		}

		this.baseTick();
		this.tickLerp();
		if (this.getControllingPassenger() != null) {
			this.setPaddleState(this.getDeltaMovement().normalize().length() > 0.1, this.getDeltaMovement().normalize().length() > 0.1);

			this.setYRot(this.getControllingPassenger().yBodyRot);

			this.floatBoat();
			if (this.getLevel().isClientSide()) {
				this.setDeltaMovement(this.getControllingPassenger().getDeltaMovement().scale(2.0F));
			}
			this.move(MoverType.SELF, this.getDeltaMovement());
		} else {
			this.setDeltaMovement(Vec3.ZERO);
		}

		this.tickBubbleColumn();

		for (int i = 0; i <= 1; ++i) {
			if (this.getPaddleState(i)) {
				if (!this.isSilent() && (double) (this.paddlePositions[i] % ((float) Math.PI * 2F)) <= (double) ((float) Math.PI / 4F) && (double) ((this.paddlePositions[i] + ((float) Math.PI / 8F)) % ((float) Math.PI * 2F)) >= (double) ((float) Math.PI / 4F)) {
					SoundEvent soundevent = this.getPaddleSound();
					if (soundevent != null) {
						Vec3 vec3 = this.getViewVector(1.0F);
						double d0 = i == 1 ? -vec3.z() : vec3.z();
						double d1 = i == 1 ? vec3.x() : -vec3.x();
						this.getLevel().playSound(null, this.getX() + d0, this.getY(), this.getZ() + d1, soundevent, this.getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
					}
				}

				this.paddlePositions[i] += ((float) Math.PI / 8F);
			} else {
				this.paddlePositions[i] = 0.0F;
			}
		}

		this.checkInsideBlocks();
		List<Entity> list = this.getLevel().getEntities(this, this.getBoundingBox().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
		if (!list.isEmpty()) {
			boolean flag = !this.getLevel().isClientSide() && !(this.getControllingPassenger() instanceof Player);

			for (Entity entity : list) {
				if (!entity.hasPassenger(this)) {
					if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && entity.getBbWidth() < this.getBbWidth() && entity instanceof Pirat) {
						entity.startRiding(this);
					} else {
						this.push(entity);
					}
				}
			}
		}
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
	}

	@Override
	public Item getDropItem() {
		return ItemStack.EMPTY.getItem();
	}

	@Override
	protected void doWaterSplashEffect() {
	}

	public void shoot(Pirat pirat) {
		LivingEntity target = pirat.getTarget();
		if (target == null) {
			target = this.getLevel().getNearestPlayer(this, 30);
		}
		if (target != null) {
			double xTarget = target.getX() - this.getX();
			double zTarget = target.getZ() - this.getZ();
			float rot = (float) (Mth.atan2(zTarget, xTarget) * (180D / Math.PI)) - 90.0F;
			this.setYRot(rot % 360);
			this.setYRot(this.getYRot());
			CheeseCannonball cannonball = new CheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL.get(), this.getLevel(), pirat);
			float radius = 1.6F;
			float angle = (0.01745329251F * (this.getYRot()));
			double extraX = (double) (radius * Mth.sin((float) (Math.PI + angle))) + getX();
			double extraZ = (double) (radius * Mth.cos(angle)) + getZ();
			double extraY = 0.8 + getY();
			double d0 = target.getY() + (double) target.getEyeHeight();
			double d1 = target.getX() - extraX;
			double d3 = target.getZ() - extraZ;
			double d2 = d0 - extraY;
			float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.65F;
			float velocity = this.distanceTo(target) * 0.045F;
			cannonball.setPos(extraX, extraY, extraZ);
			cannonball.shoot(d1, d2 + (double) f, d3, velocity, 0.4F);
			this.playSound(RatsSoundRegistry.PIRAT_SHOOT.get(), 3.0F, 2.3F / (this.random.nextFloat() * 0.4F + 0.8F));
			if (!this.getLevel().isClientSide()) {
				this.getLevel().addFreshEntity(cannonball);
			}
			this.setFiring(true);
		}
	}
}
