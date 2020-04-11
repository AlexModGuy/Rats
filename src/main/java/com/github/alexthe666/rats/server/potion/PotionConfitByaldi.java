package com.github.alexthe666.rats.server.potion;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class PotionConfitByaldi extends Effect {

    public PotionConfitByaldi() {
        super(EffectType.BENEFICIAL, 0XFFDD59);
        this.setRegistryName(RatsMod.MODID, "synesthesia");
        this.addAttributesModifier(SharedMonsterAttributes.ATTACK_SPEED, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 10.0D, AttributeModifier.Operation.ADDITION);
    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {
        if (LivingEntityIn.getHealth() < LivingEntityIn.getMaxHealth()) {
            LivingEntityIn.heal(1.0F);
        }
        if (LivingEntityIn instanceof PlayerEntity) {
            ((PlayerEntity) LivingEntityIn).getFoodStats().addStats(100, 1.0F);
        }
    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

    public void removeAttributesModifiersFromEntity(LivingEntity LivingEntityIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        super.removeAttributesModifiersFromEntity(LivingEntityIn, attributeMapIn, amplifier);
        LivingEntityIn.setAbsorptionAmount(LivingEntityIn.getAbsorptionAmount() - (float) (20 * (amplifier + 1)));
        if (LivingEntityIn.getHealth() > LivingEntityIn.getMaxHealth()) {
            LivingEntityIn.setHealth(LivingEntityIn.getMaxHealth());
        }
    }

    public void applyAttributesModifiersToEntity(LivingEntity LivingEntityIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        LivingEntityIn.setAbsorptionAmount(LivingEntityIn.getAbsorptionAmount() + (float) (20 * (amplifier + 1)));
        super.applyAttributesModifiersToEntity(LivingEntityIn, attributeMapIn, amplifier);
    }

    public String getName() {
        return "rats.synesthesia";
    }

}
