package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPlagueCloud;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPlagueScythe extends ItemSword {
    public ItemPlagueScythe() {
        super(RatsItemRegistry.PLAGUE_SCYTHE_MATERIAL);
        this.setTranslationKey("rats.plague_scythe");
        this.setCreativeTab(RatsMod.TAB);
        this.setRegistryName(RatsMod.MODID, "plague_scythe");
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format(this.getTranslationKey() + ".desc"));
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 12D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -0.5D, 0));
        }
        return multimap;
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack){
        if(entityLiving.swingProgress == 0) {
            Multimap<String, AttributeModifier> dmg = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            double totalDmg = 0;
            for (AttributeModifier modifier : dmg.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                totalDmg += modifier.getAmount();
            }
            entityLiving.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
            EntityPlagueShot shot = new EntityPlagueShot(entityLiving.world, entityLiving, totalDmg * 0.5F);
            shot.shoot(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0.0F, 0.8F, 1.0F);
            if (!entityLiving.world.isRemote) {
                entityLiving.world.addEntity(shot);
            }
        }
        return false;
    }


}