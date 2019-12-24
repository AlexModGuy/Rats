package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.Goal;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class RatAIFleeMobs extends Goal {
    private final Predicate<Entity> canBeSeenSelector;
    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    private final PathNavigate navigation;
    private final Predicate<Entity> avoidTargetSelector;
    protected EntityRat entity;
    protected LivingEntity closestLivingEntity;
    private Path path;

    public RatAIFleeMobs(EntityRat entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(entityIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public RatAIFleeMobs(EntityRat entityIn, Predicate<Entity> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.canBeSeenSelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && RatAIFleeMobs.this.entity.getEntitySenses().canSee(p_apply_1_) && !RatAIFleeMobs.this.entity.isOnSameTeam(p_apply_1_);
            }
        };
        this.entity = entityIn;
        this.avoidTargetSelector = avoidTargetSelectorIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.navigation = entityIn.getNavigator();
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.entity.isTamed() || this.entity.hasPlague()) {
            return false;
        }
        List<EntityOcelot> ocelotList = this.entity.world.getEntitiesWithinAABB(EntityOcelot.class, this.entity.getEntityBoundingBox().grow((double) avoidDistance, 8.0D, (double) avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));
        List<PlayerEntity> playerList = this.entity.world.getEntitiesWithinAABB(PlayerEntity.class, this.entity.getEntityBoundingBox().grow((double) avoidDistance, 8.0D, (double) avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));
        if (ocelotList.isEmpty() && playerList.isEmpty()) {
            return false;
        } else {
            if (!ocelotList.isEmpty()) {
                this.closestLivingEntity = ocelotList.get(0);
            } else {
                this.closestLivingEntity = playerList.get(0);
            }
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
            if (vec3d == null) {
                return false;
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)) {
                return false;
            } else {
                entity.isFleeing = true;
                this.path = entity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        }
    }

    private boolean shouldFlee(LivingEntity mob) {
        int trust = entity.wildTrust;
        Vec3d vec3d = mob.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(entity.posX - mob.posX, entity.getEntityBoundingBox().minY + (double) entity.getEyeHeight() - (mob.posY + (double) mob.getEyeHeight()), entity.posZ - mob.posZ);
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        if (trust < 100 || !(mob instanceof PlayerEntity)) {
            return d1 > 0.5 / d0 && mob.canEntityBeSeen(entity);
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        return !entity.getNavigator().noPath();
    }

    public void startExecuting() {
        entity.getNavigator().setPath(this.path, farSpeed);
    }

    public void resetTask() {
        entity.isFleeing = false;
        this.closestLivingEntity = null;
    }

    public void tick() {
        this.entity.getNavigator().setSpeed(getRunSpeed());
    }

    public double getRunSpeed() {
        if (this.closestLivingEntity == null || !(this.closestLivingEntity instanceof PlayerEntity)) {
            if (this.entity.getDistanceSq(this.closestLivingEntity) < 49.0D) {
                return nearSpeed;

            }
            return farSpeed;
        } else {
            return 0.6D + ((double) (50 - Math.min(this.entity.wildTrust, 50)) * 0.04D);
        }
    }
}
