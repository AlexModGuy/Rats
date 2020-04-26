package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.entity.ai.PiperAIStrife;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityIllagerPiper extends AbstractIllagerEntity implements IRangedAttackMob {

    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityIllagerPiper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAT_COUNT = EntityDataManager.createKey(EntityIllagerPiper.class, DataSerializers.VARINT);
    private final PiperAIStrife aiArrowAttack = new PiperAIStrife(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false);
    private int ratCooldown = 0;
    private int fluteTicks = 0;

    public EntityIllagerPiper(EntityType type, World world) {
        super(type, world);
        this.setCombatTask();
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(RAT_COUNT, Integer.valueOf(0));
    }

    @Override
    public void func_213660_a(int p_213660_1_, boolean p_213660_2_) {

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityRat.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillagerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolemEntity.class, true));
    }

    public boolean getCanSpawnHere(IWorld world, SpawnReason reason) {
        int spawnRoll = RatConfig.piperSpawnDecrease;
        if (RatUtils.canSpawnInDimension(world)) {
            if (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) {
                return super.canSpawn(world, reason);
            }
        }
        return false;
    }

    public void remove() {
        if (!isAlive()) {
            double dist = 20F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(this.getPosX() - dist, this.getPosY() - dist, this.getPosZ() - dist, this.getPosX() + dist, this.getPosY() + dist, this.getPosZ() + dist))) {
                if (rat.isOwner(this)) {
                    rat.setTamed(false);
                    rat.setOwnerId(null);
                    rat.fleePos = new BlockPos(rat);
                    rat.setAttackTarget(null);
                    rat.setRevengeTarget(null);
                }
            }
        }
        super.remove();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("RatsSummoned", this.getRatsSummoned());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setRatsSummoned(compound.getInt("RatsSummoned"));
        this.setCombatTask();
    }

    public int getRatsSummoned() {
        return Integer.valueOf(this.dataManager.get(RAT_COUNT).intValue());
    }

    public void setRatsSummoned(int count) {
        this.dataManager.set(RAT_COUNT, Integer.valueOf(count));
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        summonRat();
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        this.setCombatTask();
        return spawnDataIn;
    }

    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.world.isRemote && slotIn == EquipmentSlotType.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() == RatsItemRegistry.RAT_FLUTE) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(RatsItemRegistry.RAT_FLUTE));
    }

    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        return ArmPose.BOW_AND_ARROW;
    }

    public void summonRat() {
        if (this.getRatsSummoned() < 6 && ratCooldown == 0) {
            world.setEntityState(this, (byte) 82);
            EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, this.world);
            rat.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(this)), SpawnReason.MOB_SUMMONED, null, null);
            rat.copyLocationAndAnglesFrom(this);
            rat.setPlague(false);
            if (!world.isRemote) {
                world.addEntity(rat);
            }
            rat.setTamed(true);
            rat.setTamedByPlayerFlag(false);
            rat.setOwnerId(this.getUniqueID());
            rat.setCommand(RatCommand.FOLLOW);
            if (this.getAttackTarget() != null) {
                rat.setAttackTarget(this.getAttackTarget());
            }
            this.setRatsSummoned(this.getRatsSummoned() + 1);
            this.playSound(RatsSoundRegistry.RAT_FLUTE, 1, 1);
            ratCooldown = 150;
        }
    }

    public void livingTick() {
        super.livingTick();
        if (ratCooldown > 0) {
            ratCooldown--;
        }
        if (fluteTicks % 157 == 0) {
            this.playSound(RatsSoundRegistry.PIPER_LOOP, 1, 1);
        }
        fluteTicks++;
        if (fluteTicks % 10 == 0) {
            world.setEntityState(this, (byte) 83);
        }

        if (this.getRatsSummoned() < 3 && ratCooldown == 0 && this.ticksExisted > 20) {
            summonRat();
        }
    }

    public static boolean canSpawnOn(EntityType<? extends MobEntity> typeIn, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        BlockPos blockpos = pos.down();
        return reason == SpawnReason.SPAWNER || worldIn.getBlockState(blockpos).canEntitySpawn(worldIn, blockpos, typeIn) && randomIn.nextFloat() < 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 82) {
            this.playEffect(0);
        } else if (id == 83) {
            this.playEffect(1);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected void playEffect(int type) {
        BasicParticleType p = ParticleTypes.NOTE;
        if (type == 1) {
            double d0 = 0.0;
            this.world.addParticle(p, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 0.5D + (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, 0, 0);
        } else {
            double d0 = 0.65;
            for (int i = 0; i < 9; ++i) {
                this.world.addParticle(p, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + 0.5D + (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, 0, 0);
            }
        }

    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            if (cause.getTrueSource() instanceof EntityRat) {
                if (new Random().nextBoolean()) {
                    this.entityDropItem(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS, 1);
                } else {
                    this.entityDropItem(RatsItemRegistry.MUSIC_DISC_LIVING_MICE, 1);
                }
            }
        }
    }

    @Override
    public SoundEvent getRaidLossSound() {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VINDICATOR_HURT;
    }

    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }
}
