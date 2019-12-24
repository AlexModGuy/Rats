package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCheeseCannonball extends ThrowableEntity {

    public EntityCheeseCannonball(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCheeseCannonball(EntityType type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityCheeseCannonball(EntityType type, World worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;

            for (int i = 0; i < 18; ++i) {
                this.world.addParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(Items.EGG));
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

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            if (result.entityHit != null && (thrower == null || !result.entityHit.isOnSameTeam(thrower))) {
                result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 8.0F);
            }
            Explosion explosion = new RatNukeExplosion(world, thrower == null ? this : thrower, this.posX, this.posY, this.posZ, 1.0F, false, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, thrower == null ? this : thrower));
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            this.setDead();
        }
    }
}