package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.entity.RatCommand;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemRatFlute extends Item {

    public ItemRatFlute() {
        super();
        this.setCreativeTab(RatsMod.TAB);
        this.setTranslationKey("rats.rat_flute");
        this.setRegistryName(RatsMod.MODID, "rat_flute");
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }


    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        if (itemStackIn.getTagCompound() != null) {
            int commandInt = itemStackIn.getTagCompound().getInteger("Command");
            RatCommand ratCommand = RatCommand.values()[MathHelper.clamp(commandInt, 0, RatCommand.values().length - 1)];
            if (player.isSneaking()) {
                commandInt++;
                if (commandInt > RatCommand.values().length - 1) {
                    commandInt = 0;
                }
                itemStackIn.getTagCompound().setInteger("Command", commandInt);
                ratCommand = RatCommand.values()[MathHelper.clamp(commandInt, 0, RatCommand.values().length - 1)];
                worldIn.playSound(player, player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.NEUTRAL, 1, 1.25F);
                player.sendStatusMessage(new TextComponentTranslation("item.rats.rat_flute.comand_changed").appendText(" ").appendSibling(new TextComponentTranslation(ratCommand.getTranslateName())), true);
            } else {
                player.getCooldownTracker().setCooldown(this, 60);
                float chunksize = 16 * RatsMod.CONFIG_OPTIONS.ratFluteDistance;
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
                player.sendStatusMessage(new TextComponentTranslation("item.rats.rat_flute.rat_count", ratCount), true);
                worldIn.playSound(player, player.getPosition(), RatsSoundRegistry.getFluteSound(), SoundCategory.NEUTRAL, 1, 1.25F);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.rats.rat_flute.desc0"));
        tooltip.add(I18n.format("item.rats.rat_flute.desc1"));
        if (stack.getTagCompound() != null) {
            RatCommand ratCommand = RatCommand.values()[MathHelper.clamp(stack.getTagCompound().getInteger("Command"), 0, RatCommand.values().length - 1)];
            tooltip.add(I18n.format("entity.rat.command.current") + " " + I18n.format(ratCommand.getTranslateName()));

        }
    }
}
