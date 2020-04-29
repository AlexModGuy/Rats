package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatShot;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemGildedRatFlute extends Item {

    public ItemGildedRatFlute() {
        super(new Item.Properties().group(RatsMod.TAB).maxStackSize(1).defaultMaxDamage(100));
        this.setRegistryName(RatsMod.MODID, "gilded_rat_flute");
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == RatsItemRegistry.TANGLED_RAT_TAILS;
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        if (itemStackIn.getTag() != null) {
            EntityRatShot ratShot = new EntityRatShot(RatsEntityRegistry.RAT_SHOT, worldIn, player);
            ratShot.setColorVariant(worldIn.rand.nextInt(3));
            ratShot.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.6F, 1.0F);
            worldIn.addEntity(ratShot);
            itemStackIn.damageItem(1, player, (p_213833_1_) -> {
                p_213833_1_.sendBreakAnimation(Hand.MAIN_HAND);
            });
            player.swingArm(hand);
            player.getCooldownTracker().setCooldown(this, 5);
            worldIn.playSound(player, player.getPosition(), RatsSoundRegistry.RAT_FLUTE, SoundCategory.NEUTRAL, 1, 1.5F);
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.gilded_rat_flute.desc").applyTextStyle(TextFormatting.GRAY));
    }
}
