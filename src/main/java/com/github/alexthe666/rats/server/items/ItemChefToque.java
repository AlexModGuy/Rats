package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChefToque extends ItemArmor {

    public ItemChefToque() {
        super(RatsItemRegistry.CHEF_TOQUE_ARMOR_MATERIAL, 0, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.chef_toque");
        this.setRegistryName(RatsMod.MODID, "chef_toque");
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return (ModelBiped) RatsMod.PROXY.getArmorModel(0);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "rats:textures/model/toque.png";
    }
}
