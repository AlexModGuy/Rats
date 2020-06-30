package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityRatCaptureNet extends ProjectileItemEntity {

    public EntityRatCaptureNet(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRatCaptureNet(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.RAT_CAPTURE_NET, worldIn);
    }

    public EntityRatCaptureNet(World worldIn, LivingEntity throwerIn) {
        super(RatsEntityRegistry.RAT_CAPTURE_NET, throwerIn, worldIn);
    }

    public EntityRatCaptureNet(World worldIn, double x, double y, double z) {
        super(RatsEntityRegistry.RAT_CAPTURE_NET, x, y, z, worldIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.RAT_CAPTURE_NET);
    }

    protected void onImpact(RayTraceResult result) {
        ItemStack sack = new ItemStack(RatsItemRegistry.RAT_SACK);
        CompoundNBT tag = new CompoundNBT();
        if (!this.world.isRemote && func_234616_v_() != null) {
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(30, 16, 30);
            List<EntityRat> list = this.world.getEntitiesWithinAABB(EntityRat.class, axisalignedbb);
            int capturedRat = 0;
            if (!list.isEmpty()) {
                for (EntityRat rat : list) {
                    if (rat.isTamed() && func_234616_v_() instanceof LivingEntity && (rat.isOwner((LivingEntity) func_234616_v_()) || func_234616_v_() instanceof PlayerEntity && ((PlayerEntity) func_234616_v_()).isCreative())) {
                        CompoundNBT ratTag = new CompoundNBT();
                        rat.writeAdditional(ratTag);
                        capturedRat++;
                        world.setEntityState(rat, (byte) 86);
                        tag.put("Rat_" + capturedRat, ratTag);
                        rat.remove();
                    }
                }
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
            }
            this.remove();
        }
        sack.setTag(tag);
        ItemEntity itemEntity = new ItemEntity(world, this.getPosX(), this.getPosY(), this.getPosZ(), sack);
        itemEntity.setInvulnerable(true);
        if (!world.isRemote) {
            world.addEntity(itemEntity);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return RatsItemRegistry.RAT_CAPTURE_NET;
    }

    public ItemStack getItem() {
        return new ItemStack(RatsItemRegistry.RAT_CAPTURE_NET);
    }

        @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
