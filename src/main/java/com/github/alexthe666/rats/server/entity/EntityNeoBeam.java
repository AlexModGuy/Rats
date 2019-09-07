package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityNeoBeam extends EntityArrow {

    public EntityNeoBeam(World worldIn) {
        super(worldIn);
        this.setDamage(6F);
    }

    public EntityNeoBeam(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.setDamage(6F);
    }

    public void onUpdate() {
        float sqrt = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (sqrt < 0.3F || this.inGround || this.collidedHorizontally) {
            this.setDead();
            Explosion explosion = world.createExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 0.0F,  net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, shootingEntity));
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
        super.onUpdate();
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
        }
    }


    protected void onHit(RayTraceResult raytraceResultIn) {
        if (raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityPlayer) {
            this.damageShield((EntityPlayer)raytraceResultIn.entityHit, (float)this.getDamage());
        }
        super.onHit(raytraceResultIn);
    }

    protected void damageShield(EntityPlayer player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player);

            if (player.getActiveItemStack().isEmpty()) {
                EnumHand enumhand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, enumhand);

                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }


    public EntityNeoBeam(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(6F);
    }

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }
}
