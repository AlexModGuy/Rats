package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import scala.Int;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPlagueHealer extends ItemGenericFood {

    private float healChance;

    public ItemPlagueHealer(int amount, float saturation, String name, float healChance) {
        super(amount, saturation, false, name);
        this.healChance = healChance;
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(player.isPotionActive(RatsMod.PLAGUE_POTION)){
            if(itemRand.nextDouble() <= healChance){
                player.removePotionEffect(RatsMod.PLAGUE_POTION);
            }
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.plague_heal_chance.desc", (int) (healChance * 100F)));

    }
}
