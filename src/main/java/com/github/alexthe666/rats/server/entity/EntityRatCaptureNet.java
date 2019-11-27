package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.ItemRatSack;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityRatCaptureNet extends EntityPotion {

    public EntityRatCaptureNet(World worldIn) {
        super(worldIn);
    }

    public EntityRatCaptureNet(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn) {
        super(worldIn, throwerIn, potionDamageIn);
    }

    public EntityRatCaptureNet(World worldIn, double x, double y, double z, ItemStack potionDamageIn) {
        super(worldIn, x, y, z, potionDamageIn);
    }

    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.RAT_CAPTURE_NET);
    }

    protected void onImpact(RayTraceResult result) {
        ItemStack sack = new ItemStack(RatsItemRegistry.RAT_SACK);
        NBTTagCompound tag = new NBTTagCompound();
        if (!this.world.isRemote && thrower != null) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(30, 16, 30);
            List<EntityRat> list = this.world.getEntitiesWithinAABB(EntityRat.class, axisalignedbb);
            int capturedRat = 0;
            if (!list.isEmpty()) {
                for (EntityRat rat : list) {
                    if (rat.isTamed() && (rat.isOwner(thrower) || thrower instanceof EntityPlayer && ((EntityPlayer) thrower).isCreative())) {
                        NBTTagCompound ratTag = new NBTTagCompound();
                        rat.writeEntityToNBT(ratTag);
                        capturedRat++;
                        world.setEntityState(rat, (byte) 86);
                        tag.setTag("Rat_" + capturedRat, ratTag);
                        rat.setDead();
                    }
                }
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1, 1);
            }
            this.setDead();
        }
        sack.setTagCompound(tag);
        ItemEntity itemEntity = new ItemEntity(world, this.posX, this.posY, this.posZ, sack);
        itemEntity.setEntityInvulnerable(true);
        if(!world.isRemote){
            world.addEntity(itemEntity);
        }
    }
}
