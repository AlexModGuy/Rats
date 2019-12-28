package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPlagueShot;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPlagueScythe extends SwordItem {
    public ItemPlagueScythe() {
        super(RatsItemRegistry.PLAGUE_SCYTHE_MATERIAL, 5, 1.0F, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "plague_scythe");
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc"));
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)12, AttributeModifier.Operation.ADDITION));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-0.5, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        if (LivingEntity.swingProgress == 0) {
            Multimap<String, AttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
            double totalDmg = 0;
            for (AttributeModifier modifier : dmg.get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
                totalDmg += modifier.getAmount();
            }
            LivingEntity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
            EntityPlagueShot shot = new EntityPlagueShot(RatsEntityRegistry.PLAGUE_SHOT, LivingEntity.world, LivingEntity, totalDmg * 0.5F);
            shot.shoot(LivingEntity, LivingEntity.rotationPitch, LivingEntity.rotationYaw, 0.0F, 0.8F, 1.0F);
            if (!LivingEntity.world.isRemote) {
                LivingEntity.world.addEntity(shot);
            }
        }
        return super.onItemUseFinish(stack, worldIn, LivingEntity);
    }


}