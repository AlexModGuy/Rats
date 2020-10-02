package com.github.alexthe666.rats.server.entity.ratlantis;

import com.github.alexthe666.rats.server.entity.RatExplosion;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class EntityCheeseCannonball extends ThrowableEntity implements IRendersAsItem {
    @Override
    protected void registerData() {

    }

    public EntityCheeseCannonball(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCheeseCannonball(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatlantisEntityRegistry.CHEESE_CANNONBALL, worldIn);
    }

    public EntityCheeseCannonball(EntityType type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityCheeseCannonball(EntityType type, World worldIn, double x, double y, double z) {
        super(type, x, y, z, worldIn);
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

    @OnlyIn(Dist.CLIENT)
    public int getBrightnessForRender() {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onImpact(RayTraceResult result) {
        if(result instanceof EntityRayTraceResult && func_234616_v_() != null && func_234616_v_().isOnSameTeam(((EntityRayTraceResult) result).getEntity())){
            return;
        }
        if (!this.world.isRemote) {
            if (result instanceof EntityRayTraceResult) {
                EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
                if((func_234616_v_() == null || !entityResult.getEntity().isOnSameTeam(func_234616_v_())) && entityResult.getEntity() instanceof LivingEntity){
                    ((LivingEntity)entityResult.getEntity()).attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 8.0F);
                }
            }
            Explosion explosion = new RatExplosion(world, func_234616_v_() == null ? this : func_234616_v_(), this.getPosX(), this.getPosY(), this.getPosZ(), 1.0F, false, net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, func_234616_v_() == null ? this : func_234616_v_()));
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public ItemStack getItem() {
        return new ItemStack(RatlantisItemRegistry.CHEESE_CANNONBALL);
    }
}