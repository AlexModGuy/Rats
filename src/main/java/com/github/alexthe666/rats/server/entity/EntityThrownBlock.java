package com.github.alexthe666.rats.server.entity;


import com.github.alexthe666.rats.RatConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityThrownBlock extends Entity {
    public LivingEntity shootingEntity;
    public BlockState fallTile;
    public boolean dropBlock = true;
    public CompoundNBT tileEntityData;
    private int ticksAlive;
    private int ticksInAir;

    public EntityThrownBlock(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityThrownBlock(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.THROWN_BLOCK, worldIn);
    }

    public EntityThrownBlock(EntityType type, World worldIn, BlockState blockState, LivingEntity entityNeoRatlantean) {
        super(type, worldIn);
        this.fallTile = blockState;
        this.shootingEntity = entityNeoRatlantean;
    }

    protected void registerData() {
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getBoundingBox().getAverageEdgeLength() * 4.0D;

        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    public void tick() {
        if (this.world.isRemote || (this.shootingEntity == null || this.shootingEntity.isAlive()) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.tick();

            ++this.ticksInAir;
            if (ticksInAir > 25) {
                this.noClip = true;
                RayTraceResult raytraceresult = ProjectileHelper.func_221266_a(this, true, this.ticksInAir >= 25, this.shootingEntity, RayTraceContext.BlockMode.COLLIDER);
                if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onImpact(raytraceresult);
                }
            } else {
                this.noClip = false;
            }
            this.posX += this.getMotion().x;
            this.posY += this.getMotion().y;
            this.posZ += this.getMotion().z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;
                    this.world.addParticle(ParticleTypes.BUBBLE, this.posX - this.getMotion().x * 0.25D, this.posY - this.getMotion().y * 0.25D, this.posZ - this.getMotion().z * 0.25D, this.getMotion().x, this.getMotion().y, this.getMotion().z);
                }

                f = 0.8F;
            }
            if (this.shootingEntity != null && shootingEntity instanceof LivingEntity) {
                if (((MobEntity) shootingEntity).getAttackTarget() != null) {
                    LivingEntity target = ((MobEntity) shootingEntity).getAttackTarget();
                    double d0 = target.posX - this.posX;
                    double d1 = target.posY - this.posY;
                    double d2 = target.posZ - this.posZ;
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    d3 = (double) MathHelper.sqrt(d3);
                    Vec3d vec3d = this.getMotion();
                    vec3d.add(d0 / d3 * 0.2D, d1 / d3 * 0.2D, d2 / d3 * 0.2D);
                    this.setMotion(vec3d);
                }
            }
            this.setPosition(this.posX, this.posY, this.posZ);
        } else {
            this.remove();
        }
    }

    public boolean canEntityBeSeen(Entity entityIn) {
        return this.world.rayTraceBlocks(new RayTraceContext(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + (double) entityIn.getEyeHeight(), entityIn.posZ), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this)) == null;
    }

    protected float getMotionFactor() {
        return 0.95F;
    }

    protected void onImpact(RayTraceResult result) {
        if (fallTile != null) {
            Block block = this.fallTile.getBlock();
            BlockPos pos = null;
            if (result instanceof BlockRayTraceResult) {
                pos = ((BlockRayTraceResult) result).getPos();
            }
            if (result instanceof EntityRayTraceResult) {
                pos = new BlockPos(((EntityRayTraceResult) result).getEntity());
            }
            if (pos != null) {
                for (Entity hitMobs : world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(1.0F, 1.0F, 1.0F))) {
                    hitMobs.attackEntityFrom(DamageSource.IN_WALL, RatConfig.neoRatlanteanAttack);
                }
                BlockPos blockpos1 = pos.up();
                if (true) {
                    if (dropBlock) {
                        this.world.setBlockState(blockpos1, this.fallTile);
                    }
                    if (block instanceof FallingBlock) {
                        ((FallingBlock) block).onEndFalling(this.world, blockpos1, this.fallTile, fallTile);
                    }
                    if (this.tileEntityData != null && block.hasTileEntity(this.fallTile)) {
                        TileEntity tileentity = this.world.getTileEntity(blockpos1);

                        if (tileentity != null) {
                            CompoundNBT CompoundNBT = tileentity.write(new CompoundNBT());

                            for (String s : this.tileEntityData.keySet()) {
                                INBT nbtbase = this.tileEntityData.get(s);

                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                    CompoundNBT.put(s, nbtbase.copy());
                                }
                            }

                            tileentity.read(CompoundNBT);
                            tileentity.markDirty();
                        }
                    }
                    this.remove();
                } else if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                    if (!world.isRemote && dropBlock) {
                        this.entityDropItem(new ItemStack(block, 1), 0.0F);
                    }
                    this.remove();
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(CompoundNBT compound) {
        compound.put("direction", this.newDoubleNBTList(this.getMotion().x, this.getMotion().y, this.getMotion().z));
        compound.putInt("life", this.ticksAlive);
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = Registry.BLOCK.getKey(block);
        compound.putString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        if (this.tileEntityData != null) {
            compound.put("TileEntityData", this.tileEntityData);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(CompoundNBT compound) {
        if (compound.contains("power", 9)) {
            ListNBT nbttaglist = compound.getList("power", 6);
        }

        this.ticksAlive = compound.getInt("life");

        if (compound.contains("direction", 9) && compound.getList("direction", 6).size() == 3) {
            ListNBT nbttaglist1 = compound.getList("direction", 6);
            this.setMotion(nbttaglist1.getDouble(0), nbttaglist1.getDouble(1), nbttaglist1.getDouble(2));
        } else {
            this.remove();
        }
        int i = compound.getByte("Data") & 255;

        if (compound.contains("Block", 8)) {
            this.fallTile = Registry.BLOCK.getOrDefault(new ResourceLocation(compound.getString("Block"))).getDefaultState();
        } else if (compound.contains("TileID", 99)) {
            this.fallTile = Block.getStateById(compound.getInt("TileID")).getBlockState();
        } else {
            this.fallTile = Block.getStateById(compound.getByte("Tile") & 255).getBlockState();
        }

        Block block = this.fallTile.getBlock();
        if (block == null || block.getDefaultState().getMaterial() == Material.AIR) {
            this.fallTile = Blocks.GRASS.getDefaultState();
        }
        if (compound.contains("TileEntityData", 10)) {
            this.tileEntityData = compound.getCompound("TileEntityData");
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            this.markVelocityChanged();

            if (source.getTrueSource() != null) {
                Vec3d vec3d = source.getTrueSource().getLookVec();

                if (vec3d != null) {
                    this.setMotion(vec3d);
                }

                if (source.getTrueSource() instanceof LivingEntity) {
                    this.shootingEntity = (LivingEntity) source.getTrueSource();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public float getBrightness() {
        return 1.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}