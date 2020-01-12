package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;

public class RatsCustomEvents {
    
    public static void onPlayerSwing(PlayerEntity player, ItemStack heldItem) {
        if (player.swingProgress == 0 && heldItem.getItem() == RatsItemRegistry.PLAGUE_SCYTHE) {
            Multimap<String, AttributeModifier> dmg = heldItem.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            double totalDmg = 0;
            for (AttributeModifier modifier : dmg.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                totalDmg += modifier.getAmount();
            }
            player.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
            EntityPlagueShot shot = new EntityPlagueShot(RatsEntityRegistry.PLAGUE_SHOT, player.world, player, totalDmg * 0.5F);
            shot.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 0.8F, 1.0F);
            player.world.addEntity(shot);
        }
    }
}
