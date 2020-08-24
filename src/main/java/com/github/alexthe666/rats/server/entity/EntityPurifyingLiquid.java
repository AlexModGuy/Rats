package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class EntityPurifyingLiquid extends ProjectileItemEntity implements IRendersAsItem {

    private static final DataParameter<Boolean> NETHER = EntityDataManager.createKey(EntityPurifyingLiquid.class, DataSerializers.BOOLEAN);

    public EntityPurifyingLiquid(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerData() {
        this.getDataManager().register(NETHER, false);
    }

    public EntityPurifyingLiquid(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.PURIFYING_LIQUID, worldIn);
    }

    public EntityPurifyingLiquid(World worldIn, LivingEntity throwerIn, boolean nether) {
        super(RatsEntityRegistry.PURIFYING_LIQUID, throwerIn, worldIn);
        this.dataManager.set(NETHER, nether);
    }

    public EntityPurifyingLiquid(World worldIn, double x, double y, double z) {
        super(RatsEntityRegistry.PURIFYING_LIQUID, x, y, z, worldIn);
    }


    public ItemStack getPotion() {
        return new ItemStack(RatsItemRegistry.PURIFYING_LIQUID);
    }

    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            ItemStack itemstack = this.getPotion();
            Potion potiontype = PotionUtils.getPotionFromItem(itemstack);
            AxisAlignedBB axisalignedbb = this.getBoundingBox().grow(4.0D, 2.0D, 4.0D);
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
            if (!list.isEmpty()) {
                for (LivingEntity LivingEntity : list) {
                    if (LivingEntity.canBeHitWithPotion()) {
                        double d0 = this.getDistanceSq(LivingEntity);
                        if (d0 < 16.0D) {
                            if(dataManager.get(NETHER)){
                                if (LivingEntity instanceof EntityDemonRat) {
                                    EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, world);
                                    rat.copyLocationAndAnglesFrom(LivingEntity);
                                    if(!world.isRemote){
                                        rat.onInitialSpawn((IServerWorld) world, world.getDifficultyForLocation(new BlockPos(this.getPositionVec())), SpawnReason.SPAWNER, null, null);
                                    }
                                    rat.setPlague(false);
                                    rat.setTamed(false);
                                    rat.setOwnerId(null);
                                    world.addEntity(rat);
                                    LivingEntity.remove();
                                }
                            }else{
                                if (LivingEntity instanceof EntityRat) {
                                    EntityRat rat = (EntityRat) LivingEntity;
                                    if (rat.hasPlague()) {
                                        rat.setPlague(false);
                                        rat.setTamed(false);
                                        rat.setOwnerId(null);
                                    }
                                }
                                if (LivingEntity.isPotionActive(RatsMod.PLAGUE_POTION)) {
                                    LivingEntity.removePotionEffect(RatsMod.PLAGUE_POTION);
                                }
                                if (LivingEntity instanceof IPlagueLegion) {
                                    LivingEntity.attackEntityFrom(DamageSource.MAGIC, 10);
                                }
                                if (LivingEntity instanceof ZombieVillagerEntity && !((ZombieVillagerEntity) LivingEntity).isConverting()) {
                                    CompoundNBT tag = new CompoundNBT();
                                    LivingEntity.writeAdditional(tag);
                                    tag.putInt("ConversionTime", 200);
                                    LivingEntity.readAdditional(tag);
                                    this.world.setEntityState(LivingEntity, (byte) 16);
                                }
                            }
                        }
                    }
                }
            }
            int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            this.world.playEvent(i, new BlockPos(this.getPositionVec()), dataManager.get(NETHER) ? 0XAD141E : 0XBFDFE2);
            this.remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return dataManager.get(NETHER) ? RatsItemRegistry.CRIMSON_FLUID : RatsItemRegistry.PURIFYING_LIQUID;
    }

    public ItemStack getItem() {
        return new ItemStack(dataManager.get(NETHER) ? RatsItemRegistry.CRIMSON_FLUID : RatsItemRegistry.PURIFYING_LIQUID);
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}