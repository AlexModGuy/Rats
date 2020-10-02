package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.villager.RatsVillagerTrades;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityPlagueDoctor extends AbstractVillagerEntity implements IRangedAttackMob {

    private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(Blocks.POPPY)));
    private static final com.google.common.base.Predicate<Entity> PLAGUE_PREDICATE = new com.google.common.base.Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof LivingEntity && ((LivingEntity) entity).isPotionActive(RatsMod.PLAGUE_POTION) || entity instanceof EntityRat && ((EntityRat) entity).hasPlague() || entity instanceof IPlagueLegion;
        }
    };
    private BlockPos wanderTarget;
    private int despawnDelay;
    private static final DataParameter<Boolean> WILL_DESPAWN = EntityDataManager.createKey(EntityPlagueDoctor.class, DataSerializers.BOOLEAN);

    public EntityPlagueDoctor(EntityType type, World worldIn) {
        super(type, worldIn);
        ((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new UseItemGoal<>(this, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.INVISIBILITY), SoundEvents.ENTITY_WANDERING_TRADER_DISAPPEARED, (p_213733_1_) -> {
            return !this.world.isDaytime() && !p_213733_1_.isInvisible();
        }));
        this.goalSelector.addGoal(0, new UseItemGoal<>(this, new ItemStack(Items.MILK_BUCKET), SoundEvents.ENTITY_WANDERING_TRADER_REAPPEARED, (p_213736_1_) -> {
            return this.world.isDaytime() && p_213736_1_.isInvisible();
        }));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
           this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
        this.goalSelector.addGoal(1, new LookAtCustomerGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 60, 10.0F));
        this.goalSelector.addGoal(2, new EntityPlagueDoctor.MoveToGoal(this, 2.0D, 0.35D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 0.35D));
        this.goalSelector.addGoal(9, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, ZombieVillagerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, false, false, PLAGUE_PREDICATE));
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageableEntity) {
        return null;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(WILL_DESPAWN, Boolean.valueOf(false));
    }

    public boolean willDespawn() {
        return this.dataManager.get(WILL_DESPAWN).booleanValue();
    }

    public void setWillDespawn(boolean despawn) {
        this.dataManager.set(WILL_DESPAWN, Boolean.valueOf(despawn));
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 20.0D)        //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.5D);
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && willDespawn()) {
            this.handleDespawn();
        }
        if (this.getAttackTarget() != null) {
            if (this.getAttackTarget() instanceof EntityRat) {
                if (!((EntityRat) this.getAttackTarget()).hasPlague()) {
                    this.setAttackTarget(null);
                }
            } else if (this.getAttackTarget() instanceof ZombieVillagerEntity) {
                if (((ZombieVillagerEntity) this.getAttackTarget()).isConverting()) {
                    this.setAttackTarget(null);
                }
            } else if (!this.getAttackTarget().isPotionActive(RatsMod.PLAGUE_POTION) && !(this.getAttackTarget() instanceof IPlagueLegion)) {
                this.setAttackTarget(null);
            }
        }
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("DespawnDelay", this.despawnDelay);
        compound.putBoolean("WillDespawn", this.willDespawn());
        if (this.wanderTarget != null) {
            compound.put("WanderTarget", NBTUtil.writeBlockPos(this.wanderTarget));
        }

    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("DespawnDelay", 99)) {
            this.despawnDelay = compound.getInt("DespawnDelay");
        }
        this.setWillDespawn(compound.getBoolean("WillDespawn"));

        if (compound.contains("WanderTarget")) {
            this.wanderTarget = NBTUtil.readBlockPos(compound.getCompound("WanderTarget"));
        }

        this.setGrowingAge(Math.max(0, this.getGrowingAge()));
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    public void setDespawnDelay(int delay) {
        this.despawnDelay = delay;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }


    private void handleDespawn() {
        if (this.despawnDelay > 0 && !this.hasCustomer() && --this.despawnDelay == 0) {
            this.remove();
        }
    }


    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() != RatsMod.PLAGUE_POTION && super.isPotionApplicable(potioneffectIn);
    }

    protected boolean canDespawn() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return this.hasCustomer() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    protected SoundEvent getDrinkSound(ItemStack p_213351_1_) {
        Item item = p_213351_1_.getItem();
        return item == Items.MILK_BUCKET ? SoundEvents.ENTITY_WANDERING_TRADER_DRINK_MILK : SoundEvents.ENTITY_WANDERING_TRADER_DRINK_POTION;
    }

    protected SoundEvent func_213721_r(boolean p_213721_1_) {
        return p_213721_1_ ? SoundEvents.ENTITY_WANDERING_TRADER_YES : SoundEvents.ENTITY_WANDERING_TRADER_NO;
    }

    public SoundEvent func_213714_ea() {
        return SoundEvents.ENTITY_WANDERING_TRADER_YES;
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        double d0 = target.getPosY() + (double) target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.getPosX() + target.getMotion().x - this.getPosX();
        double d2 = d0 - this.getPosY();
        double d3 = target.getPosZ() + target.getMotion().z - this.getPosZ();
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
        EntityPurifyingLiquid entitypotion = new EntityPurifyingLiquid(this.world, this, false);
        entitypotion.rotationPitch -= -20.0F;
        entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
        this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
        this.world.addEntity(entitypotion);
    }

    public void onStruckByLightning(LightningBoltEntity lightningBolt) {
        if (!this.world.isRemote && this.isAlive()) {
            EntityBlackDeath entitywitch = new EntityBlackDeath(RatsEntityRegistry.BLACK_DEATH, this.world);
            entitywitch.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch);
            if(!world.isRemote){
                entitywitch.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.MOB_SUMMONED, null, null);
            }
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomName(this.getCustomName());
            }
            this.world.addEntity(entitywitch);
            this.remove();
        }
    }

    @Override
    protected void onVillagerTrade(MerchantOffer p_213713_1_) {
        if (p_213713_1_.getDoesRewardExp()) {
            int i = 3 + this.rand.nextInt(4);
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), i));
        }
    }

    @Nullable
    private BlockPos func_213727_eh() {
        return this.wanderTarget;
    }

    public void setWanderTarget(@Nullable BlockPos blockpos1) {
        this.wanderTarget = blockpos1;
    }

    public class MoveToGoal extends Goal {
        final EntityPlagueDoctor plagueDoctor;
        final double range;
        final double speed;

        MoveToGoal(EntityPlagueDoctor p_i50459_2_, double p_i50459_3_, double p_i50459_5_) {
            this.plagueDoctor = p_i50459_2_;
            this.range = p_i50459_3_;
            this.speed = p_i50459_5_;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.plagueDoctor.setWanderTarget((BlockPos)null);
            EntityPlagueDoctor.this.navigator.clearPath();
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            BlockPos blockpos = this.plagueDoctor.func_213727_eh();
            return blockpos != null && this.func_220846_a(blockpos, this.range);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            BlockPos blockpos = this.plagueDoctor.func_213727_eh();
            if (blockpos != null && EntityPlagueDoctor.this.navigator.noPath()) {
                if (this.func_220846_a(blockpos, 10.0D)) {
                    Vector3d vec3d = (new Vector3d((double)blockpos.getX() - this.plagueDoctor.getPosX(), (double)blockpos.getY() - this.plagueDoctor.getPosY(), (double)blockpos.getZ() - this.plagueDoctor.getPosZ())).normalize();
                    Vector3d vec3d1 = vec3d.scale(10.0D).add(this.plagueDoctor.getPosX(), this.plagueDoctor.getPosY(), this.plagueDoctor.getPosZ());
                    EntityPlagueDoctor.this.navigator.tryMoveToXYZ(vec3d1.x, vec3d1.y, vec3d1.z, this.speed);
                } else {
                    EntityPlagueDoctor.this.navigator.tryMoveToXYZ((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), this.speed);
                }
            }

        }

        private boolean func_220846_a(BlockPos p_220846_1_, double p_220846_2_) {
            return !p_220846_1_.withinDistance(this.plagueDoctor.getPositionVec(), p_220846_2_);
        }
    }

    protected void populateTradeData() {
        VillagerTrades.ITrade[] level1 = RatsVillagerTrades.PLAGUE_DOCTOR_TRADES.get(1);
        VillagerTrades.ITrade[] level2 = RatsVillagerTrades.PLAGUE_DOCTOR_TRADES.get(2);
        if (level1 != null && level2 != null) {
            MerchantOffers merchantoffers = this.getOffers();
            this.addTrades(merchantoffers, level1, 5);
            int i = this.rand.nextInt(level2.length);
            int j = this.rand.nextInt(level2.length);
            int k = this.rand.nextInt(level2.length);
            int rolls = 0;
            while((j == i) && rolls < 100){
                j = this.rand.nextInt(level2.length);
                rolls++;
            }
            rolls = 0;
            while((k == i || k == j) && rolls < 100){
                k = this.rand.nextInt(level2.length);
                rolls++;
            }
            VillagerTrades.ITrade rareTrade1 = level2[i];
            VillagerTrades.ITrade rareTrade2 = level2[j];
            VillagerTrades.ITrade rareTrade3 = level2[k];
            MerchantOffer merchantoffer1 = rareTrade1.getOffer(this, this.rand);
            if (merchantoffer1 != null) {
                merchantoffers.add(merchantoffer1);
            }
            MerchantOffer merchantoffer2 = rareTrade2.getOffer(this, this.rand);
            if (merchantoffer2 != null) {
                merchantoffers.add(merchantoffer2);
            }
            MerchantOffer merchantoffer3 = rareTrade3.getOffer(this, this.rand);
            if (merchantoffer3 != null) {
                merchantoffers.add(merchantoffer3);
            }
            if(RatsMod.RATLANTIS_LOADED){
                MerchantOffer combiner = RatsVillagerTrades.COMBINER_TRADE.getOffer(this, this.rand);
                if (combiner != null) {
                    merchantoffers.add(combiner);
                }
            }
        }
    }



    protected ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;
        if (flag) {
            this.setWillDespawn(false);
            itemstack.func_111282_a_(player, this, hand);
            return ActionResultType.SUCCESS;
        } else if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isChild()) {
            if (hand == Hand.MAIN_HAND) {
                player.addStat(Stats.TALKED_TO_VILLAGER);
            }

            if (this.getOffers().isEmpty()) {
                return super.processInitialInteract(player, hand);
            } else {
                if (!this.world.isRemote) {
                    this.setCustomer(player);
                    this.openMerchantContainer(player, this.getDisplayName(), 1);
                }

                return ActionResultType.SUCCESS;
            }
        } else {
            return super.func_230254_b_(player, hand);
        }
    }
}