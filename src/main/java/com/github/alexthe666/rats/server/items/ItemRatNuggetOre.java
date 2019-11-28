package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.Map;

public class ItemRatNuggetOre extends Item implements ICustomRendered {

    private static final ItemStack IRON_INGOT = new ItemStack(Items.IRON_INGOT);

    public ItemRatNuggetOre() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_nugget_ore");
        this.setRegistryName(RatsMod.MODID, "rat_nugget_ore");
    }

    public static ItemStack getIngot(ItemStack poopItem, ItemStack fallback) {
        if (poopItem.getTag() != null) {
            CompoundNBT poopTag = poopItem.getTag().getCompoundTag("IngotItem");
            ItemStack oreItem = new ItemStack(poopTag);
            return oreItem.isEmpty() ? fallback : oreItem;
        }
        return fallback;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        String oreName = getIngot(stack, IRON_INGOT).getDisplayName();
        String removedString = net.minecraft.util.text.translation.I18n.translateToLocal("item.rats.rat_nugget_remove_tag.name");
        if (oreName.contains(removedString)) {
            oreName = oreName.replace(removedString, "");
        } else {
            oreName += " ";
        }
        return I18n.translateToLocalFormatted(this.getUnlocalizedNameInefficiently(stack) + ".name", oreName).trim();
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Map.Entry<ItemStack, ItemStack> entry : RatsNuggetRegistry.ORE_TO_INGOTS.entrySet()) {
                ItemStack oreStack = entry.getKey();
                ItemStack ingotStack = entry.getValue();
                ItemStack stack = new ItemStack(this);
                CompoundNBT poopTag = new CompoundNBT();
                CompoundNBT oreTag = new CompoundNBT();
                oreStack.writeToNBT(oreTag);
                CompoundNBT ingotTag = new CompoundNBT();
                ingotStack.writeToNBT(ingotTag);
                poopTag.setTag("OreItem", oreTag);
                poopTag.setTag("IngotItem", ingotTag);
                stack.setTag(poopTag);
                stack.setItemDamage(RatsNuggetRegistry.getNuggetMeta(ingotStack));
                ItemStack poopyStack = new ItemStack(this, 1, stack.getItemDamage());
                poopyStack.setTag(poopTag);
                items.add(poopyStack);
            }
        }
    }

    public int getDamage(ItemStack stack) {
        return RatsNuggetRegistry.getNuggetMeta(getIngot(stack, IRON_INGOT));
    }

    public int getMetadata(ItemStack stack) {
        return RatsNuggetRegistry.getNuggetMeta(getIngot(stack, IRON_INGOT));
    }
}
