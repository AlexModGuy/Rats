package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRatProtector;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRatlantisSword  extends SwordItem {

    public ItemRatlantisSword() {
        super(RatsItemRegistry.RATLANTIS_TOOL_MATERIAL, 3, -2.4F, new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "ratlantis_sword");
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        EntityRatProtector protector = new EntityRatProtector(RatsEntityRegistry.RAT_PROTECTOR, targetEntity.world);
        protector.copyLocationAndAnglesFrom(attacker);
        protector.setAttackTarget(targetEntity);
        attacker.world.addEntity(protector);
        return super.hitEntity(stack, targetEntity, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "0.desc").applyTextStyle(TextFormatting.YELLOW));
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + "1.desc").applyTextStyle(TextFormatting.YELLOW));
    }
}
