package com.github.alexthe666.rats.server.entity;

import com.github.alexthe666.rats.server.entity.ratlantis.IRatlantean;
import com.github.alexthe666.rats.server.items.RatlantisItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EntityRatfish extends AbstractGroupFishEntity implements IRatlantean {
    public EntityRatfish(EntityType<? extends EntityRatfish> type, World world) {
        super(type, world);
    }

    protected ItemStack getFishBucket() {
        return new ItemStack(RatlantisItemRegistry.RATFISH_BUCKET);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }
}