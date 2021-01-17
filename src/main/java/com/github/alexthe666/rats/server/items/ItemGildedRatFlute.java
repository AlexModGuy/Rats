package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRatShot;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
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
            Vector3d vector3d = player.getLook(1.0F);
            Vector3f vector3f = new Vector3f(vector3d);
            ratShot.shoot((double)vector3f.getX(), (double)vector3f.getY(), (double)vector3f.getZ(), 1.0F, 1.5F);
            worldIn.addEntity(ratShot);
            itemStackIn.damageItem(1, player, (p_213833_1_) -> {
                p_213833_1_.sendBreakAnimation(Hand.MAIN_HAND);
            });
            player.swingArm(hand);
            player.getCooldownTracker().setCooldown(this, 5);
            worldIn.playSound(player, new BlockPos(player.getPositionVec()), RatsSoundRegistry.getFluteSound(), SoundCategory.NEUTRAL, 0.5F, 0.75F);
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.gilded_rat_flute.desc").mergeStyle(TextFormatting.GRAY));
    }
}
