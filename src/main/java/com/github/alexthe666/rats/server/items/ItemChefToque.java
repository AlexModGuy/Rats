package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

public class ItemChefToque extends ArmorItem {

    public ItemChefToque() {
        super(RatsItemRegistry.CHEF_TOQUE_ARMOR_MATERIAL, EquipmentSlotType.HEAD, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "chef_toque");
    }

   /* @OnlyIn(Dist.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return (ModelBiped) RatsMod.PROXY.getArmorModel(0);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "rats:textures/model/toque.png";
    }*/
}
