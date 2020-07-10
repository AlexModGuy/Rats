package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class EntityRatGolemMount extends EntityRatMountBase {

    private int attackTimer;

    protected EntityRatGolemMount(EntityType<? extends MobEntity> type, World worldIn) {
        super(type, worldIn);
        this.riderY = 2.05F;
        this.riderXZ = -0.1F;
        this.stepHeight = 1.0F;
        this.upgrade = RatsItemRegistry.RAT_UPGRADE_GOLEM_MOUNT;
    }

    public static AttributeModifierMap.MutableAttribute buildAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(Attributes.field_233818_a_, 100.0D)            //HEALTH
                .func_233815_a_(Attributes.field_233821_d_, 0.2D)           //SPEED
                .func_233815_a_(Attributes.field_233823_f_, 1.0D)            //ATTACK
                .func_233815_a_(Attributes.field_233820_c_, 1.0D);            //KNOCKBACK RESIST
    }

    public void livingTick() {
        super.livingTick();
        if (this.attackTimer > 0) {
            --this.attackTimer;
        }

        if (horizontalMag(this.getMotion()) > (double)2.5000003E-7F && this.rand.nextInt(5) == 0) {
            int i = MathHelper.floor(this.getPosX());
            int j = MathHelper.floor(this.getPosY() - (double)0.2F);
            int k = MathHelper.floor(this.getPosZ());
            BlockState blockstate = this.world.getBlockState(new BlockPos(i, j, k));
            if (!blockstate.isAir()) {
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate), this.getPosX() + ((double)this.rand.nextFloat() - 0.5D) * (double)this.getWidth(), this.getBoundingBox().minY + 0.1D, this.getPosZ() + ((double)this.rand.nextFloat() - 0.5D) * (double)this.getWidth(), 4.0D * ((double)this.rand.nextFloat() - 0.5D), 0.5D, ((double)this.rand.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        Cracks irongolementity$cracks = this.getCracks();
        boolean flag = super.attackEntityFrom(source, amount);
        if (flag && this.getCracks() != irongolementity$cracks) {
            this.playSound(SoundEvents.field_226142_fM_, 1.0F, 1.0F);
        }
        return flag;
    }

    public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();
        if (item != Items.IRON_INGOT) {
            return super.func_230254_b_(player, hand);
        } else {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() == f) {
                return super.func_230254_b_(player, hand);
            } else {
                float f1 = 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.field_226143_fP_, 1.0F, f1);
                if (!player.abilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            }
        }
    }

    public EntityRatGolemMount.Cracks getCracks() {
        return EntityRatGolemMount.Cracks.func_226515_a_(this.getHealth() / this.getMaxHealth());
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        this.attackTimer = 10;
        this.world.setEntityState(this, (byte)4);
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + this.rand.nextInt(15)));
        if (flag) {
            entityIn.setMotion(entityIn.getMotion().add(0.0D, (double)0.4F, 0.0D));
            this.applyEnchantments(this, entityIn);
        }

        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.attackTimer = 10;
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        } else {
            super.handleStatusUpdate(id);
        }

    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTimer() {
        return this.attackTimer;
    }

    public static enum Cracks {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<EntityRatGolemMount.Cracks> field_226513_e_ = Stream.of(values()).sorted(Comparator.comparingDouble((p_226516_0_) -> {
            return (double)p_226516_0_.field_226514_f_;
        })).collect(ImmutableList.toImmutableList());
        private final float field_226514_f_;

        private Cracks(float p_i225732_3_) {
            this.field_226514_f_ = p_i225732_3_;
        }

        public static EntityRatGolemMount.Cracks func_226515_a_(float p_226515_0_) {
            for(EntityRatGolemMount.Cracks EntityRatGolemMount$cracks : field_226513_e_) {
                if (p_226515_0_ < EntityRatGolemMount$cracks.field_226514_f_) {
                    return EntityRatGolemMount$cracks;
                }
            }

            return NONE;
        }
    }
}
