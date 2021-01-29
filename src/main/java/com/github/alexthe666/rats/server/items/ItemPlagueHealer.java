package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

            double chance = random.nextDouble();
            boolean removeEffect = (chance <= healChance);
            System.out.println("PLAGUE HEALER EATEN, chance: " + this.healChance + " compared to RNG: " + chance + " REMOVE:" + removeEffect);

            if (removeEffect) {
                boolean wasRemoved = player.removePotionEffect(RatsMod.PLAGUE_POTION);
                System.out.println("PLAGUE HEALER EATEN and Effect REMOVED NOW success: " + wasRemoved);
            }
        } else {
            System.out.println("PLAGUE HEALER EATEN without Plage Effect active");
        }
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {

        if (stack.getItem() == RatsItemRegistry.PLAGUE_STEW && stack.getCount() == 1) {
            System.out.println("PLAGUE HEALER STEW onItemFinish with Stack count 1 ");
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

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.plague_heal_chance.desc", (int) (healChance * 100F)).mergeStyle(TextFormatting.GRAY));

    }
}
