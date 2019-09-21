package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPiperHat extends ItemArmor {

    public ItemPiperHat() {
        super(RatsItemRegistry.HAT_ARMOR_MATERIAL, 0, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.piper_hat");
        this.setRegistryName(RatsMod.MODID, "piper_hat");
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return (ModelBiped) RatsMod.PROXY.getArmorModel(1);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "rats:textures/model/piper_hat.png";
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format(this.getTranslationKey() + ".desc"));
    }
}
