package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.message.MessageSyncThrownBlock;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityNeoRatlantean extends EntityMob implements IAnimatedEntity, IRangedAttackMob, IRatlantean {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "neo_ratlantean"));
    private static final Predicate<EntityLivingBase> NOT_RATLANTEAN = new Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase entity) {
            return entity.isEntityAlive() && !(entity instanceof IRatlantean);
        }
    };
    private static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityFeralRatlantean.class, DataSerializers.VARINT);
    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS));
    private int animationTick;
    private Animation currentAnimation;
    private int attackSelection = 0;
    private int summonCooldown = 0;
    private int humTicks = 0;

    public EntityNeoRatlantean(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.8F, 1.3F);
        ((PathNavigateGround) this.getNavigator()).setCanSwim(true);
        this.experienceValue = 80;
        this.moveHelper = new EntityNeoRatlantean.AIMoveControl(this);
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.ticksExisted % 100 == 0) {
            this.heal(1);
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public boolean isNonBoss() {
        return false;
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COLOR_VARIANT, Integer.valueOf(0));
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(COLOR_VARIANT).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(COLOR_VARIANT, Integer.valueOf(color));
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
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }


    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColorVariant(this.getRNG().nextInt(4));
        return livingdata;
    }

    public void onUpdate() {
        super.onUpdate();
        if (world.isRemote) {
            RatsMod.PROXY.spawnParticle("rat_lightning", this.posX + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    this.posY + this.getEyeHeight() + (double) (this.rand.nextFloat() * 0.35F),
                    this.posZ + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    0.0F, 0.0F, 0.0F);
        }
        if (summonCooldown > 0) {
            summonCooldown--;
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (humTicks % 80 == 0) {
            this.playSound(RatsSoundRegistry.NEORATLANTEAN_LOOP, 1, 1);
        }
        humTicks++;
        if (!world.isRemote && this.getAttackTarget() != null) {
            Entity entity = this.getAttackTarget();
            if (attackSelection == 0 && summonCooldown == 0) {
                summonCooldown = 100;
                int bounds = 5;
                for (int i = 0; i < rand.nextInt(3) + 3; i++) {
                    EntityLaserPortal laserPortal = new EntityLaserPortal(world, entity.posX + this.rand.nextInt(bounds * 2) - bounds, this.posY + 2, entity.posZ + this.rand.nextInt(bounds * 2) - bounds, this);
                    world.spawnEntity(laserPortal);
                }
                resetAttacks();
            }
            if (attackSelection == 1 && summonCooldown == 0) {
                int bounds = 20;
                this.world.addWeatherEffect(new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, false));
                for (int i = 0; i < rand.nextInt(3) + 2; i++) {
                    this.world.addWeatherEffect(new EntityLightningBolt(this.world, entity.posX + this.rand.nextInt(bounds * 2) - bounds, entity.posY, entity.posZ + this.rand.nextInt(bounds * 2) - bounds, false));
                }
                summonCooldown = 100;
                resetAttacks();
            }
            if (attackSelection == 2 && summonCooldown == 0) {
                int searchRange = 10;
                BlockPos ourPos = new BlockPos(this);
                List<BlockPos> listOfAll = new ArrayList<>();
                for (BlockPos pos : BlockPos.getAllInBox(ourPos.add(-searchRange, -searchRange, -searchRange), ourPos.add(searchRange, searchRange, searchRange))) {
                    IBlockState state = world.getBlockState(pos);
                    if (!world.isAirBlock(pos) && canPickupBlock(state)) {
                        listOfAll.add(pos);
                    }
                }
                boolean flag = false;
                if (listOfAll.size() > 0) {
                    BlockPos pos = listOfAll.get(rand.nextInt(listOfAll.size()));
                    EntityThrownBlock thrownBlock = new EntityThrownBlock(world, world.getBlockState(pos), this);
                    thrownBlock.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
                    if (!world.isRemote) {
                        world.spawnEntity(thrownBlock);
                    }
                    RatsMod.NETWORK_WRAPPER.sendToAll(new MessageSyncThrownBlock(thrownBlock.getEntityId(), pos.toLong()));
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    summonCooldown = 40;
                }
                resetAttacks();
            }
            if (attackSelection == 3 && summonCooldown == 0) {
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.WITHER, 200));
                this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200));
                summonCooldown = 40;
                resetAttacks();
            }
        }
    }

    public void resetAttacks() {
        attackSelection = rand.nextInt(4);
    }

    public boolean canPickupBlock(IBlockState state) {
        return EntityWither.canDestroyBlock(state.getBlock());
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityNeoRatlantean.AIFollowPrey(this));
        this.tasks.addTask(2, new EntityNeoRatlantean.AIMoveRandom());
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, NOT_RATLANTEAN));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(RatsMod.CONFIG_OPTIONS.neoRatlanteanHealth);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(RatsMod.CONFIG_OPTIONS.neoRatlanteanAttack);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0.0D);
    }

    public void fall(float distance, float damageMultiplier) {

    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {

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

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.NEORATLANTEAN_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.NEORATLANTEAN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.NEORATLANTEAN_DIE;
    }

    public int getTalkInterval() {
        return 10;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
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

    public class AIFollowPrey extends EntityAIBase {
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
            return entitylivingbase != null && (entitylivingbase.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(entitylivingbase));
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double maxFollow = followDist * 5;
            if (entitylivingbase.getDistance(this.parentEntity) >= maxFollow || !this.parentEntity.canEntityBeSeen(entitylivingbase)) {

                EntityNeoRatlantean.this.moveHelper.setMoveTo(entitylivingbase.posX + rand.nextInt(10) - 20, entitylivingbase.posY + 3, entitylivingbase.posZ + rand.nextInt(10) - 20, 1D);
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
