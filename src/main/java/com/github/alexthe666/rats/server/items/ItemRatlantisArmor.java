package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemRatlantisArmor extends ArmorItem {

    public ItemRatlantisArmor(IArmorMaterial material, EquipmentSlotType slot, String name) {
        super(material, slot, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "rats:textures/model/" + ( slot == EquipmentSlotType.LEGS ? "ratlantis_armor_1" : "ratlantis_armor_0") + ".png";
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) RatsMod.PROXY.getArmorModel(armorSlot == EquipmentSlotType.LEGS ? 15 : 14);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_armor0.desc").applyTextStyle(TextFormatting.YELLOW));
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_armor1.desc").applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.rats.ratlantis_armor2.desc").applyTextStyle(TextFormatting.GRAY));
    }
}
