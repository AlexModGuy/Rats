package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityRatCaptureNet extends PotionEntity {

    public EntityRatCaptureNet(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRatCaptureNet(EntityType type, World worldIn, LivingEntity throwerIn, ItemStack potionDamageIn) {
        this(type, worldIn);
        this.owner = throwerIn;
    }

    public EntityRatCaptureNet(EntityType type, World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.RAT_CAPTURE_NET);
    }

    protected void onImpact(RayTraceResult result) {
        ItemStack sack = new ItemStack(RatsItemRegistry.RAT_SACK);
        CompoundNBT tag = new CompoundNBT();
        if (!this.world.isRemote && getThrower() != null) {
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(30, 16, 30);
            List<EntityRat> list = this.world.getEntitiesWithinAABB(EntityRat.class, axisalignedbb);
            int capturedRat = 0;
            if (!list.isEmpty()) {
                for (EntityRat rat : list) {
                    if (rat.isTamed() && (rat.isOwner(getThrower()) || getThrower() instanceof PlayerEntity && ((PlayerEntity) getThrower()).isCreative())) {
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
        ItemEntity itemEntity = new ItemEntity(world, this.posX, this.posY, this.posZ, sack);
        itemEntity.setInvulnerable(true);
        if (!world.isRemote) {
            world.addEntity(itemEntity);
        }
    }
}
