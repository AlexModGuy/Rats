package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.EntityRatArrow;
import com.github.alexthe666.rats.server.entity.RatsEntityRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemRatArrow extends ArrowItem {

    public ItemRatArrow() {
        super(new Item.Properties().group(RatsMod.TAB).maxStackSize(1));
        this.setRegistryName(RatsMod.MODID, "rat_arrow");
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
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<String> ratNames = new ArrayList<>();
        if (stack.getTag() != null) {
            CompoundNBT ratTag = stack.getTag().getCompound("Rat");
            String ratName = I18n.format("entity.rats.rat");
            if (!ratTag.getString("CustomName").isEmpty() && !ratTag.getString("CustomName").startsWith("TextComponent")) {
                ITextComponent ratNameTag = ITextComponent.Serializer.fromJson(ratTag.getString("CustomName"));
                if (ratNameTag != null) {
                    ratName = ratNameTag.getFormattedText();
                }
            }
        }
    }


    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        CompoundNBT ratTag = new CompoundNBT();
        if (stack.getTag() != null && stack.getTag().getCompound("Rat") != null) {
            ratTag = stack.getTag().getCompound("Rat");
        }
        EntityRat rat = new EntityRat(RatsEntityRegistry.RAT,context.getWorld());
        BlockPos offset = context.getPos().offset(context.getFace());
        rat.readAdditional(ratTag);
        rat.setLocationAndAngles(offset.getX() + 0.5D, offset.getY(), offset.getZ() + 0.5D, 0, 0);
        if (!context.getWorld().isRemote) {
            context.getWorld().addEntity(rat);
        }
        stack.shrink(1);
        context.getPlayer().setHeldItem(context.getHand(), new ItemStack(Items.ARROW));
        context.getPlayer().swingArm(context.getHand());
        return ActionResultType.SUCCESS;
    }

    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        EntityRatArrow arrow = new EntityRatArrow(RatsEntityRegistry.RAT_ARROW, worldIn, shooter, stack);
        return arrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
        return false;
    }
}
