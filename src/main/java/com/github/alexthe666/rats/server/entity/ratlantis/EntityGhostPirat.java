package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.entity.ai.*;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityGhostPirat extends EntityRat implements IPirat, IRatlantean {

    public EntityGhostPirat(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 20.0D)        //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.35D)                //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 7.0D)       //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 32.0D);
    }

    public static boolean canSpawn(EntityType<? extends MobEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
        return canSpawnAtPos(world, pos) || reason == SpawnReason.SPAWNER;
    }

    private static boolean canSpawnAtPos(IWorld world, BlockPos pos) {
        BlockState down = world.getBlockState(pos.down());
        return BlockTags.getCollection().get(RatUtils.PIRAT_ONLY_BLOCKS).func_230235_a_(down.getBlock());
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.45D, false));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RatAIWander(this, 1.0D));
        this.goalSelector.addGoal(3, new RatAIFleeSun(this, 1.66D));
        this.goalSelector.addGoal(3, new RatAISit(this));
        this.goalSelector.addGoal(5, new RatAIEnterTrap(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false, new Predicate<LivingEntity>() {
            public boolean apply(@Nullable LivingEntity entity) {
                return !(entity instanceof IRatlantean) && entity instanceof LivingEntity && !entity.isOnSameTeam(EntityGhostPirat.this);
            }
        }));
        this.targetSelector.addGoal(2, new RatAIHurtByTarget(this));
    }

    public boolean canDigHoles() {
        return false;
    }

    protected void switchNavigator(int type) {
        this.moveController = new MovementController(this);
        this.navigator = new RatPathPathNavigateGround(this, world);
        this.navigatorType = 1;
    }

    public void livingTick() {
        super.livingTick();
        if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    public double getYOffset() {
        return 0.45D;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setMale(this.getRNG().nextBoolean());
        this.setPlague(false);
        this.setToga(false);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_CUTLASS));
        this.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(RatlantisItemRegistry.GHOST_PIRAT_HAT));
        return spawnDataIn;
    }

    public boolean canBeTamed() {
        return false;
    }

    public boolean isTamed() {
        return false;
    }

    public boolean shouldHuntAnimal() {
        return true;
    }
}
