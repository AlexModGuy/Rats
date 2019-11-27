package com.github.alexthe666.rats.server.compat.tinkers;

import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import javax.annotation.Nullable;

public class TraitRatty extends AbstractTrait {

    private static float chance = 0.01f;
    private static float chance_fight = 0.35f;

    public TraitRatty() {
        super("ratty", TextFormatting.GRAY);
    }

    @Override
    public String getLocalizedName() {
        return Util.translate(String.format(LOC_Name, "ratty"));
    }

    @Override
    public String getLocalizedDesc() {
        return Util.translate(String.format(LOC_Desc, "ratty"));
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, BlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (wasEffective && !world.isRemote && random.nextFloat() < chance) {
            spawnRat(player, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world, null);
        }
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (target.isEntityAlive() && !target.getEntityWorld().isRemote && random.nextFloat() < chance_fight) {
            spawnRat(player, target.posX, target.posY, target.posZ, target.getEntityWorld(), target);
        }
    }

    protected void spawnRat(EntityLivingBase player, double x, double y, double z, World world, @Nullable EntityLivingBase target) {
        EntityRat entity = new EntityRat(world);
        entity.setPosition(x, y, z);
        world.addEntity(entity);
        entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(x, y, z)), null);
        entity.setPlague(false);
        if (target != null) {
            entity.setAttackTarget(target);
        }
        entity.playLivingSound();
    }
}
