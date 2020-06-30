package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class EntityRatBiplaneMount extends EntityRatMountBase {

    public float prevPlanePitch;
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityRatBiplaneMount.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> PLANE_PITCH = EntityDataManager.createKey(EntityRatBiplaneMount.class, DataSerializers.FLOAT);
    @OnlyIn(Dist.CLIENT)
    public PlaneBuffer roll_buffer;
    @OnlyIn(Dist.CLIENT)
    public PlaneBuffer pitch_buffer;
    @Nullable
    private Vector3d flightTarget;
    private Vector3d startAttackVec;
    private Vector3d startPreyVec;
    private boolean hasStartedToScorch = false;
    private LivingEntity prevAttackTarget = null;
    private BlockPos escortPosition = null;
    private int soundLoopCounter = 0;

    protected EntityRatBiplaneMount(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.riderY = 1.35F;
        this.riderXZ = -0.35F;
        this.stepHeight = 1.0F;
        this.upgrade = RatsItemRegistry.RAT_UPGRADE_BIPLANE_MOUNT;
        this.moveController = new FlightMoveHelper(this);
        if (world.isRemote) {
            roll_buffer = new PlaneBuffer();
            pitch_buffer = new PlaneBuffer();
        }
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        float radius = (float)0.35F;
        float angle = (0.01745329251F * this.renderYawOffset);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double extraY = 1.35F;
        passenger.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).renderYawOffset = this.renderYawOffset;
            ((LivingEntity) passenger).rotationYaw = this.renderYawOffset;
            ((LivingEntity) passenger).rotationYawHead = this.renderYawOffset;
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIRING, Boolean.valueOf(false));
        this.dataManager.register(PLANE_PITCH, 0F);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    public void fall(float distance, float damageMultiplier) {

    }

    public boolean canDespawn(double p_213397_1_) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {

    }

    public float getPlanePitch() {
        return dataManager.get(PLANE_PITCH).floatValue();
    }

    public void setPlanePitch(float pitch) {
        dataManager.set(PLANE_PITCH, pitch);
    }

    public void incrementPlanePitch(float pitch) {
        dataManager.set(PLANE_PITCH, getPlanePitch() + pitch);
    }

    public void decrementPlanePitch(float pitch) {
        dataManager.set(PLANE_PITCH, getPlanePitch() - pitch);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
    }

    public void tick() {
        super.tick();
        if(soundLoopCounter == 0){
            this.playSound(RatsSoundRegistry.BIPLANE_LOOP, 10, 1);
        }
        soundLoopCounter++;
        if(soundLoopCounter > 90){
            soundLoopCounter = 0;
        }

        this.prevPlanePitch = this.getPlanePitch();
        if (!this.isBeingRidden() && !world.isRemote) {
            this.attackEntityFrom(DamageSource.DROWN, 1000);
        }
        if (!this.onGround && this.getMotion().y < 0.0D) {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }
        boolean flag = false;
        EntityRat rat = this.getRat();
        double up = 0.08D;
        if(rat != null && !rat.canMove()){
            up = 0;
        }
        this.setMotion(this.getMotion().x, this.getMotion().y + up, this.getMotion().z);

        if (this.getAttackTarget() != null && startPreyVec != null && startAttackVec != null) {
            float distX = (float) (startPreyVec.x - startAttackVec.x);
            float distY = 1.5F;
            float distZ = (float) (startPreyVec.z - startAttackVec.z);
            flightTarget = new Vector3d(this.getAttackTarget().getPosX() + distX, this.getAttackTarget().getPosY() + distY, this.getAttackTarget().getPosZ() + distZ);
            hasStartedToScorch = true;
            if (flightTarget != null && this.getDistanceSq(flightTarget.x, flightTarget.y, flightTarget.z) < 100) {
                flightTarget = new Vector3d(this.getAttackTarget().getPosX() - distX, this.getAttackTarget().getPosY() + distY, this.getAttackTarget().getPosZ() - distZ);
            }
        }
        if (this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())){
            Entity target = this.getAttackTarget();
            if(this.ticksExisted % 2 == 0){
                for(int i = 0; i < 2; i++) {
                    boolean left = i == 0;
                    float radius = (float) 1.15F;
                    float angle = (0.01745329251F * this.renderYawOffset + (left ? 75 : -75));
                    double extraX = this.getPosX() + radius * MathHelper.sin((float) (Math.PI + angle));
                    double extraZ = this.getPosZ() + radius * MathHelper.cos(angle);
                    double extraY = this.getPosY() +  1.35F;
                    double d0 = target.getPosY() + (double) target.getEyeHeight() / 2;
                    double d1 = target.getPosX() - extraX;
                    double d3 = target.getPosZ() - extraZ;
                    double d2 = d0 - extraY;
                    float velocity = 3.2F;
                    EntityRattlingGunBullet cannonball = new EntityRattlingGunBullet(RatsEntityRegistry.RATTLING_GUN_BULLET, world, this);
                    cannonball.setPosition(extraX, extraY, extraZ);
                    cannonball.setDamage(0.5F);
                    cannonball.shoot(d1, d2, d3, velocity, 1.4F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0F, 2.3F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
                    if (!world.isRemote) {
                        this.world.addEntity(cannonball);
                    }
                }
            }
            this.setFiring(true);
        }

        if(this.flightTarget == null || this.getDistanceSq(flightTarget.x, flightTarget.y, flightTarget.z) < 20 || rat != null && !rat.canMove()){
            escortPosition = world.getHeight(Heightmap.Type.WORLD_SURFACE, this.getPosition()).up(20 + rand.nextInt(10));
            flightTarget = null;
        }
        if (world.isRemote) {
            if (!onGround) {
                roll_buffer.calculateChainFlapBuffer(40, 20, 0.5F, 0.5F, this);
                pitch_buffer.calculateChainWaveBuffer(40, 10, 0.5F, 0.5F, this);
            }
        }
        if(!onGround){
            double ydist = this.prevPosY - this.getPosY();//down 0.4 up -0.38
            float planeDist = (float) ((Math.abs(this.getMotion().getX()) + Math.abs(this.getMotion().getZ())) * 6F);
            this.incrementPlanePitch((float) (ydist) * 10);

            this.setPlanePitch(MathHelper.clamp(this.getPlanePitch(), -60, 40));
            float plateau = 2;
            if (this.getPlanePitch() > plateau) {
                //down
                //this.motionY -= 0.2D;
                this.decrementPlanePitch(planeDist * Math.abs(this.getPlanePitch()) / 90);
            }
            if (this.getPlanePitch() < -plateau) {//-2
                //up
                this.incrementPlanePitch(planeDist * Math.abs(this.getPlanePitch()) / 90);
            }
            if (this.getPlanePitch() > 2F) {
                this.decrementPlanePitch(1);
            } else if (this.getPlanePitch() < -2F) {
                this.incrementPlanePitch(1);
            }
        } else {
            this.setPlanePitch(0);
        }
    }


    public Vector3d getBlockInViewEscort() {
        float radius = 12;
        float neg = this.getRNG().nextBoolean() ? 1 : -1;
        float renderYawOffset = this.renderYawOffset;
        BlockPos escortPos = this.getEscortPosition();
        BlockPos ground = this.world.getHeight(Heightmap.Type.WORLD_SURFACE, escortPos);
        int distFromGround = escortPos.getY() - ground.getY();
        int fromHome = getRat() != null && getRat().getCommand() == RatCommand.FOLLOW ? 14 : 30;
        for (int i = 0; i < 10; i++) {
            BlockPos pos = new BlockPos(escortPos.getX() + this.getRNG().nextInt(fromHome) - fromHome / 2,
                    (distFromGround > 16 ? escortPos.getY() : escortPos.getY() + 5 + this.getRNG().nextInt(26)),
                    (escortPos.getZ() + this.getRNG().nextInt(fromHome) - fromHome / 2));
            if (canBlockPosBeSeen(pos) && this.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) > 6) {
                return new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            }
        }
        return null;
    }


    public boolean isFiring() {
        return this.dataManager.get(FIRING).booleanValue();
    }

    public void setFiring(boolean male) {
        this.dataManager.set(FIRING, Boolean.valueOf(male));
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vector3d vec3d = new Vector3d(this.getPosX(), this.getPosYEye(), this.getPosZ());
        Vector3d vec3d1 = new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        return this.world.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)).getType() == RayTraceResult.Type.MISS;
    }

    private BlockPos getEscortPosition() {
        return escortPosition;
    }



    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected static class FlightMoveHelper extends MovementController {

        private EntityRatBiplaneMount plane;

        protected FlightMoveHelper(EntityRatBiplaneMount planeBase) {
            super(planeBase);
            this.plane = planeBase;
        }

        public static float approach(float number, float max, float min) {
            min = Math.abs(min);
            return number < max ? MathHelper.clamp(number + min, number, max) : MathHelper.clamp(number - min, max, number);
        }

        public static float approachDegrees(float number, float max, float min) {
            float add = MathHelper.wrapDegrees(max - number);
            return approach(number, number + add, min);
        }

        public static float degreesDifferenceAbs(float f1, float f2) {
            return Math.abs(MathHelper.wrapDegrees(f2 - f1));
        }

        public void tick() {
            if (plane.collidedHorizontally) {
                plane.rotationYaw += 180.0F;
                this.speed = 0.1F;
                plane.flightTarget = null;
                return;
            }
            if(plane.getRat() != null){
                if(!plane.getRat().canMove()){
                    return;
                }
            }
            if(plane.flightTarget == null && this.isUpdating()){
                plane.flightTarget = new Vector3d(this.getX(), this.getY(), this.getZ());
            }
            if(plane.flightTarget != null) {
                float distX = (float) (plane.flightTarget.x - plane.getPosX());
                float distY = (float) (plane.flightTarget.y - plane.getPosY());
                float distZ = (float) (plane.flightTarget.z - plane.getPosZ());
                double planeDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
                double yDistMod = 1.0D - (double) MathHelper.abs(distY * 0.7F) / planeDist;
                distX = (float) ((double) distX * yDistMod);
                distZ = (float) ((double) distZ * yDistMod);
                planeDist = (double) MathHelper.sqrt(distX * distX + distZ * distZ);
                double dist = (double) MathHelper.sqrt(distX * distX + distZ * distZ + distY * distY);
                if (dist > 1.0F) {
                    float yawCopy = plane.rotationYaw;
                    float atan = (float) MathHelper.atan2((double) distZ, (double) distX);
                    float yawTurn = MathHelper.wrapDegrees(plane.rotationYaw + 90);
                    float yawTurnAtan = MathHelper.wrapDegrees(atan * 57.295776F);
                    plane.rotationYaw = approachDegrees(yawTurn, yawTurnAtan, 4.0F) - 90.0F;
                    plane.renderYawOffset = plane.rotationYaw;
                    if (degreesDifferenceAbs(yawCopy, plane.rotationYaw) < 3.0F) {
                        speed = approach((float) speed, 1.2F, 0.005F * (1.2F / (float) speed));
                    } else {
                        speed = approach((float) speed, 0.2F, 0.025F);
                        if (dist < 100D && plane.getAttackTarget() != null) {
                            speed = speed * (dist / 100D);
                        }
                    }
                    float finPitch = (float) (-(MathHelper.atan2((double) (-distY), planeDist) * 57.2957763671875D));
                    plane.rotationPitch = finPitch;
                    float yawTurnHead = plane.rotationYaw + 90.0F;
                    double lvt_16_1_ = speed * MathHelper.cos(yawTurnHead * 0.017453292F) * Math.abs((double) distX / dist);
                    double lvt_18_1_ = speed * MathHelper.sin(yawTurnHead * 0.017453292F) * Math.abs((double) distZ / dist);
                    double lvt_20_1_ = speed * MathHelper.sin(finPitch * 0.017453292F) * Math.abs((double) distY / dist);
                    plane.setMotion(plane.getMotion().add(lvt_16_1_ * 0.4D, lvt_20_1_ * 0.4D, lvt_18_1_ * 0.4D));
                }
            }
        }
    }
}
