package com.github.alexthe666.rats.server.entity.ratlantis;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityRattlingGunBullet extends AbstractArrowEntity {

    public EntityRattlingGunBullet(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(EntityType type, World worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(EntityType type, World worldIn, LivingEntity shooter) {
        super(type, shooter, worldIn);
        this.setDamage(2F);
    }

    public EntityRattlingGunBullet(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(RatlantisEntityRegistry.RATTLING_GUN_BULLET, world);
    }

    public boolean isInWater() {
        return false;
    }

    public void tick() {
        float sqrt = (float)this.getMotion().length();
        if (sqrt < 0.2F || this.inGround) {
            this.remove();
        }
        super.tick();
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    protected void onImpact(RayTraceResult raytraceResultIn) {
        if(raytraceResultIn instanceof EntityRayTraceResult && func_234616_v_() != null && func_234616_v_().isOnSameTeam(((EntityRayTraceResult) raytraceResultIn).getEntity())){
            return;
        }
        if (raytraceResultIn instanceof EntityRayTraceResult && ((EntityRayTraceResult) raytraceResultIn).getEntity() instanceof PlayerEntity) {
            this.damageShield((PlayerEntity) ((EntityRayTraceResult) raytraceResultIn).getEntity(), (float) this.getDamage());
        }
        super.onImpact(raytraceResultIn);
        this.remove();
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player, (p_220048_0_) -> {
                p_220048_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });

            if (player.getActiveItemStack().isEmpty()) {
                Hand Hand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == Hand.MAIN_HAND) {
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private IntOpenHashSet piercedEntities;
    private List<Entity> hitEntities;
    private int knockbackStrength;

    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        Entity entity = p_213868_1_.getEntity();
        float f = (float)this.getMotion().length();
        int i = MathHelper.ceil(Math.max((double)f * this.getDamage(), 0.0D));
        if (this.getPierceLevel() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercedEntities.add(entity.getEntityId());
        }

        if (this.getIsCritical()) {
            i += this.rand.nextInt(i / 2 + 2);
        }

        Entity entity1 = this.func_234616_v_();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSource.causeArrowDamage(this, this);
        } else {
            damagesource = DamageSource.causeArrowDamage(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity)entity1).setLastAttackedEntity(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTimer();
        if (this.isBurning() && !flag) {
            entity.setFire(5);
        }

        if (entity.attackEntityFrom(damagesource, (float)i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                if (!this.world.isRemote && this.getPierceLevel() <= 0) {
                    //   livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
                }

                if (this.knockbackStrength > 0) {
                    Vector3d vec3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockbackStrength * 0.6D);
                    if (vec3d.lengthSquared() > 0.0D) {
                        livingentity.addVelocity(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                if (!this.world.isRemote && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.applyThornEnchantments(livingentity, entity1);
                    EnchantmentHelper.applyArthropodEnchantments((LivingEntity)entity1, livingentity);
                }

                this.arrowHit(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity)entity1).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.field_241770_g_, 0.0F));
                }

                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingentity);
                }

                if (!this.world.isRemote && entity1 instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity1;
                }
            }

            this.playSound(this.getHitGroundSound(), 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        } else {
            this.setMotion(this.getMotion().scale(-0.1D));
            this.rotationYaw += 180.0F;
            this.prevRotationYaw += 180.0F;
            //this.ticksInAir = 0;
            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
                if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
                    this.entityDropItem(this.getArrowStack(), 0.1F);
                }

                this.remove();
            }
        }

    }
}
