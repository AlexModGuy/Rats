package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class RatAIFleeMobs extends Goal {
    private final Predicate<Entity> canBeSeenSelector;
    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    private final PathNavigator navigation;
    private final Predicate<Entity> avoidTargetSelector;
    protected EntityRat entity;
    protected LivingEntity closestLivingEntity;
    private Vector3d runToPos;

    public RatAIFleeMobs(EntityRat entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this(entityIn, Predicates.alwaysTrue(), avoidDistanceIn, farSpeedIn, nearSpeedIn);
    }

    public RatAIFleeMobs(EntityRat entityIn, Predicate<Entity> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.canBeSeenSelector = new Predicate<Entity>() {
            public boolean apply(@Nullable Entity p_apply_1_) {
                return p_apply_1_.isAlive() && RatAIFleeMobs.this.entity.getEntitySenses().canSee(p_apply_1_) && !RatAIFleeMobs.this.entity.isOnSameTeam(p_apply_1_);
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
        List<OcelotEntity> ocelotList = this.entity.world.getEntitiesWithinAABB(OcelotEntity.class, this.entity.getBoundingBox().grow((double) avoidDistance, 8.0D, (double) avoidDistance), Predicates.and(this.canBeSeenSelector, this.avoidTargetSelector));
        List<CatEntity> catList = this.entity.world.getEntitiesWithinAABB(CatEntity.class, this.entity.getBoundingBox().grow((double) avoidDistance, 8.0D, (double) avoidDistance), Predicates.and(this.canBeSeenSelector, this.avoidTargetSelector));
        List<PlayerEntity> playerList = this.entity.world.getEntitiesWithinAABB(PlayerEntity.class, this.entity.getBoundingBox().grow((double) avoidDistance, 8.0D, (double) avoidDistance), Predicates.and(this.canBeSeenSelector, this.avoidTargetSelector));
        if (ocelotList.isEmpty() && playerList.isEmpty() && catList.isEmpty()) {
            return false;
        } else {
            if (!ocelotList.isEmpty()) {
                this.closestLivingEntity = ocelotList.get(0);
            } else if (!catList.isEmpty()) {
                this.closestLivingEntity = catList.get(0);
            } else{
                this.closestLivingEntity = playerList.get(0);
            }
            Vector3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vector3d(this.closestLivingEntity.getPosX(), this.closestLivingEntity.getPosY(), this.closestLivingEntity.getPosZ()));
            if (vec3d == null) {
                return false;
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity)) {
                return false;
            } else {
                entity.isFleeing = true;
                runToPos = vec3d;
                return true;
            }
        }
    }

    private boolean shouldFlee(LivingEntity mob) {
        int trust = entity.wildTrust;
        Vector3d vec3d = mob.getLook(1.0F).normalize();
        Vector3d vec3d1 = new Vector3d(entity.getPosX() - mob.getPosX(), entity.getBoundingBox().minY + (double) entity.getEyeHeight() - (mob.getPosY() + (double) mob.getEyeHeight()), entity.getPosZ() - mob.getPosZ());
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        if (trust < 100 || !(mob instanceof PlayerEntity)) {
            return d1 > 0.5 / d0 && mob.canEntityBeSeen(entity);
        }
        return false;
    }

    public boolean shouldContinueExecuting() {

        if (this.entity.getDistanceSq(runToPos.getX(), runToPos.getY(), runToPos.getZ()) <= 4) {
            return false;
        }
        return true;
    }

    public void startExecuting() {
        if(runToPos != null){
            double speed = closestLivingEntity instanceof PlayerEntity ? getRunSpeed() : nearSpeed;
            entity.getNavigator().tryMoveToXYZ(runToPos.x, runToPos.y, runToPos.z, speed);
        }
    }

    public void resetTask() {
        entity.isFleeing = false;
        this.closestLivingEntity = null;
        this.runToPos = null;
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
