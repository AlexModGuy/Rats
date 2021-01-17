package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemRatSack extends Item {

    public ItemRatSack() {
        super(new Item.Properties().group(RatsMod.TAB).maxStackSize(1));
        this.setRegistryName(RatsMod.MODID, "rat_sack");

    }

    public static int getRatsInStack(ItemStack stack) {
        int ratCount = 0;
        if (stack.getTag() != null) {
            for (String tagInfo : stack.getTag().keySet()) {
                if (tagInfo.contains("Rat")) {
                    ratCount++;
                }
            }
        }
        return ratCount;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //tooltip.add(I18n.format("item.rats.rat_sack.capacity", 10));
        int ratCount = 0;
        List<String> ratNames = new ArrayList<>();
        if (stack.getTag() != null) {
            for (String tagInfo : stack.getTag().keySet()) {
                if (tagInfo.contains("Rat")) {
                    CompoundNBT ratTag = stack.getTag().getCompound(tagInfo);
                    ratCount++;
                    String ratName = I18n.format("entity.rats.rat");
                    if (!ratTag.getString("CustomName").isEmpty() && !ratTag.getString("CustomName").startsWith("TextComponent")) {
                        ITextComponent ratNameTag = ITextComponent.Serializer.func_240643_a_(ratTag.getString("CustomName"));
                        if (ratNameTag != null) {
                            ratName = ratNameTag.getString();
                        }
                    }
                    ratNames.add(ratName);
                }
            }
        }
        tooltip.add(new TranslationTextComponent("item.rats.rat_sack.contains", ratCount).mergeStyle(TextFormatting.GRAY));
        if (!ratNames.isEmpty()) {
            for (int i = 0; i < ratNames.size(); i++) {
                if (i < 3) {
                    tooltip.add(new TranslationTextComponent("item.rats.rat_sack.contain_rat", ratNames.get(i)).mergeStyle(TextFormatting.GRAY));
                } else {
                    break;
                }
            }
            if (ratNames.size() > 3) {
                tooltip.add(new TranslationTextComponent("item.rats.rat_sack.and_more").mergeStyle(TextFormatting.GRAY));
            }
        }
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (stack.getItem() == RatsItemRegistry.RAT_SACK && getRatsInStack(stack) > 0) {
            int ratCount = 0;
            if (stack.getTag() != null) {
                for (String tagInfo : stack.getTag().keySet()) {
                    if (tagInfo.contains("Rat")) {
                        ratCount++;
                        CompoundNBT ratTag = stack.getTag().getCompound(tagInfo);
                        EntityRat rat = new EntityRat(RatsEntityRegistry.RAT, context.getWorld());
                        BlockPos offset = context.getPos().offset(context.getFace());
                        rat.readAdditional(ratTag);
                        rat.setLocationAndAngles(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
                        if (!context.getWorld().isRemote) {
                            context.getWorld().addEntity(rat);
                        }
                    }
                }
            }
            if (ratCount > 0) {
                context.getPlayer().swingArm(context.getHand());
                context.getPlayer().sendStatusMessage(new TranslationTextComponent("entity.rats.rat.sack.release", ratCount), true);
                stack.setTag(new CompoundNBT());
            }
        }
        return ActionResultType.PASS;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        return new ItemStack(RatsItemRegistry.RAT_SACK);
    }
}
