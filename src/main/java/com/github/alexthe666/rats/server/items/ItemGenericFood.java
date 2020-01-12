package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemGenericFood extends Item {
    private int healAmount;
    private float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, String name) {
        super(new Item.Properties().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, name);
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static final Food createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, EffectInstance potion) {
        Food.Builder builder = new Food.Builder();
        builder.hunger(amount);
        builder.saturation(saturation);
        if (isWolfFood) {
            builder.meat();
        }
        if (eatFast) {
            builder.fastToEat();
        }
        if (alwaysEdible) {
            builder.setAlwaysEdible();
        }
        if (potion != null) {
            builder.effect(potion, 1.0F);
        }
        return builder.build();
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        if (this == RatsItemRegistry.CONFIT_BYALDI || this == RatsItemRegistry.POTATO_KNISHES) {
            if (LivingEntity instanceof PlayerEntity) {
                PlayerEntity PlayerEntity = (PlayerEntity) LivingEntity;
                PlayerEntity.getFoodStats().addStats(healAmount, saturation);
                worldIn.playSound(null, PlayerEntity.posX, PlayerEntity.posY, PlayerEntity.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                this.onFoodEaten(stack, worldIn, PlayerEntity);
                if (PlayerEntity instanceof ServerPlayerEntity) {
                    CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) PlayerEntity, stack);
                }
            }
            if (!(LivingEntity instanceof PlayerEntity) || !((PlayerEntity) LivingEntity).isCreative()) {
                stack.shrink(1);
            }
            return stack;
        } else {
            return super.onItemUseFinish(stack, worldIn, LivingEntity);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (this == RatsItemRegistry.CONFIT_BYALDI || this == RatsItemRegistry.POTATO_KNISHES) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemstack);
        } else {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, PlayerEntity player) {
        if (!worldIn.isRemote && this == RatsItemRegistry.CONTAMINATED_FOOD) {
            Random rand = new Random();
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(RatsMod.PLAGUE_POTION, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.POISON, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.HUNGER, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.WITHER, 2400));
            }
            if (rand.nextFloat() < 0.3D) {
                player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 2400));
            }
            player.addPotionEffect(new EffectInstance(Effects.UNLUCK, 2400));
        }
        if (!worldIn.isRemote && (this == RatsItemRegistry.CONFIT_BYALDI || this == RatsItemRegistry.POTATO_KNISHES)) {
            player.addPotionEffect(new EffectInstance(RatsMod.CONFIT_BYALDI_POTION, this == RatsItemRegistry.POTATO_KNISHES ? 1200 : 2400));
        }
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (this == RatsItemRegistry.RAT_BURGER) {
            tooltip.add(new TranslationTextComponent("item.rats.rat_burger.desc").applyTextStyle(TextFormatting.GRAY));
        }
        if (this == RatsItemRegistry.CONTAMINATED_FOOD) {
            tooltip.add(new TranslationTextComponent("item.rats.contaminated_food.desc0").applyTextStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("item.rats.contaminated_food.desc1").applyTextStyle(TextFormatting.GRAY));
        }
    }
}
