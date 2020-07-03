package com.github.alexthe666.rats.server.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityRatMountBase extends MobEntity {

    protected double riderY;
    protected double riderXZ;
    protected boolean mechanical = true;
    protected Item upgrade = Items.AIR;

    protected EntityRatMountBase(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new MoveHelperController(this);
        riderY = 1.1;
        riderXZ = 0;
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

    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            float radius = (float) riderXZ;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            double extraY = riderY;
            passenger.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
            if (passenger instanceof LivingEntity) {
                ((LivingEntity) passenger).renderYawOffset = this.renderYawOffset;
            }
        }


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
        if (this.getAttackTarget() != null && this.getAttackTarget().isRidingOrBeingRiddenBy(this)) {
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
                this.world.addParticle(mechanical ? ParticleTypes.EXPLOSION : ParticleTypes.EXPLOSION, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == null) {
            return false;
        }
        return super.isOnSameTeam(entityIn) || this.getRat() != null && this.getRat().isOnSameTeam(entityIn);
    }

    static class MoveHelperController extends MovementController {
        private final EntityRatMountBase mount;

        public MoveHelperController(EntityRatMountBase mount) {
            super(mount);
            this.mount = mount;
        }

        public void tick() {
            if (this.action == MovementController.Action.STRAFE) {
                float f = (float) this.mob.getAttribute(Attributes.field_233821_d_).getValue();
                float f1 = (float) this.speed * f;
                float f2 = this.moveForward;
                float f3 = this.moveStrafe;
                float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                f4 = f1 / f4;
                f2 = f2 * f4;
                f3 = f3 * f4;
                float f5 = MathHelper.sin(this.mob.rotationYaw * ((float) Math.PI / 180F));
                float f6 = MathHelper.cos(this.mob.rotationYaw * ((float) Math.PI / 180F));
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                PathNavigator pathnavigator = this.mob.getNavigator();
                if (pathnavigator != null) {
                    NodeProcessor nodeprocessor = pathnavigator.getNodeProcessor();
                    if (nodeprocessor != null && nodeprocessor.getPathNodeType(this.mob.world, MathHelper.floor(this.mob.getPosX() + (double) f7), MathHelper.floor(this.mob.getPosY()), MathHelper.floor(this.mob.getPosZ() + (double) f8)) != PathNodeType.WALKABLE) {
                        this.moveForward = 1.0F;
                        this.moveStrafe = 0.0F;
                        f1 = f;
                    }
                }

                this.mob.setAIMoveSpeed(f1);
                this.mob.setMoveForward(this.moveForward);
                this.mob.setMoveStrafing(this.moveStrafe);
                this.action = MovementController.Action.WAIT;
            } else if (this.action == MovementController.Action.MOVE_TO) {
                this.action = MovementController.Action.WAIT;
                double d0 = this.posX - this.mob.getPosX();
                double d1 = this.posZ - this.mob.getPosZ();
                double d2 = this.posY - this.mob.getPosY();
                double d3 = d0 * d0 + d2 * d2 + d1 * d1;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setMoveForward(0.0F);
                    return;
                }

                float f9 = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f9, 90.0F);
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.field_233821_d_).getValue()));
                BlockPos blockpos = new BlockPos(this.mob.getPositionVec());
                BlockState blockstate = this.mob.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.world, blockpos);
                if (d2 > (double) this.mob.stepHeight && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.mob.getWidth()) || !voxelshape.isEmpty() && this.mob.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double) blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES)) {
                    this.mob.getJumpController().setJumping();
                    this.action = MovementController.Action.JUMPING;
                }
            } else if (this.action == MovementController.Action.JUMPING) {
                this.mob.setAIMoveSpeed((float) (this.speed * this.mob.getAttribute(Attributes.field_233821_d_).getValue()));
                if (this.mob.func_233570_aj_()) {
                    this.action = MovementController.Action.WAIT;
                }
            } else {
                this.mob.setMoveForward(0.0F);
            }
        }
    }
}
