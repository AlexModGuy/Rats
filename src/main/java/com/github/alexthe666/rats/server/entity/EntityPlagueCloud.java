package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Optional;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityPlagueCloud extends EntityMob implements IPlagueLegion {

    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityPlagueCloud.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityPlagueCloud(World worldIn) {
        super(worldIn);
        this.setSize(1.0F, 1.5F);
        this.moveHelper = new EntityPlagueCloud.AIMoveControl(this);
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        if(potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION){
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

    public void onLivingUpdate(){
        super.onLivingUpdate();
        this.setNoGravity(true);
        double d0 = this.hurtTime > 0 ? 1 : 0;
        double d1 = 0.01D;
        double d2 = 0D;
        double x = this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width;
        double y = this.posY + (double) (this.rand.nextFloat() * this.height) - (double) this.height;
        double z = this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width;
        float f = (this.width + this.height + this.width) * 0.333F + 0.5F;
        if(particleDistSq(x, y, z) < f * f){
            if(rand.nextBoolean()){
                RatsMod.PROXY.spawnParticle("black_death", x, y + 1.5F, z, d0, d1, d2);
            }else{
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, x, y + 1.5F, z, d0, d1, d2);

            }
        }
        if(this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath){
            EntityBlackDeath death = (EntityBlackDeath)this.getOwner();
            if(death.getAttackTarget() != null && !death.getAttackTarget().isDead){
                this.setAttackTarget(death.getAttackTarget());
            }else{
                float radius = (float)9 - (float)Math.sin(death.ticksExisted * 0.4D) * 0.25F;
                int maxRatStuff = 360 / Math.max(death.getCloudsSummoned(), 1);
                int ratIndex = this.getEntityId() % Math.max(death.getCloudsSummoned(), 1);
                float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + death.posX ;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + death.posZ;
                this.getMoveHelper().setMoveTo(extraX, death.posY + 2 + rand.nextInt(2), extraZ, 1.0F);
            }
        }
        if(this.getAttackTarget() != null && this.getAttackTarget().isDead){
            this.setAttackTarget(null);
        }
     }

    public void setDead() {
        if (!isDead && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath illagerPiper = (EntityBlackDeath) this.getOwner();
            illagerPiper.setCloudsSummoned(illagerPiper.getCloudsSummoned() - 1);
        }
        this.isDead = true;
    }

    public double particleDistSq(double toX, double toY, double toZ)
    {
        double d0 = (double)posX - toX;
        double d1 = (double)posY - toY;
        double d2 = (double)posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityPlagueCloud.AIMeleeAttack(this));
        this.tasks.addTask(8, new EntityPlagueCloud.AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlagueCloud.class));
        this.targetTasks.addTask(2, new BlackDeathAITargetNonPlagued(this, EntityLivingBase.class, false));
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = super.attackEntityAsMob(entityIn);
        if (flag && entityIn instanceof EntityLivingBase) {
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 600, 0));
        }
        return flag;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos){
    }

    public void fall(float distance, float damageMultiplier){
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getOwnerId() == null) {
            compound.setString("OwnerUUID", "");
        } else {
            compound.setString("OwnerUUID", this.getOwnerId().toString());
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String s;

        if (compound.hasKey("OwnerUUID", 8)) {
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
        return (UUID) ((Optional) this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
    }

    public EntityLivingBase getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            EntityLivingBase player = uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
                    if (entity instanceof EntityLivingBase) {
                        return (EntityLivingBase) entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityPlagueCloud vex) {
            super(vex);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityPlagueCloud.this.posX;
                double d1 = this.posY - EntityPlagueCloud.this.posY;
                double d2 = this.posZ - EntityPlagueCloud.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityPlagueCloud.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityPlagueCloud.this.motionX *= 0.5D;
                    EntityPlagueCloud.this.motionY *= 0.5D;
                    EntityPlagueCloud.this.motionZ *= 0.5D;
                } else {
                    EntityPlagueCloud.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityPlagueCloud.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityPlagueCloud.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityPlagueCloud.this.getAttackTarget() == null) {
                        EntityPlagueCloud.this.rotationYaw = -((float) MathHelper.atan2(EntityPlagueCloud.this.motionX, EntityPlagueCloud.this.motionZ)) * (180F / (float) Math.PI);
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

    class AIMoveRandom extends EntityAIBase {

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityPlagueCloud.this.getMoveHelper().isUpdating() && EntityPlagueCloud.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityPlagueCloud.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityPlagueCloud.this.rand.nextInt(15) - 7, EntityPlagueCloud.this.rand.nextInt(11) - 5, EntityPlagueCloud.this.rand.nextInt(15) - 7);

                if (EntityPlagueCloud.this.world.isAirBlock(blockpos1)) {
                    EntityPlagueCloud.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityPlagueCloud.this.getAttackTarget() == null) {
                        EntityPlagueCloud.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIMeleeAttack extends EntityAIBase {
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

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;
            if (entitylivingbase.getDistanceSq(this.parentEntity) >= 2.0D || !this.parentEntity.canEntityBeSeen(entitylivingbase)) {

                EntityPlagueCloud.this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY + 1.0D, entitylivingbase.posZ, 0.5D);

            }
            if (entitylivingbase.getDistanceSq(this.parentEntity) < 5.0D) {
                World world = this.parentEntity.world;
                ++this.attackTimer;
                if (this.attackTimer == 5) {
                    this.parentEntity.attackEntityAsMob(entitylivingbase);
                    this.attackTimer = -10;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
        }
    }
}
