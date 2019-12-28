package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityPurifyingLiquid;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPurifyingLiquid extends Item {

    public ItemPurifyingLiquid() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "purifying_liquid");
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc"));
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack itemstack = context.getPlayer().getHeldItem(context.getHand());
        ItemStack itemstack1 = context.getPlayer().isCreative() ? itemstack.copy() : itemstack.split(1);
        context.getWorld().playSound(null, context.getPlayer().posX, context.getPlayer().posY, context.getPlayer().posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!context.getWorld().isRemote) {
            EntityPurifyingLiquid entitypotion = new EntityPurifyingLiquid(RatsEntityRegistry.PURIFYING_LIQUID, context.getWorld(), context.getPlayer(), itemstack1);
            entitypotion.shoot(context.getPlayer(), context.getPlayer().rotationPitch, context.getPlayer().rotationYaw, -20.0F, 0.5F, 1.0F);
            context.getWorld().addEntity(entitypotion);
        }
        return ActionResultType.SUCCESS;
    }
}

