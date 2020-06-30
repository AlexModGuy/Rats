package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityDutchratSword extends Entity {
    @Nullable
    private LivingEntity creator;
    @Nullable
    private Entity target;
    private UUID ownerUniqueId;
    private int field_234610_c_;
    private boolean field_234611_d_;

    public EntityDutchratSword(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDutchratSword(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.DUTCHRAT_SWORD, worldIn);
    }

    public EntityDutchratSword(EntityType type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public EntityDutchratSword(EntityType type, World worldIn, double x, double y, double z, LivingEntity creator) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setCreator(creator);
    }


    protected boolean func_230298_a_(Entity p_230298_1_) {
        if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.canBeCollidedWith()) {
            Entity entity = this.func_234616_v_();
            return entity == null || this.field_234611_d_ || !entity.isRidingSameEntity(p_230298_1_);
        } else {
            return false;
        }
    }

    @Nullable
    public Entity func_234616_v_() {
        if (this.creator != null && this.world instanceof ServerWorld) {
            return ((ServerWorld)this.world).getEntityByUuid(this.ownerUniqueId);
        } else {
            return this.field_234610_c_ != 0 ? this.world.getEntityByID(this.field_234610_c_) : null;
        }
    }

    public void tick() {
        super.tick();
        this.noClip = true;
        if (ticksExisted > 300) {
           // this.remove();
        }
        if(ticksExisted > 5){
            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_, RayTraceContext.BlockMode.COLLIDER);
            if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }
        }

        if (world.isRemote) {
            RatsMod.PROXY.addParticle("pirat_ghost", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()),
                    this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    0.0F, 0.0F, 0.0F);
        }

        if (target == null || this.getCreator() != null && !target.isEntityEqual(((MobEntity) this.getCreator()).getAttackTarget())) {
            if (this.getCreator() != null && this.getCreator() instanceof MobEntity) {
                LivingEntity target = ((MobEntity) this.getCreator()).getAttackTarget();
                if (target == null && this.getCreator() instanceof MonsterEntity) {
                    target = world.getClosestPlayer(this, 30);
                }
                this.target = target;
            }
        }
        if (target != null) {
            double d0 = target.getPosX() - this.getPosX();
            double d1 = target.getPosY() - this.getPosY();
            double d2 = target.getPosZ() - this.getPosZ();
            double d3 =  MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            double speed = 0.1D;
            this.setMotion(this.getMotion().add(d0 / d3 * speed, d1 / d3 * speed, d2 / d3 * speed));
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.rotationYaw = f % 360;
            this.prevRotationYaw = f % 360;

        }
        this.move(MoverType.SELF, this.getMotion());
    }

    private void onImpact(RayTraceResult raytraceresult) {
        DamageSource source = DamageSource.MAGIC;
        if(this.getCreator() != null){
            source = DamageSource.causeMobDamage(this.getCreator());
        }
        boolean hit = false;
        for (Entity hitMobs : world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(1.0F, 1.0F, 1.0F))) {
            if(!(hitMobs instanceof IPirat)){
                hitMobs.attackEntityFrom(source, RatConfig.dutchratAttack);
                hit = true;
            }
        }
        if(hit){
            world.createExplosion(this.getCreator(), this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, Explosion.Mode.NONE);
            this.remove();
        }
    }

    @Nullable
    public LivingEntity getCreator() {
        if (this.creator == null && this.ownerUniqueId != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld) this.world).getEntityByUuid(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.creator = (LivingEntity) entity;
            }
        }

        return this.creator;
    }

    public void setCreator(@Nullable LivingEntity ownerIn) {
        this.creator = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    protected void readAdditional(CompoundNBT compound) {
        this.ticksExisted = compound.getInt("Age");
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
    }

    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Age", this.ticksExisted);

        if (this.ownerUniqueId != null) {
            compound.putUniqueId("OwnerUUID", this.ownerUniqueId);
        }
    }

    public PushReaction getPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    protected void registerData() {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
