package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class EntityRattlingGun extends MobEntity implements IRatlantean, IPirat {
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityRattlingGun.class, DataSerializers.BOOLEAN);
    private boolean prevFire;
    private int fireCooldown = 0;

    public EntityRattlingGun(EntityType type, World worldIn) {
        super(type, worldIn);
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
                if (entity instanceof EntityRat) {
                    return entity;
                }
            }
        }
        return null;
    }

    public double getYOffset() {
        return 1.45D;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        passenger.setPosition(this.posX, this.posY + 1.45D, this.posZ);
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
        Vec3d vec3d = this.getMotion();
        this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        this.livingSoundTime = 20;
        if (this.deathTime >= 40) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            if (!this.world.isRemote) {
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Items.STICK), 0.0F);
                }
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Items.IRON_INGOT), 0.0F);
                }
            }
            this.remove();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.EXPLOSION, this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.posY + (double) (this.rand.nextFloat() * this.getHeight()), this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public void tick() {
       super.tick();
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
        if (this.getControllingPassenger() != null) {
            if (this.getControllingPassenger() instanceof LivingEntity) {
                LivingEntity riding = (LivingEntity) this.getControllingPassenger();
                this.rotationYaw = riding.rotationYaw;
                this.rotationYawHead = riding.rotationYawHead;
                this.prevRotationYaw = riding.prevRotationYaw;
            }
        }
        this.doBlockCollisions();
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
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

    public boolean processInteract(PlayerEntity player, Hand hand) {
        if (this.getControllingPassenger() == null) {
            if (!player.getPassengers().isEmpty()) {
                boolean flag = false;
                for (Entity entity : player.getPassengers()) {
                    if (entity instanceof EntityRat) {
                        flag = true;
                        System.out.println(world.isRemote);
                        ((EntityRat) entity).dismountEntity(player);
                        entity.startRiding(this);
                        break;
                    }
                }
                return flag;
            }
        } else {
            if (this.getControllingPassenger() instanceof EntityRat) {
                if (((EntityRat) this.getControllingPassenger()).isOwner(player)) {
                    ((EntityRat) this.getControllingPassenger()).dismountEntity(this);
                    return true;
                }
            }
        }
        return false;
    }

    public void shoot(EntityRat pirat) {
        LivingEntity target = pirat.getAttackTarget();
        if (target == null) {
            target = world.getClosestPlayer(this, 30);
        }
        if (target != null) {
            {
                double d0 = target.posX - this.posX;
                double d2 = target.posZ - this.posZ;
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.renderYawOffset = this.rotationYaw = f % 360;
            }
            EntityCheeseCannonball cannonball = new EntityCheeseCannonball(RatsEntityRegistry.CHEESE_CANNONBALL, world, pirat);
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

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    public boolean shouldDismountInWater(Entity rider) {
        return false;
    }

    public void travel(Vec3d vec) {

    }
}
