package com.github.alexthe666.rats.server.entity.ai;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatUtils;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.message.MessageUpdateRatFluid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RatAIHarvestShears extends EntityAIBase {
    private static final int RADIUS = 16;
    private final EntityRat entity;
    private Entity targetSheep = null;
    private boolean reachedSheep = false;
    private int fishingCooldown = 1000;
    private int throwCooldown = 0;
    private static final ItemStack SHEAR_STACK = new ItemStack(Items.SHEARS);
    private Random rand = new Random();
    private Predicate<EntityLivingBase> SHEAR_PREDICATE = new com.google.common.base.Predicate<EntityLivingBase>() {
        public boolean apply(@Nullable EntityLivingBase entity) {
            return entity != null && entity instanceof IShearable && ((IShearable) entity).isShearable(SHEAR_STACK, entity.world, entity.getPosition());
        }
    };

    public RatAIHarvestShears(EntityRat entity) {
        super();
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.canMove() || !this.entity.isTamed() || this.entity.getCommand() != RatCommand.HARVEST || this.entity.isInCage() || !entity.hasUpgrade(RatsItemRegistry.RAT_UPGRADE_SHEARS)) {
            return false;
        }
        if (!this.entity.getHeldItemMainhand().isEmpty()) {
            return false;
        }
        resetTarget();
        return targetSheep != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return targetSheep != null && this.entity.getHeldItemMainhand().isEmpty();
    }

    public void resetTask() {
        this.entity.getNavigator().clearPath();
        resetTarget();
    }


    @Override
    public void updateTask() {
        if (this.targetSheep != null && !this.targetSheep.isDead && this.entity.getHeldItemMainhand().isEmpty()) {
            this.entity.getNavigator().tryMoveToEntityLiving(this.targetSheep, 1D);
            if (entity.getDistance(targetSheep) < 1.5D) {
                if(targetSheep instanceof IShearable){
                    java.util.List<ItemStack> drops = ((IShearable) targetSheep).onSheared(new ItemStack(Items.SHEARS), this.entity.world, targetSheep.getPosition(), 0);
                    for(ItemStack stack : drops){
                        targetSheep.entityDropItem(stack, 0.0F);
                    }
                }
                this.targetSheep = null;
                this.resetTask();
            }
        }else{
            this.resetTask();
        }
    }

    private void resetTarget() {
        List<EntityLiving> list = this.entity.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, this.entity.getEntityBoundingBox().grow(RADIUS), (com.google.common.base.Predicate<? super EntityLiving>) SHEAR_PREDICATE);
        EntityLivingBase closestSheep = null;
        for(EntityLivingBase base : list) {
            if(closestSheep == null || base.getDistanceSq(entity) < closestSheep.getDistanceSq(entity)){
                closestSheep = base;
            }
        }
        if (closestSheep != null) {
            this.targetSheep = closestSheep;
        }
    }

}