package com.github.alexthe666.rats.server.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatCombinedUpgrade extends ItemRatUpgrade {

    private boolean whitelist;

    public ItemRatCombinedUpgrade(String name) {
        super(name, 1, 1, 1);
    }

    public static boolean canCombineWithUpgrade(ItemStack combiner, ItemStack stack) {
        CompoundNBT tag = combiner.getTag();
        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            for (ItemStack contained : nonnulllist) {
                if (!(stack.getItem() instanceof ItemRatUpgrade) || stack.getItem() == contained.getItem() || RatsUpgradeConflictRegistry.doesConflict(contained.getItem(), stack.getItem())) {
                    return false;
                }
            }
        }
        return combiner.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED || combiner.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE;
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            tooltip.add(new TranslationTextComponent("item.rats.rat_upgrade_combined_creative.desc"));
        }
        tooltip.add(new TranslationTextComponent("item.rats.rat_upgrade_combined.desc"));
        CompoundNBT tag = stack.getTag();

        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            int i = 0;
            int j = 0;
            for (ItemStack itemstack : nonnulllist) {
                if (!itemstack.isEmpty()) {
                    ++j;
                    if (i <= 4) {
                        ++i;
                        tooltip.add(new StringTextComponent(String.format("%s", itemstack.getDisplayName().getFormattedText())));
                    }
                }
            }
            if (j - i > 0) {
                //tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
            }
        }
    }

    public boolean hasEffect(ItemStack stack) {
        if (stack.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            return true;
        }
        CompoundNBT tag = stack.getTag();
        boolean flag = false;
        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            flag = !nonnulllist.isEmpty();
        }
        return flag;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        if (this == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            ItemStack itemStackIn = player.getHeldItem(hand);
            if (!player.isSneaking()) {
                //player.openGui(RatsMod.INSTANCE, 3, worldIn, 0, 0, 0);
                return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
            }
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
        } else {
            return super.onItemRightClick(worldIn, player, hand);
        }
    }
}