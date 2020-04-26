package com.github.alexthe666.rats.server.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemUseContextAccess extends ItemUseContext {

    public ItemUseContextAccess(World worldIn, @Nullable PlayerEntity player, Hand handIn, ItemStack heldItem, BlockRayTraceResult rayTraceResultIn) {
        super(worldIn, player, handIn, heldItem, rayTraceResultIn);
    }
}
