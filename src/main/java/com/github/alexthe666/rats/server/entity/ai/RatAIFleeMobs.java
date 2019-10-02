package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.server.entity.EntityRat;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class RatAIFleeMobs extends EntityAIBase {
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityRat entity;
    private final double farSpeed;
    private final double nearSpeed;
    protected EntityLivingBase closestLivingEntity;
    private final float avoidDistance;
    private Path path;
    private final PathNavigate navigation;
    private final Predicate<Entity> avoidTargetSelector;

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
        this.setMutexBits(0);
    }

    public boolean shouldExecute() {
        List<EntityLivingBase> list = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().grow((double) this.avoidDistance, 3.0D, (double) this.avoidDistance), Predicates.and(EntitySelectors.CAN_AI_TARGET, this.canBeSeenSelector, this.avoidTargetSelector));
        if (list.isEmpty() || entity.isInCage()) {
            return false;
        } else {
            this.closestLivingEntity = list.get(0);
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

            if (vec3d == null) {
                return false;
            } else if (this.closestLivingEntity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.getDistanceSq(this.entity) || !shouldFlee(this.closestLivingEntity)) {
                return false;
            } else {
                this.path = this.navigation.getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                return this.path != null;
            }
        }
    }

    private boolean shouldFlee(EntityLivingBase mob){
        int trust = entity.wildTrust;
        Vec3d vec3d = mob.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(entity.posX - mob.posX, entity.getEntityBoundingBox().minY + (double)entity.getEyeHeight() - (mob.posY + (double)mob.getEyeHeight()), entity.posZ - mob.posZ);
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        if(trust < 100 || !(mob instanceof EntityPlayer)){
            return d1 > 0.5 / d0 && mob.canEntityBeSeen(entity);
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        if(this.closestLivingEntity != null && !shouldFlee(this.closestLivingEntity)){
            return false;
        }
        return !this.navigation.noPath();
    }

    public void startExecuting() {
        this.navigation.setPath(this.path, getRunSpeed());
    }

    public void resetTask() {
        this.closestLivingEntity = null;
    }

    public void updateTask() {
        this.entity.getNavigator().setSpeed(getRunSpeed());
    }

    public double getRunSpeed(){
        if(this.closestLivingEntity == null || !(this.closestLivingEntity instanceof EntityPlayer)){
            return farSpeed;
        }else{
            return 0.6D + ((double) (50 - Math.min(this.entity.wildTrust, 50)) * 0.04D);
        }
    }
}
