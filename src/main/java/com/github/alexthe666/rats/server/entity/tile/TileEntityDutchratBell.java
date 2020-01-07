package com.github.alexthe666.rats.server.entity.tile;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TileEntityDutchratBell extends TileEntity implements ITickableTileEntity {
    private long ringTime;
    public int field_213943_a;
    public boolean field_213944_b;
    public Direction field_213945_c;
    private List<LivingEntity> entitiesAtRing;
    private boolean field_213948_i;
    private int field_213949_j;

    public TileEntityDutchratBell() {
        super(RatsTileEntityRegistry.DUTCHRAT_BELL);
    }

    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.func_213941_c();
            this.field_213949_j = 0;
            this.field_213945_c = Direction.byIndex(type);
            this.field_213943_a = 0;
            this.field_213944_b = true;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    public void tick() {
        if (this.field_213944_b) {
            ++this.field_213943_a;
        }

        if (this.field_213943_a >= 50) {
            this.field_213944_b = false;
            this.field_213943_a = 0;
        }

        if (this.field_213943_a >= 5 && this.field_213949_j == 0 && this.hasRaidersNearby()) {
            this.field_213948_i = true;
            this.func_222833_c();
        }

        if (this.field_213948_i) {
            if (this.field_213949_j < 40) {
                ++this.field_213949_j;
            } else {
                this.func_222828_b(this.world);
                this.func_222826_c(this.world);
                this.field_213948_i = false;
            }
        }

    }

    private void func_222833_c() {
        this.world.playSound((PlayerEntity)null, this.getPos(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void func_213939_a(Direction p_213939_1_) {
        BlockPos blockpos = this.getPos();
        this.field_213945_c = p_213939_1_;
        if (this.field_213944_b) {
            this.field_213943_a = 0;
        } else {
            this.field_213944_b = true;
        }

        this.world.addBlockEvent(blockpos, this.getBlockState().getBlock(), 1, p_213939_1_.getIndex());
    }

    private void func_213941_c() {
        BlockPos blockpos = this.getPos();
        if (this.world.getGameTime() > this.ringTime + 60L || this.entitiesAtRing == null) {
            this.ringTime = this.world.getGameTime();
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos)).grow(48.0D);
            this.entitiesAtRing = this.world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
        }

        if (!this.world.isRemote) {
            for(LivingEntity livingentity : this.entitiesAtRing) {
                if (livingentity.isAlive() && !livingentity.removed && blockpos.withinDistance(livingentity.getPositionVec(), 32.0D)) {
                    livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.world.getGameTime());
                }
            }
        }

    }

    private boolean hasRaidersNearby() {
        BlockPos blockpos = this.getPos();

        for(LivingEntity livingentity : this.entitiesAtRing) {
            if (livingentity.isAlive() && !livingentity.removed && blockpos.withinDistance(livingentity.getPositionVec(), 32.0D) && livingentity.getType().isContained(EntityTypeTags.RAIDERS)) {
                return true;
            }
        }

        return false;
    }

    private void func_222828_b(World p_222828_1_) {
        if (!p_222828_1_.isRemote) {
            this.entitiesAtRing.stream().filter(this::isNearbyRaider).forEach(this::glow);
        }
    }

    private void func_222826_c(World p_222826_1_) {
        if (p_222826_1_.isRemote) {
            BlockPos blockpos = this.getPos();
            AtomicInteger atomicinteger = new AtomicInteger(16700985);
            int i = (int)this.entitiesAtRing.stream().filter((p_222829_1_) -> {
                return blockpos.withinDistance(p_222829_1_.getPositionVec(), 48.0D);
            }).count();
            this.entitiesAtRing.stream().filter(this::isNearbyRaider).forEach((p_222831_4_) -> {
                float f = 1.0F;
                float f1 = MathHelper.sqrt((p_222831_4_.posX - (double)blockpos.getX()) * (p_222831_4_.posX - (double)blockpos.getX()) + (p_222831_4_.posZ - (double)blockpos.getZ()) * (p_222831_4_.posZ - (double)blockpos.getZ()));
                double d0 = (double)((float)blockpos.getX() + 0.5F) + (double)(1.0F / f1) * (p_222831_4_.posX - (double)blockpos.getX());
                double d1 = (double)((float)blockpos.getZ() + 0.5F) + (double)(1.0F / f1) * (p_222831_4_.posZ - (double)blockpos.getZ());
                int j = MathHelper.clamp((i - 21) / -2, 3, 15);

                for(int k = 0; k < j; ++k) {
                    atomicinteger.addAndGet(5);
                    double d2 = (double)(atomicinteger.get() >> 16 & 255) / 255.0D;
                    double d3 = (double)(atomicinteger.get() >> 8 & 255) / 255.0D;
                    double d4 = (double)(atomicinteger.get() & 255) / 255.0D;
                    p_222826_1_.addParticle(ParticleTypes.ENTITY_EFFECT, d0, (double)((float)blockpos.getY() + 0.5F), d1, d2, d3, d4);
                }

            });
        }
    }

    private boolean isNearbyRaider(LivingEntity p_222832_1_) {
        return p_222832_1_.isAlive() && !p_222832_1_.removed && this.getPos().withinDistance(p_222832_1_.getPositionVec(), 48.0D) && p_222832_1_.getType().isContained(EntityTypeTags.RAIDERS);
    }

    private void glow(LivingEntity p_222827_1_) {
        p_222827_1_.addPotionEffect(new EffectInstance(Effects.GLOWING, 60));
    }
}