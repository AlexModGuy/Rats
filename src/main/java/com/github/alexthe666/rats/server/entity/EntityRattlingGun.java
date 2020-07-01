package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.Collections;
import java.util.List;

public class EntityRattlingGun extends MobEntity implements IRatlantean, IPirat {
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.emptyList();
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityRattlingGun.class, DataSerializers.BOOLEAN);
    private boolean prevFire;
    private int fireCooldown = 0;

    public EntityRattlingGun(EntityType type, World worldIn) {
        super(type, worldIn);
        this.recalculateSize();
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

    public boolean isNoDespawnRequired() {
        return true;
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
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
        return 0.45D;
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        float radius = 0.9F;
        float angle = (0.01745329251F * (rotationYaw + 150F));
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double extraY = 1.3215D;
        passenger.setPosition(this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FIRING, Boolean.valueOf(false));
    }

    public boolean isFiring() {
        return this.dataManager.get(FIRING).booleanValue();
    }

    public void setFiring(boolean firing) {
        this.dataManager.set(FIRING, Boolean.valueOf(firing));
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        Vector3d vec3d = this.getMotion();
        this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        this.livingSoundTime = 20;
        if (this.deathTime >= 40) {
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
                for (int j = 0; j < 1 + rand.nextInt(2); j++) {
                    this.entityDropItem(new ItemStack(Items.STICK), 0.0F);
                }
                for (int j = 0; j < 3 + rand.nextInt(7); j++) {
                    this.entityDropItem(new ItemStack(Items.IRON_INGOT), 0.0F);
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
       super.tick();
       Entity rat = this.getControllingPassenger();
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
        if (rat != null) {
            if (rat instanceof LivingEntity) {
                LivingEntity riding = (LivingEntity) this.getControllingPassenger();
                this.rotationYaw = riding.rotationYaw;
                this.prevRotationYaw = riding.rotationYaw;
                this.rotationYawHead = riding.rotationYaw;
                if (riding instanceof EntityRat) {
                    EntityRat rattus = (EntityRat)this.getControllingPassenger();
                    if(rattus.getAttackTarget() != null && rattus.getAttackTarget().getEntityId() != this.getEntityId()){
                        this.faceEntity(rattus.getAttackTarget(), 30, 30);
                        this.shoot(rattus);
                    }
                }
            }

        }
        this.setRotation(this.rotationYaw, this.rotationPitch);

        this.doBlockCollisions();
    }


    public static AttributeModifierMap.MutableAttribute func_234290_eH_() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 60.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 2.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233819_b_, 128.0D)         //FOLLOW RANGE
                .func_233815_a_(Attributes.field_233826_i_, 10);         //ARMOR
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

    @Override
    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        if (this.getControllingPassenger() == null) {
            if (!player.getPassengers().isEmpty()) {
                boolean flag = false;
                for (Entity entity : player.getPassengers()) {
                    if (entity instanceof EntityRat) {
                        flag = true;
                        System.out.println(world.isRemote);
                        entity.startRiding(this, false);
                        break;
                    }
                }
                return flag ? ActionResultType.SUCCESS : ActionResultType.PASS;
            }
        } else {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof EntityRat) {
                if (((EntityRat)passenger).isOwner(player)) {
                    ((EntityRat) passenger).stopRiding();
                    ((EntityRat) passenger).startRiding(player);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    public void shoot(EntityRat pirat) {
        LivingEntity target = pirat.getAttackTarget();
        if (target == null) {
            target = world.getClosestPlayer(this, 30);
        }
        if (target != null) {
            {
                double d0 = target.getPosX() - this.getPosX();
                double d2 = target.getPosZ() - this.getPosZ();
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.renderYawOffset = f % 360;
                this.rotationYaw = f % 360;
                this.rotationYawHead = f % 360;
                pirat.renderYawOffset = f % 360;
                pirat.rotationYaw = f % 360;
                pirat.rotationYawHead = f % 360;
            }
            EntityRattlingGunBullet cannonball = new EntityRattlingGunBullet(RatsEntityRegistry.RATTLING_GUN_BULLET, world, pirat);
            float radius = 1.6F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + getPosX() + rand.nextFloat() * 0.2F - 0.1;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + getPosZ() + rand.nextFloat() * 0.2F - 0.1;
            double extraY = 1.35 + getPosY() + rand.nextFloat() * 0.1F - 0.05;
            double d0 = target.getPosY() + (double) target.getEyeHeight()/2;
            double d1 = target.getPosX() - extraX;
            double d3 = target.getPosZ() - extraZ;
            double d2 = d0 - extraY;
            float velocity = 2.2F;
            cannonball.setPosition(extraX, extraY, extraZ);
            cannonball.shoot(d1, d2, d3, velocity, 0.4F);
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


    public boolean shouldRiderFaceForward(PlayerEntity player) {
        return true;
    }

    public EntityRattlingGun(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.RATTLING_GUN, worldIn);
    }

    public void travel(Vector3d vec) {

    }
}
