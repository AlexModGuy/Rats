package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.ai.PiratBoatPathNavigate;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.recipes.RatsRecipeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityPiratBoat extends MobEntity implements IRatlantean, IPirat {
    public ItemStack banner = generateBanner();
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityPiratBoat.class, DataSerializers.BOOLEAN);
    private final float[] paddlePositions;
    protected int navigatorType;
    private boolean prevFire;
    private int fireCooldown = 0;
    private double waterLevel;
    private BoatEntity.Status status;
    private BoatEntity.Status previousStatus;
    private double lastYd;

    public EntityPiratBoat(EntityType type, World worldIn) {
        super(type, worldIn);
        banner = generateBanner();
        this.paddlePositions = new float[2];
        switchNavigator(0);
    }

    private static ItemStack generateBanner() {
        ItemStack itemstack = new ItemStack(Items.BLACK_BANNER);
        CompoundNBT compoundnbt = itemstack.getOrCreateChildTag("BlockEntityTag");
        ListNBT listnbt = (new BannerPattern.Builder()).setPatternWithColor(RatsRecipeRegistry.RAT_AND_CROSSBONES_BANNER, DyeColor.WHITE).buildNBT();
        compoundnbt.put("Patterns", listnbt);
        return itemstack;
    }

    protected void switchNavigator(int type) {
        if (type == 1) {//land
            this.navigator = new GroundPathNavigator(this, world);
            this.moveController = new MovementController(this);
            this.navigatorType = 1;
        } else {//sea
            this.navigator = new PiratBoatPathNavigate(this, world);
            this.moveController = new MoveHelperController(this);
            this.navigatorType = 0;
        }
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

    @Override
    public Entity getControllingPassenger() {
        if (!this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof EntityPirat) {
                    return entity;
                }
            }
        }
        return null;
    }

    public double getYOffset() {
        return 0.45D;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        passenger.setPosition(this.getPosX(), this.getPosY() + 0.45D, this.getPosZ());
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).renderYawOffset = this.renderYawOffset;
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIRING, Boolean.valueOf(false));
    }

    public boolean isFiring() {
        return this.dataManager.get(FIRING).booleanValue();
    }

    public void setFiring(boolean male) {
        this.dataManager.set(FIRING, Boolean.valueOf(male));
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        Vector3d vec3d = this.getMotion();
        this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        this.livingSoundTime = 20;
        if (this.deathTime >= 80) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), j));
                }
            }
            if (!this.world.isRemote) {
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Items.STICK), 0.0F);
                }
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Blocks.OAK_PLANKS), 0.0F);
                }
                if (rand.nextInt(3) == 0) {
                    this.entityDropItem(banner.copy(), 0.0F);
                }
                if (rand.nextInt(2) == 0) {
                    this.entityDropItem(new ItemStack(RatlantisItemRegistry.CHEESE_CANNONBALL), 0.0F);
                }
            }
            this.remove();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.EXPLOSION, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public void tick() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        super.tick();
        boolean groundNavigate = !this.isInWater() && this.status != BoatEntity.Status.IN_WATER;
        if (!world.isRemote) {
            if (groundNavigate && navigatorType != 1) {
                switchNavigator(1);
            }
            if (!groundNavigate && navigatorType != 0) {
                switchNavigator(0);
            }
        }
        if (this.getRidingEntity() != null) {
            if (!this.getRidingEntity().isPassenger()) {
                this.getRidingEntity().startRiding(this, true);
            }
        }
        if (this.prevFire != isFiring()) {
            fireCooldown = 4;
        }
        if (isFiring() && fireCooldown == 0) {
            setFiring(false);
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        prevFire = this.isFiring();
        if (!this.isBeingRidden() && !world.isRemote) {
            this.attackEntityFrom(DamageSource.DROWN, 1000);
        }
        if (this.getControllingPassenger() != null) {
            this.updateMotion();
            if (this.getControllingPassenger() instanceof LivingEntity) {
                LivingEntity riding = (LivingEntity) this.getControllingPassenger();
                this.moveStrafing = riding.moveStrafing;
                this.moveForward = riding.moveForward;
                this.moveRelative(1, new Vector3d(moveStrafing, 0, moveForward));
                this.rotationYaw = riding.rotationYaw;
                this.rotationYawHead = riding.rotationYawHead;
                this.prevRotationYaw = riding.prevRotationYaw;
            }
        }
        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow((double)0.2F, (double)-0.01F, (double)0.2F), EntityPredicates.pushableBy(this));

        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof PlayerEntity);

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);

                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isPassenger() && entity.getWidth() < this.getWidth() && entity instanceof EntityPirat) {
                        entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }

    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 60.0D)        //HEALTH
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.1D)                //SPEED
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 64.0D)               //FOLLOW RANGE
                .createMutableAttribute(Attributes.ARMOR, 10.0D);
    }

    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof BoatEntity) {
            if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        } else if (entityIn.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.applyEntityCollision(entityIn);
        }
    }

    protected void doWaterSplashEffect() {
    }

    @OnlyIn(Dist.CLIENT)
    public float getRowingTime(int side, float limbSwing) {
        return (float) MathHelper.clampedLerp((double) this.paddlePositions[side] - 0.39269909262657166D, (double) this.paddlePositions[side], (double) limbSwing);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return EMPTY_EQUIPMENT;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public void shoot(EntityPirat pirat) {
        //world.updateEntityWithOptionalForce(this, true);

        LivingEntity target = pirat.getAttackTarget();
        if (target == null) {
            target = world.getClosestPlayer(this, 30);
        }
        if (target != null) {
            {
                double d0 = target.getPosX() - this.getPosX();
                double d2 = target.getPosZ() - this.getPosZ();
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.renderYawOffset = this.rotationYaw = f % 360;
            }
            EntityCheeseCannonball cannonball = new EntityCheeseCannonball(RatlantisEntityRegistry.CHEESE_CANNONBALL, world, pirat);
            //cannonball.ignoreEntity = this;
            float radius = 1.6F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX();
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ();
            double extraY = 0.8 + getPosY();
            double d0 = target.getPosY() + (double) target.getEyeHeight();
            double d1 = target.getPosX() - extraX;
            double d3 = target.getPosZ() - extraZ;
            double d2 = d0 - extraY;
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.65F;
            float velocity = this.getDistance(target) * 0.045F;
            cannonball.setPosition(extraX, extraY, extraZ);
            cannonball.shoot(d1, d2 + (double) f, d3, velocity, 0.4F);
            this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 3.0F, 2.3F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                this.world.addEntity(cannonball);
            }
            this.setFiring(true);
        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        label39:
        for(int k1 = k; k1 < l; ++k1) {
            float f = 0.0F;
            int l1 = i;

            while(true) {
                if (l1 >= j) {
                    if (f < 1.0F) {
                        return (float)blockpos$mutable.getY() + f;
                    }
                    break;
                }

                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(l1, k1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        f = Math.max(f, fluidstate.getActualHeight(this.world, blockpos$mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }

                ++l1;
            }
        }

        return (float)(l + 1);
    }


    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        float f = (float)l1 + fluidstate.getActualHeight(this.world, blockpos$mutable);
                        this.waterLevel = Math.max((double)f, this.waterLevel);
                        flag |= axisalignedbb.minY < (double)f;
                    }
                }
            }
        }

        return flag;
    }

    private boolean isOverWater() {
        return this.isInWater();
    }

    public BoatEntity.Status getBoatStatus() {
        BoatEntity.Status BoatEntity$status = this.getUnderwaterStatus();

        if (BoatEntity$status != null) {
            this.waterLevel = this.getBoundingBox().minY;
            return BoatEntity$status;
        } else if (this.checkInWater()) {
            return BoatEntity.Status.IN_WATER;
        } else {
            return BoatEntity.Status.ON_LAND;
        }
    }

    @Nullable
    private BoatEntity.Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER) && d0 < (double)((float)blockpos$mutable.getY() + fluidstate.getActualHeight(this.world, blockpos$mutable))) {
                        if (!fluidstate.isSource()) {
                            return BoatEntity.Status.UNDER_FLOWING_WATER;
                        }

                        flag = true;
                    }
                }
            }
        }

        return flag ? BoatEntity.Status.UNDER_WATER : null;
    }

    private void updateMotion() {
        double d0 = (double)-0.04F;
        double d1 = this.hasNoGravity() ? 0.0D : (double)-0.04F;
        double d2 = 0.0D;
        float momentum = 0.45F;
        if (this.previousStatus == BoatEntity.Status.IN_AIR && this.status != BoatEntity.Status.IN_AIR && this.status != BoatEntity.Status.ON_LAND) {
            this.waterLevel = this.getBoundingBox().minY;
            this.setPosition(this.getPosX(), (double)(this.getWaterLevelAbove() - this.getHeight()) + 0.501D, this.getPosZ());
            this.setMotion(this.getMotion().mul(1.0D, 0.0D, 1.0D));
            this.lastYd = 0.0D;
            this.status = BoatEntity.Status.IN_WATER;
        } else {
            if (this.status == BoatEntity.Status.IN_WATER) {
                d2 = (this.waterLevel - this.getBoundingBox().minY) / (double)this.getHeight() * 2.5F;
                momentum = 0.9F;
            } else if (this.status == BoatEntity.Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4D;
                momentum = 0.9F;
            } else if (this.status == BoatEntity.Status.UNDER_WATER) {
                d2 = (double)2.9F;
                momentum = 0.45F;
            } else if (this.status == BoatEntity.Status.IN_AIR) {
                momentum = 0.9F;
            } else if (this.status == BoatEntity.Status.ON_LAND) {
               /* momentum = this.boatGlide;
                if (this.getControllingPassenger() instanceof PlayerEntity) {
                    this.boatGlide /= 2.0F;
                }*/
            }

            Vector3d vec3d = this.getMotion();
            this.setMotion(vec3d.x * (double)momentum, vec3d.y + d1, vec3d.z * (double)momentum);
            if (d2 > 0.0D) {
                Vector3d vec3d1 = this.getMotion();
                this.setMotion(vec3d1.x, (vec3d1.y + d2 * 0.06153846016296973D) * 0.75D, vec3d1.z);
            }
        }
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public void travel(Vector3d vec) {
        super.travel(vec);

    }

    static class MoveHelperController extends MovementController {
        private final EntityPiratBoat turtle;

        MoveHelperController(EntityPiratBoat turtleIn) {
            super(turtleIn);
            this.turtle = turtleIn;
        }

        public void tick() {
            double d0 = this.getX() - this.turtle.getPosX();
            double d1 = this.getY() - this.turtle.getPosY();
            double d2 = this.getZ() - this.turtle.getPosZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double)2.5000003E-7F) {
                this.mob.setMoveForward(0.0F);
            } else {
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.turtle.rotationYaw = this.limitAngle(this.turtle.rotationYaw, f, 10.0F);
                this.turtle.renderYawOffset = this.turtle.rotationYaw;
                this.turtle.rotationYawHead = this.turtle.rotationYaw;
                float f1 = 1;//(float)(this.speed * this.turtle.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                if (this.turtle.isInWater()) {
                    this.turtle.setAIMoveSpeed(f1 * 1F);
                    float f2 = -((float)(MathHelper.atan2(d1, (double)MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.turtle.rotationPitch = this.limitAngle(this.turtle.rotationPitch, f2, 5.0F);
                    float f3 = MathHelper.cos(this.turtle.rotationPitch * ((float)Math.PI / 180F));
                    float f4 = MathHelper.sin(this.turtle.rotationPitch * ((float)Math.PI / 180F));
                    this.turtle.moveForward = f3 * f1;
                    this.turtle.moveVertical = -f4 * f1;
                } else {
                    this.turtle.setAIMoveSpeed(f1 * 1F);
                }

            }
        }
    }

}
