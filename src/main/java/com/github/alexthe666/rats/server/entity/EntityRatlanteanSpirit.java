package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityRatlanteanSpirit extends EntityMob implements IAnimatedEntity, IRatlantean {

    public static final Animation ANIMATION_ATTACK = Animation.create(10);
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("rats", "ratlantean_soul"));
    private int animationTick;
    private Animation currentAnimation;

    public EntityRatlanteanSpirit(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.85F);
        this.moveHelper = new EntityRatlanteanSpirit.AIMoveControl(this);
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityRatlanteanSpirit.AIFireballAttack(this));
        this.tasks.addTask(8, new EntityRatlanteanSpirit.AIMoveRandom());
        this.tasks.addTask(9, new EntityAIWatchClosest(this, PlayerEntity.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityRatlanteanSpirit.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, PlayerEntity.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityGolem.class, false));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    public void move(MoverType type, double x, double y, double z) {
        super.move(type, x, y, z);
        this.doBlockCollisions();
    }

    public void onUpdate() {
        this.noClip = true;
        super.onUpdate();
        this.noClip = false;
        this.setNoGravity(true);
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (world.isRemote) {
            RatsMod.PROXY.spawnParticle("rat_ghost", this.posX + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    this.posY + (double) (this.rand.nextFloat() * this.height),
                    this.posZ + (double) (this.rand.nextFloat() * this.width * 2F) - (double) this.width,
                    0.92F, 0.82, 0.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_ATTACK};
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    protected SoundEvent getAmbientSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return RatsSoundRegistry.RATLANTEAN_SPIRIT_DIE;
    }

    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityRatlanteanSpirit vex) {
            super(vex);
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityRatlanteanSpirit.this.posX;
                double d1 = this.posY - EntityRatlanteanSpirit.this.posY;
                double d2 = this.posZ - EntityRatlanteanSpirit.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityRatlanteanSpirit.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityRatlanteanSpirit.this.motionX *= 0.5D;
                    EntityRatlanteanSpirit.this.motionY *= 0.5D;
                    EntityRatlanteanSpirit.this.motionZ *= 0.5D;
                } else {
                    EntityRatlanteanSpirit.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityRatlanteanSpirit.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityRatlanteanSpirit.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityRatlanteanSpirit.this.getAttackTarget() == null) {
                        EntityRatlanteanSpirit.this.rotationYaw = -((float) MathHelper.atan2(EntityRatlanteanSpirit.this.motionX, EntityRatlanteanSpirit.this.motionZ)) * (180F / (float) Math.PI);
                        EntityRatlanteanSpirit.this.renderYawOffset = EntityRatlanteanSpirit.this.rotationYaw;
                    } else {
                        double d4 = EntityRatlanteanSpirit.this.getAttackTarget().posX - EntityRatlanteanSpirit.this.posX;
                        double d5 = EntityRatlanteanSpirit.this.getAttackTarget().posZ - EntityRatlanteanSpirit.this.posZ;
                        EntityRatlanteanSpirit.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRatlanteanSpirit.this.renderYawOffset = EntityRatlanteanSpirit.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase {

        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityRatlanteanSpirit.this.getMoveHelper().isUpdating() && EntityRatlanteanSpirit.this.rand.nextInt(2) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = new BlockPos(EntityRatlanteanSpirit.this);

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.add(EntityRatlanteanSpirit.this.rand.nextInt(15) - 7, EntityRatlanteanSpirit.this.rand.nextInt(11) - 5, EntityRatlanteanSpirit.this.rand.nextInt(15) - 7);

                if (EntityRatlanteanSpirit.this.world.isAirBlock(blockpos1)) {
                    EntityRatlanteanSpirit.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityRatlanteanSpirit.this.getAttackTarget() == null) {
                        EntityRatlanteanSpirit.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }

                    break;
                }
            }
        }
    }

    class AIFireballAttack extends EntityAIBase {
        private final EntityRatlanteanSpirit parentEntity;
        public int attackTimer;

        public AIFireballAttack(EntityRatlanteanSpirit ghast) {
            this.parentEntity = ghast;
        }

        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }

        public void startExecuting() {
            this.attackTimer = 0;
        }

        public void resetTask() {
        }

        public void updateTask() {
            EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            double d0 = 64.0D;
            if (entitylivingbase.getDistanceSq(this.parentEntity) >= 4096.0D || !this.parentEntity.canEntityBeSeen(entitylivingbase)) {

                EntityRatlanteanSpirit.this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);

            }
            if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D) {
                World world = this.parentEntity.world;
                ++this.attackTimer;

                if (this.attackTimer == 20) {
                    double d1 = 4.0D;
                    double d2 = entitylivingbase.posX - (this.parentEntity.posX);
                    double d3 = entitylivingbase.posY + (double) (entitylivingbase.height) - (this.parentEntity.posY + (double) (this.parentEntity.height / 2.0F));
                    double d4 = entitylivingbase.posZ - (this.parentEntity.posZ);
                    world.playEvent(null, 1016, new BlockPos(this.parentEntity), 0);
                    EntityRatlanteanFlame entitylargefireball = new EntityRatlanteanFlame(world, this.parentEntity, d2, d3, d4);
                    entitylargefireball.posX = this.parentEntity.posX;
                    entitylargefireball.posY = this.parentEntity.posY + (double) (this.parentEntity.height / 2.0F);
                    entitylargefireball.posZ = this.parentEntity.posZ;
                    world.addEntity(entitylargefireball);
                    this.attackTimer = -10;
                }
            } else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
        }
    }

}
