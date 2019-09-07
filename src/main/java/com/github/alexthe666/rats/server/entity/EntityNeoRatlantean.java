package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityNeoRatlantean extends EntityMob implements IAnimatedEntity, IRangedAttackMob {

    private int animationTick;
    private Animation currentAnimation;
    private static final Predicate<EntityLivingBase> NOT_RATLANTEAN = new Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase entity) {
            return entity.isEntityAlive() && !(entity instanceof EntityMarbleCheeseGolem) && !(entity instanceof EntityNeoRatlantean) && !(entity instanceof EntityRatlanteanSpirit) && !(entity instanceof EntityFeralRatlantean);
        }
    };
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private int attackSelection = 0;
    private int summonCooldown = 0;
    public EntityNeoRatlantean(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.8F, 1.3F);
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 80;
        this.moveHelper = new EntityNeoRatlantean.AIMoveControl(this);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("ColorVariant", this.getColorVariant());
        compound.setInteger("AttackSelection", attackSelection);
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setColorVariant(compound.getInteger("ColorVariant"));
        attackSelection = compound.getInteger("AttackSelection");
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColorVariant(this.getRNG().nextInt(4));
        return livingdata;
    }

    public void onUpdate() {
        super.onUpdate();
        if(world.isRemote){
            RatsMod.PROXY.spawnParticle("rat_lightning", this.posX + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    this.posY + this.getEyeHeight() + (double) (this.rand.nextFloat() * 0.35F),
                    this.posZ + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    0.0F,  0.0F, 0.0F);
        }
        if(summonCooldown > 0){
            summonCooldown--;
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!world.isRemote && this.getAttackTarget() != null){
            if(attackSelection == 0 && summonCooldown == 0){
                summonCooldown = 100;
                int bounds = 5;
                for(int i = 0; i < rand.nextInt(2) + 3; i++){
                    EntityLaserPortal laserPortal = new EntityLaserPortal(world, this.getAttackTarget().posX + this.rand.nextInt(bounds*2) - bounds, this.posY + 2, this.getAttackTarget().posZ + this.rand.nextInt(bounds*2) - bounds, this);
                    world.spawnEntity(laserPortal);
                }
            }
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityNeoRatlantean.AIFollowPrey(this));
        this.tasks.addTask(2, new EntityNeoRatlantean.AIMoveRandom());
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, NOT_RATLANTEAN));
    }
    
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
    }

    @Override
    public int getAnimationTick() {
        return 0;
    }

    @Override
    public void setAnimationTick(int tick) {

    }

    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public void setAnimation(Animation animation) {

    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityNeoRatlantean vex) {
            super(vex);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityNeoRatlantean.this.posX;
                double d1 = this.posY - EntityNeoRatlantean.this.posY;
                double d2 = this.posZ - EntityNeoRatlantean.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityNeoRatlantean.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityNeoRatlantean.this.motionX *= 0.5D;
                    EntityNeoRatlantean.this.motionY *= 0.5D;
                    EntityNeoRatlantean.this.motionZ *= 0.5D;
                } else {
                    EntityNeoRatlantean.this.motionX += d0 / d3 * 0.25D * this.speed;
                    EntityNeoRatlantean.this.motionY += d1 / d3 * 0.25D * this.speed;
                    EntityNeoRatlantean.this.motionZ += d2 / d3 * 0.25D * this.speed;

                    if (EntityNeoRatlantean.this.getAttackTarget() == null) {
                        EntityNeoRatlantean.this.rotationYaw = -((float) MathHelper.atan2(EntityNeoRatlantean.this.motionX, EntityNeoRatlantean.this.motionZ)) * (180F / (float) Math.PI);
                        EntityNeoRatlantean.this.renderYawOffset = EntityNeoRatlantean.this.rotationYaw;
                    } else {
                        double d4 = EntityNeoRatlantean.this.getAttackTarget().posX - EntityNeoRatlantean.this.posX;
                        double d5 = EntityNeoRatlantean.this.getAttackTarget().posZ - EntityNeoRatlantean.this.posZ;
                        EntityNeoRatlantean.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityNeoRatlantean.this.renderYawOffset = EntityNeoRatlantean.this.rotationYaw;
                    }
                }
            }
        }
    }

    public class AIFollowPrey  extends EntityAIBase {
        private final EntityNeoRatlantean parentEntity;
        public int attackTimer;
        private double followDist;

        public AIFollowPrey(EntityNeoRatlantean ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            followDist = EntityNeoRatlantean.this.getEntityBoundingBox().getAverageEdgeLength();
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double maxFollow = followDist * 5;
            return entitylivingbase!= null && (entitylivingbase.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(entitylivingbase));
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double maxFollow = followDist * 5;
            if(entitylivingbase.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(entitylivingbase)){

                EntityNeoRatlantean.this.moveHelper.setMoveTo((double) entitylivingbase.posX + rand.nextInt(10) - 20, (double) entitylivingbase.posY + 3, (double) entitylivingbase.posZ  + rand.nextInt(10) - 20, 1D);
            }
        }
    }

    public class AIMoveRandom extends EntityAIBase {

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityNeoRatlantean.this.getMoveHelper().isUpdating() && EntityNeoRatlantean.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityNeoRatlantean.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityNeoRatlantean.this.rand.nextInt(15) - 7, EntityNeoRatlantean.this.rand.nextInt(11) - 5, EntityNeoRatlantean.this.rand.nextInt(15) - 7);

                if (EntityNeoRatlantean.this.world.isAirBlock(blockpos1)) {
                    EntityNeoRatlantean.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 1D);

                    if (EntityNeoRatlantean.this.getAttackTarget() == null) {
                        EntityNeoRatlantean.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

}
