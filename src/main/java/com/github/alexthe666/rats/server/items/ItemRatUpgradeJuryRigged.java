package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.ContainerRatUpgradeJuryRigged;
import com.github.alexthe666.rats.server.inventory.InventoryRatUpgrade;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgradeJuryRigged extends ItemRatUpgrade {

    public ItemRatUpgradeJuryRigged(String name) {
        super(name, 1, 1, 3);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.rat_upgrade_jury_rigged0.desc").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.rats.rat_upgrade_jury_rigged1.desc").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.rats.rat_upgrade_combined.desc").func_240699_a_(TextFormatting.GRAY));
        CompoundNBT tag = stack.getTag();

        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
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
        CompoundNBT tag = stack.getTag();
        boolean flag = false;
        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            flag = !nonnulllist.get(0).isEmpty() && !nonnulllist.get(1).isEmpty();
        }
        return flag;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        CompoundNBT tag = itemStackIn.getTag();
        boolean flag = false;
        if (tag != null && tag.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(2, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(tag, nonnulllist);
            flag = !nonnulllist.get(0).isEmpty() && !nonnulllist.get(1).isEmpty();
        }
        if (!player.isSneaking() && !flag) {
            RatsMod.PROXY.setRefrencedItem(itemStackIn);
            if (!worldIn.isRemote) {
                NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                    @Override
                    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                        return new ContainerRatUpgradeJuryRigged(p_createMenu_1_, new InventoryRatUpgrade(itemStackIn), p_createMenu_2_);
                    }

                    @Override
                    public ITextComponent getDisplayName() {
                        return ItemRatUpgradeJuryRigged.this.getDisplayName(itemStackIn);
                    }
                });
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }


}