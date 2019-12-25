package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    public void livingTick() {
        super.livingTick();

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

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() != RatsMod.PLAGUE_POTION && super.isPotionApplicable(potioneffectIn);
    }

    protected boolean canDespawn() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return this.func_213716_dX() ? SoundEvents.ENTITY_VILLAGER_TRADE : SoundEvents.ENTITY_VILLAGER_AMBIENT;
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
        double d0 = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX + target.getMotion().x - this.posX;
        double d2 = d0 - this.posY;
        double d3 = target.posZ + target.getMotion().z - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
        EntityPurifyingLiquid entitypotion = new EntityPurifyingLiquid(RatsEntityRegistry.PURIFYING_LIQUID, this.world, this, new ItemStack(RatsItemRegistry.PURIFYING_LIQUID));
        entitypotion.rotationPitch -= -20.0F;
        entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
        this.world.addEntity(entitypotion);
    }

    public void onStruckByLightning(LightningBoltEntity lightningBolt) {
        if (!this.world.isRemote && this.isAlive()) {
            EntityBlackDeath entitywitch = new EntityBlackDeath(RatsEntityRegistry.BLACK_DEATH, this.world);
            entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitywitch.onInitialSpawn(world, this.world.getDifficultyForLocation(new BlockPos(entitywitch)), SpawnReason.NATURAL, null, null);
            entitywitch.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitywitch.setCustomName(this.getCustomName());
            }
            this.world.addEntity(entitywitch);
            this.remove();
        }
    }

    @Override
    protected void func_213713_b(MerchantOffer p_213713_1_) {

    }

    @Override
    protected void populateTradeData() {

    }

    public void func_213726_g(@Nullable BlockPos p_213726_1_) {
        this.wanderTarget = p_213726_1_;
    }

    @Nullable
    private BlockPos func_213727_eh() {
        return this.wanderTarget;
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
            this.plagueDoctor.func_213726_g((BlockPos)null);
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
                    Vec3d vec3d = (new Vec3d((double)blockpos.getX() - this.plagueDoctor.posX, (double)blockpos.getY() - this.plagueDoctor.posY, (double)blockpos.getZ() - this.plagueDoctor.posZ)).normalize();
                    Vec3d vec3d1 = vec3d.scale(10.0D).add(this.plagueDoctor.posX, this.plagueDoctor.posY, this.plagueDoctor.posZ);
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
}