package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityPirat extends EntityRat implements IRangedAttackMob, IRatlantean {

    private PiratAIStrife aiArrowAttack;
    private EntityAIAttackMelee aiAttackOnCollide;
    private int attackCooldown = 40;

    public EntityPirat(World worldIn) {
        super(worldIn);
        waterBased = true;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, aiArrowAttack = new PiratAIStrife(this, 1.0D, 20, 15.0F));
        this.tasks.addTask(1, aiAttackOnCollide = new EntityAIAttackMelee(this, 1.45D, false));
        this.tasks.addTask(2, new RatAIWander(this, 1.0D));
        this.tasks.addTask(3, new RatAIFleeSun(this, 1.66D));
        this.tasks.addTask(3, this.aiSit = new RatAISit(this));
        this.tasks.addTask(5, new RatAIEnterTrap(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new RatAIHuntPrey(this, new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable EntityLivingBase entity) {
                return !(entity instanceof IRatlantean) && entity instanceof EntityLivingBase && !entity.isOnSameTeam(EntityPirat.this);
            }
        }));
        this.targetTasks.addTask(2, new RatAIHurtByTarget(this, false, new Class[0]));
        this.tasks.removeTask(this.aiAttackOnCollide);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            if (this.isRiding()) {
                int i = 20;
                if (this.world.getDifficulty() != EnumDifficulty.HARD) {
                    i = 40;
                }
                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(1, this.aiArrowAttack);
            } else {
                this.tasks.addTask(1, this.aiAttackOnCollide);
            }
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.holdInMouth = false;
        if(attackCooldown > 0){
            attackCooldown--;
        }
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setCombatTask();
    }

    public void updateRiding(Entity riding) {
        super.updateRiding(riding);
        if (riding != null && riding.isPassenger(this) && riding instanceof EntityPiratBoat) {
            this.setPosition(riding.posX, riding.posY + 0.45F, riding.posZ);
        }
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(RatsItemRegistry.PIRAT_CUTLASS));
        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.PIRAT_HAT));
        if (!this.isRiding()) {
            EntityPiratBoat boat = new EntityPiratBoat(world);
            boat.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                world.spawnEntity(boat);
            }
            this.startRiding(boat, true);
        }
        this.setCombatTask();
        return livingdata;
    }

    public boolean canBeTamed() {
        return false;
    }

    public boolean isTamed() {
        return false;
    }

    public boolean startRiding(Entity entityIn, boolean force) {
        boolean flag = super.startRiding(entityIn, force);
        this.setCombatTask();
        return flag;
    }

    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.setCombatTask();
    }

    public void updateRidden() {
        Entity entity = this.getRidingEntity();
        if (entity != null && entity.isDead) {
           // this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();
            if (this.isRiding()) {
                this.updateRiding(entity);
            }
        }
    }


    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        if(attackCooldown == 0){
            this.faceEntity(target, 180, 180);
            double d0 = target.posX - this.posX;
            double d2 = target.posZ - this.posZ;
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.renderYawOffset = this.rotationYaw = f % 360;
            if (this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityPiratBoat) {
                ((EntityPiratBoat) this.getRidingEntity()).shoot(this);
            }
            attackCooldown = 40;
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    public boolean shouldHunt() {
        return true;
    }

    public boolean shouldDismountInWater(Entity rider){
        return false;
    }
}
