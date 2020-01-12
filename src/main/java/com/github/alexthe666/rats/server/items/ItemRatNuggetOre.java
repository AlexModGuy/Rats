package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemRatNuggetOre extends Item implements ICustomRendered {

    private static final ItemStack IRON_INGOT = new ItemStack(Items.IRON_INGOT);

    public ItemRatNuggetOre() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rat_nugget_ore");
    }

    public static ItemStack getIngot(ItemStack poopItem, ItemStack fallback) {
        if (poopItem.getTag() != null) {
            CompoundNBT poopTag = poopItem.getTag().getCompound("OreItem");
            ItemStack oreItem = ItemStack.read(poopTag);
            return oreItem.isEmpty() ? fallback : oreItem;
        }
        return fallback;
    }

    public ITextComponent getDisplayName(ItemStack stack) {
        String oreName = getIngot(stack, IRON_INGOT).getDisplayName().getFormattedText();
        String removedString = I18n.format("item.rats.rat_nugget_remove_tag");
        if (oreName.contains(removedString)) {
            oreName = oreName.replace(removedString, "");
        } else {
            oreName += " ";
        }
        return new TranslationTextComponent(this.getTranslationKey(stack), oreName);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
    }

    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            RatsNuggetRegistry.init();
            for (ItemStack entry : RatsNuggetRegistry.ORE_TO_INGOTS) {
                ItemStack oreStack = entry;
                ItemStack stack = new ItemStack(this);
                CompoundNBT poopTag = new CompoundNBT();
                CompoundNBT oreTag = new CompoundNBT();
                oreStack.write(oreTag);
                poopTag.put("OreItem", oreTag);
                stack.setTag(poopTag);
                ItemStack poopyStack = new ItemStack(this, 1);
                poopyStack.setTag(poopTag);
                items.add(poopyStack);
            }
        }

    }
}
