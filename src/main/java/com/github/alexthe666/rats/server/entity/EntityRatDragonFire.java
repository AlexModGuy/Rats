package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRatDragonFire extends AbstractFireballEntity {

    public EntityRatDragonFire(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRatDragonFire(EntityType type, LivingEntity shooter, World worldIn, double accelX, double accelY, double accelZ) {
        super(type, shooter, accelX, accelY, accelZ, worldIn);
    }

    public EntityRatDragonFire(EntityType type, World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(type, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
        float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
        this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
        this.motionX += entityThrower.motionX;
        this.motionZ += entityThrower.motionZ;

        if (!entityThrower.onGround) {
            this.motionY += entityThrower.motionY;
        }
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    protected boolean isFireballFiery() {
        return false;
    }

    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            /*RatsMod.PROXY.spawnParticle("rat_ghost", this.posX + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    this.posY + (double) (this.rand.nextFloat() * this.height),
                    this.posZ + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    0.92F, 0.82, 0.0F);*/
            world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    this.posY + (double) (this.rand.nextFloat() * this.height),
                    this.posZ + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width, 0, 0, 0);
        }
        if (ticksExisted > 200) {
            this.setDead();
        }
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null) {
                if (!result.entityHit.isImmuneToFire()) {
                    boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);

                    if (flag) {
                        this.applyEnchantments(this.shootingEntity, result.entityHit);
                        result.entityHit.setFire(5);
                    }
                }
            } else {
                boolean flag1 = true;
                if (this.shootingEntity != null && this.shootingEntity instanceof EntityLiving) {
                    flag1 = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity);
                }
                if (flag1) {
                    BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
                    if (this.world.isAirBlock(blockpos)) {
                        this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
            if (result.entityHit != null) {
                this.setDead();
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }
}
