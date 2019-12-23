package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.PiperAIStrife;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityIllagerPiper extends AbstractIllagerEntity implements IRangedAttackMob {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "illager_piper"));
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityIllagerPiper.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAT_COUNT = EntityDataManager.createKey(EntityIllagerPiper.class, DataSerializers.VARINT);
    private final PiperAIStrife aiArrowAttack = new PiperAIStrife(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false);
    private int ratCooldown = 0;
    private int fluteTicks = 0;

    public EntityIllagerPiper(EntityType type, World world) {
        super(type, world);
        this.setCombatTask();
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(RAT_COUNT, Integer.valueOf(0));
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, PlayerEntity.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityRat.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    public boolean getCanSpawnHere() {
        int spawnRoll = RatConfig.piperSpawnDecrease;
        if (RatUtils.canSpawnInDimension(world)) {
            if (spawnRoll == 0 || rand.nextInt(spawnRoll) == 0) {
                return super.getCanSpawnHere();
            }
        }
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

    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        compound.setInt("RatsSummoned", this.getRatsSummoned());
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
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
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setCombatTask();
        return livingdata;
    }

    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }

    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() == RatsItemRegistry.RAT_FLUTE) {
                int i = 100;
                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            } else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(RatsItemRegistry.RAT_FLUTE));
    }


    @Override
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }

    @SideOnly(Side.CLIENT)
    public AbstractIllager.IllagerArmPose getArmPose() {
        return IllagerArmPose.BOW_AND_ARROW;
    }

    public void summonRat() {
        if (this.getRatsSummoned() < 6 && ratCooldown == 0) {
            world.setEntityState(this, (byte) 82);
            EntityRat rat = new EntityRat(this.world);
            rat.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(this)), null);
            rat.copyLocationAndAnglesFrom(this);
            rat.setPlague(false);
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
            this.playSound(RatsSoundRegistry.RAT_FLUTE, 1, 1);
            ratCooldown = 150;
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
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

        if (this.getRatsSummoned() < 3 && ratCooldown == 0) {
            summonRat();
        }
    }

    @SideOnly(Side.CLIENT)
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
        EnumParticleTypes enumparticletypes = EnumParticleTypes.NOTE;

        if (type == 1) {
            double d0 = 0.0;
            this.world.spawnParticle(enumparticletypes, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, 0, 0);
        } else {
            double d0 = 0.65;
            for (int i = 0; i < 9; ++i) {
                this.world.spawnParticle(enumparticletypes, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, 0, 0);
            }
        }

    }

    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (this.world.getGameRules().getBoolean("doMobLoot")) {
            if (cause.getTrueSource() instanceof EntityRat) {
                if (new Random().nextBoolean()) {
                    this.dropItem(RatsItemRegistry.MUSIC_DISC_MICE_ON_VENUS, 1);
                } else {
                    this.dropItem(RatsItemRegistry.MUSIC_DISC_LIVING_MICE, 1);

                }
            }
        }
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATION_ILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
    }

    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
}
