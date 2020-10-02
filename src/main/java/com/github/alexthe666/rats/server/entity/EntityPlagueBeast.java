package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.github.alexthe666.rats.server.entity.ratlantis.EntityFeralRatlantean;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityPlagueBeast extends EntityFeralRatlantean implements IPlagueLegion {

    protected static final DataParameter<java.util.Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityPlagueBeast.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityPlagueBeast(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BlackDeathAITargetNonPlagued(this, LivingEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 40.0D)        //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.5D)                //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 5.0D)       //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 64.0D)               //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 4.0D);
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    public void tick() {
        super.tick();
        double d0 = 0D;
        double d1 = this.rand.nextGaussian() * 0.05D + 0.5D;
        double d2 = 0D;
        this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
        if (this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath death = (EntityBlackDeath) this.getOwner();
            if (death.getAttackTarget() != null && death.getAttackTarget().isAlive()) {
                this.setAttackTarget(death.getAttackTarget());
            } else {
                float radius = (float) 8 - (float) Math.sin(death.ticksExisted * 0.4D) * 0.5F;
                int maxRatStuff = 360 / Math.max(death.getBeastsSummoned(), 1);
                int ratIndex = this.getEntityId() % Math.max(death.getBeastsSummoned(), 1);
                float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + death.getPosX();
                double extraZ = (double) (radius * MathHelper.cos(angle)) + death.getPosZ();
                BlockPos runToPos = new BlockPos(extraX, death.getPosY(), extraZ);
                int steps = 0;
                while (world.getBlockState(runToPos).isOpaqueCube(world, runToPos) && steps < 10) {
                    runToPos = runToPos.up();
                    steps++;
                }
                this.getNavigator().tryMoveToXYZ(extraX, runToPos.getY(), extraZ, 1.0F);
            }
        }
        if (this.getAttackTarget() != null && !this.getAttackTarget().isAlive()) {
            this.setAttackTarget(null);
        }
    }

    public void remove() {
        if (isAlive() && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath illagerPiper = (EntityBlackDeath) this.getOwner();
            illagerPiper.setBeastsSummoned(illagerPiper.getBeastsSummoned() - 1);
        }
        super.remove();
    }

    public boolean doExtraEffect(LivingEntity target) {
        target.addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 1200, 0));
        return true;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, java.util.Optional.empty());
    }

    public boolean hasToga() {
        return false;
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwnerId() == null) {
            compound.putString("OwnerUUID", "");
        } else {
            compound.putString("OwnerUUID", this.getOwnerId().toString());
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        String s;

        if (compound.contains("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1).toString();
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
    }

    public UUID getOwnerId() {
        return (UUID) ((Optional) this.dataManager.get(OWNER_UNIQUE_ID)).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            LivingEntity player = uuid == null ? null : this.world.getPlayerByUuid(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getServer().getWorld(this.world.func_234923_W_()).getEntityByUuid(uuid);
                    if (entity instanceof LivingEntity) {
                        return (LivingEntity) entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    public boolean isOnSameTeam(Entity entityIn) {
        return super.isOnSameTeam(entityIn) || entityIn instanceof IPlagueLegion;
    }
}
