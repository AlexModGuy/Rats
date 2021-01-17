package com.github.alexthe666.rats.server.items;

import com.github.alexthe666.rats.RatsMod;
import com.github.alexthe666.rats.server.entity.EntityRat;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemRatPapers extends Item {

    public ItemRatPapers() {
        super(new Properties().group(RatsMod.TAB));
        this.setRegistryName("rats:rat_papers");
    }

    public boolean hasEffect(ItemStack stack) {
        return isEntityBound(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(!isEntityBound(stack)){
            tooltip.add(new TranslationTextComponent("item.rats.rat_papers.desc0").mergeStyle(TextFormatting.GRAY));
            tooltip.add(new TranslationTextComponent("item.rats.rat_papers.desc1").mergeStyle(TextFormatting.GRAY));
        }
        tooltip.add(new TranslationTextComponent("item.rats.rat_papers.desc2").mergeStyle(TextFormatting.GRAY));
        if (stack.getTag() != null) {
            tooltip.add(new TranslationTextComponent("item.rats.rat_papers.rat_desc").mergeStyle(TextFormatting.GRAY));
            String ratName = I18n.format("entity.rats.rat");
            String entity = stack.getTag().getString("RatName");
            if (stack.getTag().hasUniqueId("RatUUID")) {
                if(entity.isEmpty()){
                    tooltip.add(new TranslationTextComponent(ratName).mergeStyle(TextFormatting.GRAY));
                }else{
                    tooltip.add(new StringTextComponent(entity).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
    }

    public static boolean isEntityBound(ItemStack stack) {
        if(stack.getTag() != null){
            return stack.getTag().hasUniqueId("RatUUID");
        }
        return false;
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        CompoundNBT nbt = stack.getTag() == null ? new CompoundNBT() : stack.getTag();
        if(target instanceof PlayerEntity){
            try{
                if(nbt.hasUniqueId("RatUUID")){
                    UUID ratUUID = nbt.getUniqueId("RatUUID");
                    if (!target.world.isRemote) {
                        Entity entity = target.world.getServer().getWorld(target.world.func_234923_W_()).getEntityByUuid(ratUUID);
                        if (entity instanceof EntityRat) {
                            EntityRat rat = (EntityRat)entity;
                            if(rat.isTamed() && rat.isOwner(playerIn)){
                                rat.setTamedBy((PlayerEntity) target);
                                stack.shrink(1);
                            }
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }catch (Exception e){

            }
            return ActionResultType.PASS;
        }

        if(target instanceof EntityRat && ((EntityRat) target).isOwner(playerIn)){
            EntityRat rat = (EntityRat)target;
            if(rat.hasCustomName()){
                nbt.putString("RatName", rat.getCustomName().getString());
            }
            nbt.putUniqueId("RatUUID", rat.getUniqueID());
            ItemStack stackReplacement = new ItemStack(this);
            if(!playerIn.isCreative()){
                stack.shrink(1);
            }
            stackReplacement.setTag(nbt);
            playerIn.swingArm(hand);
            if(!playerIn.addItemStackToInventory(stackReplacement)){
                ItemEntity itementity = playerIn.dropItem(stackReplacement, false);
                if (itementity != null) {
                    itementity.setNoPickupDelay();
                    itementity.setOwnerId(playerIn.getUniqueID());
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
