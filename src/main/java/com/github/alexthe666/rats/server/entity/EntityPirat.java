package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class EntityPirat extends EntityRat implements IRangedAttackMob, IRatlantean, IMob, IPirat {

    private PiratAIStrife aiArrowAttack;
    private MeleeAttackGoal aiAttackOnCollide;
    private int attackCooldown = 70;

    public EntityPirat(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 0.0F);
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

    public static boolean canSpawn(EntityType<EntityPirat> p_223332_0_, IWorld p_223332_1_, SpawnReason p_223332_2_, BlockPos p_223332_3_, Random p_223332_4_) {
        Biome biome = p_223332_1_.getBiome(p_223332_3_);
        boolean flag = p_223332_1_.getDifficulty() != Difficulty.PEACEFUL && func_223323_a(p_223332_1_, p_223332_3_, p_223332_4_) && (p_223332_2_ == SpawnReason.SPAWNER || p_223332_1_.getFluidState(p_223332_3_).isTagged(FluidTags.WATER));
        return p_223332_4_.nextInt(150) == 0 && flag;
    }

    public static boolean func_223323_a(IWorld p_223323_0_, BlockPos p_223323_1_, Random p_223323_2_) {
        return p_223323_0_.getLightFor(LightType.SKY, p_223323_1_) > 10;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, aiArrowAttack = new PiratAIStrife(this, 1.0D, 20, 30.0F));
        this.goalSelector.addGoal(1, aiAttackOnCollide = new MeleeAttackGoal(this, 1.45D, false));
        this.goalSelector.addGoal(2, new PiratAIWander(this, 1.0D));
        this.goalSelector.addGoal(2, new RatAIWander(this, 1.0D));
        this.goalSelector.addGoal(3, new RatAIFleeSun(this, 1.66D));
        this.goalSelector.addGoal(3, new RatAISit(this));
        this.goalSelector.addGoal(5, new RatAIEnterTrap(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return !(entity instanceof IRatlantean) && entity instanceof LivingEntity && !entity.isOnSameTeam(EntityPirat.this) && (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative());
            }
        }));
        this.targetSelector.addGoal(2, new RatAIHurtByTarget(this));
        this.goalSelector.removeGoal(this.aiAttackOnCollide);
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 30.0D)        //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.3D)                //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 5.0D)       //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 64.0D)               //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 0.0D);
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
        BlockPos pos = new BlockPos(this.getPositionVec());
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

    @Override
    public boolean func_233566_aG_() {
        if (this.getRidingEntity() instanceof EntityPiratBoat) {
            this.inWater = false;
        } else if (this.handleFluidAcceleration(FluidTags.WATER, 0.014D)) {
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
        super.updateRiding(riding);
        this.setPosition(riding.getPosX(), riding.getPosY() + 0.5D, riding.getPosZ());
    }

    public void updateRidden() {
        super.updateRidden();
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
            double d0 = target.getPosX() - this.getPosX();
            double d2 = target.getPosZ() - this.getPosZ();
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.renderYawOffset = this.rotationYaw = f % 360;
            if (this.getRidingEntity() != null && this.getRidingEntity() instanceof EntityPiratBoat) {
                ((EntityPiratBoat) this.getRidingEntity()).shoot(this);
            }
            attackCooldown = 70;
        }
    }

    public boolean shouldHuntAnimal() {
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
         super.tick();
        }
    }
}
