package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.ArrowlikeProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

public class LaserBeam extends ArrowlikeProjectile {

	private static final EntityDataAccessor<Float> R = SynchedEntityData.defineId(LaserBeam.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> G = SynchedEntityData.defineId(LaserBeam.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Float> B = SynchedEntityData.defineId(LaserBeam.class, EntityDataSerializers.FLOAT);

	public LaserBeam(EntityType<? extends ArrowlikeProjectile> type, Level level) {
		super(type, level);
		this.setBaseDamage(3.0F);
	}

	public LaserBeam(EntityType<? extends ArrowlikeProjectile> type, Level level, LivingEntity shooter) {
		super(type, shooter, level);
		this.setBaseDamage(4.0F);
	}

	@Override
	public boolean isInWater() {
		return false;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(R, 0.66F);
		this.getEntityData().define(G, 0.97F);
		this.getEntityData().define(B, 0.97F);
	}

	public float[] getRGB() {
		return new float[]{this.getEntityData().get(R), this.getEntityData().get(G), this.getEntityData().get(B)};
	}

	public void setRGB(float newR, float newG, float newB) {
		this.getEntityData().set(R, newR);
		this.getEntityData().set(G, newG);
		this.getEntityData().set(B, newB);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putFloat("ColorR", this.getRGB()[0]);
		compound.putFloat("ColorG", this.getRGB()[1]);
		compound.putFloat("ColorB", this.getRGB()[2]);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setRGB(compound.getFloat("ColorR"), compound.getFloat("ColorG"), compound.getFloat("ColorB"));
	}

	@Override
	public void tick() {
		float sqrt = (float) this.getDeltaMovement().length();
		if (sqrt < 0.3F || this.inGround || this.horizontalCollision) {
			this.discard();
			Explosion explosion = this.getLevel().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 0.0F, Level.ExplosionInteraction.MOB);
			explosion.explode();
			explosion.finalizeExplosion(true);
		}
		super.tick();
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	public boolean explodesOnHit() {
		return true;
	}
}
