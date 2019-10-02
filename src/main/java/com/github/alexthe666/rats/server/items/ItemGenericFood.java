package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGenericFood extends ItemFood {
    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, String name) {
        super(amount, saturation, isWolfFood);
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats." + name);
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (this == RatsItemRegistry.CONFIT_BYALDI) {
            if (entityLiving instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entityLiving;
                entityplayer.getFoodStats().addStats(this, stack);
                worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                this.onFoodEaten(stack, worldIn, entityplayer);
                entityplayer.addStat(StatList.getObjectUseStats(this));
                if (entityplayer instanceof EntityPlayerMP) {
                    CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
                }
            }
            if(!(entityLiving instanceof EntityPlayer) || !((EntityPlayer) entityLiving).isCreative()){
                stack.shrink(1);
            }
            return stack;
        }else{
            return super.onItemUseFinish(stack, worldIn, entityLiving);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (this == RatsItemRegistry.CONFIT_BYALDI) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        } else {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(this == RatsItemRegistry.RAT_BURGER)
        tooltip.add(I18n.format("item.rats.rat_burger.desc"));
    }
}
