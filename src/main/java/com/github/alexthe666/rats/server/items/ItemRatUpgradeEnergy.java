package com.github.alexthe666.rats.server.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgradeEnergy extends ItemRatUpgrade {

    public ItemRatUpgradeEnergy(String name, int rarity, int textLength) {
        super(name, rarity, textLength);
    }

    public static int getRFTransferRate(Item item) {
        if (item == RatsItemRegistry.RAT_UPGRADE_BASIC_ENERGY) {
            return RatConfig.ratRFTransferBasic;
        }
        if (item == RatsItemRegistry.RAT_UPGRADE_ADVANCED_ENERGY) {
            return RatConfig.ratRFTransferAdvanced;
        }
        if (item == RatsItemRegistry.RAT_UPGRADE_ELITE_ENERGY) {
            return RatConfig.ratRFTransferElite;
        }
        if (item == RatsItemRegistry.RAT_UPGRADE_EXTREME_ENERGY) {
            return RatConfig.ratRFTransferExtreme;
        }
        return 0;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int transferRate = getRFTransferRate(stack.getItem());
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.desc0"));
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.desc1"));
        tooltip.add(I18n.format("item.rats.rat_upgrade_energy.transfer_rate", transferRate));
    }
}
