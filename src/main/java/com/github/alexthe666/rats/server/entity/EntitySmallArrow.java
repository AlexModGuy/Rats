package com.github.alexthe666.rats.server.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySmallArrow extends AbstractArrowEntity {

    public EntitySmallArrow(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntitySmallArrow( World worldIn, LivingEntity shooter) {
        super(RatsEntityRegistry.SMALL_ARROW, shooter, worldIn);
    }

    public EntitySmallArrow(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(RatsEntityRegistry.SMALL_ARROW, world);
    }

    protected boolean func_230298_a_(Entity p_230298_1_) {
        Entity shooter = this.func_234616_v_();
        return super.func_230298_a_(p_230298_1_) && (shooter == null || !shooter.isOnSameTeam(p_230298_1_));
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
