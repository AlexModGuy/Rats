package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityRatArrow extends EntityArrow {

    private ItemStack stack;

    public EntityRatArrow(World worldIn) {
        super(worldIn);
        this.stack = new ItemStack(RatsItemRegistry.RAT_ARROW);
    }

    public EntityRatArrow(World worldIn, EntityLivingBase shooter, ItemStack stack) {
        super(worldIn, shooter);
        this.stack = stack;
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        Entity entity = raytraceResultIn.entityHit;
        EntityRat rat = new EntityRat(world);
        NBTTagCompound ratTag = new NBTTagCompound();
        if(stack.getTagCompound() != null && !stack.getTagCompound().getCompoundTag("Rat").isEmpty()){
            ratTag = stack.getTagCompound().getCompoundTag("Rat");
        }
        rat.readEntityFromNBT(ratTag);
        BlockPos pos = raytraceResultIn.getBlockPos();
        if(pos == null){
          pos = new BlockPos(raytraceResultIn.hitVec);
        }else{
            if(raytraceResultIn.sideHit != null){
                pos = pos.offset(raytraceResultIn.sideHit);
            }
        }
        rat.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        if(!world.isRemote){
            world.addEntity(rat);
        }
        if (entity != null && entity instanceof EntityLivingBase && !rat.isOnSameTeam(entity)) {
            rat.setAttackTarget((EntityLivingBase)entity);
        }
        if(this.inGround){
            this.setDead();
            if(!world.isRemote){
                this.entityDropItem(getArrowStack(), 0.0F);
            }
        }
    }

}
