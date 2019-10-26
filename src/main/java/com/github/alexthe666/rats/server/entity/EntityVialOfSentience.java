package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityVialOfSentience extends EntityPotion {

    public EntityVialOfSentience(World worldIn) {
        super(worldIn);
    }

    public EntityVialOfSentience(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
        super(worldIn, throwerIn, potionDamageIn);
    }

    public EntityVialOfSentience(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        super(worldIn, x, y, z, potionDamageIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.VIAL_OF_SENTIENCE);
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

            if (!list.isEmpty()) {
                for (EntityLivingBase entitylivingbase : list) {
                    if (entitylivingbase.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(entitylivingbase);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entitylivingbase == result.entityHit) {
                                d1 = 1.0D;
                            }
                            if (entitylivingbase instanceof EntityFeralRatlantean) {
                                EntityNeoRatlantean ratlantean = new EntityNeoRatlantean(world);
                                ratlantean.setColorVariant(((EntityFeralRatlantean) entitylivingbase).getColorVariant());
                                ratlantean.copyLocationAndAnglesFrom(entitylivingbase);
                                entitylivingbase.setDead();
                                if (!world.isRemote) {
                                    world.spawnEntity(ratlantean);
                                }
                            }
                        }
                    }
                }
                this.setDead();
            }
        }
        this.world.playEvent(2002, new BlockPos(this), 0XFEFE7E);
    }
}