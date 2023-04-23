package com.github.alexthe666.rats.server.entity.projectile;

import com.github.alexthe666.rats.registry.RatsEntityRegistry;
import com.github.alexthe666.rats.registry.RatsItemRegistry;
import com.github.alexthe666.rats.server.entity.RatSummoner;
import com.github.alexthe666.rats.server.entity.rat.AbstractRat;
import com.github.alexthe666.rats.server.entity.rat.Rat;
import com.github.alexthe666.rats.server.entity.rat.RatCommand;
import com.github.alexthe666.rats.server.entity.rat.TamedRat;
import com.github.alexthe666.rats.server.misc.RatVariant;
import com.github.alexthe666.rats.registry.RatVariantRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class RatShot extends ThrowableProjectile {

	private static final EntityDataAccessor<String> RAT_COLOR = SynchedEntityData.defineId(RatShot.class, EntityDataSerializers.STRING);

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(RAT_COLOR, RatVariantRegistry.getVariantId(RatVariantRegistry.getRandomVariant(this.random, false)));
	}

	public RatShot(EntityType<? extends ThrowableProjectile> type, Level level) {
		super(type, level);
	}

	public RatShot(EntityType<? extends ThrowableProjectile> type, Level level, LivingEntity thrower) {
		super(type, thrower, level);
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("RatColor", RatVariantRegistry.getVariantId(this.getColorVariant()));
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setColorVariant(RatVariantRegistry.getVariant(compound.getString("RatColor")));
	}

	public void handleEntityEvent(byte id) {
		if (id == 3) {
			for (int i = 0; i < 18; ++i) {
				this.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(RatsItemRegistry.CHEESE.get())), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D, ((double) this.random.nextFloat() - 0.5D) * 0.08D);
			}
		}
	}

	public void tick() {
		super.tick();
		Vec3 vec3d = this.getDeltaMovement();
		if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
			double f = vec3d.horizontalDistanceSqr();
			this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));
			this.setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
		}
	}

	protected void onHit(HitResult result) {
		if (result instanceof EntityHitResult entityResult && this.getOwner() != null && this.getOwner().isAlliedTo(entityResult.getEntity())) {
			return;
		}
		Entity hitEntity = null;
		float damage = this.getOwner() instanceof Player ? 6 : 8;
		if (!this.getLevel().isClientSide()) {
			if (result instanceof EntityHitResult entityResult) {
				if ((this.getOwner() == null || !entityResult.getEntity().isAlliedTo(this.getOwner())) && entityResult.getEntity() instanceof LivingEntity) {
					entityResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), damage);
					hitEntity = entityResult.getEntity();
				}
			}
			Entity thrower = this.getOwner();
			if (thrower != null) {
				AbstractRat rat = thrower instanceof Player ? new TamedRat(RatsEntityRegistry.TAMED_RAT.get(), this.getLevel()) : new Rat(RatsEntityRegistry.RAT.get(), this.getLevel());
				rat.copyPosition(this);
				if (thrower instanceof Player player) {
					rat.tame(player);
					((TamedRat) rat).setCommand(RatCommand.WANDER);
					if (hitEntity instanceof LivingEntity living) {
						rat.setTarget(living);
					}
				} else if (thrower instanceof Mob mob) {
					rat.setTarget(mob.getTarget());
					rat.setTame(false);
					rat.setOwnerUUID(mob.getUUID());
				}
				if (thrower instanceof RatSummoner ratter) {
					ratter.setRatsSummoned(ratter.getRatsSummoned() + 1);
				}
				if (this.getLevel() instanceof ServerLevelAccessor accessor) {
					ForgeEventFactory.onFinalizeSpawn(rat, accessor, this.getLevel().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);
				}
				rat.setColorVariant(this.getColorVariant());
				if (rat instanceof Rat plagueable) {
					plagueable.setPlagued(false);
				}
				this.getLevel().addFreshEntity(rat);
			}

			this.discard();
		}
	}

	public RatVariant getColorVariant() {
		return RatVariantRegistry.getVariant(this.getEntityData().get(RAT_COLOR));
	}

	public void setColorVariant(RatVariant variant) {
		this.getEntityData().set(RAT_COLOR, RatVariantRegistry.getVariantId(variant));
	}

	public BlockPos getLightPosition() {
		BlockPos pos = this.blockPosition();
		if (!this.getLevel().getBlockState(pos).canOcclude()) {
			return pos.above();
		}
		return pos;
	}
}