package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemRatFlute extends Item {

    public ItemRatFlute() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rat_flute");
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
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

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        if (itemStackIn.getTag() != null) {
            int commandInt = itemStackIn.getTag().getInt("Command");
            RatCommand ratCommand = RatCommand.values()[MathHelper.clamp(commandInt, 0, RatCommand.values().length - 1)];
            if (player.isSneaking()) {
                commandInt++;
                if (commandInt > RatCommand.values().length - 1) {
                    commandInt = 0;
                }
                itemStackIn.getTag().putInt("Command", commandInt);
                ratCommand = RatCommand.values()[MathHelper.clamp(commandInt, 0, RatCommand.values().length - 1)];
                worldIn.playSound(player, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1, 1.25F);
                player.sendStatusMessage(new TranslationTextComponent("item.rats.rat_flute.comand_changed").appendText(" ").appendSibling(new TranslationTextComponent(ratCommand.getTranslateName())), true);
            } else {
                player.getCooldownTracker().setCooldown(this, 60);
                float chunksize = 16 * RatConfig.ratFluteDistance;
                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(player, (new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX + 1.0D, player.posY + 1.0D, player.posZ + 1.0D)).grow(chunksize, 256, chunksize));
                Iterator<Entity> itr = list.iterator();
                int ratCount = 0;
                while (itr.hasNext()) {
                    Entity entity = itr.next();
                    if (entity instanceof EntityRat) {
                        if (((EntityRat) entity).onHearFlute(player, ratCommand)) {
                            ratCount++;
                        }
                    }
                }
                player.swingArm(hand);
                player.sendStatusMessage(new TranslationTextComponent("item.rats.rat_flute.rat_count", ratCount), true);
                worldIn.playSound(player, player.getPosition(), RatsSoundRegistry.RAT_FLUTE, SoundCategory.NEUTRAL, 1, 1.25F);
            }
        }
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.rat_flute.desc0"));
        tooltip.add(new TranslationTextComponent("item.rats.rat_flute.desc1"));
        if (stack.getTag() != null) {
            RatCommand ratCommand = RatCommand.values()[MathHelper.clamp(stack.getTag().getInt("Command"), 0, RatCommand.values().length - 1)];
            tooltip.add(new TranslationTextComponent("entity.rat.command.current").appendText(" ").appendSibling(new TranslationTextComponent(ratCommand.getTranslateName())));

        }
    }
}
