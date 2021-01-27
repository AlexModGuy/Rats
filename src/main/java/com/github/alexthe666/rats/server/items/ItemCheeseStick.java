package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCheeseStick extends Item {

    public ItemCheeseStick() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "cheese_stick");
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

    public ActionResultType onItemUse(ItemUseContext context) {
      /*  RatsMod.PROXY.setCheeseStaffContext(context.getPos(), context.getFace());
        Entity rat = null;
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        if (stack.getTag() != null && stack.getTag().hasUniqueId("RatUUID")) {
            if (!context.getWorld().isRemote() && context.getWorld() instanceof ServerWorld) {
                rat = ((ServerWorld)context.getWorld()).getEntityByUuid(stack.getTag().getUniqueId("RatUUID"));
            }
        }
        if (!context.getWorld().isRemote && context.getPlayer() instanceof ServerPlayerEntity) {
            if (rat instanceof EntityRat) {
                RatsMod.sendNonLocal(new MessageCheeseStaffRat(rat.getEntityId(), false), (ServerPlayerEntity) context.getPlayer());
                context.getPlayer().swingArm(context.getHand());
            } else {
                RatsMod.sendNonLocal(new MessageCheeseStaffRat(0, true), (ServerPlayerEntity) context.getPlayer());
                context.getPlayer().sendStatusMessage(new TranslationTextComponent("entity.rats.rat.staff.no_rat"), true);
            }
        }*/
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.cheese_stick.desc0").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.rats.cheese_stick.desc1").mergeStyle(TextFormatting.GRAY));
    }
}
