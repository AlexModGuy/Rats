package com.github.alexthe666.rats.server.potion;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;

public class PotionPlague extends Effect {

    public PotionPlague() {
        super(EffectType.HARMFUL, 0X445637);
        this.setRegistryName(RatsMod.MODID, "plague");
    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {
    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

    public String getName() {
        return "rats.plague";
    }

    public java.util.List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
