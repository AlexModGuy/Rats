package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAIStrife;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class EntityBlackDeath extends MonsterEntity implements IPlagueLegion, IRangedAttackMob, ISummonsRats {

    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_SUMMONING = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAT_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> CLOUD_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BEAST_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private final ServerBossInfo bossInfo = (new ServerBossInfo( this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS));
    private int ratCooldown = 0;
    private int summonAnimationCooldown = 0;

    public EntityBlackDeath(EntityType type, World worldIn) {
        super(type, worldIn);
        ((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new BlackDeathAIStrife(this, 1.0D, 100, 32.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, EntityRat.class).setCallsForHelp());
        this.targetSelector.addGoal(2, new BlackDeathAITargetNonPlagued(this, LivingEntity.class, true));
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(IS_SUMMONING, Boolean.valueOf(false));
        this.dataManager.register(RAT_COUNT, Integer.valueOf(0));
        this.dataManager.register(CLOUD_COUNT, Integer.valueOf(0));
        this.dataManager.register(BEAST_COUNT, Integer.valueOf(0));
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.BLACK_DEATH_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.BLACK_DEATH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.BLACK_DEATH_DIE;
    }

    public int getTalkInterval() {
        return 10;
    }

    protected boolean canDespawn() {
        return false;
    }

    public void remove() {
        if (!this.isAlive()) {
            double dist = 20F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(this.getPosX() - dist, this.getPosY() - dist, this.getPosZ() - dist, this.getPosX() + dist, this.getPosY() + dist, this.getPosZ() + dist))) {
                if (rat.isOwner(this)) {
                    rat.setTamed(false);
                    rat.setOwnerId(null);
                    rat.fleePos = new BlockPos(rat.getPositionVec());
                    rat.setAttackTarget(null);
                    rat.setRevengeTarget(null);
                }
            }
        }
        super.remove();
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, RatConfig.blackDeathHealth)        //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.25D)                //SPEED
                .func_233815_a_(Attributes.field_233823_f_, RatConfig.blackDeathAttack)       //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 128D)               //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 12D);             //ARMOR
    }


    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public boolean isNonBoss() {
        return false;
    }

    public boolean isSummoning() {
        return this.dataManager.get(IS_SUMMONING);
    }

    public void setSummoning(boolean summoning) {
        this.dataManager.set(IS_SUMMONING, summoning);
    }

    @Override
    public boolean encirclesSummoner() {
        return true;
    }

    @Override
    public boolean readsorbRats() {
        return false;
    }

    public int getRatsSummoned() {
        return Integer.valueOf(this.dataManager.get(RAT_COUNT).intValue());
    }

    public void setRatsSummoned(int count) {
        this.dataManager.set(RAT_COUNT, Integer.valueOf(count));
    }

    @Override
    public float getRadius() {
        return (float) 5 - (float) Math.sin(this.ticksExisted * 0.4D) * 0.5F;
    }

    public int getCloudsSummoned() {
        return Integer.valueOf(this.dataManager.get(CLOUD_COUNT).intValue());
    }

    public void setCloudsSummoned(int count) {
        this.dataManager.set(CLOUD_COUNT, Integer.valueOf(count));
    }

    public int getBeastsSummoned() {
        return Integer.valueOf(this.dataManager.get(BEAST_COUNT).intValue());
    }

    public void setBeastsSummoned(int count) {
        this.dataManager.set(BEAST_COUNT, Integer.valueOf(count));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("RatsSummoned", this.getRatsSummoned());
        compound.putInt("CloudsSummoned", this.getCloudsSummoned());
        compound.putInt("BeastsSummoned", this.getBeastsSummoned());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setRatsSummoned(compound.getInt("RatsSummoned"));
        this.setCloudsSummoned(compound.getInt("CloudsSummoned"));
        this.setBeastsSummoned(compound.getInt("BeastsSummoned"));

    }

    public void setCustomName(ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {

        summonMinion(getMinionTypeToSpawn());
    }

    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }

    public void summonMinion(int type) {
        if (ratCooldown == 0) {
            if (!this.isSummoning()) {
                this.setSummoning(true);
                summonAnimationCooldown = 20;
            }
            world.setEntityState(this, (byte) 82);
            if (type == 0 && this.getRatsSummoned() < 15) {
                EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, this.world);
                if(!world.isRemote){
                    rat.onInitialSpawn((ServerWorld)this.world, this.world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.MOB_SUMMONED, null, null);
                }
                rat.copyLocationAndAnglesFrom(this);
                rat.setPlague(true);
                if (!world.isRemote) {
                    world.addEntity(rat);
                }
                rat.setTamed(false);
                rat.setTamedByMonster(true);
                rat.setMonsterOwnerUniqueId(this.getUniqueID());
                rat.setCommand(RatCommand.FOLLOW);
                if (this.getAttackTarget() != null) {
                    rat.setAttackTarget(this.getAttackTarget());
                }
                this.setRatsSummoned(this.getRatsSummoned() + 1);
            }
            if (type == 1 && this.getCloudsSummoned() < 4) {
                EntityPlagueCloud cloud = new EntityPlagueCloud(RatsEntityRegistry.PLAGUE_CLOUD, this.world);
                if(!world.isRemote){
                    cloud.onInitialSpawn((ServerWorld)this.world, this.world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.MOB_SUMMONED, null, null);
                }
                cloud.copyLocationAndAnglesFrom(this);
                if (!world.isRemote) {
                    world.addEntity(cloud);
                }
                cloud.setOwnerId(this.getUniqueID());
                if (this.getAttackTarget() != null) {
                    cloud.setAttackTarget(this.getAttackTarget());
                }
                this.setCloudsSummoned(this.getCloudsSummoned() + 1);
            }
            if (type == 2 && this.getBeastsSummoned() < 3) {
                EntityPlagueBeast beast = new EntityPlagueBeast(RatsEntityRegistry.PLAGUE_BEAST, this.world);
                if(!world.isRemote){
                    beast.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.MOB_SUMMONED, null, null);
                }
                beast.copyLocationAndAnglesFrom(this);
                if (!world.isRemote) {
                    world.addEntity(beast);
                }
                beast.setOwnerId(this.getUniqueID());
                if (this.getAttackTarget() != null) {
                    beast.setAttackTarget(this.getAttackTarget());
                }
                this.setBeastsSummoned(this.getBeastsSummoned() + 1);
            }
            ratCooldown = 40;
        }
    }

    public void livingTick() {
        super.livingTick();
        if(this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityRat && ((EntityRat) this.getAttackTarget()).hasPlague()){
            this.setAttackTarget(null);
        }
        if (ratCooldown > 0) {
            ratCooldown--;
        }
        if (summonAnimationCooldown > 0) {
            summonAnimationCooldown--;
        }
        if (this.isSummoning() && summonAnimationCooldown <= 0) {
            this.setSummoning(false);
            summonAnimationCooldown = 0;
        }
        if (this.world.isRemote && this.isSummoning()) {
            double d0 = 0;
            double d1 = 0;
            double d2 = 0;
            float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            RatsMod.PROXY.addParticle("black_death", this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, d0, d1, d2);
            RatsMod.PROXY.addParticle("black_death", this.getPosX() - (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double) f2 * 0.6D, d0, d1, d2);
        }
        if (this.getRatsSummoned() < 15 && ratCooldown == 0) {
            summonMinion(0);
        }
    }

    private int getMinionTypeToSpawn() {
        if (this.getAttackTarget() != null) {
            return getLowestMinionType();
        } else {
            return 0;
        }
    }

    private int getLowestMinionType() {
        if (this.getCloudsSummoned() < 4 && rand.nextBoolean()) {
            return 1;
        }
        if (this.getBeastsSummoned() < 3 && rand.nextBoolean()) {
            return 2;
        }
        return 0;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(RatsItemRegistry.PLAGUE_SCYTHE));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData iLivingEntitydata = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return iLivingEntitydata;
    }
}
