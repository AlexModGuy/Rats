package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityVialOfSentience extends ProjectileItemEntity implements IRendersAsItem {

    public EntityVialOfSentience(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityVialOfSentience(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.VIAL_OF_SENTIENCE, worldIn);
    }

    public EntityVialOfSentience(World worldIn, LivingEntity throwerIn) {
        super(RatsEntityRegistry.VIAL_OF_SENTIENCE, throwerIn, worldIn);
    }

    public EntityVialOfSentience(World worldIn, double x, double y, double z) {
        super(RatsEntityRegistry.VIAL_OF_SENTIENCE, x, y, z, worldIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.VIAL_OF_SENTIENCE);
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);

            if (!list.isEmpty()) {
                for (LivingEntity LivingEntity : list) {
                    if (LivingEntity.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(LivingEntity);
                        if (d0 < 16.0D) {
                            if (LivingEntity instanceof EntityFeralRatlantean) {
                                EntityNeoRatlantean ratlantean = new EntityNeoRatlantean(RatsEntityRegistry.NEO_RATLANTEAN, world);
                                ratlantean.setColorVariant(((EntityFeralRatlantean) LivingEntity).getColorVariant());
                                ratlantean.copyLocationAndAnglesFrom(LivingEntity);
                                LivingEntity.remove();
                                if (!world.isRemote) {
                                    world.addEntity(ratlantean);
                                }
                            }
                        }
                    }
                }
                this.remove();
            }
        }
        this.world.playEvent(2002, new BlockPos(this), 0XFEFE7E);
    }

    @Override
    protected Item func_213885_i() {
        return RatsItemRegistry.VIAL_OF_SENTIENCE;
    }

    public ItemStack getItem() {
        return new ItemStack(RatsItemRegistry.VIAL_OF_SENTIENCE);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}