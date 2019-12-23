package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityVialOfSentience extends PotionEntity {

    public EntityVialOfSentience(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityVialOfSentience(EntityType type, World worldIn, LivingEntity throwerIn, ItemStack potionDamageIn) {
        super(type, worldIn, throwerIn, potionDamageIn);
    }

    public EntityVialOfSentience(EntityType type, World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        super(type, worldIn, x, y, z, potionDamageIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.VIAL_OF_SENTIENCE);
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);

            if (!list.isEmpty()) {
                for (LivingEntity LivingEntity : list) {
                    if (LivingEntity.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(LivingEntity);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (LivingEntity == result.entityHit) {
                                d1 = 1.0D;
                            }
                            if (LivingEntity instanceof EntityFeralRatlantean) {
                                EntityNeoRatlantean ratlantean = new EntityNeoRatlantean(world);
                                ratlantean.setColorVariant(((EntityFeralRatlantean) LivingEntity).getColorVariant());
                                ratlantean.copyLocationAndAnglesFrom(LivingEntity);
                                LivingEntity.setDead();
                                if (!world.isRemote) {
                                    world.addEntity(ratlantean);
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