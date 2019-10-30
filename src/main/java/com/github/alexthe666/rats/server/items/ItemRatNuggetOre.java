package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
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
        if (poopItem.getTagCompound() != null) {
            NBTTagCompound poopTag = poopItem.getTagCompound().getCompoundTag("IngotItem");
            ItemStack oreItem = new ItemStack(poopTag);
            return oreItem.isEmpty() ? fallback : oreItem;
        }
        return fallback;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        String oreName = getIngot(stack, IRON_INGOT).getDisplayName();
        String removedString = I18n.format("item.rats.rat_nugget_remove_tag.name");
        if (oreName.contains(removedString)) {
            oreName = oreName.replace(removedString, "");
        } else {
            oreName += " ";
        }
        return I18n.format(this.getUnlocalizedNameInefficiently(stack) + ".name", oreName).trim();
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (Map.Entry<ItemStack, ItemStack> entry : RatsNuggetRegistry.ORE_TO_INGOTS.entrySet()) {
                ItemStack oreStack = entry.getKey();
                ItemStack ingotStack = entry.getValue();
                ItemStack stack = new ItemStack(this);
                NBTTagCompound poopTag = new NBTTagCompound();
                NBTTagCompound oreTag = new NBTTagCompound();
                oreStack.writeToNBT(oreTag);
                NBTTagCompound ingotTag = new NBTTagCompound();
                ingotStack.writeToNBT(ingotTag);
                poopTag.setTag("OreItem", oreTag);
                poopTag.setTag("IngotItem", ingotTag);
                stack.setTagCompound(poopTag);
                stack.setItemDamage(RatsNuggetRegistry.getNuggetMeta(ingotStack));
                ItemStack poopyStack = new ItemStack(this, 1, stack.getItemDamage());
                poopyStack.setTagCompound(poopTag);
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
