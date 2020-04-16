package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.RatConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityLaserBeam extends AbstractArrowEntity {

    private static final DataParameter<Float> R = EntityDataManager.createKey(EntityLaserBeam.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> G = EntityDataManager.createKey(EntityLaserBeam.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> B = EntityDataManager.createKey(EntityLaserBeam.class, DataSerializers.FLOAT);

    public EntityLaserBeam(EntityType type, World worldIn) {
        super(type, worldIn);
        this.setDamage(6F);
    }

    public EntityLaserBeam(EntityType type, World worldIn, double x, double y, double z, float r, float g, float b) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(6F);
    }

    public EntityLaserBeam(EntityType type, World worldIn, LivingEntity shooter) {
        super(type, shooter, worldIn);
        this.setDamage(RatConfig.neoRatlanteanAttack);
    }

    public EntityLaserBeam(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(RatsEntityRegistry.LASER_BEAM, worldIn);
    }

    public boolean isInWater() {
        return false;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(R, Float.valueOf(0.66F));
        this.dataManager.register(G, Float.valueOf(0.97F));
        this.dataManager.register(B, Float.valueOf(0.97F));
    }

    public float[] getRGB() {
        return new float[]{this.dataManager.get(R), this.dataManager.get(G), this.dataManager.get(B)};
    }

    public void setRGB(float newR, float newG, float newB) {
        this.dataManager.set(R, newR);
        this.dataManager.set(G, newG);
        this.dataManager.set(B, newB);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("ColorR", getRGB()[0]);
        compound.putFloat("ColorG", getRGB()[1]);
        compound.putFloat("ColorB", getRGB()[2]);
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setRGB(compound.getFloat("ColorR"), compound.getFloat("ColorG"), compound.getFloat("ColorB"));
    }

    public void tick() {
        float sqrt = (float)this.getMotion().length();
        if (sqrt < 0.3F || this.inGround || this.collidedHorizontally) {
            this.remove();
            Explosion.Mode mode = world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.Mode.NONE : Explosion.Mode.DESTROY;
            Explosion explosion = world.createExplosion(this.getShooter(), this.getPosX(), this.getPosY(), this.getPosZ(), 0.0F, mode);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
        super.tick();
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundIn != SoundEvents.ENTITY_ARROW_HIT && soundIn != SoundEvents.ENTITY_ARROW_HIT_PLAYER) {
            this.world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), soundIn, this.getSoundCategory(), volume, pitch);
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        if(raytraceResultIn instanceof EntityRayTraceResult && getShooter() != null && getShooter().isOnSameTeam(((EntityRayTraceResult) raytraceResultIn).getEntity())){
            return;
        }
        if (raytraceResultIn instanceof EntityRayTraceResult && ((EntityRayTraceResult) raytraceResultIn).getEntity() instanceof PlayerEntity) {
            this.damageShield((PlayerEntity) ((EntityRayTraceResult) raytraceResultIn).getEntity(), (float) this.getDamage());
        }
        super.onHit(raytraceResultIn);
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getActiveItemStack().getItem().isShield(player.getActiveItemStack(), player)) {
            ItemStack copyBeforeUse = player.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItemStack().damageItem(i, player, (p_220048_0_) -> {
                p_220048_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
            if (player.getActiveItemStack().isEmpty()) {
                Hand Hand = player.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);

                if (Hand == Hand.MAIN_HAND) {
                    this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                }
                player.resetActiveHand();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
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

    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
