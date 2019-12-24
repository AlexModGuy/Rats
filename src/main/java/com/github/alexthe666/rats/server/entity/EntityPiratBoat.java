package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ai.PiratBoatPathNavigate;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EntityPiratBoat extends MobEntity implements IRatlantean {
    public static final ItemStack BANNER = generateBanner();
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityPiratBoat.class, DataSerializers.BOOLEAN);
    private final float[] paddlePositions;
    protected int navigatorType;
    private boolean prevFire;
    private int fireCooldown = 0;
    private double waterLevel;
    private EntityBoat.Status status;
    private EntityBoat.Status previousStatus;
    private double lastYd;

    public EntityPiratBoat(EntityType type, World worldIn) {
        super(type, worldIn);
        this.paddlePositions = new float[2];
        this.setSize(1.75F, 0.8F);
        switchNavigator(0);
    }

    private static ItemStack generateBanner() {
        NBTTagList patterns = new NBTTagList();
        CompoundNBT currentPattern = new CompoundNBT();
        currentPattern.setString("Pattern", "rats.rat_and_crossbones");
        currentPattern.putInt("Color", 15);
        patterns.appendTag(currentPattern);
        return ItemBanner.makeBanner(EnumDyeColor.BLACK, patterns);
    }

    protected void switchNavigator(int type) {
        if (type == 1) {//land
            this.navigator = new PathNavigateGround(this, world);
            this.navigatorType = 1;
        } else {//sea
            this.navigator = new PiratBoatPathNavigate(this, world);
            this.navigatorType = 0;
        }
    }

    public boolean writeToNBTOptional(CompoundNBT compound) {
        String s = this.getEntityString();
        compound.setString("id", s);
        this.writeToNBT(compound);
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
        passenger.setPosition(this.posX, this.posY + 0.45D, this.posZ);
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
        this.motionY *= 0.6D;
        this.livingSoundTime = 20;
        if (this.deathTime >= 80) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            if (!this.world.isRemote) {
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Items.STICK), 0.0F);
                }
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Blocks.PLANKS), 0.0F);
                }
                if (rand.nextInt(3) == 0) {
                    this.entityDropItem(BANNER.copy(), 0.0F);
                }
                if (rand.nextInt(2) == 0) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.CHEESE_CANNONBALL), 0.0F);
                }
            }
            this.setDead();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
            }
        }
    }

    public void onUpdate() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        super.onUpdate();
        boolean groundNavigate = !this.isInWater() && this.status != EntityBoat.Status.IN_WATER;
        if (!world.isRemote) {
            if (groundNavigate && navigatorType != 1) {
                switchNavigator(1);
            }
            if (!groundNavigate && navigatorType != 0) {
                switchNavigator(0);
            }
        }
        if (this.getRidingEntity() != null) {
            if (!this.getRidingEntity().isRiding()) {
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
                this.moveRelative(moveStrafing, 0, moveForward, 0.10F);
                this.rotationYaw = riding.rotationYaw;
                this.rotationYawHead = riding.rotationYawHead;
                this.prevRotationYaw = riding.prevRotationYaw;
            }
        }
        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof PlayerEntity);

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);

                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityPirat) {
                        entity.startRiding(this);
                    } else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getAttribute(SWIM_SPEED).setBaseValue(0.1D);
    }

    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof EntityBoat) {
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
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public void shoot(EntityPirat pirat) {
        world.updateEntityWithOptionalForce(this, true);

        LivingEntity target = pirat.getAttackTarget();
        if (target == null) {
            target = world.getNearestPlayerNotCreative(this, 30);
        }
        if (target != null) {
            {
                double d0 = target.posX - this.posX;
                double d2 = target.posZ - this.posZ;
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.renderYawOffset = this.rotationYaw = f % 360;
            }
            EntityCheeseCannonball cannonball = new EntityCheeseCannonball(world, pirat);
            cannonball.ignoreEntity = this;
            float radius = 1.6F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 0.8 + posY;
            double d0 = target.posY + (double) target.getEyeHeight();
            double d1 = target.posX - extraX;
            double d3 = target.posZ - extraZ;
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
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try {
            label108:

            for (int k1 = k; k1 < l; ++k1) {
                float f = 0.0F;
                int l1 = i;

                while (true) {
                    if (l1 >= j) {
                        if (f < 1.0F) {
                            float f2 = (float) blockpos$pooledmutableblockpos.getY() + f;
                            return f2;
                        }

                        break;
                    }

                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
                        BlockState BlockState = this.world.getBlockState(blockpos$pooledmutableblockpos);

                        if (BlockState.getMaterial() == Material.WATER) {
                            f = Math.max(f, BlockLiquid.getBlockLiquidHeight(BlockState, this.world, blockpos$pooledmutableblockpos));
                        }

                        if (f >= 1.0F) {
                            continue label108;
                        }
                    }

                    ++l1;
                }
            }

            float f1 = (float) (l + 1);
            return f1;
        } finally {
            blockpos$pooledmutableblockpos.release();
        }
    }

    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY - 0.5D);
        int l = MathHelper.ceil(axisalignedbb.maxY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        BlockState BlockState = this.world.getBlockState(blockpos$pooledmutableblockpos);

                        if (BlockState.getMaterial() == Material.WATER) {
                            float f = BlockLiquid.getLiquidHeight(BlockState, this.world, blockpos$pooledmutableblockpos);
                            this.waterLevel = Math.max((double) f, this.waterLevel);
                            flag |= axisalignedbb.minY < (double) f;
                        }
                    }
                }
            }
        } finally {
            blockpos$pooledmutableblockpos.release();
        }

        return flag || this.isOverWater();
    }

    private EntityBoat.Status getBoatStatus() {
        EntityBoat.Status entityboat$status = this.getUnderwaterStatus();

        if (entityboat$status != null) {
            this.waterLevel = this.getBoundingBox().minY;
            return entityboat$status;
        } else if (this.checkInWater()) {
            return EntityBoat.Status.IN_WATER;
        } else {
            return EntityBoat.Status.ON_LAND;
        }
    }

    @Nullable
    private EntityBoat.Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        BlockState BlockState = this.world.getBlockState(blockpos$pooledmutableblockpos);

                        if (BlockState.getMaterial() == Material.WATER && d0 < (double) BlockLiquid.getLiquidHeight(BlockState, this.world, blockpos$pooledmutableblockpos)) {
                            if (BlockState.getValue(BlockLiquid.LEVEL).intValue() != 0) {
                                EntityBoat.Status entityboat$status = EntityBoat.Status.UNDER_FLOWING_WATER;
                                return entityboat$status;
                            }

                            flag = true;
                        }
                    }
                }
            }
        } finally {
            blockpos$pooledmutableblockpos.release();
        }

        return flag ? EntityBoat.Status.UNDER_WATER : null;
    }

    private void updateMotion() {
        double d0 = -0.03999999910593033D;
        double d1 = this.hasNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;
        float momentum = 0.05F;
        if (this.previousStatus == EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.IN_AIR && this.status != EntityBoat.Status.ON_LAND) {
            this.waterLevel = this.getBoundingBox().minY + (double) this.height;
            this.setPosition(this.posX, (double) (this.getWaterLevelAbove() - this.height) + 0.101D, this.posZ);
            this.motionY = 0.0D;
            this.lastYd = 0.0D;
            this.status = EntityBoat.Status.IN_WATER;
        } else {
            double up1 = this.waterLevel + 1.5;
            double up2 = this.getBoundingBox().minY + 0.5;
            if (this.status == EntityBoat.Status.IN_WATER) {
                d2 = (up1 - up2) / (double) this.height;
                momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_FLOWING_WATER) {
                d2 = (up1 - up2) / (double) this.height;
                momentum = 0.9F;
            } else if (this.status == EntityBoat.Status.UNDER_WATER) {
                d2 = (up1 - up2) / (double) this.height;
                momentum = 0.45F;
            } else if (this.status == EntityBoat.Status.IN_AIR) {
                momentum = 0.9F;
                this.motionY += d1;
            } else if (this.status == EntityBoat.Status.ON_LAND) {
                momentum = 0.0F;
                this.motionY += d1;
            }

            if (d2 > 0.0D) {
                this.motionY += d2 * 0.06153846016296973D;
                this.motionY *= 0.75D;
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

    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && (this.isInWater() || status == EntityBoat.Status.IN_WATER)) {
            float forwards = forward;
            float strafes = strafe * 0.5F;
            this.moveRelative(forwards, vertical, strafes, 0);
            this.motionX *= 0.8999999761581421D;
            this.motionY *= 0.8999999761581421D;
            this.motionZ *= 0.8999999761581421D;
            this.move(MoverType.SELF, this.motionX, this.isInWater() ? this.motionY : 0, this.motionZ);
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

}
