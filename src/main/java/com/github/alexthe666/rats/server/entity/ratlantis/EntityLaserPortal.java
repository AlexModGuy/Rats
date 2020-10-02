package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityLaserPortal extends Entity {
    public static final Predicate<Entity> MONSTER_NOT_RAT = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity != null && !(entity instanceof EntityRat) && entity instanceof IMob;
        }
    };
    public float scaleOfPortal;
    public float scaleOfPortalPrev;
    @Nullable
    private LivingEntity creator;
    @Nullable
    private Entity facingTarget;
    private UUID ownerUniqueId;

    public EntityLaserPortal(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityLaserPortal(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatlantisEntityRegistry.LASER_PORTAL, worldIn);
    }

    public EntityLaserPortal(EntityType type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public EntityLaserPortal(EntityType type, World worldIn, double x, double y, double z, LivingEntity creator) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setCreator(creator);
    }

    public void tick() {
        super.tick();
        if (ticksExisted > 300) {
            this.remove();
        }
        if (ticksExisted < 250 && scaleOfPortal < 1.0F) {
            scaleOfPortal += 0.05F;
        }
        if (ticksExisted > 250 && scaleOfPortal > 0.0F) {
            scaleOfPortal -= 0.05F;
        }
        if (ticksExisted % 50 == 0) {
            this.tryFiring();
        } else {
            faceTarget();
        }
        if (world.isRemote && scaleOfPortal >= 0.5F) {
            RatsMod.PROXY.addParticle("rat_lightning", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()),
                    this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth()) - (double) this.getWidth() / 2,
                    0.0F, 0.0F, 0.0F);
        }
        scaleOfPortalPrev = scaleOfPortal;
    }


    private void faceTarget() {
        if (facingTarget == null || this.getCreator() != null && !facingTarget.isEntityEqual(((MobEntity) this.getCreator()).getAttackTarget())) {
            if (this.getCreator() != null && this.getCreator() instanceof MobEntity) {
                LivingEntity target = ((MobEntity) this.getCreator()).getAttackTarget();
                if (target == null && this.getCreator() instanceof MonsterEntity) {
                    target = world.getClosestPlayer(this, 30);
                }
                facingTarget = target;
            }
        }
        if (facingTarget != null) {
            double d0 = this.getPosX() - facingTarget.getPosX();
            double d2 = this.getPosZ() - facingTarget.getPosZ();
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.rotationYaw = f % 360;
        }
    }

    private void tryFiring() {
        if (this.getCreator() != null && this.getCreator() instanceof MobEntity) {
            LivingEntity target = ((MobEntity) this.getCreator()).getAttackTarget();
            if (target == null && this.getCreator() instanceof MonsterEntity) {
                target = world.getClosestPlayer(this, 30);
            }
            if (target == null && this.getCreator() instanceof EntityRat && ((EntityRat) this.getCreator()).isTamed()) {
                LivingEntity closest = null;
                for (Entity entity : world.getEntitiesInAABBexcluding(this.getCreator(), this.getBoundingBox().grow(40, 10, 40), MONSTER_NOT_RAT)) {
                    if (entity instanceof LivingEntity && (closest == null || entity.getDistanceSq(this) < closest.getDistanceSq(this))) {
                        closest = (LivingEntity) entity;
                    }
                }
                target = closest;
            }
            if (target != null) {
                double d0 = this.getPosX() - target.getPosX();
                double d1 = this.getPosY() - (target.getPosY());
                double d2 = this.getPosZ() - target.getPosZ();
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.rotationYaw = f % 360;
                double targetRelativeX = target.getPosX() - this.getPosX();
                double targetRelativeY = target.getPosY() + target.getHeight() / 2 - this.getPosY() - 1.0F;
                double targetRelativeZ = target.getPosZ() - this.getPosZ();
                EntityLaserBeam beam = new EntityLaserBeam(RatlantisEntityRegistry.LASER_BEAM, world, this.getCreator());
                this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
                beam.setPosition(this.getPosX(), this.getPosY() + 1.0F, this.getPosZ());
                beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
                if (!world.isRemote) {
                    world.addEntity(beam);
                }
            }
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
