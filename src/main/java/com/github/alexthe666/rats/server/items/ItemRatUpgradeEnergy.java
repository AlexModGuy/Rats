package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgradeEnergy extends ItemRatUpgrade {

    public ItemRatUpgradeEnergy(String name, int rarity, int textLength) {
        super(name, rarity, textLength);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int transferRate = getRFTransferRate(stack.getItem());
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.desc0"));
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.desc1"));
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.transfer_rate", transferRate));
    }

    public static int getRFTransferRate(Item item){
        if(item == RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY){
            return RatsMod.CONFIG_OPTIONS.ratRFTransferBasic;
        }
        if(item == RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY){
            return RatsMod.CONFIG_OPTIONS.ratRFTransferAdvanced;
        }
        if(item == RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY){
            return RatsMod.CONFIG_OPTIONS.ratRFTransferElite;
        }
        if(item == RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY){
            return RatsMod.CONFIG_OPTIONS.ratRFTransferExtreme;
        }
        return 0;
    }
}
