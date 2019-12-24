package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public class EntityPirat extends EntityRat implements IRangedAttackMob, IRatlantean, IMob {

    private PiratAIStrife aiArrowAttack;
    private EntityAIAttackMelee aiAttackOnCollide;
    private int attackCooldown = 70;

    public EntityPirat(EntityType type, World worldIn) {
        super(type, worldIn);
        waterBased = true;
        Arrays.fill(this.inventoryArmorDropChances, 0.2F);
        Arrays.fill(this.inventoryHandsDropChances, 0.2F);
        this.moveHelper = new PiratMoveHelper(this);
        this.navigator = new PiratPathNavigate(this, world);
    }

    public boolean isInWater() {
        return super.isInWater() && !this.isRiding();
    }


    protected void switchNavigator(int type) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(1, aiArrowAttack = new PiratAIStrife(this, 1.0D, 20, 30.0F));
        this.goalSelector.addGoal(1, aiAttackOnCollide = new EntityAIAttackMelee(this, 1.45D, false));
        this.goalSelector.addGoal(2, new PiratAIWander(this, 1.0D));
        this.goalSelector.addGoal(2, new RatAIWander(this, 1.0D));
        this.goalSelector.addGoal(3, new RatAIFleeSun(this, 1.66D));
        this.goalSelector.addGoal(3, this.aiSit = new RatAISit(this));
        this.goalSelector.addGoal(5, new RatAIEnterTrap(this));
        this.goalSelector.addGoal(7, new EntityAIWatchClosest(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAINearestAttackableTarget(this, LivingEntity.class, 5, true, false, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return !(entity instanceof IRatlantean) && entity instanceof LivingEntity && !entity.isOnSameTeam(EntityPirat.this);
            }
        }));
        this.targetSelector.addGoal(2, new RatAIHurtByTarget(this, false));
        this.goalSelector.removeTask(this.aiAttackOnCollide);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
    }

    public void setAttackTarget(@Nullable LivingEntity LivingEntityIn) {
        super.setAttackTarget(LivingEntityIn);
        this.setCombatTask();
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.goalSelector.removeTask(this.aiAttackOnCollide);
            this.goalSelector.removeTask(this.aiArrowAttack);
            if (this.isRiding()) {
                int i = 20;
                if (this.world.getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(1, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(1, this.aiAttackOnCollide);
            }
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.holdInMouth = false;
        if (attackCooldown > 0) {
            attackCooldown--;
        }
        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.setDead();
        }
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        this.setCombatTask();
    }

    public double getYOffset() {
        return 0.45D;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, @Nullable ILivingEntityData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(RatsItemRegistry.PIRAT_CUTLASS));
        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(RatsItemRegistry.PIRAT_HAT));
        if (!this.isRiding()) {
            EntityPiratBoat boat = new EntityPiratBoat(world);
            boat.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                world.addEntity(boat);
            }
            this.startRiding(boat, true);
        }
        this.setCombatTask();
        return livingdata;
    }

    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this);
        BlockState BlockState = this.world.getBlockState(pos.down());
        return this.world.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && BlockState.getMaterial() == Material.WATER && rand.nextFloat() < 0.1F;
    }

    protected boolean isValidLightLevel() {
        BlockPos blockpos = world.getHeight(new BlockPos(posX, 0, this.posZ));
        if (this.world.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        } else {
            int i = this.world.getLightFromNeighbors(blockpos);

            if (this.world.isThundering()) {
                int j = this.world.getSkylightSubtracted();
                this.world.setSkylightSubtracted(10);
                i = this.world.getLightFromNeighbors(blockpos);
                this.world.setSkylightSubtracted(j);
            }

            return i <= this.rand.nextInt(8);
        }
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getBoundingBox(), this);
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

    public boolean handleWaterMovement() {
        if (this.getRidingEntity() instanceof EntityPiratBoat) {
            this.inWater = false;
        } else if (this.world.handleMaterialAcceleration(this.getBoundingBox().grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D), Material.WATER, this)) {
            if (!this.inWater && !this.firstUpdate) {
                this.doWaterSplashEffect();
            }
            this.fallDistance = 0.0F;
            this.inWater = true;
            this.extinguish();
        } else {
            this.inWater = false;
        }

        return this.inWater;
    }

    public void updateRiding(Entity riding) {

    }

    public void updateRidden() {
        Entity entity = this.getRidingEntity();
        if (this.isRiding() && entity.isDead) {
            this.dismountRidingEntity();
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            if (!updateBlocked)
                this.onUpdate();
            if (this.isRiding()) {
                entity.updatePassenger(this);
            }
        }
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0F;
        this.fallDistance = 0.0F;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (attackCooldown == 0) {
            this.faceEntity(target, 180, 180);
            double d0 = target.posX - this.posX;
            double d2 = target.posZ - this.posZ;
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.renderYawOffset = this.rotationYaw = f % 360;
            if (this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityPiratBoat) {
                ((EntityPiratBoat) this.getRidingEntity()).shoot(this);
            }
            attackCooldown = 70;
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {

    }

    public boolean shouldHunt() {
        return true;
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public static class PiratMoveHelper extends EntityMoveHelper {

        public PiratMoveHelper(LivingEntity LivingEntityIn) {
            super(LivingEntityIn);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.STRAFE) {
                float f = (float) this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
                float f1 = (float) this.speed * f;
                float f2 = this.moveForward;
                float f3 = this.moveStrafe;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.entity.rotationYaw * 0.017453292F);
                float f6 = MathHelper.cos(this.entity.rotationYaw * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigate pathnavigate = this.entity.getNavigator();

                if (pathnavigate != null) {
                    NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                    if (nodeprocessor != null) {
                        PathNodeType type = nodeprocessor.getPathNodeType(this.entity.world, MathHelper.floor(this.entity.posX + (double) f7), MathHelper.floor(this.entity.posY), MathHelper.floor(this.entity.posZ + (double) f8));
                        if (type != PathNodeType.WALKABLE && type != PathNodeType.WATER) {
                            this.moveForward = 1.0F;
                            this.moveStrafe = 0.0F;
                            f1 = f;
                        }
                    }
                }

                this.entity.setAIMoveSpeed(f1);
                this.entity.setMoveForward(this.moveForward);
                this.entity.setMoveStrafing(this.moveStrafe);
                this.action = EntityMoveHelper.Action.WAIT;
            } else if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                this.action = EntityMoveHelper.Action.WAIT;
                double d0 = this.posX - this.entity.posX;
                double d1 = this.posZ - this.entity.posZ;
                double d2 = this.posY - this.entity.posY;
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }

                float f9 = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f9, 90.0F);
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                this.entity.setMoveForward(this.entity.getAIMoveSpeed());

                if (d2 > (double) this.entity.stepHeight && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.entity.width)) {
                    this.entity.getJumpHelper().setJumping();
                    this.action = EntityMoveHelper.Action.JUMPING;
                }
            } else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

                if (this.entity.onGround) {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            } else {
                this.entity.setMoveForward(0.0F);
            }
        }
    }
}
