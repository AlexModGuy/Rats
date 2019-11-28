package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityPurifyingLiquid extends EntityPotion {

    public EntityPurifyingLiquid(World worldIn) {
        super(worldIn);
    }

    public EntityPurifyingLiquid(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
        super(worldIn, throwerIn, potionDamageIn);
    }

    public EntityPurifyingLiquid(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        super(worldIn, x, y, z, potionDamageIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.PURIFYING_LIQUID);
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
                            if (entitylivingbase instanceof EntityRat) {
                                EntityRat rat = (EntityRat) entitylivingbase;
                                if (rat.hasPlague()) {
                                    rat.setPlague(false);
                                    rat.setTamed(false);
                                    rat.setOwnerId(null);
                                }
                            }
                            if (entitylivingbase.isPotionActive(RatsMod.PLAGUE_POTION)) {
                                entitylivingbase.removePotionEffect(RatsMod.PLAGUE_POTION);
                            }
                            if (entitylivingbase instanceof IPlagueLegion) {
                                entitylivingbase.attackEntityFrom(DamageSource.MAGIC, 10);
                            }
                            if (entitylivingbase instanceof EntityZombieVillager && !((EntityZombieVillager) entitylivingbase).isConverting()) {
                                CompoundNBT tag = entitylivingbase.writeToNBT(new CompoundNBT());
                                tag.setInt("ConversionTime", 200);
                                entitylivingbase.readEntityFromNBT(tag);
                                this.world.setEntityState(entitylivingbase, (byte) 16);
                            }
                        }
                    }
                }
            }
            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            this.world.playEvent(i, new BlockPos(this), 0XBFDFE2);
            this.setDead();
        }
    }
}