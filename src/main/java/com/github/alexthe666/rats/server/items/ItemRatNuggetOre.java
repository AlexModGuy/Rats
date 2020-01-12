package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.blocks.ICustomRendered;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatNuggetOre extends Item implements ICustomRendered {

    private static final ItemStack IRON_INGOT = new ItemStack(Blocks.IRON_ORE);

    public ItemRatNuggetOre() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rat_nugget_ore");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_COMPOSTER_READY, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        ItemStack poopStack = getIngot(itemstack, IRON_INGOT, worldIn);
        IInventory iinventory = new Inventory(poopStack);
        FurnaceRecipe irecipe = Minecraft.getInstance().world.getRecipeManager().getRecipe(IRecipeType.SMELTING, iinventory, Minecraft.getInstance().world).orElse(null);
        ItemStack burntItem = poopStack;
        if(irecipe != null && !irecipe.getRecipeOutput().isEmpty()){
            burntItem = irecipe.getRecipeOutput().copy();
        }
        if(burntItem != poopStack){
            ItemStack dropStack = new ItemStack(burntItem.getItem(), 2);
            if(!playerIn.addItemStackToInventory(dropStack)){
                playerIn.dropItem(dropStack, false);
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
    }

    public static ItemStack getIngot(ItemStack poopItem, ItemStack fallback, @Nullable World world) {
        if (poopItem.getTag() != null) {
            CompoundNBT poopTag = poopItem.getTag().getCompound("OreItem");
            ItemStack oreItem = ItemStack.read(poopTag);
            if(oreItem.isEmpty()){
                return fallback;
            }else{
                return oreItem;
            }
        }
        return fallback;
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.rat_nugget_ore.desc").applyTextStyle(TextFormatting.GRAY));
    }

    public ITextComponent getDisplayName(ItemStack stack) {
        String oreName = getIngot(stack, IRON_INGOT, RatsMod.PROXY.getWorld()).getDisplayName().getFormattedText();
        String removedString = I18n.format("item.rats.rat_nugget_remove_tag");
        if (oreName.contains(removedString)) {
            oreName = oreName.replace(removedString, "");
        } else {
            oreName += " ";
        }
        return new TranslationTextComponent(this.getTranslationKey(stack), oreName).applyTextStyle(TextFormatting.GRAY);
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
            for (Block entry : RatsNuggetRegistry.ORE_TO_INGOTS) {
                ItemStack oreStack = new ItemStack(entry);
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
