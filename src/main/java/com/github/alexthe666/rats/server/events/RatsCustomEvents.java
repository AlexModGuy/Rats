package com.github.alexthe666.rats.server.events;

import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.items.RatsItemRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class RatsCustomEvents {
    
    public static void onPlayerSwing(PlayerEntity player, ItemStack heldItem) {
        if (player.swingProgress == 0 && heldItem.getItem() == RatsItemRegistry.PLAGUE_SCYTHE) {
            Multimap<Attribute, AttributeModifier> dmg = heldItem.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            double totalDmg = 0;
            for (AttributeModifier modifier : dmg.get(Attributes.field_233823_f_)) {
                totalDmg += modifier.getAmount();
            }
            player.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
            EntityPlagueShot shot = new EntityPlagueShot(RatsEntityRegistry.PLAGUE_SHOT, player.world, player, totalDmg * 0.5F);
            Vector3d vector3d1 = player.getUpVector(1.0F);
            Vector3d vector3d = player.getLook(1.0F);
            Vector3f vector3f = new Vector3f(vector3d);

            shot.shoot((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), 1.0F, 0.5F);
            player.world.addEntity(shot);
        }
    }
}
