package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityRatAutomatonMount extends EntityRatMountBase implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    public static final Animation ANIMATION_MELEE = Animation.create(15);
    public static final Animation ANIMATION_RANGED = Animation.create(15);
    private boolean useRangedAttack = false;

    protected EntityRatAutomatonMount(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
        this.moveController = new MoveHelperController(this);
        this.upgrade = RatsItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT;
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
        return new Animation[]{ANIMATION_MELEE, ANIMATION_RANGED};
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.useRangedAttack ? ANIMATION_MELEE : ANIMATION_RANGED);
        }
        return true;
    }

    public void livingTick() {
        super.livingTick();
        this.riderY = 3.22F;
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            this.useRangedAttack = this.getDistance(target) > 10;
        }
        if (this.useRangedAttack && this.getAnimation() != ANIMATION_RANGED && target != null && this.canEntityBeSeen(target) && target.isAlive()) {
            this.setAnimation(ANIMATION_RANGED);
            this.faceEntity(target, 360, 80);

        }
        if (!this.useRangedAttack && target != null && this.getDistance(target) < 7 && this.canEntityBeSeen(target) && target.isAlive()) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_MELEE);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 0.5F + rand.nextFloat() * 0.5F);
            }
            this.faceEntity(target, 360, 80);
            if (this.getAnimation() == ANIMATION_MELEE && this.getAnimationTick() == 10) {
                target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) RatConfig.ratlanteanAutomatonAttack);
                target.knockBack(target, 1.5F, this.posX - target.posX, this.posZ - target.posZ);
                this.useRangedAttack = rand.nextBoolean();
            }
        }
        if (world.isRemote && rand.nextDouble() < 0.5F) {
            float radius = -0.5F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 1.25 + posY;
            world.addParticle(ParticleTypes.END_ROD, extraX + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    0F, -0.1F, 0F);
        }
        if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
            float radius = -3.4F;
            float angle = (0.01745329251F * (this.renderYawOffset)) - 160F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 2.4F + posY;
            double targetRelativeX = (target == null ? this.getLook(1.0F).x : target.posX) - extraX;
            double targetRelativeY = (target == null ? this.getLook(1.0F).y : target.posY) - extraY;
            double targetRelativeZ = (target == null ? this.getLook(1.0F).z : target.posZ) - extraZ;
            EntityGolemBeam beam = new EntityGolemBeam(RatsEntityRegistry.RATLANTEAN_AUTOMATON_BEAM, world, this);
            beam.setPosition(extraX, extraY, extraZ);
            beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.1F);
            this.playSound(RatsSoundRegistry.LASER, 1.0F, 0.75F + rand.nextFloat() * 0.5F);
            if (!world.isRemote) {
                world.addEntity(beam);
            }
            this.useRangedAttack = rand.nextBoolean();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    class AIMoveControl extends MovementController {
        public AIMoveControl(EntityRatAutomatonMount vex) {
            super(vex);
        }

        public void tick() {
            if (this.action == MovementController.Action.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.posX - EntityRatAutomatonMount.this.posX, this.posY - EntityRatAutomatonMount.this.posY, this.posZ - EntityRatAutomatonMount.this.posZ);
                double d0 = vec3d.length();
                double edgeLength = EntityRatAutomatonMount.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityRatAutomatonMount.this.setMotion(EntityRatAutomatonMount.this.getMotion().scale(0.5D));
                } else {
                    EntityRatAutomatonMount.this.setMotion(EntityRatAutomatonMount.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityRatAutomatonMount.this.getAttackTarget() == null) {
                        Vec3d vec3d1 = EntityRatAutomatonMount.this.getMotion();
                        EntityRatAutomatonMount.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityRatAutomatonMount.this.renderYawOffset = EntityRatAutomatonMount.this.rotationYaw;
                    } else {
                        double d4 = EntityRatAutomatonMount.this.getAttackTarget().posX - EntityRatAutomatonMount.this.posX;
                        double d5 = EntityRatAutomatonMount.this.getAttackTarget().posZ - EntityRatAutomatonMount.this.posZ;
                        EntityRatAutomatonMount.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRatAutomatonMount.this.renderYawOffset = EntityRatAutomatonMount.this.rotationYaw;
                    }
                }
            }
        }
    }
}
