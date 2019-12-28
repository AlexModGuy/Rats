package com.github.alexthe666.rats.server.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

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

    public EntityRatDragonFire(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        super(RatsEntityRegistry.RAT_DRAGON_FIRE, worldIn);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setMotion(vec3d);
        float f = MathHelper.sqrt(func_213296_b(vec3d));
        this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
        this.setMotion(this.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
    }

    protected boolean isFireballFiery() {
        return false;
    }

    public void tick() {
        super.tick();
        if (world.isRemote) {
            /*RatsMod.PROXY.addParticle("rat_ghost", this.posX + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    this.posY + (double) (this.rand.nextFloat() * this.height),
                    this.posZ + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    0.92F, 0.82, 0.0F);*/
            world.addParticle(ParticleTypes.FLAME, this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                    this.posY + (double) (this.rand.nextFloat() * this.getHeight()),
                    this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(), 0, 0, 0);
        }
        if (ticksExisted > 200) {
            this.remove();
        }
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.getType() == RayTraceResult.Type.ENTITY) {
                Entity entity = ((EntityRayTraceResult)result).getEntity();
                if (!entity.isImmuneToFire()) {
                    int i = entity.func_223314_ad();
                    entity.setFire(10);
                    boolean flag = entity.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
                    if (flag) {
                        this.applyEnchantments(this.shootingEntity, entity);
                    } else {
                        entity.func_223308_g(i);
                    }
                }
            } else if (this.shootingEntity == null || !(this.shootingEntity instanceof MobEntity) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.shootingEntity)) {
                BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)result;
                BlockPos blockpos = blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
                if (this.world.isAirBlock(blockpos)) {
                    this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
