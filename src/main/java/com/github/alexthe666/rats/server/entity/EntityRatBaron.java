package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.RatAIHuntPrey;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
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

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128D);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
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
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatsItemRegistry.AVIATOR_HAT));
        this.setDropChance(EquipmentSlotType.HEAD, 0);
        if (!this.isPassenger()) {
            EntityRatBaronPlane boat = new EntityRatBaronPlane(RatsEntityRegistry.RAT_BARON_PLANE, world);
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
            return entity.isAlive() && !(entity instanceof IRatlantean);
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
