package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityRatShot extends ThrowableEntity {

    private static final DataParameter<Integer> RAT_COLOR = EntityDataManager.createKey(EntityRatShot.class, DataSerializers.VARINT);

    @Override
    protected void registerData() {
        this.dataManager.register(RAT_COLOR, Integer.valueOf(0));

    }

    public EntityRatShot(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRatShot(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.RAT_SHOT, worldIn);
    }

    public EntityRatShot(EntityType type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityRatShot(EntityType type, World worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("RatColor", this.getColorVariant());
    }
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setColorVariant(compound.getInt("RatColor"));
    }

        @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            double d0 = 0.08D;

            for (int i = 0; i < 18; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(RatsItemRegistry.CHEESE)), this.getPosX(), this.getPosY(), this.getPosZ(), ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D, ((double) this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    public void tick() {
        super.tick();
        Vec3d vec3d = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(vec3d));
            this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI));
            this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, (double) f) * (double) (180F / (float) Math.PI));
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }

    protected void onImpact(RayTraceResult result) {
        if(result instanceof EntityRayTraceResult && getThrower() != null && getThrower().isOnSameTeam(((EntityRayTraceResult) result).getEntity())){
            return;
        }
        if (!this.world.isRemote) {
            if (result instanceof EntityRayTraceResult) {
                EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
                if((getThrower() == null || !entityResult.getEntity().isOnSameTeam(getThrower())) && entityResult.getEntity() instanceof LivingEntity){
                    ((LivingEntity)entityResult.getEntity()).attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 8.0F);
                }
            }
            Entity thrower = this.getThrower();
            if(thrower != null){
                EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, world);
                rat.copyLocationAndAnglesFrom(this);
                rat.setColorVariant(this.getColorVariant());
                rat.setTamedByPlayerFlag(false);
                rat.setTamed(true);
                if(thrower instanceof MobEntity){
                    rat.setAttackTarget(((MobEntity) thrower).getAttackTarget());
                }
                rat.setOwnerId(thrower.getUniqueID());
                if(thrower instanceof ISummonsRats){
                    ((ISummonsRats) thrower).setRatsSummoned(((ISummonsRats) thrower).getRatsSummoned() + 1);
                }
                world.addEntity(rat);
            }

            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getColorVariant() {
        return Integer.valueOf(this.dataManager.get(RAT_COLOR).intValue());
    }

    public void setColorVariant(int color) {
        this.dataManager.set(RAT_COLOR, Integer.valueOf(color));
    }

    public BlockPos getLightPosition() {
        BlockPos pos = new BlockPos(this);
        if (!world.getBlockState(pos).isSolid()) {
            return pos.up();
        }
        return pos;
    }
}