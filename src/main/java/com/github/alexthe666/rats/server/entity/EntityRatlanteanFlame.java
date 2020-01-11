package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRatlanteanFlame extends AbstractFireballEntity {

    public EntityRatlanteanFlame(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRatlanteanFlame(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(RatsEntityRegistry.RATLANTEAN_FLAME, shooter.posX, shooter.posY, shooter.posZ, accelX, accelY, accelZ, worldIn);
    }

    public EntityRatlanteanFlame(EntityType type, World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(type, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    public EntityRatlanteanFlame(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(RatsEntityRegistry.RATLANTEAN_FLAME, world);
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
            RatsMod.PROXY.addParticle("rat_ghost", this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                    this.posY + (double) (this.rand.nextFloat() * this.getHeight()),
                    this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2F) - (double) this.getWidth(),
                    0.92F, 0.82, 0.0F);
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
    public ItemStack getItem() {
        return new ItemStack(RatsItemRegistry.RATLANTEAN_FLAME);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
