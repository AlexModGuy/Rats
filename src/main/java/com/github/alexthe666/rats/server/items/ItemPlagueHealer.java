package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPlagueHealer extends ItemGenericFood {

    private float healChance;

    public ItemPlagueHealer(int amount, float saturation, String name, float healChance) {
        super(amount, saturation,false, false, true, name);
        this.healChance = healChance;
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, PlayerEntity player) {
        if (player.isPotionActive(RatsMod.PLAGUE_POTION)) {
            if (random.nextDouble() <= healChance) {
                player.removePotionEffect(RatsMod.PLAGUE_POTION);
            }
        }
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        if (stack.getItem() == RatsItemRegistry.PLAGUE_STEW) {
            super.onItemUseFinish(stack, worldIn, LivingEntity);
            return new ItemStack(Items.BOWL);
        } else {
            return super.onItemUseFinish(stack, worldIn, LivingEntity);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.plague_heal_chance.desc", (int) (healChance * 100F)));

    }
}
