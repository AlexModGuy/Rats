package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;

public class EntityPirat extends EntityRat implements IRangedAttackMob, IRatlantean, IMob {

    private PiratAIStrife aiArrowAttack;
    private MeleeAttackGoal aiAttackOnCollide;
    private int attackCooldown = 70;

    public EntityPirat(EntityType type, World worldIn) {
        super(type, worldIn);
        waterBased = true;
        Arrays.fill(this.inventoryArmorDropChances, 0.2F);
        Arrays.fill(this.inventoryHandsDropChances, 0.2F);
        this.moveController = new PiratMoveController(this);
        this.navigator = new PiratPathNavigate(this, world);
    }

    public boolean isInWater() {
        return super.isInWater() && !this.isPassenger();
    }


    protected void switchNavigator(int type) {
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, aiArrowAttack = new PiratAIStrife(this, 1.0D, 20, 30.0F));
        this.goalSelector.addGoal(1, aiAttackOnCollide = new MeleeAttackGoal(this, 1.45D, false));
        this.goalSelector.addGoal(2, new PiratAIWander(this, 1.0D));
        this.goalSelector.addGoal(2, new RatAIWander(this, 1.0D));
        this.goalSelector.addGoal(3, new RatAIFleeSun(this, 1.66D));
        this.goalSelector.addGoal(3, this.sitGoal = new RatAISit(this));
        this.goalSelector.addGoal(5, new RatAIEnterTrap(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return !(entity instanceof IRatlantean) && entity instanceof LivingEntity && !entity.isOnSameTeam(EntityPirat.this);
            }
        }));
        this.targetSelector.addGoal(2, new RatAIHurtByTarget(this));
        this.goalSelector.removeGoal(this.aiAttackOnCollide);
    }

    protected void registerAttributes() {
        super.registerAttributes();
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
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            if (this.isPassenger()) {
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

    public void livingTick() {
        super.livingTick();
        this.holdInMouth = false;
        if (attackCooldown > 0) {
            attackCooldown--;
        }
        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setCombatTask();
    }

    public double getYOffset() {
        return 0.45D;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(RatsItemRegistry.PIRAT_CUTLASS));
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatsItemRegistry.PIRAT_HAT));
        if (!this.isPassenger()) {
            EntityPiratBoat boat = new EntityPiratBoat(RatsEntityRegistry.PIRAT_BOAT, world);
            boat.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                world.addEntity(boat);
            }
            this.startRiding(boat, true);
        }
        this.setCombatTask();
        return spawnDataIn;
    }

    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        BlockPos pos = new BlockPos(this);
        BlockState BlockState = this.world.getBlockState(pos.down());
        return this.world.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && BlockState.getMaterial() == Material.WATER && rand.nextFloat() < 0.1F;
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

    public void stopRiding() {
        super.stopRiding();
        this.setCombatTask();
    }

    public boolean handleWaterMovement() {
        if (this.getRidingEntity() instanceof EntityPiratBoat) {
            this.inWater = false;
        } else if (this.handleFluidAcceleration(FluidTags.WATER)) {
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
        if (this.isPassenger() && !entity.isAlive()) {
            this.stopRiding();
        } else {
            this.setMotion(0, 0, 0);
            if (!firstUpdate)
                this.tick();
            if (this.isPassenger()) {
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

    public boolean shouldHunt() {
        return true;
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public class PiratMoveController extends MovementController {

        public PiratMoveController(MobEntity LivingEntityIn) {
            super(LivingEntityIn);
        }

        public void tick() {
            if (this.action == MovementController.Action.STRAFE) {
                float f = (float) EntityPirat.this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue();
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
                float f5 = MathHelper.sin(EntityPirat.this.rotationYaw * 0.017453292F);
                float f6 = MathHelper.cos(EntityPirat.this.rotationYaw * 0.017453292F);
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigator pathnavigate = EntityPirat.this.getNavigator();

                if (pathnavigate != null) {
                    NodeProcessor nodeprocessor = pathnavigate.getNodeProcessor();

                    if (nodeprocessor != null) {
                        PathNodeType type = nodeprocessor.getPathNodeType(EntityPirat.this.world, MathHelper.floor(EntityPirat.this.posX + (double) f7), MathHelper.floor(EntityPirat.this.posY), MathHelper.floor(EntityPirat.this.posZ + (double) f8));
                        if (type != PathNodeType.WALKABLE && type != PathNodeType.WATER) {
                            this.moveForward = 1.0F;
                            this.moveStrafe = 0.0F;
                            f1 = f;
                        }
                    }
                }

                EntityPirat.this.setAIMoveSpeed(f1);
                EntityPirat.this.setMoveForward(this.moveForward);
                EntityPirat.this.setMoveStrafing(this.moveStrafe);
                this.action = MovementController.Action.WAIT;
            } else if (this.action == MovementController.Action.MOVE_TO) {
                this.action = MovementController.Action.WAIT;
                double d0 = this.posX - EntityPirat.this.posX;
                double d1 = this.posZ - EntityPirat.this.posZ;
                double d2 = this.posY - EntityPirat.this.posY;
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;

                if (d3 < 2.500000277905201E-7D) {
                    EntityPirat.this.setMoveForward(0.0F);
                    return;
                }

                float f9 = (float) (MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
                EntityPirat.this.rotationYaw = this.limitAngle(EntityPirat.this.rotationYaw, f9, 90.0F);
                EntityPirat.this.setAIMoveSpeed((float) (this.speed * EntityPirat.this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                EntityPirat.this.setMoveForward(EntityPirat.this.getAIMoveSpeed());

                if (d2 > (double) EntityPirat.this.stepHeight && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, EntityPirat.this.getWidth())) {
                    EntityPirat.this.getJumpController().setJumping();
                    this.action = MovementController.Action.JUMPING;
                }
            } else if (this.action == MovementController.Action.JUMPING) {
                EntityPirat.this.setAIMoveSpeed((float) (this.speed * EntityPirat.this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));

                if (EntityPirat.this.onGround) {
                    this.action = MovementController.Action.WAIT;
                }
            } else {
                EntityPirat.this.setMoveForward(0.0F);
            }
        }
    }
}
