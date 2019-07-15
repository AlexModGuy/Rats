package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.message.MessageCheeseStaffRat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;

public class ItemChunkyCheeseToken extends Item {

    public ItemChunkyCheeseToken() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.chunky_cheese_token");
        this.setRegistryName(RatsMod.MODID, "chunky_cheese_token");
    }


    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String formattedChance = NumberFormat.getNumberInstance().format(RatsMod.CONFIG_OPTIONS.tokenDropRate);
        tooltip.add(I18n.format("item.rats.chunky_cheese_token.desc0", formattedChance));
    }

    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
