package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRatArrow extends AbstractArrowEntity {

    private ItemStack stack;

    public EntityRatArrow(EntityType type, World worldIn) {
        super(type, worldIn);
        this.stack = new ItemStack(RatsItemRegistry.RAT_ARROW);
    }

    public EntityRatArrow(EntityType type, World worldIn, LivingEntity shooter, ItemStack stack) {
        super(type, shooter, worldIn);
        this.stack = stack;
    }

    public EntityRatArrow(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(RatsEntityRegistry.RAT_ARROW, world);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        if(raytraceResultIn.getType() != RayTraceResult.Type.MISS) {
            Entity entity = null;
            BlockPos pos = null;
            if (raytraceResultIn instanceof EntityRayTraceResult) {
                entity = ((EntityRayTraceResult) raytraceResultIn).getEntity();
                pos = new BlockPos(((EntityRayTraceResult) raytraceResultIn).getEntity());
            }
            EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, world);
            CompoundNBT ratTag = new CompoundNBT();
            if (stack.getTag() != null && !stack.getTag().getCompound("Rat").isEmpty()) {
                ratTag = stack.getTag().getCompound("Rat");
            }
            rat.readAdditional(ratTag);
            if (raytraceResultIn instanceof BlockRayTraceResult) {
                pos = ((BlockRayTraceResult) raytraceResultIn).getPos();

            }
            if (pos == null) {
                pos = new BlockPos(raytraceResultIn.getHitVec());
            } else {
                if (raytraceResultIn instanceof BlockRayTraceResult && ((BlockRayTraceResult) raytraceResultIn).getFace() != null) {
                    pos = pos.offset(((BlockRayTraceResult) raytraceResultIn).getFace());
                }
            }
            rat.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
            if (!world.isRemote) {
                world.addEntity(rat);
            }
            if (entity != null && entity instanceof LivingEntity && !rat.isOnSameTeam(entity)) {
                rat.setAttackTarget((LivingEntity) entity);
            }
            if (this.inGround) {
                this.remove();
                if (!world.isRemote) {
                    this.entityDropItem(getArrowStack(), 0.0F);
                }
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
