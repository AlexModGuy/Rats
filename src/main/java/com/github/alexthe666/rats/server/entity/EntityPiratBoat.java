package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class EntityPiratBoat extends EntityLiving implements IRatlantean {
    private static final List<ItemStack> EMPTY_EQUIPMENT = Collections.<ItemStack>emptyList();
    public static final ItemStack BANNER = generateBanner();
    private final float[] paddlePositions;
    private static final DataParameter<Boolean> FIRING = EntityDataManager.createKey(EntityPiratBoat.class, DataSerializers.BOOLEAN);
    private boolean prevFire;
    private int fireCooldown = 0;
    private double waterLevel;

    public EntityPiratBoat(World worldIn) {
        super(worldIn);
        this.paddlePositions = new float[2];
        this.setSize(1.75F, 0.8F);
    }

    public boolean writeToNBTOptional(NBTTagCompound compound) {
        String s = this.getEntityString();
        compound.setString("id", s);
        this.writeToNBT(compound);
        return true;
    }

    public boolean canBeSteered()
    {
        return true;
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(FIRING, Boolean.valueOf(false));
    }

    public void setFiring(boolean male) {
        this.dataManager.set(FIRING, Boolean.valueOf(male));
    }

    public boolean isFiring() {
        return this.dataManager.get(FIRING).booleanValue();
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        this.motionY *= 0.6D;
        this.livingSoundTime = 20;
        if (this.deathTime >= 80) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            if (!this.world.isRemote) {
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Items.STICK), 0.0F);
                }
                for (int j = 0; j < rand.nextInt(3); j++) {
                    this.entityDropItem(new ItemStack(Blocks.PLANKS), 0.0F);
                }
                if (rand.nextInt(3) == 0) {
                    this.entityDropItem(BANNER.copy(), 0.0F);
                }
                if (rand.nextInt(2) == 0) {
                    this.entityDropItem(new ItemStack(RatsItemRegistry.CHEESE_CANNONBALL), 0.0F);
                }
            }
            this.setDead();
            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1, new int[0]);
            }
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if(this.posY < this.waterLevel && deathTime == 0){
            this.setPosition(this.posX, this.waterLevel, this.posZ);
        }
        checkInWater();
        if (this.prevFire != isFiring()) {
            fireCooldown = 4;
        }
        if (isFiring() && fireCooldown == 0) {
            setFiring(false);
        }
        if (fireCooldown > 0) {
            fireCooldown--;
        }
        prevFire = this.isFiring();
        if(!this.isBeingRidden()){
            this.attackEntityFrom(DamageSource.DROWN, 1000);
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
    }

    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof EntityBoat) {
            if (entityIn.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        } else if (entityIn.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
            super.applyEntityCollision(entityIn);
        }
    }

    protected void doWaterSplashEffect(){
    }

    private static ItemStack generateBanner() {
        NBTTagList patterns = new NBTTagList();
        NBTTagCompound currentPattern = new NBTTagCompound();
        currentPattern.setString("Pattern", "rats.rat_and_crossbones");
        currentPattern.setInteger("Color", 15);
        patterns.appendTag(currentPattern);
        return ItemBanner.makeBanner(EnumDyeColor.BLACK, patterns);
    }

    @SideOnly(Side.CLIENT)
    public float getRowingTime(int side, float limbSwing) {
        return (float) MathHelper.clampedLerp((double) this.paddlePositions[side] - 0.39269909262657166D, (double) this.paddlePositions[side], (double) limbSwing);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return EMPTY_EQUIPMENT;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    public void shoot(EntityPirat pirat) {
        EntityLivingBase target = pirat.getAttackTarget();
        if (target == null) {
            target = world.getNearestPlayerNotCreative(this, 30);
        }
        if (target != null) {
            {
                double d0 = target.posX - this.posX;
                double d2 = target.posZ - this.posZ;
                float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.renderYawOffset = this.rotationYaw = f % 360;
            }
            EntityCheeseCannonball cannonball = new EntityCheeseCannonball(world, pirat);
            cannonball.ignoreEntity = this;
            float radius = 1.4F;
            float angle = (0.01745329251F * (this.renderYawOffset));
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle))) + posX;
            double extraZ = (double) (radius * MathHelper.cos(angle)) + posZ;
            double extraY = 0.8 + posY;
            double d0 = target.posY + (double) target.getEyeHeight();
            double d1 = target.posX - extraX;
            double d2 = d0 - extraY;
            double d3 = target.posZ - extraZ;
            float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
            cannonball.setPosition(extraX, extraY, extraZ);
            cannonball.shoot(d1, d2 + (double) f, d3, 1F, 0F);
            this.playSound(SoundEvents.ENTITY_FIREWORK_SHOOT, 3.0F, 0.3F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                this.world.spawnEntity(cannonball);

            }
            this.setFiring(true);
        }
    }

    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);

                        if (iblockstate.getMaterial() == Material.WATER) {
                            float f = BlockLiquid.getLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos);
                            this.waterLevel = Math.max((double) f, this.waterLevel);
                            flag |= axisalignedbb.minY < (double) f;
                        }
                    }
                }
            }
        } finally {
            blockpos$pooledmutableblockpos.release();
        }

        return flag;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BLOCK_WOOD_BREAK;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BLOCK_WOOD_HIT;
    }

    public boolean shouldDismountInWater(Entity rider){
        return false;
    }
}
