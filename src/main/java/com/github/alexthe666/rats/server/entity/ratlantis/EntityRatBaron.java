package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;

import javax.annotation.Nullable;

public class EntityRatBaron extends EntityRat {

    private final ServerBossInfo bossInfo = (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS));

    public EntityRatBaron(EntityType type, World worldIn) {
        super(type, worldIn);
    }


    protected void updateAITasks() {
        super.updateAITasks();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new AIHuntPlayers(this));
    }

    public double getYOffset() {
        return 0.45D;
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 300.0D)        //HEALTH
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)                //SPEED
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0D)       //ATTACK
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 128.0D);
    }


    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        if (this.getRidingEntity() instanceof EntityRatBaronPlane) {
            return SoundEvents.ENTITY_IRON_GOLEM_HURT;
        }
        return RatsSoundRegistry.RAT_HURT;
    }


    public void tick() {
        super.tick();
        if(world.getDifficulty() == Difficulty.PEACEFUL){
            this.remove();
        }
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

    public boolean canBeTamed() {
        return false;
    }


    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatlantisItemRegistry.AVIATOR_HAT));
        this.setDropChance(EquipmentSlotType.HEAD, 0);
        if (!this.isPassenger()) {
            EntityRatBaronPlane boat = new EntityRatBaronPlane(RatlantisEntityRegistry.RAT_BARON_PLANE, world);
            boat.copyLocationAndAnglesFrom(this);
            if (!world.isRemote) {
                world.addEntity(boat);
            }
            this.startRiding(boat, true);
        }
        return spawnDataIn;
    }

    private static final Predicate<LivingEntity> NOT_RATLANTEAN = new Predicate<LivingEntity>() {
        public boolean apply(@Nullable LivingEntity entity) {
            return entity.isAlive() && !(entity instanceof IRatlantean) && ((entity instanceof PlayerEntity) && !((PlayerEntity) entity).isCreative());
        }
    };

    private class AIHuntPlayers extends NearestAttackableTargetGoal<LivingEntity> {
        private final EntityRat rat;

         public AIHuntPlayers(EntityRat entityIn) {
            super(entityIn, LivingEntity.class, 10, true, false, NOT_RATLANTEAN);
            this.rat = entityIn;
        }
        protected AxisAlignedBB getTargetableArea(double targetDistance) {
            return this.goalOwner.getBoundingBox().grow(targetDistance, 128.0D, targetDistance);
        }
    }
}
