package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatConfig;
import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import com.github.alexthe666.rats.server.misc.RatsSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ItemRatWhistle extends Item {

    public ItemRatWhistle() {
        super(new Item.Properties().group(RatsMod.TAB));
        this.setRegistryName(RatsMod.MODID, "rat_whistle");
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }


    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.IRON_INGOT;
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
        player.getCooldownTracker().setCooldown(this, 5);
        float chunksize = 48 * RatConfig.ratFluteDistance;
        List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(player, (new AxisAlignedBB(player.getPosX(), player.getPosY(), player.getPosZ(), player.getPosX() + 1.0D, player.getPosY() + 1.0D, player.getPosZ() + 1.0D)).grow(chunksize, 256, chunksize));
        Iterator<Entity> itr = list.iterator();
        int ratCount = 0;
        while (itr.hasNext()) {
            Entity entity = itr.next();
            if (entity instanceof EntityRat) {
                ratCount++;
                EntityRat rat = (EntityRat)entity;
                if(rat.detachHome()){
                    BlockPos homePos = rat.getHomePosition();
                    double dist = Math.sqrt(rat.getRatDistanceSq(homePos.getX() + 0.5D, homePos.getY() + 0.5D, homePos.getZ() + 0.5D));
                    if(dist > 2F && rat.canMove()){
                        if(!rat.getNavigator().tryMoveToXYZ(homePos.getX() + 0.5D, homePos.getY() + 0.5D, homePos.getZ() + 0.5D, 1.5F) || dist > 1000F){
                            rat.attemptTeleport(homePos.getX() + 0.5D, homePos.getY() + 1.5D, homePos.getZ() + 0.5D);
                        }
                    }
                }
            }
        }
        player.swingArm(hand);
        player.sendStatusMessage(new TranslationTextComponent("item.rats.rat_flute.rat_count", ratCount).func_240699_a_(TextFormatting.GRAY), true);
        worldIn.playSound(player, new BlockPos(player.getPositionVec()), RatsSoundRegistry.RAT_WHISTLE, SoundCategory.NEUTRAL, 1, 1.25F);

        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.rats.rat_whistle.desc0").func_240699_a_(TextFormatting.GRAY));
    }
}
