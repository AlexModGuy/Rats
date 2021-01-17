package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatUpgrade extends Item {
    private int rarity = 0;
    private int textLength = 0;

    public ItemRatUpgrade(String name, int stacksize) {
        super(new Item.Properties().group(RatsMod.TAB_UPGRADES).maxStackSize(stacksize));
        this.setRegistryName(RatsMod.MODID, name);
    }

    public ItemRatUpgrade(String name, int stacksize, int rarity, int textLength) {
        this(name, stacksize);
        this.rarity = rarity;
        this.textLength = textLength;
    }

    public ItemRatUpgrade(String name) {
        this(name, 64);
    }

    public ItemRatUpgrade(String name, int rarity, int textLength) {
        this(name, 64, rarity, textLength);
    }

    public Rarity getRarity(ItemStack stack) {
        if (rarity != 0 && rarity != 4) {
            return Rarity.values()[rarity];
        }
        return super.getRarity(stack);
    }

    public boolean hasEffect(ItemStack stack) {
        return rarity >= 3 || super.hasEffect(stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (textLength > 0) {
            for (int i = 0; i < textLength; i++) {
                tooltip.add(new TranslationTextComponent(this.getTranslationKey() + i + ".desc").mergeStyle(TextFormatting.GRAY));

            }
        } else {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").mergeStyle(TextFormatting.GRAY));
        }
    }

    // START OF RATS API ADDON SUPPORT //

    /**
     * Called when Upgrade slot is changed/updated.
     * Used primarily for stat increases for health, damage, etc.
     * Make sure you call {@link EntityRat#tryIncreaseStat} and {@link LivingEntity#getAttribute} too.
     */
    public void onUpgradeChanged(EntityRat rat) {
    }

    /**
     * Called when Rat AI is changed/updated.
     * Used primarily for harvest AI changes and RF transfer.
     * Called when Upgrade slot is changed/updated.
     * Used primarily for stat increases for health, damage, etc.
     * Set either aiHarvest, aiPickup or aiDeposit in rat to something new.
     */
    public void onInitalizeAI(EntityRat rat) {

    }

    public boolean isRatHoldingFood(EntityRat rat) {
        return false;
    }

    public boolean shouldSitAnimation(EntityRat rat) {
        return false;
    }

    public boolean shouldHoldItemInHands(EntityRat rat) {
        return false;
    }

    public boolean shouldNotIdleAnimation(EntityRat rat) {
        return false;
    }

    public boolean canFly(EntityRat rat) {
        return false;
    }

    /**
     * True if the rat should deposit or cook an item instead of holding it.
     */
    public boolean shouldDepositItem(EntityRat rat, ItemStack stack) {
        return true;
    }

    /**
     * True if the rat cannot take damage from the source.
     */
    public boolean isRatInvulnerableTo(EntityRat rat, DamageSource source) {
        return false;
    }

    /**
     * Called after a rat hits a mob.
     */
    public void onPostAttack(EntityRat rat, Entity target) {
    }

    /**
     * Called each tick.
     */
    public void onRatUpdate(EntityRat rat) {
    }

}
