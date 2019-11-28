package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatCombinedUpgrade extends ItemRatUpgrade {

    private boolean whitelist;

    public ItemRatCombinedUpgrade(String name) {
        super(name, 1, 1);
        this.setMaxStackSize(1);
    }

    public static boolean canCombineWithUpgrade(ItemStack combiner, ItemStack stack) {
        CompoundNBT CompoundNBT1 = combiner.getTag();
        if (CompoundNBT1 != null && CompoundNBT1.hasKey("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
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

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            tooltip.add(net.minecraft.client.resources.I18n.format("item.rats.rat_upgrade_combined_creative.desc"));
        }
        tooltip.add(net.minecraft.client.resources.I18n.format("item.rats.rat_upgrade_combined.desc"));
        CompoundNBT CompoundNBT1 = stack.getTag();

        if (CompoundNBT1 != null && CompoundNBT1.hasKey("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
            int i = 0;
            int j = 0;
            for (ItemStack itemstack : nonnulllist) {
                if (!itemstack.isEmpty()) {
                    ++j;
                    if (i <= 4) {
                        ++i;
                        tooltip.add(String.format("%s", itemstack.getDisplayName()));
                    }
                }
            }
            if (j - i > 0) {
                tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        if (stack.getItem() == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            return true;
        }
        CompoundNBT CompoundNBT1 = stack.getTag();
        boolean flag = false;
        if (CompoundNBT1 != null && CompoundNBT1.hasKey("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
            flag = !nonnulllist.isEmpty();
        }
        return flag;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, EnumHand hand) {
        if (this == RatsItemRegistry.RAT_UPGRADE_COMBINED_CREATIVE) {
            ItemStack itemStackIn = player.getHeldItem(hand);
            if (!player.isSneaking()) {
                player.openGui(RatsMod.INSTANCE, 3, worldIn, 0, 0, 0);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
            }
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
        } else {
            return super.onItemRightClick(worldIn, player, hand);
        }
    }
}