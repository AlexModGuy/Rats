package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.inventory.ContainerRatUpgrade;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatListUpgrade extends ItemRatUpgrade {

    private boolean whitelist;

    public ItemRatListUpgrade(String name, boolean whitelist) {
        super(name, 1);
        this.whitelist = whitelist;
    }

    public ItemRatListUpgrade(String name, int rarity, int textLength, boolean whitelist) {
        super(name, 1, rarity, textLength);
        this.whitelist = whitelist;
    }

    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        if (!player.isSneaking()) {
            RatsMod.PROXY.setRefrencedItem(itemStackIn);
            if(!worldIn.isRemote){
                NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
                    @Override
                    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                        return new ContainerRatUpgrade(p_createMenu_1_, new InventoryRatUpgrade(itemStackIn), p_createMenu_2_);
                    }
                    @Override
                    public ITextComponent getDisplayName() {
                        return ItemRatListUpgrade.this.getDisplayName(itemStackIn);
                    }
                });
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    @OnlyIn(Dist.CLIENT) public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        CompoundNBT CompoundNBT1 = stack.getTag();

        if (CompoundNBT1 != null && CompoundNBT1.contains("Items", 9)) {
            NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(CompoundNBT1, nonnulllist);
            int i = 0;
            int j = 0;
            for (ItemStack itemstack : nonnulllist) {
                if (!itemstack.isEmpty()) {
                    ++j;
                    if (i <= 4) {
                        ++i;
                        tooltip.add(new StringTextComponent(String.format("%s", itemstack.getDisplayName().getString())));
                    }
                }
            }
            if (j - i > 0) {
                //tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), j - i));
            }
        }
    }
}