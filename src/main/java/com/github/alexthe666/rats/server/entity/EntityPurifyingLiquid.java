package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityPurifyingLiquid extends PotionEntity {

    public EntityPurifyingLiquid(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityPurifyingLiquid(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.PURIFYING_LIQUID, worldIn);
    }

    public EntityPurifyingLiquid(EntityType type, World worldIn, LivingEntity throwerIn, ItemStack potionDamageIn) {
        this(type, worldIn);
        this.owner = throwerIn;
    }

    public EntityPurifyingLiquid(EntityType type, World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.PURIFYING_LIQUID);
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            Potion potiontype = PotionUtils.getPotionFromItem(itemstack);
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
            if (!list.isEmpty()) {
                for (LivingEntity LivingEntity : list) {
                    if (LivingEntity.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(LivingEntity);
                        if (d0 < 16.0D) {
                            if (LivingEntity instanceof EntityRat) {
                                EntityRat rat = (EntityRat) LivingEntity;
                                if (rat.hasPlague()) {
                                    rat.setPlague(false);
                                    rat.setTamed(false);
                                    rat.setOwnerId(null);
                                }
                            }
                            if (LivingEntity.isPotionActive(RatsMod.PLAGUE_POTION)) {
                                LivingEntity.removePotionEffect(RatsMod.PLAGUE_POTION);
                            }
                            if (LivingEntity instanceof IPlagueLegion) {
                                LivingEntity.attackEntityFrom(DamageSource.MAGIC, 10);
                            }
                            if (LivingEntity instanceof ZombieVillagerEntity && !((ZombieVillagerEntity) LivingEntity).isConverting()) {
                                CompoundNBT tag = new CompoundNBT();
                                LivingEntity.writeAdditional(tag);
                                tag.putInt("ConversionTime", 200);
                                LivingEntity.readAdditional(tag);
                                this.world.setEntityState(LivingEntity, (byte) 16);
                            }
                        }
                    }
                }
            }
            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            this.world.playEvent(i, new BlockPos(this), 0XBFDFE2);
            this.remove();
        }
    }

    public ItemStack getItem() {
        return new ItemStack(RatsItemRegistry.PURIFYING_LIQUID);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}