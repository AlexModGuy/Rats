package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAIStrife;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityBlackDeath extends MobEntity implements IPlagueLegion, IRangedAttackMob {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "black_death"));
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_SUMMONING = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAT_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> CLOUD_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BEAST_COUNT = EntityDataManager.createKey(EntityBlackDeath.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS));
    private int ratCooldown = 0;
    private int summonAnimationCooldown = 0;

    public EntityBlackDeath(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.goalSelector.addGoal(0, new EntityAISwimming(this));
        this.goalSelector.addGoal(4, new BlackDeathAIStrife(this, 1.0D, 100, 32.0F));
        this.goalSelector.addGoal(8, new EntityAIWander(this, 0.6D));
        this.goalSelector.addGoal(9, new EntityAIWatchClosest(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, true, EntityRat.class));
        this.targetSelector.addGoal(2, new BlackDeathAITargetNonPlagued(this, LivingEntity.class, true));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(IS_SUMMONING, Boolean.valueOf(false));
        this.dataManager.register(RAT_COUNT, Integer.valueOf(0));
        this.dataManager.register(CLOUD_COUNT, Integer.valueOf(0));
        this.dataManager.register(BEAST_COUNT, Integer.valueOf(0));
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
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

    public void setDead() {
        if (!isDead) {
            double dist = 20F;
            for (EntityRat rat : world.getEntitiesWithinAABB(EntityRat.class, new AxisAlignedBB(this.posX - dist, this.posY - dist, this.posZ - dist, this.posX + dist, this.posY + dist, this.posZ + dist))) {
                if (rat.isOwner(this)) {
                    rat.setTamed(false);
                    rat.setOwnerId(null);
                    rat.fleePos = new BlockPos(rat);
                    rat.setAttackTarget(null);
                    rat.setRevengeTarget(null);
                }
            }
        }
        this.isDead = true;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(RatConfig.blackDeathHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(RatConfig.blackDeathAttack);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
    }

    protected void updateAIgoalSelector() {
        super.updateAIgoalSelector();
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

    public int getRatsSummoned() {
        return Integer.valueOf(this.dataManager.get(RAT_COUNT).intValue());
    }

    public void setRatsSummoned(int count) {
        this.dataManager.set(RAT_COUNT, Integer.valueOf(count));
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

    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInt("RatsSummoned", this.getRatsSummoned());
        compound.setInt("CloudsSummoned", this.getCloudsSummoned());
        compound.setInt("BeastsSummoned", this.getBeastsSummoned());
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.setRatsSummoned(compound.getInt("RatsSummoned"));
        this.setCloudsSummoned(compound.getInt("CloudsSummoned"));
        this.setBeastsSummoned(compound.getInt("BeastsSummoned"));

    }

    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
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

    @Override
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
                EntityRat rat = new EntityRat(this.world);
                rat.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), null);
                rat.copyLocationAndAnglesFrom(this);
                rat.setPlague(true);
                if (!world.isRemote) {
                    world.addEntity(rat);
                }
                rat.setTamed(true);
                rat.setOwnerId(this.getUniqueID());
                rat.setCommand(RatCommand.FOLLOW);
                if (this.getAttackTarget() != null) {
                    rat.setAttackTarget(this.getAttackTarget());
                }
                this.setRatsSummoned(this.getRatsSummoned() + 1);
            }
            if (type == 1 && this.getCloudsSummoned() < 4) {
                EntityPlagueCloud cloud = new EntityPlagueCloud(this.world);
                cloud.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), null);
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
                EntityPlagueBeast beast = new EntityPlagueBeast(this.world);
                beast.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), null);
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

    public void onLivingUpdate() {
        super.onLivingUpdate();
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
            RatsMod.PROXY.spawnParticle("black_death", this.posX + (double) f1 * 0.6D, this.posY + 1.8D, this.posZ + (double) f2 * 0.6D, d0, d1, d2);
            RatsMod.PROXY.spawnParticle("black_death", this.posX - (double) f1 * 0.6D, this.posY + 1.8D, this.posZ - (double) f2 * 0.6D, d0, d1, d2);
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
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.PLAGUE_SCYTHE));
        this.setDropChance(EntityEquipmentSlot.MAINHAND, 0);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        return ientitylivingdata;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
}
