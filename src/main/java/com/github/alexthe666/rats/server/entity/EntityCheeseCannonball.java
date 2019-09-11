package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCheeseCannonball extends EntityThrowable {

    public EntityCheeseCannonball(World worldIn) {
        super(worldIn);
    }

    public EntityCheeseCannonball(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    public EntityCheeseCannonball(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;

            for (int i = 0; i < 18; ++i) {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(Items.EGG));
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

    protected void onImpact(RayTraceResult result) {
        if (result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 8.0F);
        }
        if (result != null && result.getBlockPos() != null) {
            Explosion explosion = world.createExplosion(this, this.posX, this.posY, this.posZ, 1.0F, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this));
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            this.setDead();
        }
    }
}