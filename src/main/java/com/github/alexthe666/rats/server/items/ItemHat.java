package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class ItemHat extends ArmorItem {
    private int modelIndex;
    private String name;

    public ItemHat(String name, int modelIndex) {
        super(RatsItemRegistry.HAT_ARMOR_MATERIAL, EquipmentSlotType.HEAD, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
        this.modelIndex = modelIndex;
        this.name = name;
    }

    public ItemHat(IArmorMaterial mat, String name, int modelIndex) {
        super(mat, EquipmentSlotType.HEAD, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
        this.modelIndex = modelIndex;
        this.name = name;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() == RatsItemRegistry.ARCHEOLOGIST_HAT) {
            tooltip.add(new TranslationTextComponent("item.rats.archeologist_hat.desc"));
        }
        if (stack.getItem() == RatsItemRegistry.PLAGUE_DOCTOR_MASK) {
            tooltip.add(new TranslationTextComponent("item.rats.plague_doctor_mask.desc"));
        }
        if (stack.getItem() == RatsItemRegistry.BLACK_DEATH_MASK) {
            tooltip.add(new TranslationTextComponent("item.rats.plague_doctor_mask.desc"));
            tooltip.add(new TranslationTextComponent("item.rats.black_death_mask.desc"));
        }
        if (stack.getItem() == RatsItemRegistry.RAT_FEZ) {
            tooltip.add(new TranslationTextComponent("item.rats.rat_fez.desc0"));
            tooltip.add(new TranslationTextComponent("item.rats.rat_fez.desc1"));
        }
    }

    /*@OnlyIn(Dist.CLIENT)
    public ModelBiped getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, ModelBiped _default) {
        return (ModelBiped) RatsMod.PROXY.getArmorModel(modelIndex);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "rats:textures/model/" + name + ".png";
    }*/
}
