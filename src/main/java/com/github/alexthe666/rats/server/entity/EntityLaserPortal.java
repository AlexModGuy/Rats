package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityLaserPortal extends Entity {
    @Nullable
    private EntityLivingBase creator;
    @Nullable
    private Entity facingTarget;
    private UUID ownerUniqueId;
    public float scaleOfPortal;
    public float scaleOfPortalPrev;
    public EntityLaserPortal(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 1.5F);
        this.isImmuneToFire = true;
    }

    public EntityLaserPortal(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setSize(0.9F, 1.5F);
        this.setPosition(x, y, z);
        this.isImmuneToFire = true;
    }

    public EntityLaserPortal(World worldIn, double x, double y, double z, EntityLivingBase creator) {
        this(worldIn);
        this.setSize(0.9F, 1.5F);
        this.setPosition(x, y, z);
        this.setCreator(creator);
        this.isImmuneToFire = true;
    }

    public void setCreator(@Nullable EntityLivingBase ownerIn) {
        this.creator = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUniqueID();
    }

    public void onUpdate() {
        super.onUpdate();
        if(ticksExisted > 300){
            this.setDead();
        }
        if(ticksExisted < 250 && scaleOfPortal < 1.0F){
            scaleOfPortal += 0.05F;
        }
        if(ticksExisted > 250 && scaleOfPortal > 0.0F){
            scaleOfPortal -= 0.05F;
        }
        if(ticksExisted % 50 == 0){
            this.tryFiring();
        }else{
            faceTarget();
        }
        if(world.isRemote && scaleOfPortal >= 0.5F){
            RatsMod.PROXY.spawnParticle("rat_lightning", this.posX + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    this.posY + (double) (this.rand.nextFloat() * this.height),
                    this.posZ + (double) (this.rand.nextFloat() * this.width) - (double) this.width / 2,
                    0.0F,  0.0F, 0.0F);
        }
        scaleOfPortalPrev = scaleOfPortal;
    }

    private void faceTarget(){
        if(facingTarget == null ||this.getCreator() != null && !facingTarget.isEntityEqual(((EntityLiving) this.getCreator()).getAttackTarget())){
            if(this.getCreator() != null && this.getCreator() instanceof EntityLiving) {
                EntityLivingBase target = ((EntityLiving) this.getCreator()).getAttackTarget();
                if (target == null && this.getCreator() instanceof EntityMob) {
                    target = world.getNearestPlayerNotCreative(this, 30);
                }
                facingTarget = target;
            }
        }
        if (facingTarget != null) {
            double d0 = this.posX - facingTarget.posX;
            double d2 = this.posZ - facingTarget.posZ;
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            this.rotationYaw = f % 360;
        }
    }
    private void tryFiring() {
        if(this.getCreator() != null && this.getCreator() instanceof EntityLiving){
            EntityLivingBase target = ((EntityLiving) this.getCreator()).getAttackTarget();
            if(target == null && this.getCreator() instanceof EntityMob){
                target = world.getNearestPlayerNotCreative(this, 30);
            }
            if(target != null){
                double d0 = this.posX - target.posX;
                double d1 = this.posY - (target.posY);
                double d2 = this.posZ - target.posZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.rotationYaw = f % 360;
                double targetRelativeX = target.posX - this.posX;
                double targetRelativeY = target.posY - this.posY - 1.0F;
                double targetRelativeZ = target.posZ - this.posZ;
                EntityNeoBeam beam = new EntityNeoBeam(world, this.getCreator());
                beam.setPosition(this.posX, this.posY + 1.0F, this.posZ);
                beam.shoot(targetRelativeX, targetRelativeY, targetRelativeZ, 2.0F, 0.4F);
                if(!world.isRemote){
                    world.spawnEntity(beam);
                }
            }
        }
    }

    @Nullable
    public EntityLivingBase getCreator() {
        if (this.creator == null && this.ownerUniqueId != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.ownerUniqueId);

            if (entity instanceof EntityLivingBase) {
                this.creator = (EntityLivingBase) entity;
            }
        }

        return this.creator;
    }

    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.ticksExisted = compound.getInteger("Age");
        this.ownerUniqueId = compound.getUniqueId("OwnerUUID");
    }

    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Age", this.ticksExisted);

        if (this.ownerUniqueId != null) {
            compound.setUniqueId("OwnerUUID", this.ownerUniqueId);
        }
    }

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }


    @Override
    protected void entityInit() {

    }
}
