package com.github.alexthe666.rats.server.compat.tinkers;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitDisease extends AbstractTrait {

    public TraitDisease() {
        super("disease", 0xffffff);
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        target.addPotionEffect(new PotionEffect(RatsMod.PLAGUE_POTION, 2000, 0));
    }
}