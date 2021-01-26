package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class EntityRatStriderMount extends StriderEntity {

    protected Item upgrade = Items.AIR;

    protected EntityRatStriderMount(EntityType<? extends StriderEntity> type, World worldIn) {
        super(type, worldIn);
        this.upgrade = RatsItemRegistry.RAT_UPGRADE_STRIDER_MOUNT;
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MOVEMENT_SPEED, (double)0.175F).createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        EntityRat rat = this.getRat();
        return rat != null ? rat.func_230254_b_(player, hand) : ActionResultType.PASS;
    }

    public boolean writeUnlessPassenger(CompoundNBT compound) {
        String s = this.getEntityString();
        compound.putString("id", s);
        super.writeUnlessPassenger(compound);
        return true;
    }

    public boolean canBeSteered() {
        return true;
    }

    public boolean canPassengerSteer() {
        return false;
    }

    public boolean shouldRiderFaceForward(PlayerEntity player) {
        return true;
    }

    @Override
    public Entity getControllingPassenger() {
        return getRat();
    }

    protected void onDeathUpdate() {
        Vector3d vec3d = this.getMotion();
        EntityRat rat = this.getRat();
        if (rat != null) {
            rat.mountRespawnCooldown = 1000;
        }
        this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        this.livingSoundTime = 20;
        ++this.deathTime;
        if (deathTime >= 5) {
            this.remove();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.EXPLOSION, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        return super.isOnSameTeam(entityIn) || this.getRat() != null && this.getRat().isOnSameTeam(entityIn);
    }

    public EntityRat getRat() {
        if (!this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof EntityRat) {
                    return (EntityRat) entity;
                }
            }
        }
        return null;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        EntityRat rat = getRat();
        if (rat != null) {
            return rat.isInvulnerableTo(source);
        }
        return super.isInvulnerableTo(source);
    }


    public void tick() {
        super.tick();
        EntityRat rat = this.getRat();
        if (this.getAttackTarget() != null && this.isOnSameTeam(this.getAttackTarget())) {
            this.setAttackTarget(null);
        }
        if (this.getAttackTarget() != null && this.getAttackTarget().getEntityId() == this.getEntityId()) {
            this.setAttackTarget(null);
        }
        if (this.getAttackTarget() != null && RatUtils.isRidingOrBeingRiddenBy(this.getAttackTarget(), this)) {
            this.setAttackTarget(null);
        }
        if (rat != null && rat.hasUpgrade(upgrade)) {
            if (this.getAttackTarget() != null && rat.getAttackTarget() != this.getAttackTarget()) {
                this.setAttackTarget(null);
            }
            if (rat.getAttackTarget() != null && rat.getAttackTarget() != this) {
                this.setAttackTarget(rat.getAttackTarget());
            }
            if (this.getRevengeTarget() != null && rat.getRevengeTarget() != this && !this.isOnSameTeam(this.getRevengeTarget())) {
                rat.setRevengeTarget(this.getRevengeTarget());
                rat.setAttackTarget(this.getRevengeTarget());
            }
        } else {
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
            this.remove();
        }
    }
}
