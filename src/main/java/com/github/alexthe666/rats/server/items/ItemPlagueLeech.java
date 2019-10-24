package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class ItemPlagueLeech extends Item {

    public ItemPlagueLeech() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.plague_leech");
        this.setRegistryName(RatsMod.MODID, "plague_leech");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if(!playerIn.isCreative()){
            playerIn.getHeldItem(handIn).shrink(1);
            playerIn.attackEntityFrom(DamageSource.CACTUS, 2);
            if(playerIn.isPotionActive(RatsMod.PLAGUE_POTION) && worldIn.rand.nextBoolean()){
                playerIn.removePotionEffect(RatsMod.PLAGUE_POTION);
            }
            playerIn.swingArm(handIn);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.plague_heal_chance.desc", 50));
    }

}
