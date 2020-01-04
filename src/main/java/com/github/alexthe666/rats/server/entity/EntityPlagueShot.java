package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityPlagueShot extends AbstractArrowEntity {

    public EntityPlagueShot(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setDamage(6F);
    }

    public EntityPlagueShot(EntityType type, World worldIn, double x, double y, double z, float r, float g, float b) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(6F);
    }

    public EntityPlagueShot(EntityType type, World worldIn, LivingEntity shooter, double dmg) {
        super(type, shooter, worldIn);
        this.setDamage(dmg);
    }

    public EntityPlagueShot(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.PLAGUE_SHOT, worldIn);
    }

    public boolean isInWater() {
        return false;
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    public void tick() {
        float sqrt = MathHelper.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z);
        if ((sqrt < 0.1F || this.inGround || this.collidedHorizontally) && this.ticksExisted > 5) {
            this.remove();
        }
        double d0 = 0;
        double d1 = 0.01D;
        double d2 = 0D;
        double x = this.posX + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth();
        double y = this.posY + (double) (this.rand.nextFloat() * this.getHeight()) - (double) this.getHeight();
        double z = this.posZ + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth();
        float f = (this.getWidth() + this.getHeight() + this.getWidth()) * 0.333F + 0.5F;
        if (particleDistSq(x, y, z) < f * f) {
            if (rand.nextBoolean()) {
                RatsMod.PROXY.addParticle("black_death", x, y + 0.5D, z, d0, d1, d2);
            } else {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, x, y + 0.5D, z, d0, d1, d2);

            }
        }
        super.tick();
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = posX - toX;
        double d1 = posY - toY;
        double d2 = posZ - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }


    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        if (living != null && (this.getShooter() == null || !living.isEntityEqual(this.getShooter()))) {
            living.addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 1200));
            if (living instanceof PlayerEntity) {
                this.damageShield((PlayerEntity) living, (float) this.getDamage());
            }
        }
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player, (p_213833_1_) -> {
                p_213833_1_.sendBreakAnimation(Hand.MAIN_HAND);
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

    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
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
}
