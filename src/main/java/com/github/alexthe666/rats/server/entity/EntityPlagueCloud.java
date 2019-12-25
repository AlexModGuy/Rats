package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class EntityPlagueCloud extends MonsterEntity implements IPlagueLegion {

    protected static final DataParameter<java.util.Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityPlagueCloud.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityPlagueCloud(EntityType type, World worldIn) {
        super(type, worldIn);
        this.moveController = new EntityPlagueCloud.AIMoveControl(this);
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_INFECT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE;
    }

    public void livingTick() {
        super.livingTick();
        this.setNoGravity(true);
        double d0 = this.hurtTime > 0 ? 1 : 0;
        double d1 = 0.01D;
        double d2 = 0D;
        double x = this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth();
        double y = this.posY + (double) (this.rand.nextFloat() * this.getHeight()) - (double) this.getHeight();
        double z = this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth();
        float f = (this.getWidth() + this.getHeight() + this.getWidth()) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            if (rand.nextBoolean()) {
                RatsMod.PROXY.addParticle("black_death", x, y + 1.5F, z, d0, d1, d2);
            } else {
                this.world.addParticle(ParticleTypes.EFFECT, x, y + 1.5F, z, d0, d1, d2);

            }
        }
        if (this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath death = (EntityBlackDeath) this.getOwner();
            if (death.getAttackTarget() != null && death.getAttackTarget().isAlive()) {
                this.setAttackTarget(death.getAttackTarget());
            } else {
                float radius = (float) 9 - (float) Math.sin(death.ticksExisted * 0.4D) * 0.25F;
                int maxRatStuff = 360 / Math.max(death.getCloudsSummoned(), 1);
                int ratIndex = this.getEntityId() % Math.max(death.getCloudsSummoned(), 1);
                float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + death.posX;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + death.posZ;
                this.moveController.setMoveTo(extraX, death.posY + 2 + rand.nextInt(2), extraZ, 1.0F);
            }
        }
        if (this.getAttackTarget() != null && !this.getAttackTarget().isAlive()) {
            this.setAttackTarget(null);
        }
    }

    public void remove() {
        if (!isAlive() && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath illagerPiper = (EntityBlackDeath) this.getOwner();
            illagerPiper.setCloudsSummoned(illagerPiper.getCloudsSummoned() - 1);
        }
        super.remove();
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = (double) posX - toX;
        double d1 = (double) posY - toY;
        double d2 = (double) posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new EntityPlagueCloud.AIMeleeAttack(this));
        this.goalSelector.addGoal(8, new EntityPlagueCloud.AIMoveRandom());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new BlackDeathAITargetNonPlagued(this, LivingEntity.class, false));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && entityIn instanceof LivingEntity) {
            ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 600, 0));
        }
        return flag;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwnerId() == null) {
            compound.putString("OwnerUUID", "");
        } else {
            compound.putString("OwnerUUID", this.getOwnerId().toString());
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        String s;

        if (compound.contains("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
    }

    public UUID getOwnerId() {
        return (UUID) ((Optional) this.dataManager.get(OWNER_UNIQUE_ID)).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            LivingEntity player = uuid == null ? null : this.world.getPlayerByUuid(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getServer().getWorld(this.dimension).getEntityByUuid(uuid);
                    if (entity instanceof LivingEntity) {
                        return (LivingEntity) entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityPlagueCloud vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.posX - EntityPlagueCloud.this.posX, this.posY - EntityPlagueCloud.this.posY, this.posZ - EntityPlagueCloud.this.posZ);
                double d0 = vec3d.length();
                double edgeLength = EntityPlagueCloud.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityPlagueCloud.this.setMotion(EntityPlagueCloud.this.getMotion().scale(0.5D));
                } else {
                    EntityPlagueCloud.this.setMotion(EntityPlagueCloud.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityPlagueCloud.this.getAttackTarget() == null) {
                        Vec3d vec3d1 = EntityPlagueCloud.this.getMotion();
                        EntityPlagueCloud.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityPlagueCloud.this.renderYawOffset = EntityPlagueCloud.this.rotationYaw;
                    } else {
                        double d4 = EntityPlagueCloud.this.getAttackTarget().posX - EntityPlagueCloud.this.posX;
                        double d5 = EntityPlagueCloud.this.getAttackTarget().posZ - EntityPlagueCloud.this.posZ;
                        EntityPlagueCloud.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityPlagueCloud.this.renderYawOffset = EntityPlagueCloud.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends Goal {

        public AIMoveRandom() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return !EntityPlagueCloud.this.moveController.isUpdating() && EntityPlagueCloud.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityPlagueCloud.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityPlagueCloud.this.rand.nextInt(15) - 7, EntityPlagueCloud.this.rand.nextInt(11) - 5, EntityPlagueCloud.this.rand.nextInt(15) - 7);

                if (EntityPlagueCloud.this.world.isAirBlock(blockpos1)) {
                    EntityPlagueCloud.this.moveController.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityPlagueCloud.this.getAttackTarget() == null) {
                        EntityPlagueCloud.this.getLookController().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIMeleeAttack extends Goal {
        private final EntityPlagueCloud parentEntity;
        public int attackTimer;

        public AIMeleeAttack(EntityPlagueCloud ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void tick() {
            LivingEntity LivingEntity = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;
            if (LivingEntity.getDistanceSq(this.parentEntity) >= 2.0D || !this.parentEntity.canEntityBeSeen(LivingEntity)) {

                EntityPlagueCloud.this.moveController.setMoveTo(LivingEntity.posX, LivingEntity.posY + 1.0D, LivingEntity.posZ, 0.5D);

            }
            if (LivingEntity.getDistanceSq(this.parentEntity) < 5.0D) {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 5) {
                    this.parentEntity.attackEntityAsMob(LivingEntity);
                    this.attackTimer = -10;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
        }
    }
}
