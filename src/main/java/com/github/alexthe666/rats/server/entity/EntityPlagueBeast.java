package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.ai.BlackDeathAITargetNonPlagued;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityPlagueBeast extends EntityFeralRatlantean implements IPlagueLegion {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "plague_beast"));
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityPlagueBeast.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityPlagueBeast(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new EntityAISwimming(this));
        this.goalSelector.addGoal(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.goalSelector.addGoal(6, new EntityAIWatchClosest(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(7, new EntityAILookIdle(this));
        this.targetSelector.addGoal(1, new EntityAIHurtByTarget(this, false));
        this.targetSelector.addGoal(2, new BlackDeathAITargetNonPlagued(this, LivingEntity.class, false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }


    public boolean isPotionApplicable(PotionEffect potioneffectIn) {
        if (potioneffectIn.getPotion() == RatsMod.PLAGUE_POTION) {
            return false;
        }
        return super.isPotionApplicable(potioneffectIn);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        double d0 = 0D;
        double d1 = this.rand.nextGaussian() * 0.05D + 0.5D;
        double d2 = 0D;
        this.world.addParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
        if (this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath death = (EntityBlackDeath) this.getOwner();
            if (death.getAttackTarget() != null && !death.getAttackTarget().isDead) {
                this.setAttackTarget(death.getAttackTarget());
            } else {
                float radius = (float) 8 - (float) Math.sin(death.ticksExisted * 0.4D) * 0.5F;
                int maxRatStuff = 360 / Math.max(death.getBeastsSummoned(), 1);
                int ratIndex = this.getEntityId() % Math.max(death.getBeastsSummoned(), 1);
                float angle = (0.01745329251F * (ratIndex * maxRatStuff + ticksExisted * 4.1F));
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + death.posX;
                double extraZ = (double) (radius * MathHelper.cos(angle)) + death.posZ;
                BlockPos runToPos = new BlockPos(extraX, death.posY, extraZ);
                int steps = 0;
                while (world.getBlockState(runToPos).isOpaqueCube() && steps < 10) {
                    runToPos = runToPos.up();
                    steps++;
                }
                this.getNavigator().tryMoveToXYZ(extraX, runToPos.getY(), extraZ, 1.0F);
            }
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().isDead) {
            this.setAttackTarget(null);
        }
    }

    public void setDead() {
        if (!isDead && this.getOwnerId() != null && this.getOwner() != null && this.getOwner() instanceof EntityBlackDeath) {
            EntityBlackDeath illagerPiper = (EntityBlackDeath) this.getOwner();
            illagerPiper.setBeastsSummoned(illagerPiper.getBeastsSummoned() - 1);
        }
        this.isDead = true;
    }

    public boolean doExtraEffect(LivingEntity target) {
        target.addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 1200, 0));
        return true;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
    }

    public boolean hasToga() {
        return false;
    }

    public void writeEntityToNBT(CompoundNBT compound) {
        super.writeEntityToNBT(compound);
        if (this.getOwnerId() == null) {
            compound.setString("OwnerUUID", "");
        } else {
            compound.setString("OwnerUUID", this.getOwnerId().toString());
        }
    }

    public void readEntityFromNBT(CompoundNBT compound) {
        super.readEntityFromNBT(compound);
        String s;

        if (compound.hasKey("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }

        if (!s.isEmpty()) {
            try {
                this.setOwnerId(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
    }

    public UUID getOwnerId() {
        return (UUID) ((Optional) this.dataManager.get(OWNER_UNIQUE_ID)).orNull();
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
    }

    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            LivingEntity player = uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
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

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
}
