package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatlantisConfig;
import com.github.alexthe666.rats.server.entity.EntityRatMountBase;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityRatAutomatonMount extends EntityRatMountBase implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    public static final Animation ANIMATION_MELEE = Animation.create(15);
    public static final Animation ANIMATION_RANGED = Animation.create(15);
    private boolean useRangedAttack = false;

    public EntityRatAutomatonMount(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.stepHeight = 1.0F;
        this.moveController = new AIMoveControl(this);
        this.upgrade = RatlantisItemRegistry.RAT_UPGRADE_AUTOMATON_MOUNT;
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

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.field_233818_a_, 250.0D)        //HEALTH
                .createMutableAttribute(Attributes.field_233821_d_, 0.2D)                //SPEED
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)       //ATTACK
                .createMutableAttribute(Attributes.field_233819_b_, 128.0D)               //FOLLOW RANGE
                .createMutableAttribute(Attributes.field_233826_i_, 10.0D);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.useRangedAttack ? ANIMATION_MELEE : ANIMATION_RANGED);
        }
        return true;
    }

    public void livingTick() {
        super.livingTick();
        this.riderY = 2.52F;
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
                target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) RatlantisConfig.ratlanteanAutomatonAttack);
                target.func_233627_a_(1.5F, this.getPosX() - target.getPosX(), this.getPosZ() - target.getPosZ());
                this.useRangedAttack = rand.nextBoolean();
            }
        }
        if (world.isRemote && rand.nextDouble() < 0.5F) {
            float radius = -0.5F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 1.25 + getPosY();
            world.addParticle(ParticleTypes.END_ROD, extraX + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    extraY,
                    extraZ + (double) (this.rand.nextFloat() * 0.5F) - (double) 0.25F,
                    0F, -0.1F, 0F);
        }
        if (this.useRangedAttack && this.getAnimation() == ANIMATION_RANGED && this.getAnimationTick() == 6) {
            float radius = -3.4F;
            float angle = (0.01745329251F * (this.renderYawOffset)) - 160F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 2.4F + getPosY();
            double targetRelativeX = (target == null ? this.getLook(1.0F).x : target.getPosX()) - extraX;
            double targetRelativeY = (target == null ? this.getLook(1.0F).y : target.getPosY()) - extraY;
            double targetRelativeZ = (target == null ? this.getLook(1.0F).z : target.getPosZ()) - extraZ;
            EntityGolemBeam beam = new EntityGolemBeam(RatlantisEntityRegistry.RATLANTEAN_AUTOMATON_BEAM, world, this);
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
                Vector3d vec3d = new Vector3d(this.getX() - EntityRatAutomatonMount.this.getPosX(), this.getY() - EntityRatAutomatonMount.this.getPosY(), this.getZ() - EntityRatAutomatonMount.this.getPosZ());
                double d0 = vec3d.length();
                double edgeLength = EntityRatAutomatonMount.this.getBoundingBox().getAverageEdgeLength();
                if (d0 < edgeLength) {
                    this.action = MovementController.Action.WAIT;
                    EntityRatAutomatonMount.this.setMotion(EntityRatAutomatonMount.this.getMotion().scale(0.5D));
                } else {
                    EntityRatAutomatonMount.this.setMotion(EntityRatAutomatonMount.this.getMotion().add(vec3d.scale(this.speed * 0.1D / d0)));
                    if (EntityRatAutomatonMount.this.getAttackTarget() == null) {
                        Vector3d vec3d1 = EntityRatAutomatonMount.this.getMotion();
                        EntityRatAutomatonMount.this.rotationYaw = -((float)MathHelper.atan2(vec3d1.x, vec3d1.z)) * (180F / (float)Math.PI);
                        EntityRatAutomatonMount.this.renderYawOffset = EntityRatAutomatonMount.this.rotationYaw;
                    } else {
                        double d4 = EntityRatAutomatonMount.this.getAttackTarget().getPosX() - EntityRatAutomatonMount.this.getPosX();
                        double d5 = EntityRatAutomatonMount.this.getAttackTarget().getPosZ() - EntityRatAutomatonMount.this.getPosZ();
                        EntityRatAutomatonMount.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityRatAutomatonMount.this.renderYawOffset = EntityRatAutomatonMount.this.rotationYaw;
                    }
                }
            }
        }
    }
}
